///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.display.layout.controls.ControlPanel;
import com.graphi.suicideintent.tasks.ClearDeleteTask;
import com.graphi.suicideintent.tasks.DiffKillTask;
import com.graphi.suicideintent.tasks.SelfEvalTask;
import com.graphi.suicideintent.tasks.SuicDeleteTask;
import com.graphi.suicideintent.tasks.SuicIndexTask;
import com.graphi.tasks.TaskManager;
import java.awt.Dimension;
import javax.swing.Box;


public class SuicideControlPanel extends ControlPanel
{
    private SuicidePanel suicidePanel; 
    
    public SuicideControlPanel(PluginLayout mainPanel)
    {
        super(mainPanel);
        
        suicidePanel =   new SuicidePanel(mainPanel);
        
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(suicidePanel);
    }
    
    @Override
    protected void initControls()
    {
        initTasks();
        super.initControls();
    }
    
    private void initTasks()
    {
        TaskManager tm  =   TaskManager.getInstance();
        tm.registerTask(new ClearDeleteTask());
        tm.registerTask(new DiffKillTask());
        tm.registerTask(new SelfEvalTask());
        tm.registerTask(new SuicDeleteTask());
        tm.registerTask(new SuicIndexTask());
    }
    
    public SuicidePanel getSuicidePanel()
    {
        return suicidePanel;
    }
    
    public static SuicidePanel getSuicidePanelInstance()
    {
        return ((SuicideControlPanel) PluginLayout.getInstance().getControlPanel()).getSuicidePanel();
    }
}
