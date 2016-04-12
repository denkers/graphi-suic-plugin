
package com.graphi.suicideintent.util;

import com.graphi.util.Node;
import java.awt.Color;

public class SuicideNode extends Node implements SuicideInt
{
    private boolean isDeleted;
    
    public SuicideNode(int id)
    {
        super(id);
    }
    
    public SuicideNode(int id, String name)
    {
        super(id, name);
    }
    
    public SuicideNode(int id, String name, Color fill)
    {
        super(id, name, fill);
    }
    
    public SuicideNode(Node node)
    {
        this(node.getID(), node.getName(), node.getFill());
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
