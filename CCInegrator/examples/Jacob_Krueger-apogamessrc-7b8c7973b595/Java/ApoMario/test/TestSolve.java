// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TestSolve.java

package test;

import apoMario.ai.*;

public class TestSolve extends ApoMarioAI
{

    public TestSolve()
    {
    }

    public String getTeamName()
    {
        return "Shorti";
    }

    public String getAuthor()
    {
        return "KTMNJJPFJSFvuzG";
    }

    public void think(ApoMarioAIPlayer player, ApoMarioAILevel level)
    {
        byte map[] = level.getLevelArray()[level.getLevelArray().length - 1];
        player.runRight();
        player.runFast();
        for(int i = 5; i >= 0; i--)
        {
            if(i + (int)player.getX() < map.length - 1 && map[(int)player.getX() + 1] != 0 && map[(int)player.getX() + i] == 0 && player.getVecX() != 0.0F)
            {
                player.runNormal();
                return;
            }
            player.jump();
        }

    }
}
