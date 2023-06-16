// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingButtonEntity.java

package apoEditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// Referenced classes of package apoEditor:
//            ApoCheatingEntity

public class ApoCheatingButtonEntity extends ApoCheatingEntity
{

    public ApoCheatingButtonEntity(BufferedImage iBackground, int x, int y, int width, int height)
    {
        super(null, x, y, width, height, -1);
        this.iBackground = iBackground;
        bOver = false;
        bPressed = false;
        bVisible = true;
        bImage = true;
    }

    public ApoCheatingButtonEntity(Color backgroundColor, Color textColor, int x, int y, int width, int height)
    {
        super(null, x, y, width, height, -1);
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        bOver = false;
        bPressed = false;
        bVisible = true;
        bImage = false;
    }

    protected boolean isBOver()
    {
        return bOver;
    }

    protected void setBOver(boolean bOver)
    {
        this.bOver = bOver;
    }

    protected boolean isBPressed()
    {
        return bPressed;
    }

    protected void setBPressed(boolean bPressed)
    {
        this.bPressed = bPressed;
    }

    protected boolean isBVisible()
    {
        return bVisible;
    }

    protected void setBVisible(boolean bVisible)
    {
        this.bVisible = bVisible;
    }

    protected Color getTextColor()
    {
        return textColor;
    }

    protected Color getBackgroundColor()
    {
        return backgroundColor;
    }

    protected boolean getMove(int x, int y)
    {
        if(!bOver && getIn(x, y) && bVisible)
        {
            bOver = true;
            return true;
        }
        if(bOver && !getIn(x, y))
        {
            bOver = false;
            bPressed = false;
            return true;
        } else
        {
            return false;
        }
    }

    protected boolean getPressed(int x, int y)
    {
        if(bOver && getIn(x, y) && bVisible)
        {
            bPressed = true;
            return true;
        } else
        {
            return false;
        }
    }

    protected boolean getReleased(int x, int y)
    {
        if(bPressed && getIn(x, y) && bVisible)
        {
            bPressed = false;
            return true;
        } else
        {
            bPressed = false;
            bOver = false;
            return false;
        }
    }

    protected void render(Graphics2D g)
    {
        if(bVisible)
            if(bImage)
            {
                if(bPressed)
                    g.drawImage(iBackground, (int)getX(), (int)getY(), (int)getX() + getWidth(), (int)getY() + getHeight(), 2 * getWidth(), 0, 3 * getWidth(), getHeight(), null);
                else
                if(bOver)
                    g.drawImage(iBackground, (int)getX(), (int)getY(), (int)getX() + getWidth(), (int)getY() + getHeight(), 1 * getWidth(), 0, 2 * getWidth(), getHeight(), null);
                else
                    g.drawImage(iBackground, (int)getX(), (int)getY(), (int)getX() + getWidth(), (int)getY() + getHeight(), 0 * getWidth(), 0, 1 * getWidth(), getHeight(), null);
            } else
            {
                if(bPressed)
                    g.setColor(new Color(backgroundColor.getRed() / 3, backgroundColor.getGreen() / 3, backgroundColor.getBlue() / 3));
                else
                if(bOver)
                    g.setColor(new Color(backgroundColor.getRed() / 2, backgroundColor.getGreen() / 2, backgroundColor.getBlue() / 2));
                else
                    g.setColor(backgroundColor);
                g.fillRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 3, 3);
                g.setColor(Color.BLACK);
                g.drawRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 3, 3);
            }
    }

    private BufferedImage iBackground;
    private boolean bOver;
    private boolean bPressed;
    private boolean bVisible;
    private boolean bImage;
    private Color backgroundColor;
    private Color textColor;
}
