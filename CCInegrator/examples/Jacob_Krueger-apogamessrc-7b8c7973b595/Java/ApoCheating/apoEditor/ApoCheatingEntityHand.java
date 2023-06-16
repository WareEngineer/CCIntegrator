// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingEntityHand.java

package apoEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

// Referenced classes of package apoEditor:
//            ApoCheatingEntity

public class ApoCheatingEntityHand extends ApoCheatingEntity
{

    public ApoCheatingEntityHand(BufferedImage iPaper, Color color, double x, double y, int width, 
            int height, int value)
    {
        super(color, x, y, width, height, value);
        up = 0;
        left = 0;
        bLeft = false;
        bFrame = false;
        frame = 0.0D;
        frameAdd = 0.0D;
        this.iPaper = iPaper;
        init();
    }

    public ApoCheatingEntityHand(BufferedImage iPaper, Color color, double x, double y, int width, 
            int height, boolean bDetect, int value)
    {
        super(color, x, y, width, height, bDetect, value);
        up = 0;
        left = 0;
        bLeft = false;
        bFrame = false;
        frame = 0.0D;
        frameAdd = 0.0D;
        this.iPaper = iPaper;
        init();
    }

    protected void init()
    {
        super.init();
        int random = (int)(Math.random() * 100D);
        if(random < 50)
            bLeft = true;
        else
            bLeft = false;
        bFrame = false;
        frame = Math.random() * 8D;
        frameAdd = Math.random() / 5D + 0.10000000000000001D;
    }

    protected void setWayHand(int up, int left)
    {
        this.up = up;
        this.left = left;
    }

    protected void addFrame()
    {
        if(!bFrame)
            frame += frameAdd;
        else
            frame -= frameAdd;
        if(frame >= 8D || frame <= 0.0D)
            bFrame = !bFrame;
    }

    protected void render(Graphics2D g)
    {
        java.awt.Stroke clone = g.getStroke();
        g.setStroke(new BasicStroke(2.0F, 0, 0));
        g.setColor(Color.BLACK);
        if(up == -1)
        {
            if(bLeft)
            {
                int x = ((int)getX() + getWidth() / 2) - iPaper.getWidth() / 4;
                int y = (int)getY() - 4 - iPaper.getHeight();
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), 0, 0, iPaper.getWidth() / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX(), (int)getY() + getHeight() / 2, (int)(getX() + frame + 4D), ((int)getY() + getHeight() / 2) - 22);
                g.fillRect((int)(getX() + frame) + 3, ((int)getY() + getHeight() / 2) - 22, 4, 4);
            } else
            {
                int x = ((int)getX() + getWidth() / 2) - iPaper.getWidth() / 4;
                int y = (int)getY() - 4 - iPaper.getHeight();
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), 0, 0, iPaper.getWidth() / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX() + getWidth(), (int)getY() + getHeight() / 2, ((int)(getX() - frame) - 6) + getWidth(), ((int)getY() + getHeight() / 2) - 22);
                g.fillRect(((int)(getX() - frame) - 5) + getWidth(), ((int)getY() + getHeight() / 2) - 22, 4, 4);
            }
        } else
        if(up == 1)
        {
            if(bLeft)
            {
                int x = ((int)getX() + getWidth() / 2) - iPaper.getWidth() / 4;
                int y = (int)getY() + getHeight() + 5;
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), 0, 0, 0 + iPaper.getWidth() / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX(), (int)getY() + getHeight() / 2, (int)(getX() + frame + 4D), (int)getY() + getHeight() / 2 + 22);
                g.fillRect((int)(getX() + frame) + 3, (int)getY() + getHeight() / 2 + 22, 4, 4);
            } else
            {
                int x = ((int)getX() + getWidth() / 2) - iPaper.getWidth() / 4;
                int y = (int)getY() + getHeight() + 5;
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), 0, 0, 0 + iPaper.getWidth() / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX() + getWidth(), (int)getY() + getHeight() / 2, ((int)(getX() - frame) - 6) + getWidth(), (int)getY() + getHeight() / 2 + 22);
                g.fillRect(((int)(getX() - frame) - 8) + getWidth(), (int)getY() + getHeight() / 2 + 22, 4, 4);
            }
        } else
        if(left == -1)
        {
            if(bLeft)
            {
                int x = (int)getX() - 6 - iPaper.getWidth() / 2;
                int y = (int)getY() + 3;
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), (1 * iPaper.getWidth()) / 2, 0, (2 * iPaper.getWidth()) / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX() + getWidth() / 2, (int)getY(), (int)((getX() + (double)(getWidth() / 2)) - 22D), (int)(getY() + frame) + 6);
                g.fillRect((int)((getX() + (double)(getWidth() / 2)) - 22D), (int)(getY() + frame) + 5, 4, 4);
            } else
            {
                int x = (int)getX() - 6 - iPaper.getWidth() / 2;
                int y = (int)getY() + 4;
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), (1 * iPaper.getWidth()) / 2, 0, (2 * iPaper.getWidth()) / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX() + getWidth() / 2, (int)getY() + getHeight(), (int)((getX() + (double)(getWidth() / 2)) - 22D), ((int)(getY() - frame) - 6) + getHeight());
                g.fillRect((int)((getX() + (double)(getWidth() / 2)) - 22D), ((int)(getY() - frame) - 5) + getHeight(), 4, 4);
            }
        } else
        if(left == 1)
            if(bLeft)
            {
                int x = (int)getX() + getWidth() + 6;
                int y = (int)getY() + 3;
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), (1 * iPaper.getWidth()) / 2, 0, (2 * iPaper.getWidth()) / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX() + getWidth() / 2, (int)getY(), (int)(getX() + (double)(getWidth() / 2) + 22D), (int)(getY() + frame) + 6);
                g.fillRect((int)(getX() + (double)(getWidth() / 2) + 22D), (int)(getY() + frame) + 5, 4, 4);
            } else
            {
                int x = (int)getX() + getWidth() + 6;
                int y = (int)getY() + 4;
                g.drawImage(iPaper, x, y, x + iPaper.getWidth() / 2, y + iPaper.getHeight(), (1 * iPaper.getWidth()) / 2, 0, (2 * iPaper.getWidth()) / 2, iPaper.getHeight(), null);
                g.drawLine((int)getX() + getWidth() / 2, (int)getY() + getHeight(), (int)(getX() + (double)(getWidth() / 2) + 22D), ((int)(getY() - frame) - 6) + getHeight());
                g.fillRect((int)(getX() + (double)(getWidth() / 2) + 22D), ((int)(getY() - frame) - 7) + getHeight(), 4, 4);
            }
        g.setStroke(clone);
        super.render(g);
    }

    private int up;
    private int left;
    private boolean bLeft;
    private boolean bFrame;
    private double frame;
    private double frameAdd;
    private BufferedImage iPaper;
}
