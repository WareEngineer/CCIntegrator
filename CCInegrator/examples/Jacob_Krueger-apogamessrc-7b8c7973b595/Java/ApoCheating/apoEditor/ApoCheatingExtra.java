// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingExtra.java

package apoEditor;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Referenced classes of package apoEditor:
//            ApoCheatingEntityHand

public class ApoCheatingExtra extends ApoCheatingEntityHand
{

    public ApoCheatingExtra(BufferedImage iPaper, int x, int y, int width, int height)
    {
        super(iPaper, Color.YELLOW, x, y, width, height, 1);
    }

    public ApoCheatingExtra(BufferedImage iPaper, int x, int y, int width, int height, boolean bDetect)
    {
        super(iPaper, Color.YELLOW, x, y, width, height, bDetect, 1);
    }
}
