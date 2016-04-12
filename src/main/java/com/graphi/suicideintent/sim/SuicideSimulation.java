package com.graphi.suicideintent.sim;

import com.graphi.suicideintent.util.SuicideEdge;
import com.graphi.suicideintent.util.SuicideNode;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SuicideSimulation
{
    public static void deleteGraphObjs(double p, Graph<Node, Edge> graph, boolean deleteNodes)
    {
        Random rGen         =   new Random();
        Object[] objs       =   (deleteNodes? graph.getVertices() : graph.getEdges()).toArray();
        
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
    
    public static void killGraphObjects(Graph<Node, Edge> graph, double p, boolean killNodes)
    {
        Random rGen         =   new Random();
        Collection gObjs    =   killNodes? graph.getVertices() : graph.getEdges();
        
        for(Object gObj : gObjs)
        {
            double prob =   rGen.nextDouble();
            if(prob <= p)
            {
                if(killNodes)
                {
                    Node node                   =   (Node) gObj;
                    Collection<Edge> indEdges   =   graph.getIncidentEdges(node);
                    ((SuicideNode) node).setDeleted(true);
                    
                    for(Edge edge : indEdges)
                        ((SuicideEdge) edge).setDeleted(true);
                }
                
                else ((SuicideEdge) gObj).setDeleted(true);
            }
        }
    }
}
