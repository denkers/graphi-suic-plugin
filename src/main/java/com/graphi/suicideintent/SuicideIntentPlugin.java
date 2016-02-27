///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import com.graphi.app.AppManager;
import com.graphi.plugins.AbstractPlugin;

public class SuicideIntentPlugin extends AbstractPlugin
{
    public SuicideIntentPlugin()
    {
        super("SuicideIntent", "Suicide intent plugin");
    }
    
    @Override
    public void attachPanel(AppManager appManager)
    {
        panel   =   new PluginLayout(appManager);
    }
}
