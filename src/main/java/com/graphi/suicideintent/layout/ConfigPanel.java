package com.graphi.suicideintent.layout;

import com.graphi.suicideintent.SuicideIntentConfig;
import com.graphi.suicideintent.SuicideIntentPlugin;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ConfigPanel extends JPanel implements ActionListener
{
    private JButton reloadConfigBtn;
    private JLabel dirWeightLabel, undirWeightLabel, selfWeightLabel;
    private JLabel deadWeightLabel;

    public ConfigPanel()
    {
        setLayout(new MigLayout("fillx"));
        reloadConfigBtn     =   new JButton("Reload config");
        dirWeightLabel      =   new JLabel();
        undirWeightLabel    =   new JLabel();
        selfWeightLabel     =   new JLabel();
        deadWeightLabel     =   new JLabel();

        JLabel dirWeightTitle   =   new JLabel("Directed weight:");
        JLabel undirWeightTitle =   new JLabel("Undirected weight:");
        JLabel selfWeightTitle  =   new JLabel("Self weight:");
        JLabel deadWeightTitle  =   new JLabel("Dead weight:");
        Font titleFont          =   new Font("Arial", Font.BOLD, 12);

        dirWeightTitle.setFont(titleFont);
        undirWeightTitle.setFont(titleFont);
        selfWeightTitle.setFont(titleFont);
        deadWeightTitle.setFont(titleFont);

        add(dirWeightTitle);
        add(dirWeightLabel, "wrap");
        add(undirWeightTitle);
        add(undirWeightLabel, "wrap");
        add(selfWeightTitle);
        add(selfWeightLabel, "wrap");
        add(deadWeightTitle);
        add(deadWeightLabel, "wrap");
        add(reloadConfigBtn, "al center, span 2");

        updateConfig();
        reloadConfigBtn.addActionListener(this);
    }

    private void updateConfig()
    {
        SuicideIntentConfig config  =   SuicideIntentConfig.getConfig();
        dirWeightLabel.setText("" + config.getDirectedWeight());
        undirWeightLabel.setText("" + config.getUndirectedWeight());
        selfWeightLabel.setText("" + config.getSelfWeight());
        deadWeightLabel.setText("" + config.getDeadWeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();

        if(src == reloadConfigBtn)
        {
            SuicideIntentConfig.refreshConfig();
            updateConfig();
        }
    }
        
}