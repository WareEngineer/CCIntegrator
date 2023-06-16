// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingAI.java

package apoEditor;

import java.util.ArrayList;

// Referenced classes of package apoEditor:
//            ApoCheatingPlayer, ApoCheatingEnemy

public interface ApoCheatingAI
{

    public abstract String getName();

    public abstract String getAuthorName();

    public abstract void think(int ai[][], ApoCheatingPlayer apocheatingplayer, ArrayList arraylist, ApoCheatingEnemy apocheatingenemy, ArrayList arraylist1);
}
