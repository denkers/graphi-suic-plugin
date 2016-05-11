
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
            
            switch(actionIndex)
            {
                case 3: suicPanel.getComputePanel().computeSuicideIntent(); break;
                case 4: suicPanel.getSimPanel().executeDelete(); break;
                case 5: suicPanel.getSimPanel().clearDeadObjects(); break;
                case 6: suicPanel.getComputePanel().computeAverageSuicideIntent(); break;
            }
        }
    }
}
