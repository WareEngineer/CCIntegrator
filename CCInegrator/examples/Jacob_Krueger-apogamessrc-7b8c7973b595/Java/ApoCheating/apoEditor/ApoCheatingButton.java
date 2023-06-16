// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingButton.java

package apoEditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// Referenced classes of package apoEditor:
//            ApoCheatingButtonEntity, ApoCheatingInterface

public class ApoCheatingButton extends ApoCheatingButtonEntity
    implements ApoCheatingInterface
{

    public ApoCheatingButton(BufferedImage iBackground, int x, int y, int width, int height, String function)
    {
        super(iBackground, x, y, width, height);
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
    }

    private String function;
}
