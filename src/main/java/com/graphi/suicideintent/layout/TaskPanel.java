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
        "Suicide simulation",
        "Reset suicide simulation",
        "Average suicide computation"
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
        if(actionIndex < 3) super.handleAction(actionIndex);
        else
        {
            SuicidePanel suicPanel  =   outer.getSuicidePanel();
            int pluginIndex         =   getOptionsCount();
            if(actionIndex == pluginIndex)
                suicPanel.getComputePanel().computeSuicideIntent();
            
            else if(actionIndex == pluginIndex + 1)
                suicPanel.getSimPanel().executeDelete();
            
            else if(actionIndex == pluginIndex + 2)
                suicPanel.getSimPanel().clearDeadObjects();
            
            else if(actionIndex == pluginIndex + 3)
                suicPanel.getComputePanel().computeAverageSuicideIntent();
        }
    }
}
