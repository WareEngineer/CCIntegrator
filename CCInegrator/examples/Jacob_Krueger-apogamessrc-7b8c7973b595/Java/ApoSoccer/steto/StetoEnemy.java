// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoEnemy.java

package steto;

import apoSoccer.entityForAI.ApoSoccerEnemy;

// Referenced classes of package steto:
//            StetoEntity, StetoEntityPanel

public class StetoEnemy extends StetoEntity
{

    public StetoEnemy(ApoSoccerEnemy enemy)
    {
        this.enemy = enemy;
    }

    public final boolean canShot()
    {
        return enemy.canShoot();
    }

    public final int getAimSpeed()
    {
        return enemy.getAimSpeed();
    }

    public final int getAngle()
    {
        return enemy.getLineOfSight();
    }

    public final int getFreshness()
    {
        return enemy.getFreshness();
    }

    public final String getName()
    {
        return enemy.getName();
    }

    public final int getRadius()
    {
        return enemy.getRadius();
    }

    public final int getSpeed()
    {
        return enemy.getSpeed();
    }

    public final int getX()
    {
        return enemy.getX();
    }

    public final int getY()
    {
        return enemy.getY();
    }

    public final boolean isBallInArea()
    {
        return enemy.isBallInArea();
    }

    public final boolean isGoalKeeper()
    {
        return enemy.isGoalKeeper();
    }

    public final boolean isForward()
    {
        return enemy.isForward();
    }

    public StetoEntityPanel getPanel()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final boolean isLeftTeam()
    {
        return enemy.isLeftTeam();
    }

    public final boolean isRightTeam()
    {
        return !isLeftTeam();
    }

    public final float getAcceleration()
    {
        return !enemy.isGoalKeeper() ? 0.3F : 1.1F;
    }

    public final float getBreak()
    {
        return -4F;
    }

    public final float getMovesUntilSpeedCanBeReached(int aimSpeed)
    {
        return (float)(aimSpeed - getSpeed()) / (aimSpeed >= getSpeed() ? getAcceleration() : getBreak());
    }

    private ApoSoccerEnemy enemy;
}
