// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoBall.java

package steto;

import apoSoccer.entityForAI.ApoSoccerBall;
import java.util.ArrayList;
import java.util.Vector;

// Referenced classes of package steto:
//            StetoEntity, StetoBallPanel, StetoEntityCoordinate, StetoEntityPanel

public class StetoBall extends StetoEntity
{

    public StetoBall(ApoSoccerBall ball)
    {
        coordinates = new ArrayList();
        ballHitsWall = new Vector();
        this.ball = ball;
    }

    public StetoEntityPanel getPanel()
    {
        if(panel == null)
            panel = new StetoBallPanel(this);
        return panel;
    }

    public double getAimDistance()
    {
        return aimDistance;
    }

    public int getAimX()
    {
        return aimX;
    }

    public int getAimY()
    {
        return aimY;
    }

    public int getAngle()
    {
        return ball.getLineOfSight();
    }

    public int getSpeed()
    {
        return ball.getSpeed();
    }

    public int getX()
    {
        return ball.getX();
    }

    public int getY()
    {
        return ball.getY();
    }

    public boolean isAimingLeftGoal()
    {
        return aimsLeftGoal;
    }

    public boolean isAimingRightGoal()
    {
        return aimsRightGoal;
    }

    public int getRadius()
    {
        return ball.getRadius();
    }

    public void onThink()
    {
        aimX = getX();
        aimY = getY();
        aimDistance = 0.0D;
        aimsLeftGoal = false;
        aimsRightGoal = false;
        coordinates = new ArrayList();
        float currentSpeed = getSpeed();
        float currentX = getX();
        float currentY = getY();
        int currentAngle = getAngle();
        int count = 0;
        ballHitsWall = new Vector();
        for(boolean goal = false; count < 400 && !goal && currentSpeed > 0.0F; count++)
        {
            currentSpeed *= 0.995F;
            float radius = (6.5F * currentSpeed) / 100F;
            if(radius > 0.01F)
            {
                currentX += radius * (float)Math.cos(Math.toRadians(currentAngle));
                currentY += radius * (float)Math.sin(Math.toRadians(currentAngle));
            } else
            {
                currentSpeed = 0.0F;
            }
            goal = false;
            boolean wall = false;
            if(currentX + (float)getRadius() > 900F)
            {
                if(currentY >= 150F && currentY <= 350F)
                {
                    goal = true;
                } else
                {
                    currentAngle = correctAngle(180 - currentAngle);
                    currentSpeed *= 0.9F;
                    currentX = 900 - getRadius() - 1;
                    wall = true;
                }
            } else
            if(currentX < (float)getRadius())
                if(currentY >= 150F && currentY <= 350F)
                {
                    goal = true;
                } else
                {
                    currentAngle = correctAngle(180 - currentAngle);
                    currentSpeed *= 0.9F;
                    currentX = getRadius();
                    wall = true;
                }
            if(currentY + (float)getRadius() >= 500F)
            {
                currentAngle = correctAngle(360 - currentAngle);
                currentSpeed *= 0.9F;
                currentY = 500 - getRadius() - 1;
                wall = true;
            } else
            if(currentY < (float)getRadius())
            {
                currentAngle = correctAngle(360 - currentAngle);
                currentSpeed *= 0.9F;
                currentY = getRadius();
                wall = true;
            }
            if(goal)
            {
                ballHitsWall.add(new StetoEntityCoordinate((int)currentX, (int)currentY, (int)currentSpeed, currentAngle));
                if(currentX < 450F)
                    aimsLeftGoal = true;
                else
                    aimsRightGoal = true;
                break;
            }
            if(wall)
                ballHitsWall.add(new StetoEntityCoordinate((int)currentX, (int)currentY, (int)currentSpeed, currentAngle));
            coordinates.add(new StetoEntityCoordinate((int)currentX, (int)currentY, (int)currentSpeed, currentAngle));
        }

        onStetoEntityChanged();
    }

    private int correctAngle(int angle)
    {
        for(; angle < 0; angle += 360);
        return angle % 360;
    }

    public Vector getBallHitsWall()
    {
        return ballHitsWall;
    }

    public ArrayList getCoordinates()
    {
        return coordinates;
    }

    private ApoSoccerBall ball;
    private StetoBallPanel panel;
    private int aimX;
    private int aimY;
    private double aimDistance;
    private boolean aimsLeftGoal;
    private boolean aimsRightGoal;
    private ArrayList coordinates;
    private Vector ballHitsWall;
}
