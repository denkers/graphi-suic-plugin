
package com.graphi.suicideintent.util;

import com.graphi.util.Edge;
import com.graphi.util.factory.EdgeFactory;

public class SuicideEdgeFactory extends EdgeFactory
{
    public SuicideEdgeFactory(int lastID)
    {
        super(lastID);
    }
    
    public SuicideEdgeFactory(int lastID, int incAmount)
    {
        super(lastID, incAmount);
    }
    
    @Override
    public Edge create()
    {
        SuicideEdge edge    =   new SuicideEdge(super.create());
        return edge;
    }
}
