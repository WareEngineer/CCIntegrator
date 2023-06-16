// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoAIAStarSquare.java

package apoSkunkman.ai.ai;


public class ApoAIAStarSquare
{

    public ApoAIAStarSquare(int x, int y, int cost, int myCostToFinish)
    {
        this.x = x;
        this.y = y;
        myCost = cost;
        this.myCostToFinish = myCostToFinish;
        this.cost = 0;
        parent = null;
    }

    public final int getX()
    {
        return x;
    }

    public final int getY()
    {
        return y;
    }

    public void setMyCost(int myCost)
    {
        this.myCost = myCost;
    }

    public final int getMyCost()
    {
        return myCost;
    }

    public int getMyCostToFinish()
    {
        return myCostToFinish;
    }

    public final ApoAIAStarSquare getParent()
    {
        return parent;
    }

    public void setParent(ApoAIAStarSquare parent)
    {
        this.parent = parent;
    }

    public final int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public final int getAllCosts()
    {
        return cost + myCost + myCostToFinish;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(x))).append(" ").append(y).append(", ").append(myCost).append(" ").append(getAllCosts()).toString();
    }

    private final int x;
    private final int y;
    private int myCost;
    private int myCostToFinish;
    private ApoAIAStarSquare parent;
    private int cost;
}
