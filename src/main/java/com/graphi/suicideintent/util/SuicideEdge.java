package com.graphi.suicideintent.util;

import com.graphi.util.Edge;

public class SuicideEdge extends Edge implements SuicideInt
{
    private boolean isDeleted;
    
    public SuicideEdge(int id)
    {
        super(id);
    }
    
    public SuicideEdge(int id, double weight)
    {
        super(id, weight);
    }
    
    public SuicideEdge(Edge edge)
    {
        this(edge.getID(), edge.getWeight());
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
    
    @Override
    public Edge copyGraphObject()
    {
        SuicideEdge edge    =   new SuicideEdge(id, weight);
        edge.setDeleted(isDeleted);
        
        return edge;
    }
}
