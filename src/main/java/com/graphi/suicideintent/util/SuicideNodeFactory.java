
package com.graphi.suicideintent.util;

import com.graphi.util.Node;
import com.graphi.util.factory.NodeFactory;

public class SuicideNodeFactory extends NodeFactory
{
    public SuicideNodeFactory()
    {
        super();
    }
    
    public SuicideNodeFactory(int lastID)
    {
        super(lastID);
    }
    
    public SuicideNodeFactory(int lastID, int incAmount)
    {
        super(lastID, incAmount);
    }
    
    @Override
    public Node create()
    {
        SuicideNode node   =   new SuicideNode(super.create());
        return node;
    }
}
