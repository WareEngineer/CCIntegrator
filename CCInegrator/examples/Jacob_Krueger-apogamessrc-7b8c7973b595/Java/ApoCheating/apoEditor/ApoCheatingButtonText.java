// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingButtonText.java

package apoEditor;

import java.awt.*;

// Referenced classes of package apoEditor:
//            ApoCheatingButtonEntity, ApoCheatingInterface

public class ApoCheatingButtonText extends ApoCheatingButtonEntity
    implements ApoCheatingInterface
{

    public ApoCheatingButtonText(Color backgroundColor, Color textColor, int x, int y, int width, int height, String function)
    {
        super(backgroundColor, textColor, x, y, width, height);
        this.function = function;
    }

    protected String getFunction()
    {
        return function;
    }

    protected void setFunction(String function)
    {
        this.function = function;
    }

    public void render(Graphics2D g)
    {
        super.render(g);
        if(isBVisible())
        {
            int w = g.getFontMetrics().stringWidth(function);
            if(w + 4 > getWidth())
                setWidth(w + 4);
            g.setColor(getTextColor());
            g.drawString(function, (int)(getX() + (double)(getWidth() / 2 - w / 2)), (int)(getY() + (double)(getHeight() / 2) + 5D));
        }
    }

    private String function;
}
