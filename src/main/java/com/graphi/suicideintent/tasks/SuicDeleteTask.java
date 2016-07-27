///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.app.AppManager;
import com.graphi.suicideintent.layout.PluginLayout;
import com.graphi.suicideintent.layout.SimulationPanel;
import com.graphi.suicideintent.layout.SuicideControlPanel;
import com.graphi.tasks.AbstractTask;

public class SuicDeleteTask extends AbstractTask
{

    @Override
    public void initTaskDetails() 
    {
        name    =   "Suicide/Disaster simulation";
    }

    @Override
    public void initDefaultProperties() 
    {
        properties.put("Delete random", "true");
        properties.put("Delete probability", "0.5");
    }

    @Override
    public void performTask() 
    {
        SimulationPanel panel   =   SuicideControlPanel.getSuicidePanelInstance().getSimPanel();
        boolean deleteRandom    =   properties.get("Delete Random").equalsIgnoreCase("true");
        double deleteProb       =   Double.parseDouble(properties.get("Delete probability"));
        
        if(deleteRandom)
            panel.deleteRandomNodes(deleteProb);
        else
            panel.deleteTargetNodes();
        
        panel.repaintViewer();
    }
}
