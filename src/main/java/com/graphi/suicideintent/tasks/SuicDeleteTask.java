///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.suicideintent.layout.SimulationPanel;
import com.graphi.suicideintent.layout.SuicideControlPanel;
import com.graphi.tasks.AbstractTask;

public class SuicDeleteTask extends AbstractTask
{

    @Override
    public void initTaskDetails() 
    {
        setTaskName("Suicide/Disaster simulation");
    }

    @Override
    public void initDefaultProperties() 
    {
        setProperty("Delete random", "true");
        setProperty("Delete probability", "0.5");
    }

    @Override
    public void performTask() 
    {
        SimulationPanel panel   =   SuicideControlPanel.getSuicidePanelInstance().getSimPanel();
        boolean deleteRandom    =   getProperty("Delete random").equalsIgnoreCase("true");
        double deleteProb       =   Double.parseDouble(getProperty("Delete probability"));
        
        if(deleteRandom)
            panel.deleteRandomNodes(deleteProb);
        else
            panel.deleteTargetNodes();
        
        panel.repaintViewer();
    }
}
