
package com.graphi.suicideintent.util;

import com.graphi.util.GraphObject;
import com.graphi.util.transformer.ObjectFillTransformer;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Color;
import java.awt.Paint;

public class SuicideFillTransformer<T extends GraphObject> extends ObjectFillTransformer<T>
{
    private Color deadColour;
    private Color deadSelectedColour;
    
    public SuicideFillTransformer(PickedInfo<T> pickedInfo) 
    {
        super(pickedInfo);
        deadColour          =   new Color(214, 26, 45);
        deadSelectedColour  =   new Color(255, 87, 41);
    }
    
    @Override
    public Paint transform(T gObj)
    {
        if(((SuicideInt) gObj).isDeleted())
        {
            if(pickedInfo.isPicked(gObj)) return deadSelectedColour;
            else return deadColour;
        }
        
        else 
            return super.transform(gObj);
    }
}
