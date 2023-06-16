// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingButtonTextImage.java

package apoEditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// Referenced classes of package apoEditor:
//            ApoCheatingButtonText, ApoCheatingInterface

public class ApoCheatingButtonTextImage extends ApoCheatingButtonText
    implements ApoCheatingInterface
{

    public ApoCheatingButtonTextImage(BufferedImage iBackground, int imageX, int imageY, Color backgroundColor, Color textColor, int x, int y, 
            int width, int height, String function)
    {
        super(backgroundColor, textColor, x, y, width, height, function);
        this.iBackground = iBackground;
        imageStartX = imageX;
        imageFinishX = imageX + 1;
        imageStartY = imageY;
        imageFinishY = imageY + 1;
    }

    public ApoCheatingButtonTextImage(BufferedImage iBackground, int imageStartX, int imageStartY, int imageFinishX, int imageFinishY, Color backgroundColor, Color textColor, 
            int x, int y, int width, int height, String function)
    {
        super(backgroundColor, textColor, x, y, width, height, function);
        this.iBackground = iBackground;
        this.imageStartX = imageStartX;
        this.imageFinishX = imageFinishX;
        this.imageStartY = imageStartY;
        this.imageFinishY = imageFinishY;
    }

    public void render(Graphics2D g)
    {
        if(isBVisible())
        {
            if(isBPressed())
                g.setColor(new Color(getBackgroundColor().getRed() / 3, getBackgroundColor().getGreen() / 3, getBackgroundColor().getBlue() / 3));
            else
            if(isBOver())
                g.setColor(new Color(getBackgroundColor().getRed() / 2, getBackgroundColor().getGreen() / 2, getBackgroundColor().getBlue() / 2));
            else
                g.setColor(getBackgroundColor());
            g.fillRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 3, 3);
            g.setColor(Color.BLACK);
            g.drawRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 3, 3);
            g.drawImage(iBackground, (int)((getX() + (double)(getWidth() / 2)) - 16D), (int)((getY() + (double)(getHeight() / 2)) - 16D), (int)(getX() + (double)(getWidth() / 2) + 16D), (int)(getY() + (double)(getHeight() / 2) + 16D), imageStartX * 32, imageStartY * 32, imageFinishX * 32, imageFinishY * 32, null);
        }
    }

    private BufferedImage iBackground;
    private int imageStartX;
    private int imageFinishX;
    private int imageStartY;
    private int imageFinishY;
}
