// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingImage.java

package apoEditor;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ApoCheatingImage extends Component
    implements ImageObserver
{

    public ApoCheatingImage()
    {
        C = this;
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public BufferedImage setPicsMain(String BildString, boolean bLoad)
    {
        MediaTracker mt = new MediaTracker(C);
        BufferedImage i = getImage(BildString, bLoad);
        mt.addImage(i, 0);
        try
        {
            mt.waitForAll();
        }
        catch(InterruptedException interruptedexception) { }
        width = i.getWidth(null);
        height = i.getHeight(null);
        return i;
    }

    private BufferedImage getImage(String BildName, boolean bLoad)
    {
        BufferedImage result = null;
        BufferedImage img = null;
        BufferedImage icon;
        if(!bLoad)
        {
            try
            {
                img = ImageIO.read(ApoCheatingImage.class.getResource(BildName));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            result = gc.createCompatibleImage(img.getWidth(), img.getHeight(), 3);
            result.createGraphics().drawImage(img, 0, 0, null);
            icon = result;
        } else
        {
            img = getBufferedImage((new ImageIcon(BildName)).getImage());
            result = gc.createCompatibleImage(img.getWidth(), img.getHeight(), 3);
            result.createGraphics().drawImage(img, 0, 0, null);
            icon = result;
        }
        return icon;
    }

    public BufferedImage getBufferedImage(Image img)
    {
        return toBufferedImage(img);
    }

    private boolean hasAlpha(Image image)
    {
        if(image instanceof BufferedImage)
        {
            BufferedImage bImage = (BufferedImage)image;
            return bImage.getColorModel().hasAlpha();
        }
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false);
        try
        {
            pixelGrabber.grabPixels();
        }
        catch(InterruptedException interruptedexception) { }
        ColorModel colorModel = pixelGrabber.getColorModel();
        return colorModel.hasAlpha();
    }

    private BufferedImage toBufferedImage(Image image)
    {
        if(image instanceof BufferedImage)
            return (BufferedImage)image;
        image = (new ImageIcon(image)).getImage();
        boolean hasAlpha = hasAlpha(image);
        BufferedImage bImage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try
        {
            int transparency = 1;
            if(hasAlpha)
                transparency = 2;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bImage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        }
        catch(HeadlessException headlessexception) { }
        if(bImage == null)
        {
            int type = 1;
            if(hasAlpha)
                type = 2;
            bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bImage.createGraphics();
        g.drawImage(image, 0, 0, this);
        g.dispose();
        return bImage;
    }

    public boolean imageUpdate(Image arg0, int arg1, int arg2, int i, int j, int k)
    {
        return false;
    }

    private static final long serialVersionUID = 1L;
    private Component C;
    private int width;
    private int height;
    private GraphicsConfiguration gc;
    static Class class$0;
}
