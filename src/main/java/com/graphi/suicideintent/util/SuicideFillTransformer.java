
package com.graphi.suicideintent.util;

import com.graphi.util.GraphObject;
import com.graphi.util.transformer.ObjectFillTransformer;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Color;
import java.awt.Paint;
import java.util.List;

public class SuicideFillTransformer<T extends GraphObject> extends ObjectFillTransformer<T>
{
    private List<T> deletedList;
    
    public SuicideFillTransformer(PickedInfo<T> pickedInfo, List<T> deletedList) 
    {
        super(pickedInfo);
        this.deletedList    =   deletedList;
    }
    
    @Override
    public Paint transform(T gObj)
    {
        if(deletedList.contains(gObj))
            return Color.BLACK;
        
        else 
            return super.transform(gObj);
    }
}
