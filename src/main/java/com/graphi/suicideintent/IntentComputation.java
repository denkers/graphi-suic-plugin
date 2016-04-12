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
import com.graphi.util.Edge;
import com.graphi.util.GraphData;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class IntentComputation 
{
    private static final DecimalFormat FORMATTER  =   new DecimalFormat("#.###");
    
    public static SparseDoubleMatrix2D getSelfEvaluationVector(int nodeIndex, Graph<Node, Edge> g)
    {
        SuicideGModelTransformer transformer        =   new SuicideGModelTransformer(nodeIndex);
        SparseDoubleMatrix2D matrix                 =   transformer.transform(g);
        Entry<Double, SparseDoubleMatrix2D> evCombo =   MatrixTools.powerIteration(matrix);
        SparseDoubleMatrix2D evalVector             =   MatrixTools.normalizeVector(evCombo.getValue(), evCombo.getKey());
        
        return evalVector;
    }
    
    public static double getSelfEvaluation(int nodeIndex, Graph<Node, Edge> g)
    {
        SparseDoubleMatrix2D evalVector =   getSelfEvaluationVector(nodeIndex, g);
        return Double.parseDouble(FORMATTER.format(evalVector.get(nodeIndex - 1, 0)));
    }
    
    public static DefaultTableModel getIntentTableModel(Map<Node, Double> scores)
    {
        DefaultTableModel model     =   new DefaultTableModel();
        model.addColumn("Node ID");
        model.addColumn("Suicide intent");
        
        for(Entry<Node, Double> scoreEntry : scores.entrySet())
        {
            int nodeID      =   scoreEntry.getKey().getID();
            double score    =   scoreEntry.getValue();
            
            model.addRow(new Object[] { nodeID, score });
        }
        
        return model;
    }
    
    public static DefaultTableModel getSuicideIntent(GraphData gData, int perspectiveIndex, boolean computeAll)
    {
        Map<Node, Double> scores    =   computeEvalScores(gData, perspectiveIndex, computeAll);
        return getIntentTableModel(scores);
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
        for(Node node : gData.getGraph().getVertices())
        {
            double scoreFirst       =   (double) timeFirst.getValueAt(row, 1);
            double scoreLast        =   (double) timeLast.getValueAt(row, 1);
            double averageScore     =   1.0 - (scoreLast / scoreFirst);
            
            scores.put(node, averageScore);
            row++;
        }
        
        return getIntentTableModel(scores);
    }
    
    public static Map<Node, Double> computeEvalScores(GraphData gData, int perspectiveIndex, boolean computeAll)
    {
        Map<Node, Double> nodeEvalScores    =   new LinkedHashMap<>();
        
        if(!computeAll && gData.getNodes().containsKey(perspectiveIndex))
            JOptionPane.showMessageDialog(null, "That node ID does not exist");
        
        else
        {
            if(!computeAll)
            {
                double eval                     =   IntentComputation.getSelfEvaluation(perspectiveIndex, gData.getGraph());
                Node node                       =   gData.getNodes().get(perspectiveIndex);
                nodeEvalScores.put(node, eval);
            }
            
            else
            {
                for(Node node : gData.getGraph().getVertices())
                {
                    double eval     =   IntentComputation.getSelfEvaluation(node.getID(), gData.getGraph());
                    nodeEvalScores.put(node, eval);
                }
            }
        }
        
        return nodeEvalScores;
    }
}
