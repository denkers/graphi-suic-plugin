///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import com.graphi.app.AppManager;
import com.graphi.plugins.AbstractPlugin;
import com.graphi.suicideintent.layout.PluginLayout;

public class SuicideIntentPlugin extends AbstractPlugin
{
    public static SuicideIntentConfig CONFIG;
    
    public SuicideIntentPlugin()
    {
        super("SuicideIntent", "Suicide intent plugin");
        CONFIG  =   SuicideIntentConfig.getConfig();
    }
    
    public static void reloadConfig()
    {
        CONFIG  =   SuicideIntentConfig.getConfig();
    }
    
    @Override
    public void attachPanel(AppManager appManager)
    {
        panel   =   new PluginLayout(appManager);
    }
}
