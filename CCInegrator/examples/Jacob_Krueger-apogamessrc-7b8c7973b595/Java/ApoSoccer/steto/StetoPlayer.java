// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoPlayer.java

package steto;

import apoSoccer.entityForAI.ApoSoccerTeam;

// Referenced classes of package steto:
//            StetoEnemy, StetoHelp, StetoEntity, StetoPlayerPanel, 
//            StetoEntityPanel

public class StetoPlayer extends StetoEnemy
{

    public StetoPlayer(ApoSoccerTeam player)
    {
        super(player);
        this.player = player;
    }

    public int goTo(int x, int y, int speed)
    {
        return goTo(x, y, speed, true);
    }

    public int goTo(int x, int y, int speed, boolean stop)
    {
        if(x > 900 || x < 0 || y < 0 || y > 500)
            return 0;
        double distance = getDistanceToPoint(x, y);
        goToX = x;
        goToY = y;
        if(distance > 0.0D)
            if(Math.abs(getTurnAngleToPoint(x, y)) > 30 && getSpeed() > 0)
            {
                setAimSpeed(0);
            } else
            {
                turnToPoint(x, y);
                if(Math.abs(getTurnAngleToPoint(x, y)) <= 15)
                {
                    float nextSpeed = getSpeed();
                    if(speed < getSpeed())
                        nextSpeed += Math.max(getBreak(), speed - getSpeed());
                    else
                    if(speed >= getSpeed())
                        nextSpeed += Math.min(getAcceleration(), (float)(getFreshness() - getSpeed()) - getAcceleration());
                    if(StetoHelp.canPlayerStop(nextSpeed, distance - (double)((nextSpeed / 100F) * 4.5F)))
                        setAimSpeed(speed);
                    else
                        setAimSpeed(0);
                }
            }
        return -1;
    }

    public int turnToAngle(int aimAngle)
    {
        if(getAngle() != aimAngle)
            if(getAngle() < aimAngle && aimAngle - getAngle() < 180 || getAngle() > aimAngle && getAngle() - aimAngle > 180)
                changeAngle(Math.min(15, Math.abs(getAngle() - aimAngle)));
            else
                changeAngle(Math.max(-15, -Math.abs(getAngle() - aimAngle)));
        onStetoEntityChanged();
        return aimAngle - getAngle();
    }

    public int turnToEntity(StetoEntity entity)
    {
        return turnToPoint(entity.getX(), entity.getY());
    }

    public int turnToPoint(int x, int y)
    {
        return turnToAngle(getAngleToPoint(x, y));
    }

    public int getGoToX()
    {
        return goToX;
    }

    public int getGoToY()
    {
        return goToY;
    }

    public int getGoToSpeed()
    {
        return goToSpeed;
    }

    public void setShoot(int shootDirection, int shootStrength)
    {
        player.setShoot(shootDirection, shootStrength);
    }

    public void changeAngle(int difference)
    {
        player.setLineOfSight(difference);
    }

    public void setAimSpeed(int aimSpeed)
    {
        player.setSpeed(aimSpeed);
    }

    public void onThink()
    {
        setAimSpeed(0);
        onStetoEntityChanged();
    }

    public StetoEntityPanel getPanel()
    {
        if(panel == null)
            panel = new StetoPlayerPanel(this);
        return panel;
    }

    private int goToX;
    private int goToY;
    private int goToSpeed;
    private ApoSoccerTeam player;
    private StetoPlayerPanel panel;
}
