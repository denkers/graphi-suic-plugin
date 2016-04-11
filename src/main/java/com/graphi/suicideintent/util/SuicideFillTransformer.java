
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
    private Color deadColour;
    private Color deadSelectedColour;
    
    public SuicideFillTransformer(PickedInfo<T> pickedInfo, List<T> deletedList) 
    {
        super(pickedInfo);
        this.deletedList    =   deletedList;
        deadColour          =   new Color(214, 26, 45);
        deadSelectedColour  =   new Color(255, 87, 41);
    }
    
    @Override
    public Paint transform(T gObj)
    {
        if(deletedList.contains(gObj))
        {
            if(pickedInfo.isPicked(gObj)) return deadSelectedColour;
            else return deadColour;
        }
        
        else 
            return super.transform(gObj);
    }
}
