///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.suicideintent.layout.SuicideControlPanel;
import com.graphi.tasks.AbstractTask;

public class SuicIndexTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Suicide Index Computation");
    }

    @Override
    public void initDefaultProperties() {}

    @Override
    public void performTask() 
    {
        SuicideControlPanel.getSuicidePanelInstance()
                .getComputePanel().computeAverageSuicideIntent();
    }
}
