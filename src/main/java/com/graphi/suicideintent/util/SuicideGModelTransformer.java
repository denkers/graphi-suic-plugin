///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.util;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections15.Transformer;

public class SuicideGModelTransformer implements Transformer<Graph<Node, Edge>, SparseDoubleMatrix2D>
{
    private final double directedWeight;
    private final double undirectedWeight;
    private final double selfWeight;
    private final int perspectiveIndex;
    
    public SuicideGModelTransformer(int perspectiveIndex)
    {
        this(perspectiveIndex, 1.0, 0.5, 2.0);
    }
    
    public SuicideGModelTransformer(int perspectiveIndex, double directedWeight, double undirectedWeight, double selfWeight)
    {
        this.perspectiveIndex   =   perspectiveIndex;
        this.directedWeight     =   directedWeight;
        this.undirectedWeight   =   undirectedWeight;
        this.selfWeight         =   selfWeight;
    }
    
    @Override
    public SparseDoubleMatrix2D transform(Graph<Node, Edge> g)
    {
        int n                           =   g.getVertexCount();
        SparseDoubleMatrix2D matrix     =   new SparseDoubleMatrix2D(n, n);
        
        List<Node> vertices   =  new ArrayList<>(g.getVertices());
        for(int row = 0; row < n; row++)
        {
            Node current    =   vertices.get(row);
            for(int col = 0; col < n; col++)
            {
                if(row == col && col == perspectiveIndex)
                    matrix.set(perspectiveIndex, perspectiveIndex, selfWeight);
                
                else
                {
                    Node next   =   vertices.get(col);
                    if(g.isNeighbor(current, next))
                    {
                        Edge edge   =   g.findEdge(current, next);
                        if(edge.getEdgeType() == EdgeType.UNDIRECTED)
                            matrix.set(row, col, undirectedWeight);
                        else
                            matrix.set(row, col, directedWeight);
                    }
                    
                    else matrix.set(row, col, 0);
                }
            }
        }
        
        return matrix;
    }
}
