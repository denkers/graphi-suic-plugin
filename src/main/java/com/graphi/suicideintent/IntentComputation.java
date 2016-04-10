///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.suicideintent.util.SuicideGModelTransformer;
import com.graphi.util.Edge;
import com.graphi.util.GraphData;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class IntentComputation 
{
    public static SparseDoubleMatrix2D getSelfEvaluationVector(int nodeIndex, Graph<Node, Edge> g)
    {
        SuicideGModelTransformer transformer    =   new SuicideGModelTransformer(nodeIndex);
        SparseDoubleMatrix2D matrix             =   transformer.transform(g);
        SparseDoubleMatrix2D evalVector         =   MatrixTools.powerIteration(matrix);
        
        return evalVector;
    }
    
    public static double getSelfEvaluation(int nodeIndex, Graph<Node, Edge> g)
    {
        SparseDoubleMatrix2D evalVector =   getSelfEvaluationVector(nodeIndex, g);
        return evalVector.get(0, nodeIndex);
    }
    
    public static DefaultTableModel getIntentTableModel(GraphData gData, int perspectiveIndex, boolean computeAll)
    {
        Map<Node, Double> scores    =   computeEvalScores(gData, perspectiveIndex, computeAll);
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
                List<Node> nodes     =   new ArrayList<>(gData.getGraph().getVertices());
                PriorityQueue<Map.Entry<Node, Double>> nodeEvalQueue   =   new PriorityQueue<>(0, (Map.Entry<Node, Double> entryA, Map.Entry<Node, Double> entryB) 
                        -> entryB.getValue().compareTo(entryA.getValue()));
                
                for(Node node : nodes)
                {
                    double eval                 =   IntentComputation.getSelfEvaluation(node.getID(), gData.getGraph());
                    nodeEvalScores.put(node, eval);
                }
                
                while(!nodeEvalScores.isEmpty())
                {
                    Entry<Node, Double> entry   =   nodeEvalQueue.poll();
                    nodeEvalScores.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        return nodeEvalScores;
    }
}
