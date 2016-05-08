
package com.graphi.suicideintent.util;

import com.graphi.util.Node;
import com.graphi.util.factory.NodeFactory;
import java.util.Random;

public class SuicideNodeFactory extends NodeFactory
{
    private int selfEval;
    private Random random;
    
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
        if(selfEval == -1 && random == null) random = new Random();
        
        SuicideNode node   =   new SuicideNode(super.create());
        
        if(selfEval != -1)
            node.setSelfEvaluation(selfEval);
        else
            node.setSelfEvaluation(random.nextInt(10));
        
        return node;
    }
}
