///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;

public class PluginLayout extends MainPanel
{
    SuicideIntentControlPanel suicideControlPanel;
    
    public PluginLayout(AppManager appManager) 
    {
        super(appManager);
        suicideControlPanel =   new SuicideIntentControlPanel();
        controlPanel.add(suicideControlPanel);
    }
}
