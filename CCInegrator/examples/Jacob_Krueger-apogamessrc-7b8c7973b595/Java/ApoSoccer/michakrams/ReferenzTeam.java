// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReferenzTeam.java

package michakrams;

import apoSoccer.ApoSoccerConstants;
import apoSoccer.entityForAI.*;
import java.awt.Color;
import java.awt.geom.*;
import java.io.PrintStream;
import java.util.ArrayList;

public class ReferenzTeam extends ApoSoccerAI
{

    public ReferenzTeam()
    {
    }

    public void setPosition(ApoSoccerTeamSet team[])
    {
        ApoSoccerTeamSet p = team[0];
        System.out.println("Aufstellung !!");
        if(p.isLeftTeam())
            p.setPosition(370, 230, 45);
        else
            p.setPosition(530, 230, 135);
        p = team[1];
        if(p.isLeftTeam())
            p.setPosition(370, 270, 315);
        else
            p.setPosition(530, 270, 225);
    }

    public String getAuthor()
    {
        return "Micha";
    }

    public String getTeamName()
    {
        return "Referenzteam";
    }

    public String[] getNames()
    {
        String names[] = {
            "Verteidiger 1", "Verteidiger 2", "Torwart", "St\374rmer"
        };
        return names;
    }

    public Color getShirtColor()
    {
        return new Color(255, 0, 0);
    }

    public Color getTrouserColor()
    {
        return new Color(0, 0, 0);
    }

    public String[] getSpeech()
    {
        return (new String[] {
            "Schuss", "Pass", "Annehmen", "Wegfausten"
        });
    }

