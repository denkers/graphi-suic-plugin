///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.sim.GraphPlayback;
import com.graphi.sim.PlaybackEntry;
import com.graphi.suicideintent.util.SuicideGModelTransformer;
import com.graphi.suicideintent.util.SuicideNode;
import com.graphi.util.Edge;
import com.graphi.util.GraphData;
import com.graphi.util.GraphUtilities;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class IntentComputation 
{
    private static final DecimalFormat FORMATTER  =   new DecimalFormat("#.###");
    
    public static double getSelfEvaluation(Node node, Graph<Node, Edge> g)
    {
        SuicideGModelTransformer transformer        =   new SuicideGModelTransformer(node);
        Graph<Node, Edge> neighbourhood             =   GraphUtilities.getNeighbourhood(g, node, 1);
        int perspectiveIndex                        =   new ArrayList<>(neighbourhood.getVertices()).indexOf(node);
        SparseDoubleMatrix2D matrix                 =   transformer.transform(neighbourhood);
        
        Entry<Double, SparseDoubleMatrix2D> evCombo =   MatrixTools.powerIteration(matrix);
        SparseDoubleMatrix2D evalVector             =   normalizeEvalVector(evCombo.getValue(), evCombo.getKey());
        double evaluation                           =   Double.parseDouble(FORMATTER.format(evalVector.get(perspectiveIndex, 0)));
        
        return evaluation;
    }
    
    private static SparseDoubleMatrix2D normalizeEvalVector(SparseDoubleMatrix2D vector, double largestEigenValue)
    {
        double length                               =   MatrixTools.getVectorLength(vector);
        SparseDoubleMatrix2D normalizedEvalVector   =   new SparseDoubleMatrix2D(vector.rows(), 1);   
        
        for(int row = 0; row < vector.rows(); row++)
        {
            double entryVal =   vector.get(row, 0);
            double val      =   (largestEigenValue * entryVal) / length;
            normalizedEvalVector.setQuick(row, 0, val);
        }
        
        return normalizedEvalVector;
    }
    
    public static DefaultTableModel getIntentTableModel(Map<Node, Double> scores, boolean average)
    {
        DefaultTableModel model     =   new DefaultTableModel();
        model.addColumn("Node ID");
        model.addColumn(average? "Suicide-Index" : "Self-Perception");
        model.addColumn("State");
        model.addColumn("Self-Weight");
        
        for(Entry<Node, Double> scoreEntry : scores.entrySet())
        {
            int nodeID      =   scoreEntry.getKey().getID();
            double score    =   scoreEntry.getValue();
            String state    =   ((SuicideNode) scoreEntry.getKey()).isDeleted()? "DEAD" : "ALIVE";
            int selfEval    =   ((SuicideNode) scoreEntry.getKey()).getSelfEvaluation();
            
            model.addRow(new Object[] { nodeID, score, state, selfEval });
        }
        
        return model;
    }
    
    public static DefaultTableModel getSuicideIntent(GraphData gData, Node node, boolean computeAll)
    {
        Map<Node, Double> scores    =   computeEvalScores(gData, node, computeAll);
        return getIntentTableModel(scores, false);
    }
    
    public static DefaultTableModel getAverageSuicideIntent(GraphPlayback playback, GraphData gData)
    {
        Map<Node, Double> scores        =   new LinkedHashMap<>();
        List<PlaybackEntry> entries     =   playback.getEntries();
        
        if(!entries.get(0).hasComputationModel() || !entries.get(entries.size() - 1).hasComputationModel())
            return null;
        
        DefaultTableModel timeFirst =   entries.get(0).getComputationModel().getModel();
        DefaultTableModel timeLast  =   entries.get(entries.size() - 1).getComputationModel().getModel();
        
        int row = 0;
        for(Node node : entries.get(entries.size() - 1).getGraph().getVertices())
        {
            double scoreFirst       =   (double) timeFirst.getValueAt(row, 1);
            double scoreLast        =   (double) timeLast.getValueAt(row, 1);
            double averageScore     =   1.0 - (scoreLast / scoreFirst);
            
            scores.put(node, averageScore);
            row++;
        }
        
        return getIntentTableModel(scores, true);
    }
    
    public static Map<Node, Double> computeEvalScores(GraphData gData, Node node, boolean computeAll)
    {
        Map<Node, Double> nodeEvalScores    =   new LinkedHashMap<>();
        
        if(!computeAll && (node == null || gData.getNodes().containsKey(node.getID())))
            JOptionPane.showMessageDialog(null, "That node ID does not exist");
        
        else
        {
            if(!computeAll)
            {
                double eval                     =   IntentComputation.getSelfEvaluation(node, gData.getGraph());
                nodeEvalScores.put(node, eval);
            }
            
            else
            {
                for(Node otherNode : gData.getGraph().getVertices())
                {
                    double eval;
                    
                    if(((SuicideNode) otherNode).isDeleted())
                        eval        =   ((SuicideNode) otherNode).getSuicideIntent();
                    else 
                        eval        =   IntentComputation.getSelfEvaluation(otherNode, gData.getGraph());
                    
                    ((SuicideNode) otherNode).setSuicideIntent(eval);
                    nodeEvalScores.put(otherNode, eval);
                }
            }
        }
        
        return nodeEvalScores;
    }
}
