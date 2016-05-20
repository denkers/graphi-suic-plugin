///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.display.layout.controls.TaskControlPanel;

public class TaskPanel extends TaskControlPanel
{
    protected String[] SUIC_OPTIONS    =   
    { 
        "Suicide computation", 
        "Suicide/Disaster sim (random)",
        "Reset suicide simulation",
        "Average suicide computation",
        "Suicide sim (Random)",
        "Killing sim (Accumulated-Drop)"
    };
    
    private SuicideControlPanel outer;
    
    public TaskPanel(SuicideControlPanel outer)
    {
        super(outer);
        
        this.outer   =   outer;
        initOptions();
    }
    
    private void initOptions()
    {
        for(String option : SUIC_OPTIONS)
            addOption(option);
    }
    
    @Override
    protected void handleAction(int actionIndex) 
    {
        SuicidePanel suicPanel  =   outer.getSuicidePanel();
        int pluginIndex         =   getOptionsCount();
        if(actionIndex < pluginIndex) super.handleAction(actionIndex);
        else
        {
            if(actionIndex == pluginIndex)
                suicPanel.getComputePanel().computeSuicideIntent();

            else if(actionIndex == pluginIndex + 1)
                suicPanel.getSimPanel().executeDelete();

            else if(actionIndex == pluginIndex + 2)
                suicPanel.getSimPanel().clearDeadObjects();

            else if(actionIndex == pluginIndex + 3)
                suicPanel.getComputePanel().computeAverageSuicideIntent();
            
            else if(actionIndex == pluginIndex + 4)
                suicPanel.getSimPanel().excecuteKillingDifussion(true);
            
             else if(actionIndex == pluginIndex + 5)
                suicPanel.getSimPanel().excecuteKillingDifussion(false);
        }
    }
}
