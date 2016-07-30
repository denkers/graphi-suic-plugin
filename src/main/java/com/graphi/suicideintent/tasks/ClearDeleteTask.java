///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.suicideintent.layout.SuicideControlPanel;
import com.graphi.tasks.AbstractTask;

public class ClearDeleteTask extends AbstractTask
{
    @Override
    public void initTaskDetails()
    {
        setTaskName("Reset Suicide Simulation");
    }

    @Override
    public void initDefaultProperties() {}

    @Override
    public void performTask() 
    {
        SuicideControlPanel.getSuicidePanelInstance()
                .getSimPanel().clearDeadObjects();
    }
}
