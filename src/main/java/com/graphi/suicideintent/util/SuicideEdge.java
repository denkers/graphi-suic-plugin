package com.graphi.suicideintent.util;

import com.graphi.util.Edge;
import edu.uci.ics.jung.graph.util.EdgeType;

public class SuicideEdge extends Edge implements SuicideInt
{
    private boolean isDeleted;
    
    public SuicideEdge(int id, EdgeType edgeType)
    {
        super(id, edgeType);
    }
    
    public SuicideEdge(int id, double weight, EdgeType edgeType)
    {
        super(id, weight, edgeType);
    }
    
    public SuicideEdge(Edge edge)
    {
        this(edge.getID(), edge.getWeight(), edge.getEdgeType());
    }
    
    @Override
    public void setDeleted(boolean deleted) 
    {
        this.isDeleted  =   deleted;
    }

    @Override
    public boolean isDeleted()
    {
        return isDeleted;
    }
    
}
