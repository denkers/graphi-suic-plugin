///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import com.graphi.app.AppManager;
import com.graphi.plugins.AbstractPlugin;
import com.graphi.suicideintent.layout.PluginLayout;
import com.graphi.suicideintent.util.SuicideEdgeFactory;
import com.graphi.suicideintent.util.SuicideNodeFactory;
import com.graphi.util.GraphData;

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
    
    @Override
    public void passData(GraphData data)
    {
        data.setEdgeFactory(new SuicideEdgeFactory());
        data.setNodeFactory(new SuicideNodeFactory());
        panel.setGraphData(data);
    }
}
