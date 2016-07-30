///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.suicideintent.layout.SuicideControlPanel;
import com.graphi.tasks.AbstractTask;

public class SelfEvalTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Compute Self-Perception");
    }

    @Override
    public void initDefaultProperties() 
    {
        setProperty("Compute all", "true");
        setProperty("Perspective ID", "-1");
    }

    @Override
    public void performTask()
    {
        boolean computeAll  =   getProperty("Compute all").equalsIgnoreCase("true");
        int perspectiveID   =   Integer.parseInt(getProperty("Perspective ID"));
        SuicideControlPanel.getSuicidePanelInstance().getComputePanel()
                .computeSuicideIntent(computeAll, perspectiveID);
    }
}
