///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.tasks;

import com.graphi.tasks.AbstractTask;

public class SelfEvalTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        name    =   "Compute Self-Perception";
    }

    @Override
    public void initDefaultProperties() 
    {
        properties.put("Compute all", "true");
        properties.put("Perspective ID", "-1");
    }

    @Override
    public void performTask()
    {
        boolean computeAll  =   properties.get("Compute all").equalsIgnoreCase("true");
        int perspectiveID   =   Integer.parseInt(properties.get("Perspective ID"));
    }
}
