// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoPlayerPanel.java

package steto;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

// Referenced classes of package steto:
//            StetoEntityPanel, StetoPlayer, StetoEntity

public class StetoPlayerPanel extends StetoEntityPanel
{

    public StetoPlayerPanel(StetoPlayer player)
    {
        this.player = player;
        player.addStetoEntityListener(this);
        setBorder(BorderFactory.createTitledBorder(player.isGoalKeeper() ? "Goalkeeper" : player.isForward() ? "Forward" : "Defender"));
        setLayout(new GridLayout(5, 2));
        add(new JLabel("Position: "));
        goalkeeperPos = new JLabel();
        add(goalkeeperPos);
        add(new JLabel("Angle: "));
        goalkeeperAngle = new JLabel();
        add(goalkeeperAngle);
        add(new JLabel("Speed: "));
        goalkeeperSpeed = new JLabel();
        add(goalkeeperSpeed);
        add(new JLabel("goTo:  "));
        goalkeeperGoToPos = new JLabel();
        add(goalkeeperGoToPos);
        add(new JLabel("goTo Speed:  "));
        goalkeeperGoToSpeed = new JLabel();
        add(goalkeeperGoToSpeed);
    }

    public void stetoEntityChanged(StetoEntity entity)
    {
        if((entity instanceof StetoPlayer) && (StetoPlayer)entity == player && player.isGoalKeeper())
        {
            goalkeeperPos.setText((new StringBuilder("x: ")).append(player.getX()).append("   y: ").append(player.getY()).toString());
            goalkeeperAngle.setText((new StringBuilder(String.valueOf(player.getAngle()))).append("\302\260").toString());
            goalkeeperSpeed.setText((new StringBuilder(String.valueOf(player.getSpeed()))).toString());
            goalkeeperGoToPos.setText((new StringBuilder("x: ")).append(player.getGoToX()).append("   y: ").append(player.getGoToY()).toString());
            goalkeeperGoToSpeed.setText((new StringBuilder(String.valueOf(player.getGoToSpeed()))).toString());
        }
    }

    private StetoPlayer player;
    private JLabel goalkeeperPos;
    private JLabel goalkeeperAngle;
    private JLabel goalkeeperSpeed;
    private JLabel goalkeeperGoToPos;
    private JLabel goalkeeperGoToSpeed;
}
