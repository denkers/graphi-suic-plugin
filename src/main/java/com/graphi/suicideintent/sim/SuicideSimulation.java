package com.graphi.suicideintent.sim;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.Random;

public class SuicideSimulation
{
    public static void deleteGraphObj(double p, Graph<Node, Edge> graph, boolean deleteNodes)
    {
        Random rGen        =   new Random();
        Collection objs    =   deleteNodes? graph.getVertices() : graph.getEdges();
        
        for(Object gObj : objs)
        {
            double prob =   rGen.nextDouble();
            if(prob <= p)
            {
                if(deleteNodes)
                    graph.removeVertex((Node) gObj);
                else
                    graph.removeEdge((Edge) gObj);
            }
        }
    }
}
