///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import com.graphi.suicideintent.util.SuicideEdgeFactory;
import com.graphi.suicideintent.util.SuicideNodeFactory;

public class PluginLayout extends MainPanel
{
    SuicideIntentControlPanel suicideControlPanel;
    
    public PluginLayout(AppManager appManager) 
    {
        super(appManager);
        suicideControlPanel =   new SuicideIntentControlPanel(this);
        controlPanel.add(suicideControlPanel);
        
        data.setEdgeFactory(new SuicideEdgeFactory());
        data.setNodeFactory(new SuicideNodeFactory());
    }
}
