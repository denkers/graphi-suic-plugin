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
import java.awt.Color;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import javax.swing.JOptionPane;

public class IntentComputation 
{
    public static SparseDoubleMatrix2D getSelfEvaluationVector(int nodeIndex, Graph<Node, Edge> g)
    {
        SuicideGModelTransformer transformer    =   new SuicideGModelTransformer(nodeIndex);
        SparseDoubleMatrix2D matrix             =   transformer.transform(g);
        SparseDoubleMatrix2D evalVector         =   MatrixTools.powerIterationFull(matrix);
        
        return evalVector;
    }
    
    public static double getSelfEvaluation(int nodeIndex, Graph<Node, Edge> g)
    {
        SparseDoubleMatrix2D evalVector =   getSelfEvaluationVector(nodeIndex, g);
        return evalVector.get(nodeIndex, nodeIndex);
    }
    
    public static List<Entry<Node, Double>> computeEvalScores(GraphData gData, int perspectiveIndex, boolean computeAll)
    {
        List<Entry<Node, Double>> nodeEvalScores    =   new ArrayList<>();
        
        if(gData.getNodes().containsKey(perspectiveIndex))
            JOptionPane.showMessageDialog(null, "That node ID does not exist");
        
        else
        {
            if(!computeAll)
            {
                double eval                     =   IntentComputation.getSelfEvaluation(perspectiveIndex, gData.getGraph());
                Node node                       =   gData.getNodes().get(perspectiveIndex);
                Entry<Node, Double> entry       =   new SimpleEntry<>(node, eval); 
                nodeEvalScores.add(entry);
            }
            
            else
            {
                List<Node> nodes     =   new ArrayList<>(gData.getGraph().getVertices());
                PriorityQueue<Map.Entry<Node, Double>> nodeEvalQueue   =   new PriorityQueue<>(0, (Map.Entry<Node, Double> entryA, Map.Entry<Node, Double> entryB) 
                        -> entryB.getValue().compareTo(entryA.getValue()));
                
                for(Node node : nodes)
                {
                    double eval                 =   IntentComputation.getSelfEvaluation(node.getID(), gData.getGraph());
                    Map.Entry<Node, Double> entry   =   new AbstractMap.SimpleEntry<>(node, eval); 
                    nodeEvalScores.add(entry);
                }
                
                while(!nodeEvalScores.isEmpty())
                {
                    Entry<Node, Double> entry   =   nodeEvalQueue.poll();
                    nodeEvalScores.add(entry);
                }
            }
        }
        
        return nodeEvalScores;
    }
}
