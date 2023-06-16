// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoBallPanel.java

package steto;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

// Referenced classes of package steto:
//            StetoEntityPanel, StetoBall, StetoEntityCoordinate, StetoEntity

public class StetoBallPanel extends StetoEntityPanel
{

    public StetoBallPanel(StetoBall ball)
    {
        this.ball = ball;
        this.ball.addStetoEntityListener(this);
        setBorder(BorderFactory.createTitledBorder("Ball"));
        setLayout(new GridLayout(0, 2));
        add(new JLabel("Position:  "));
        ballPosLabel = new JLabel();
        add(ballPosLabel);
        add(new JLabel("Ballrichtung:  "));
        ballAngleLabel = new JLabel();
        add(ballAngleLabel);
        add(new JLabel("Geschwindigkeit:  "));
        ballSpeedLabel = new JLabel();
        add(ballSpeedLabel);
        add(new JLabel("Einschlagsort:  "));
        ballAimLabel = new JLabel();
        add(ballAimLabel);
        add(new JLabel("Entfernung:  "));
        ballAimDistanceLabel = new JLabel();
        add(ballAimDistanceLabel);
        bhwLabels = new JLabel[5];
        for(int i = 0; i < bhwLabels.length; i++)
        {
            add(new JLabel((new StringBuilder("Einschlagort ")).append(i).append(":  ").toString()));
            bhwLabels[i] = new JLabel();
            bhwLabels[i].setFont(new Font("Dialog", 1, 12));
            add(bhwLabels[i]);
        }

    }

    public void stetoEntityChanged(StetoEntity entity)
    {
        if((entity instanceof StetoBall) && (StetoBall)entity == ball)
        {
            ballPosLabel.setText((new StringBuilder("x: ")).append(ball.getX()).append("    y: ").append(ball.getY()).toString());
            ballAngleLabel.setText((new StringBuilder(String.valueOf(ball.getAngle()))).append("\302\260").toString());
            ballSpeedLabel.setText((new StringBuilder(String.valueOf(ball.getSpeed()))).toString());
            Color foreground = Color.black;
            if(ball.isAimingLeftGoal())
                foreground = Color.red;
            else
            if(ball.isAimingRightGoal())
                foreground = Color.green;
            ballAimLabel.setForeground(foreground);
            ballAimLabel.setText((new StringBuilder("x: ")).append(ball.getAimX()).append("    y: ").append(ball.getAimY()).toString());
            ballAimDistanceLabel.setText((new StringBuilder(String.valueOf((int)ball.getAimDistance()))).toString());
            int count = 0;
            for(Iterator iterator = ball.getBallHitsWall().iterator(); iterator.hasNext();)
            {
                StetoEntityCoordinate coordinate = (StetoEntityCoordinate)iterator.next();
                String text = (new StringBuilder("x: ")).append(coordinate.getX()).toString();
                if(coordinate.getX() < 10)
                    text = (new StringBuilder(String.valueOf(text))).append("  ").toString();
                else
                if(coordinate.getX() < 100)
                    text = (new StringBuilder(String.valueOf(text))).append(" ").toString();
                text = (new StringBuilder(String.valueOf(text))).append("  y: ").append(coordinate.getY()).toString();
                bhwLabels[count++].setText(text);
                if(count >= bhwLabels.length)
                    break;
            }

            if(count < bhwLabels.length)
            {
                for(int i = count; i < bhwLabels.length; i++)
                    bhwLabels[i].setText("");

            }
            onStetoEntityPanelChanged();
        }
    }

    private static final long serialVersionUID = 1L;
    private StetoBall ball;
    private JLabel ballPosLabel;
    private JLabel ballAimLabel;
    private JLabel ballAngleLabel;
    private JLabel ballSpeedLabel;
    private JLabel ballAimDistanceLabel;
    private JLabel bhwLabels[];
}
