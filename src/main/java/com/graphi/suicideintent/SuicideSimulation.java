///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import com.graphi.sim.PlaybackEntry;
import com.graphi.suicideintent.util.SuicideEdge;
import com.graphi.suicideintent.util.SuicideNode;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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
    
    public static void killNodes(Graph<Node, Edge> graph, double p, List exceptionList)
    {
        Random rGen                 =   new Random();
        Collection<Node> nodes      =   graph.getVertices();
        
        for(Node node : nodes)
        {
            if(exceptionList.contains(node.getID()))
                continue;
            
            double prob =   rGen.nextDouble();
            if(prob <= p)
                killNode(node, graph);
        }
    }
    
    public static void diffuseKillNodes(Graph<Node, Edge> graph, List exceptionList, List<PlaybackEntry> entries, boolean startEntry)
    {
        if(entries == null || entries.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "[Error] You must have atleast two entries");
            return;
        }
        
        Random rGen                 =   new Random();
        Collection<Node> nodes      =   graph.getVertices();
        DefaultTableModel latest    =   entries.get(entries.size() - 1).getComputationModel().getModel();
        DefaultTableModel prev      =   entries.get(startEntry? 0 : entries.size() - 2).getComputationModel().getModel();
        int entryRow                =   0;
        
        for(Node node : nodes)
        {
            if(!exceptionList.contains(node) && !((SuicideNode) node).isDeleted())
            {
                double scoreLatest  =   (double) latest.getValueAt(entryRow, 1);
                double scorePrev    =   (double) prev.getValueAt(entryRow, 1);
                double drop         =   1 - (scoreLatest / scorePrev);
                double p            =   rGen.nextDouble();
                
                if(p <= drop)
                    killNode(node, graph);
            }
            
            entryRow++;
        }
    }
    
    public static void killNode(Node node, Graph<Node, Edge> graph)
    {
        Collection<Edge> indEdges   =   graph.getIncidentEdges(node);
        ((SuicideNode) node).setDeleted(true);

        for(Edge edge : indEdges)
            ((SuicideEdge) edge).setDeleted(true);
    }
}
