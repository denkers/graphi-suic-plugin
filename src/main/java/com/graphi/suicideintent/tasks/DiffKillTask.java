///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.suicideintent.layout.SuicideControlPanel;
import com.graphi.tasks.AbstractTask;

public class DiffKillTask extends AbstractTask
{
    @Override
    public void initTaskDetails()
    {
        name    =   "Killing simulation (Random/Accumulated-Drop)";
    }

    @Override
    public void initDefaultProperties() 
    {
        properties.put("Use Accumulated-Drop", "true");
    }

    @Override
    public void performTask() 
    {
        boolean useAccumDrop    =   properties.get("Use Accumulated-Drop").equalsIgnoreCase("true");
        SuicideControlPanel.getSuicidePanelInstance().getSimPanel().excecuteKillingDifussion(!useAccumDrop);
    }
}