    public void think(ApoSoccerBall ball, ArrayList ownTeam, ArrayList enemy)
    {
        boolean left = ((ApoSoccerTeam)ownTeam.get(0)).isLeftTeam();
        java.awt.geom.Rectangle2D.Float mySide = left ? ApoSoccerConstants.LEFT_SIDE_REC : ApoSoccerConstants.RIGHT_SIDE_REC;
        java.awt.geom.Rectangle2D.Float myGoalArea = left ? ApoSoccerConstants.LEFT_GOALKEEPER_REC : ApoSoccerConstants.RIGHT_GOALKEEPER_REC;
        java.awt.geom.Point2D.Float ballPosition = new java.awt.geom.Point2D.Float(ball.getX(), ball.getY());
        java.awt.geom.Line2D.Float opGoal;
        java.awt.geom.Line2D.Float myGoal;
        if(left)
        {
            opGoal = new java.awt.geom.Line2D.Float(ApoSoccerConstants.RIGHT_GOAL_REC.x, ApoSoccerConstants.RIGHT_GOAL_REC.y, ApoSoccerConstants.LEFT_GOAL_REC.x, ApoSoccerConstants.RIGHT_GOAL_REC.y + ApoSoccerConstants.RIGHT_GOAL_REC.height);
            myGoal = new java.awt.geom.Line2D.Float(ApoSoccerConstants.LEFT_GOAL_REC.x + ApoSoccerConstants.LEFT_GOAL_REC.width, ApoSoccerConstants.LEFT_GOAL_REC.y, ApoSoccerConstants.LEFT_GOAL_REC.x + ApoSoccerConstants.LEFT_GOAL_REC.width, ApoSoccerConstants.LEFT_GOAL_REC.y + ApoSoccerConstants.LEFT_GOAL_REC.height);
        } else
        {
            myGoal = new java.awt.geom.Line2D.Float(ApoSoccerConstants.RIGHT_GOAL_REC.x, ApoSoccerConstants.RIGHT_GOAL_REC.y, ApoSoccerConstants.LEFT_GOAL_REC.x, ApoSoccerConstants.RIGHT_GOAL_REC.y + ApoSoccerConstants.RIGHT_GOAL_REC.height);
            opGoal = new java.awt.geom.Line2D.Float(ApoSoccerConstants.LEFT_GOAL_REC.x + ApoSoccerConstants.LEFT_GOAL_REC.width, ApoSoccerConstants.LEFT_GOAL_REC.y, ApoSoccerConstants.LEFT_GOAL_REC.x + ApoSoccerConstants.LEFT_GOAL_REC.width, ApoSoccerConstants.LEFT_GOAL_REC.y + ApoSoccerConstants.LEFT_GOAL_REC.height);
        }
        ApoSoccerTeam defender = (ApoSoccerTeam)ownTeam.get(left ? 0 : 1);
        java.awt.geom.Point2D.Float defenderPosition = new java.awt.geom.Point2D.Float(defender.getX(), defender.getY());
        ApoSoccerTeam sweeper = (ApoSoccerTeam)ownTeam.get(left ? 1 : 0);
        java.awt.geom.Point2D.Float sweeperPosition = new java.awt.geom.Point2D.Float(sweeper.getX(), sweeper.getY());
        ApoSoccerTeam forward = (ApoSoccerTeam)ownTeam.get(3);
        java.awt.geom.Point2D.Float forwardPosition = new java.awt.geom.Point2D.Float(forward.getX(), forward.getY());
        ApoSoccerTeam keeper = (ApoSoccerTeam)ownTeam.get(2);
        java.awt.geom.Point2D.Float keeperPosition = new java.awt.geom.Point2D.Float(keeper.getX(), keeper.getY());
        java.awt.geom.Point2D.Float estimate = estimateBallContact(sweeper, ball);
        java.awt.geom.Point2D.Float shootTarget = new java.awt.geom.Point2D.Float();
        if(mySide.contains(ballPosition))
            shootTarget.setLocation(forwardPosition);
        else
            shootTarget.setLocation((float)opGoal.getX1(), (float)(opGoal.getY1() + opGoal.getY2()) / 2.0F);
        int pDir = angleOf((float)(estimate.getX() - shootTarget.getX()), (float)(estimate.getY() - shootTarget.getY()));
        int fDir = angleOf((float)(sweeperPosition.getX() - estimate.getX()), (float)(sweeperPosition.getY() - estimate.getY()));
        int tDir = angleAdd(fDir, angleDif(fDir, pDir));
        java.awt.geom.Point2D.Float sweeperTo = new java.awt.geom.Point2D.Float((float)estimate.getX(), (float)estimate.getY());
        move(sweeperTo, Math.max(0, Math.abs(angleDif(fDir, pDir)) - 30), tDir);
        sweeper.setLineOfSight(angleDif(sweeper.getLineOfSight(), sweeper.getAngleToPoint((float)sweeperTo.getX(), (float)sweeperTo.getY())));
        sweeper.setSpeed((80 - angleDif(fDir, pDir)) + (int)sweeperPosition.distance(sweeperTo));
        sweeper.drawLine(sweeper.getX(), sweeper.getY(), (new Double(sweeperTo.getX())).intValue(), (new Double(sweeperTo.getY())).intValue(), 50, new Color(0, 0, 255));
        if(sweeper.canShoot())
            if(Math.abs(angleDif(sweeper.getAngleToPoint(ball.getX(), ball.getY()), sweeper.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()))) < 15)
            {
                sweeper.setShoot(sweeper.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()), 150 / (mySide.contains(ballPosition) ? 2 : 1));
                if(sweeper.canSpeak())
                    sweeper.speakText(mySide.contains(ballPosition) ? 1 : 0);
            } else
            {
                sweeper.setShoot(sweeper.getAngleToPoint(ball.getX(), ball.getY()), 0);
                if(sweeper.canSpeak())
                    sweeper.speakText(2);
            }
        if(mySide.contains(ballPosition))
        {
            estimate = estimateBallContact(defender, ball);
            shootTarget = new java.awt.geom.Point2D.Float();
            shootTarget.setLocation(forwardPosition);
            pDir = angleOf((float)(estimate.getX() - shootTarget.getX()), (float)(estimate.getY() - shootTarget.getY()));
            fDir = angleOf((float)(defenderPosition.getX() - estimate.getX()), (float)(defenderPosition.getY() - estimate.getY()));
            tDir = angleAdd(fDir, angleDif(fDir, pDir));
            java.awt.geom.Point2D.Float defenderTo = new java.awt.geom.Point2D.Float((float)estimate.getX(), (float)estimate.getY());
            move(defenderTo, Math.max(0, Math.abs(angleDif(fDir, pDir)) - 30), tDir);
            defender.setLineOfSight(angleDif(defender.getLineOfSight(), defender.getAngleToPoint((float)defenderTo.getX(), (float)defenderTo.getY())));
            defender.setSpeed((100 - angleDif(fDir, pDir)) + (int)defenderPosition.distance(defenderTo));
            defender.drawLine(defender.getX(), defender.getY(), (new Double(defenderTo.getX())).intValue(), (new Double(defenderTo.getY())).intValue(), 50, new Color(255, 0, 0));
            if(defender.canShoot())
                if(Math.abs(angleDif(defender.getAngleToPoint(ball.getX(), ball.getY()), defender.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()))) < 15)
                {
                    defender.setShoot(defender.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()), 75);
                    if(defender.canSpeak())
                        defender.speakText(1);
                } else
                {
                    defender.setShoot(defender.getAngleToPoint(ball.getX(), ball.getY()), 0);
                    if(defender.canSpeak())
                        defender.speakText(2);
                }
        } else
        {
            defender.setLineOfSight(angleDif(defender.getLineOfSight(), defender.getAngleToPoint(450 + (left ? -200 : '\310'), 250F)));
            defender.setSpeed(Math.max(0, (int)defenderPosition.distance(450 + (left ? -200 : '\310'), 250D) - 30));
        }
        if(!mySide.contains(ballPosition))
        {
            estimate = estimateBallContact(forward, ball);
            java.awt.geom.Point2D.Float sT1 = new java.awt.geom.Point2D.Float((float)opGoal.getX1(), (float)(opGoal.getY1() * 4D + opGoal.getY2()) / 5F);
            java.awt.geom.Point2D.Float sT2 = new java.awt.geom.Point2D.Float((float)opGoal.getX1(), (float)(opGoal.getY1() + opGoal.getY2() * 4D) / 5F);
            shootTarget = new java.awt.geom.Point2D.Float();
            shootTarget.setLocation(sT1.distance(((ApoSoccerEnemy)enemy.get(2)).getX(), ((ApoSoccerEnemy)enemy.get(2)).getY()) <= sT2.distance(((ApoSoccerEnemy)enemy.get(2)).getX(), ((ApoSoccerEnemy)enemy.get(2)).getY()) ? ((Point2D) (sT2)) : ((Point2D) (sT1)));
            pDir = angleOf((float)(estimate.getX() - shootTarget.getX()), (float)(estimate.getY() - shootTarget.getY()));
            fDir = angleOf((float)(forwardPosition.getX() - estimate.getX()), (float)(forwardPosition.getY() - estimate.getY()));
            tDir = angleAdd(fDir, angleDif(fDir, pDir));
            java.awt.geom.Point2D.Float forwardTo = new java.awt.geom.Point2D.Float((float)estimate.getX(), (float)estimate.getY());
            move(forwardTo, Math.max(0, Math.abs(angleDif(fDir, pDir)) - 30), tDir);
            forward.setLineOfSight(angleDif(forward.getLineOfSight(), forward.getAngleToPoint((float)forwardTo.getX(), (float)forwardTo.getY())));
            forward.setSpeed((150 - Math.abs(angleDif(forward.getAngleToPoint((float)forwardTo.getX(), (float)forwardTo.getY()), forward.getLineOfSight()))) + (int)forwardPosition.distance(forwardTo));
            forward.drawLine(forward.getX(), forward.getY(), (new Double(forwardTo.getX())).intValue(), (new Double(forwardTo.getY())).intValue(), 50, new Color(0, 255, 0));
            if(forward.canShoot())
                if(Math.abs(angleDif(forward.getAngleToPoint(ball.getX(), ball.getY()), forward.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()))) < 15)
                {
                    forward.setShoot(forward.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()), 150);
                    if(forward.canSpeak())
                        forward.speakText(0);
                } else
                {
                    forward.setShoot(forward.getAngleToPoint(ball.getX(), ball.getY()), 0);
                    if(forward.canSpeak())
                        forward.speakText(2);
                }
        } else
        {
            forward.setLineOfSight(angleDif(forward.getLineOfSight(), forward.getAngleToPoint(450 + (left ? '\310' : -200), 250F)));
            forward.setSpeed(Math.max(0, (int)forwardPosition.distance(450 + (left ? '\310' : -200), 250D) - 30));
        }
        if(mySide.contains(ballPosition))
        {
            estimate = new java.awt.geom.Point2D.Float();
            estimate.setLocation(ballPosition);
            if(left && (ball.getLineOfSight() <= 90 || ball.getLineOfSight() >= 270) || !left && ball.getLineOfSight() >= 90 && ball.getLineOfSight() <= 270)
                estimate.setLocation(myGoalArea.getCenterX(), Math.min(myGoal.getY2(), Math.max(myGoal.getY1(), ballPosition.getY())));
            else
                move(estimate, (float)(left ? -estimate.getX() : 900D - estimate.getX()) / cos(ball.getLineOfSight()) - 10F, ball.getLineOfSight());
            java.awt.geom.Point2D.Float sT1 = new java.awt.geom.Point2D.Float();
            sT1.setLocation(forwardPosition);
            java.awt.geom.Point2D.Float keeperTo = new java.awt.geom.Point2D.Float((float)estimate.getX(), (float)estimate.getY());
            if(keeperPosition.distance(ballPosition) > (double)myGoalArea.width)
            {
                keeper.setLineOfSight(angleDif(keeper.getLineOfSight(), keeper.getAngleToPoint((float)keeperTo.getX(), (float)keeperTo.getY())));
                keeper.setSpeed((int)keeperPosition.distance(keeperTo) * 5);
            } else
            {
                keeperTo.setLocation(ballPosition);
                int setAngle = angleDif(keeper.getLineOfSight(), keeper.getAngleToPoint((float)keeperTo.getX(), (float)keeperTo.getY()));
                keeper.setLineOfSight(setAngle);
                keeper.setSpeed(Math.abs(setAngle) >= 80 ? 0 : 100);
            }
            keeper.drawLine(keeper.getX(), keeper.getY(), (new Double(keeperTo.getX())).intValue(), (new Double(keeperTo.getY())).intValue(), 50, new Color(0, 0, 0));
            if(keeper.canShoot())
            {
                int angle = angleOf(ball.getX() - keeper.getX(), ball.getY() - keeper.getY());
                if(left && (angle < 80 || angle > 280) || !left && angle > 100 && angle < 260)
                {
                    keeper.setShoot(keeper.getAngleToPoint(ball.getX(), ball.getY()), 150);
                    if(keeper.canSpeak())
                        keeper.speakText(3);
                } else
                {
                    keeper.setShoot(keeper.getAngleToPoint(ball.getX(), ball.getY()), 0);
                    if(keeper.canSpeak())
                        keeper.speakText(2);
                }
            }
        } else
        {
            keeper.setLineOfSight(angleDif(keeper.getLineOfSight(), keeper.getAngleToPoint((float)myGoalArea.getCenterX(), (float)myGoalArea.getCenterY())));
            keeper.setSpeed(Math.max(0, (int)keeperPosition.distance((float)myGoalArea.getCenterX(), (float)myGoalArea.getCenterY()) - 30));
        }
    }

    private void move(java.awt.geom.Point2D.Float point, float dist, int direction)
    {
        if(dist <= 1.0F)
            return;
        float toX = point.x + dist * (float)Math.cos(((double)direction * 3.1415926535897931D) / 180D);
        float toY = point.y + dist * (float)Math.sin(((double)direction * 3.1415926535897931D) / 180D);
        if(direction == 0)
        {
            toX = (float)point.getX() + dist;
            toY = (float)point.getY();
        }
        if(direction == 90)
        {
            toX = (float)point.getX();
            toY = (float)point.getY() + dist;
        }
        if(direction == 180)
        {
            toX = (float)point.getX() - dist;
            toY = (float)point.getY();
        }
        if(direction == 270)
        {
            toX = (float)point.getX();
            toY = (float)point.getY() - dist;
        }
        int count = 0;
        while(toX < 0.0F || toX > 900F) 
        {
            if(++count > 1000)
            {
                System.out.println((new StringBuilder("distance: ")).append(dist).append(" direction: ").append(direction).toString());
                try
                {
                    throw new Exception();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return;
            }
            if(toX < 0.0F)
                toX = -toX;
            if(toX > 900F)
                toX = 1800F - toX;
        }
        while(toY < 0.0F || toY > 500F) 
        {
            if(++count > 1000)
            {
                System.out.println((new StringBuilder("distance: ")).append(dist).append(" direction: ").append(direction).toString());
                try
                {
                    throw new Exception();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return;
            }
            if(toY < 0.0F)
                toY = -toY;
            if(toY > 500F)
                toY = 1000F - toY;
        }
        point.setLocation(toX, toY);
    }

    private int angleDif(int from, int to)
    {
        int out = to - from;
        if(out < -180)
            out += 360;
        if(out > 180)
            out -= 360;
        return out;
    }

    private int angleAdd(int angle1, int angle2)
    {
        int out = angle1 + angle2;
        if(out > 360)
            out -= 360;
        if(out < 0)
            out += 360;
        return out;
    }

    protected float sin(int arc)
    {
        return (float)Math.sin(((double)arc * 3.1415926535897931D) / 180D);
    }

    private float cos(int arc)
    {
        return (float)Math.cos(((double)arc * 3.1415926535897931D) / 180D);
    }

    private int angleOf(float dx, float dy)
    {
        int arc = (int)(57.295779513082323D * Math.atan2(dy, dx));
        if(arc < 0)
            arc += 360;
        return arc;
    }

    private java.awt.geom.Point2D.Float estimateBallContact(ApoSoccerTeam player, ApoSoccerBall ball)
    {
        java.awt.geom.Point2D.Float estimate = new java.awt.geom.Point2D.Float(ball.getX(), ball.getY());
        float playerSpeed = Math.max(player.getSpeed(), 1);
        float ballSpeed = Math.max(ball.getSpeed(), 1);
        for(int i = 1; estimate.distance(player.getX(), player.getY()) > (double)(((float)i * playerSpeed) / ballSpeed); i++)
        {
            estimate.setLocation(ball.getX(), ball.getY());
            move(estimate, i, ball.getLineOfSight());
        }

        return estimate;
    }
}
