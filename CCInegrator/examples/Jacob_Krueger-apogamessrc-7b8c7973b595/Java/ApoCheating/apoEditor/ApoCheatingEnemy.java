// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingEnemy.java

package apoEditor;


// Referenced classes of package apoEditor:
//            ApoCheatingPlayer

public class ApoCheatingEnemy
{

    public ApoCheatingEnemy(ApoCheatingPlayer player)
    {
        this.player = player;
    }

    public double getX()
    {
        return player.getX();
    }

    public double getY()
    {
        return player.getY();
    }

    public double getCheated()
    {
        return player.getCheated();
    }

    public double getDetected()
    {
        return player.getDetected();
    }

    private ApoCheatingPlayer player;
}
