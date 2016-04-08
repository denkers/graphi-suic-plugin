package com.graphi.suicideintent.sim;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.Random;

public class SuicideSimulation
{
    public static void deleteNodes(double p, Graph<Node, Edge> graph)
    {
        Random rGen                 =   new Random();   
        Collection<Node> nodes      =   graph.getVertices();
        for(Node node : nodes)
        {
            double prob =   rGen.nextDouble();
            if(prob <= p)
                graph.removeVertex(node);
        }
    }
    
    public static void deleteEdges(double p, Graph<Node, Edge> graph)
    {
        Random rGen =   new Random();
        Collection<Edge> edges      =   graph.getEdges();
        for(Edge edge : edges)
        {
            double prob =   rGen.nextDouble();
            if(prob <= p)
                graph.removeEdge(edge);
        }
    }
}
