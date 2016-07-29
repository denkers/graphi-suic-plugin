///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import com.graphi.app.AppManager;
import com.graphi.plugins.AbstractPlugin;
import com.graphi.suicideintent.layout.PluginLayout;
import com.graphi.suicideintent.tasks.ClearDeleteTask;
import com.graphi.suicideintent.tasks.DiffKillTask;
import com.graphi.suicideintent.tasks.SelfEvalTask;
import com.graphi.suicideintent.tasks.SuicDeleteTask;
import com.graphi.suicideintent.tasks.SuicIndexTask;
import com.graphi.suicideintent.util.SuicideEdgeFactory;
import com.graphi.suicideintent.util.SuicideNodeFactory;
import com.graphi.tasks.TaskManager;
import com.graphi.util.GraphData;

public class SuicideIntentPlugin extends AbstractPlugin
{
    
    public SuicideIntentPlugin()
    {
        super("SuicideIntent", "Suicide intent plugin");
        initTasks();
    }
    
    @Override
    public void attachPanel(AppManager appManager)
    {
        panel   =   new PluginLayout(appManager);
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
    
    @Override
    public void passData(GraphData data)
    {
        data.setEdgeFactory(new SuicideEdgeFactory());
        data.setNodeFactory(new SuicideNodeFactory());
        panel.setGraphData(data);
    }
}
