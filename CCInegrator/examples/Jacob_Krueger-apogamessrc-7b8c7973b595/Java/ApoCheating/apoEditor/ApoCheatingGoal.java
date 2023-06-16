// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingGoal.java

package apoEditor;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Referenced classes of package apoEditor:
//            ApoCheatingEntityHand, ApoCheatingPlayer

public class ApoCheatingGoal extends ApoCheatingEntityHand
{

    public ApoCheatingGoal(BufferedImage iPaper, int x, int y, int width, int height)
    {
        super(iPaper, Color.GREEN, x, y, width, height, 3);
        oldPlus = 0.29999999999999999D;
        init(oldPlus);
    }

    public ApoCheatingGoal(BufferedImage iPaper, int x, int y, int width, int height, boolean bDetect)
    {
        super(iPaper, Color.GREEN, x, y, width, height, bDetect, 3);
        oldPlus = 0.29999999999999999D;
        init(oldPlus);
    }

    public ApoCheatingGoal(BufferedImage iPaper, int x, int y, int width, int height, boolean bDetect, double plus)
    {
        super(iPaper, Color.GREEN, x, y, width, height, bDetect, 3);
        oldPlus = plus;
        init(oldPlus);
    }

    protected void init()
    {
        super.init();
        init(oldPlus);
    }

    private void init(double plus)
    {
        currentRed = Color.RED.getRed();
        currentGreen = Color.RED.getGreen();
        currentBlue = Color.RED.getBlue();
        this.plus = plus;
    }

    public void action(ApoCheatingPlayer player)
    {
        if(super.isIn(player))
        {
            if(!super.isBDetect())
                super.setBDetect(true);
            if(player.getCheated() < 100D)
            {
                player.setCheat(player.getCheated() + plus);
                double value = 2.5499999999999998D * plus;
                currentRed -= value;
                if(currentRed < 0.0D)
                    currentRed = 0.0D;
                currentGreen += value;
                if(currentGreen > 255D)
                    currentGreen = 255D;
                player.setPercentColor(new Color((int)currentRed, (int)currentGreen, (int)currentBlue));
            }
        }
    }

    protected double getOldPlus()
    {
        return oldPlus;
    }

    protected void setOldPlus(double oldPlus)
    {
        this.oldPlus = oldPlus;
    }

    private double currentRed;
    private double currentGreen;
    private double currentBlue;
    private double plus;
    private double oldPlus;
}
