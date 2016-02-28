///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.util;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

public class SuicideGModelTransformer implements Transformer<Graph<Node, Edge>, double[][]>
{
    private double directedWeight;
    private double undirectedWeight;
    private double selfWeight;
    private double perspectiveIndex;
    
    public SuicideGModelTransformer(int perspectiveIndex)
    {
        this(perspectiveIndex, 1.0, 0.5, 2);
    }
    
    public SuicideGModelTransformer(int perspectiveIndex, double directedWeight, double undirectedWeight, double selfWeight)
    {
        this.perspectiveIndex   =   perspectiveIndex;
        this.directedWeight     =   directedWeight;
        this.undirectedWeight   =   undirectedWeight;
        this.selfWeight         =   selfWeight;
    }
    
    @Override
    public double[][] transform(Graph<Node, Edge> g)
    {
        int n               =   g.getVertexCount();
        double[][] matrix   =   new double[n][n];
        
        return matrix;
    }
}
