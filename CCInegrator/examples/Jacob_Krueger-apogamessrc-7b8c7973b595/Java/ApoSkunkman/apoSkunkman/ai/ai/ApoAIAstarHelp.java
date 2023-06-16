// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoAIAstarHelp.java

package apoSkunkman.ai.ai;

import java.util.ArrayList;

// Referenced classes of package apoSkunkman.ai.ai:
//            ApoAIAStarSquare

public class ApoAIAstarHelp
{

    public ApoAIAstarHelp(ArrayList bestWay, boolean bBack, ApoAIAStarSquare goalSquare)
    {
        this.bBack = bBack;
        this.bestWay = bestWay;
        this.goalSquare = goalSquare;
    }

    public ArrayList getBestWay()
    {
        return bestWay;
    }

    public boolean isBack()
    {
        return bBack;
    }

    public void setBack(boolean back)
    {
        bBack = back;
    }

    public ApoAIAStarSquare getGoalSquare()
    {
        return goalSquare;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(bestWay.toString()))).append(" ").append(goalSquare.toString()).toString();
    }

    private final ArrayList bestWay;
    private boolean bBack;
    private final ApoAIAStarSquare goalSquare;
}
