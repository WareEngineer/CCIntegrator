// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FCM.java

package fcm;

import apoSoccer.ApoSoccerConstants;
import apoSoccer.entityForAI.*;
import java.awt.Color;
import java.awt.geom.*;
import java.io.PrintStream;
import java.util.ArrayList;

public class FCM extends ApoSoccerAI
{

    public FCM()
    {
        left = false;
    }

    public String getAuthor()
    {
        return "The Apo";
    }

    public String getTeamName()
    {
        return "1.FC Magdeburg";
    }

    public String[] getNames()
    {
        String names[] = new String[4];
        names[2] = "Christian B.";
        names[3] = "Radovan V.";
        names[0] = "Daniel R.";
        names[1] = "Catalin R.";
        return names;
    }

    public int[] getHair()
    {
        int hair[] = new int[4];
        hair[2] = 1;
        hair[3] = 25;
        hair[0] = 11;
        hair[1] = 7;
        return hair;
    }

    public boolean[] getGender()
    {
        boolean gender[] = new boolean[4];
        gender[2] = false;
        gender[3] = false;
        gender[0] = false;
        gender[1] = false;
        return gender;
    }

    public String getEmblem()
    {
        return "emblem.gif";
    }

    public Color getShirtColor()
    {
        return Color.blue;
    }

    public Color getTrouserColor()
    {
        return Color.white;
    }

    public void think(ApoSoccerBall ball, ArrayList ownTeam, ArrayList enemy)
    {
        left = ((ApoSoccerTeam)ownTeam.get(0)).isLeftTeam();
        java.awt.geom.Rectangle2D.Float mySide = left ? ApoSoccerConstants.LEFT_SIDE_REC : ApoSoccerConstants.RIGHT_SIDE_REC;
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
        ApoSoccerEnemy enemyKeeper = (ApoSoccerEnemy)enemy.get(2);
        defenderBehaivor(defender, ball, ballPosition, defenderPosition, forwardPosition, mySide, 0);
        forwardBehaivor(forward, ball, ballPosition, forwardPosition, (ApoSoccerEnemy)enemy.get(2), mySide, opGoal, true, enemyKeeper);
        forwardBehaivor(sweeper, ball, ballPosition, sweeperPosition, (ApoSoccerEnemy)enemy.get(2), mySide, opGoal, false, enemyKeeper);
        bApo = true;
        if(bApo)
            goalkeeperApo(keeper, ball, GOALKEEPER_LEFT, GOALKEEPER_RIGHT, enemyKeeper);
        else
            goalkeeperRef(keeper, ball, ballPosition, keeperPosition, forwardPosition, mySide, myGoal);
    }

    private boolean playerShoot(ApoSoccerTeam ownTeam, ApoSoccerBall ball, boolean bShootAlways, ApoSoccerEnemy keeper)
    {
        return playerShoot(ownTeam, ball, 150, bShootAlways, keeper);
    }

    private boolean playerShoot(ApoSoccerTeam ownTeam, ApoSoccerBall ball, int strength, boolean bShootAlways, ApoSoccerEnemy keeper)
    {
        ApoSoccerTeam player = ownTeam;
        if(player.canShoot())
        {
            int los = player.getAngleToPoint(ball.getX(), ball.getY());
            int magic = 0;
            int middle = player.getAngleToPoint(0.0F, 200 + magic, ball.getX(), ball.getY());
            if(keeper.getY() < 250)
                middle = player.getAngleToPoint(0.0F, 300 - magic, ball.getX(), ball.getY());
            if(this.left)
            {
                middle = player.getAngleToPoint(900F, 25 + magic, ball.getX(), ball.getY());
                if(keeper.getY() < 250)
                    middle = player.getAngleToPoint(900F, 225 - magic, ball.getX(), ball.getY());
                if(los > 270 || los < 90)
                {
                    int left;
                    for(left = los - 60; left < 0; left += 360);
                    int right;
                    for(right = los + 60; right >= 360; right -= 360);
                    boolean bMiddle = false;
                    if(left > 270)
                    {
                        if(right < 180)
                        {
                            if(left < middle || right > middle)
                                bMiddle = true;
                        } else
                        if(left < middle && right > middle)
                            bMiddle = true;
                    } else
                    if(left < middle && right > middle)
                        bMiddle = true;
                    if(bMiddle)
                    {
                        player.setShoot(middle, strength);
                        return true;
                    }
                    if(los > 270)
                        player.setShoot(los + 60, strength);
                    else
                        player.setShoot(los - 60, strength);
                } else
                if(bShootAlways)
                    if(los > 180)
                        player.setShoot(los + 60, strength);
                    else
                        player.setShoot(los - 60, strength);
            } else
            if(los > 90 && los < 270)
            {
                if(middle < los + 60 && middle > los - 60)
                {
                    player.setShoot(middle, strength);
                    return true;
                }
                if(los < 180)
                    player.setShoot(los + 60, strength);
                else
                    player.setShoot(los - 60, strength);
            } else
            if(bShootAlways)
                if(los >= 0)
                    player.setShoot(los + 60, strength);
                else
                    player.setShoot(los - 60, strength);
        }
        return true;
    }

    private boolean playerMoveToPoint(ApoSoccerBall ball, java.awt.geom.Point2D.Float point, int lineOfSight, int speed, ApoSoccerTeam player)
    {
        playerSight(player, point);
        if(player.isBallInArea() && !player.canShoot())
        {
            playerSight(player, new java.awt.geom.Point2D.Float(450F, 250F));
            player.setSpeed(0);
            return true;
        }
        if(point.x == (float)player.getX() && point.y == (float)player.getY())
        {
            player.setSpeed(0);
            playerSight(player, ball);
        } else
        if(Math.abs(point.x - (float)player.getX()) < 4F && Math.abs(point.y - (float)player.getY()) < 4F)
            player.setSpeed(3);
        else
        if(Math.abs(point.x - (float)player.getX()) < 15F && Math.abs(point.y - (float)player.getY()) < 15F)
            player.setSpeed(10);
        else
            player.setSpeed(speed);
        return true;
    }

    private boolean playerSight(ApoSoccerTeam player, ApoSoccerBall ball)
    {
        return playerSight(player, new java.awt.geom.Point2D.Float(ball.getX(), ball.getY()));
    }

    private boolean playerSight(ApoSoccerTeam player, java.awt.geom.Point2D.Float point)
    {
        int dif = 360 - player.getLineOfSight();
        int change = player.getAngleToPoint((float)point.getX(), (float)point.getY()) + dif;
        if(change > 360)
            change -= 360;
        if(change > 180)
            player.setLineOfSight(-Math.abs(change));
        else
            player.setLineOfSight(Math.abs(change));
        return true;
    }

    private void goalkeeperRef(ApoSoccerTeam keeper, ApoSoccerBall ball, java.awt.geom.Point2D.Float ballPosition, java.awt.geom.Point2D.Float keeperPosition, java.awt.geom.Point2D.Float forwardPosition, java.awt.geom.Rectangle2D.Float mySide, java.awt.geom.Line2D.Float myGoal)
    {
        java.awt.geom.Rectangle2D.Float myGoalArea = left ? ApoSoccerConstants.LEFT_GOALKEEPER_REC : ApoSoccerConstants.RIGHT_GOALKEEPER_REC;
        if(mySide.contains(ballPosition))
        {
            java.awt.geom.Point2D.Float estimate = new java.awt.geom.Point2D.Float();
            estimate.setLocation(ballPosition);
            if(left && (ball.getLineOfSight() <= 90 || ball.getLineOfSight() >= 270) || !left && ball.getLineOfSight() >= 90 && ball.getLineOfSight() <= 270)
                estimate.setLocation(myGoalArea.getCenterX(), Math.min(myGoal.getY2(), Math.max(myGoal.getY1(), ballPosition.getY())));
            else
                move(estimate, (float)(left ? -estimate.getX() : 900D - estimate.getX()) / cos(ball.getLineOfSight()) - 10F, ball.getLineOfSight());
            java.awt.geom.Point2D.Float shootTarget = new java.awt.geom.Point2D.Float();
            shootTarget.setLocation(forwardPosition);
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
            if(keeper.canShoot())
            {
                int angle = angleOf(ball.getX() - keeper.getX(), ball.getY() - keeper.getY());
                if(left && (angle < 80 || angle > 280) || !left && angle > 100 && angle < 260)
                    keeper.setShoot(keeper.getAngleToPoint(ball.getX(), ball.getY()), 150);
                else
                    keeper.setShoot(keeper.getAngleToPoint(ball.getX(), ball.getY()), 0);
            }
        } else
        {
            keeper.setLineOfSight(angleDif(keeper.getLineOfSight(), keeper.getAngleToPoint((float)myGoalArea.getCenterX(), (float)myGoalArea.getCenterY())));
            keeper.setSpeed(Math.max(0, (int)keeperPosition.distance((float)myGoalArea.getCenterX(), (float)myGoalArea.getCenterY()) - 30));
        }
    }

    private void goalkeeperApo(ApoSoccerTeam goalkeeper, ApoSoccerBall ball, java.awt.geom.Point2D.Float left, java.awt.geom.Point2D.Float right, ApoSoccerEnemy keeper)
    {
        int los = 0;
        if(!goalkeeper.isLeftTeam())
            los = 180;
        ArrayList goalPoint = getBallInGoal(ball, this.left);
        if(goalPoint == null)
        {
            if(this.left)
                playerMoveToPoint(ball, left, los, 100, goalkeeper);
            else
                playerMoveToPoint(ball, right, los, 100, goalkeeper);
        } else
        {
            java.awt.geom.Point2D.Float gPoint = null;
            if(goalPoint.size() >= 20)
                gPoint = new java.awt.geom.Point2D.Float(((java.awt.geom.Point2D.Float)goalPoint.get(13)).x, ((java.awt.geom.Point2D.Float)goalPoint.get(13)).y);
            else
                gPoint = new java.awt.geom.Point2D.Float(((java.awt.geom.Point2D.Float)goalPoint.get(0)).x, ((java.awt.geom.Point2D.Float)goalPoint.get(0)).y);
            playerMoveToPoint(ball, gPoint, goalkeeper.getAngleToPoint((float)gPoint.getX(), (float)gPoint.getY()), 50, goalkeeper);
        }
        playerShoot(goalkeeper, ball, true, keeper);
    }

    private ArrayList getBallInGoal(ApoSoccerBall ball, boolean bLeft)
    {
        ArrayList lastPoints = new ArrayList();
        int count = 0;
        java.awt.geom.Rectangle2D.Float leftGoal = new java.awt.geom.Rectangle2D.Float(-30F, 125F, 50F, 250F);
        java.awt.geom.Rectangle2D.Float rightGoal = new java.awt.geom.Rectangle2D.Float(880F, 125F, 50F, 250F);
        while(count <= 400) 
        {
            count++;
            if(ball.whereIsTheBallInXSteps(count) != null)
            {
                lastPoints.add(ball.whereIsTheBallInXSteps(count));
                if(lastPoints.size() > 30)
                    lastPoints.remove(0);
                if(leftGoal.intersects(((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getX(), ((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getY(), ball.getRadius(), ball.getRadius()) && bLeft)
                    return lastPoints;
                if(rightGoal.intersects(((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getX(), ((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getY(), ball.getRadius(), ball.getRadius()) && !bLeft)
                    return lastPoints;
            }
        }
        return null;
    }

    private void forwardBehaivor(ApoSoccerTeam forward, ApoSoccerBall ball, java.awt.geom.Point2D.Float ballPosition, java.awt.geom.Point2D.Float forwardPosition, ApoSoccerEnemy goalEnemy, java.awt.geom.Rectangle2D.Float mySide, java.awt.geom.Line2D.Float opGoal, 
            boolean bSide, ApoSoccerEnemy keeper)
    {
        if(!mySide.contains(ballPosition) || !bSide)
        {
            java.awt.geom.Point2D.Float estimate = estimateBallContact(forward, ball);
            java.awt.geom.Point2D.Float sT1 = new java.awt.geom.Point2D.Float((float)opGoal.getX1(), (float)(opGoal.getY1() * 4D + opGoal.getY2()) / 5F);
            java.awt.geom.Point2D.Float sT2 = new java.awt.geom.Point2D.Float((float)opGoal.getX1(), (float)(opGoal.getY1() + opGoal.getY2() * 4D) / 5F);
            java.awt.geom.Point2D.Float shootTarget = new java.awt.geom.Point2D.Float();
            shootTarget.setLocation(sT1.distance(goalEnemy.getX(), goalEnemy.getY()) <= sT2.distance(goalEnemy.getX(), goalEnemy.getY()) ? ((Point2D) (sT2)) : ((Point2D) (sT1)));
            int pDir = angleOf((float)(estimate.getX() - shootTarget.getX()), (float)(estimate.getY() - shootTarget.getY()));
            int fDir = angleOf((float)(forwardPosition.getX() - estimate.getX()), (float)(forwardPosition.getY() - estimate.getY()));
            int tDir = angleAdd(fDir, angleDif(fDir, pDir));
            java.awt.geom.Point2D.Float forwardTo = new java.awt.geom.Point2D.Float((float)estimate.getX(), (float)estimate.getY());
            move(forwardTo, Math.max(0, Math.abs(angleDif(fDir, pDir)) - 30), tDir);
            forward.setLineOfSight(angleDif(forward.getLineOfSight(), forward.getAngleToPoint((float)forwardTo.getX(), (float)forwardTo.getY())));
            forward.setSpeed((150 - Math.abs(angleDif(forward.getAngleToPoint((float)forwardTo.getX(), (float)forwardTo.getY()), forward.getLineOfSight()))) + (int)forwardPosition.distance(forwardTo));
            if(forward.canShoot())
                if(left && forward.getX() > 700 || !left && forward.getX() < 200)
                    playerShoot(forward, ball, false, keeper);
                else
                if(Math.abs(angleDif(forward.getAngleToPoint(ball.getX(), ball.getY()), forward.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()))) < 15)
                    playerShoot(forward, ball, false, keeper);
                else
                    forward.setShoot(forward.getAngleToPoint(ball.getX(), ball.getY()), 5);
        } else
        {
            forward.setLineOfSight(angleDif(forward.getLineOfSight(), forward.getAngleToPoint(450 + (left ? '\310' : -200), 250F)));
            forward.setSpeed(Math.max(0, (int)forwardPosition.distance(450 + (left ? '\310' : -200), 250D) - 30));
        }
    }

    private void defenderBehaivor(ApoSoccerTeam defender, ApoSoccerBall ball, java.awt.geom.Point2D.Float ballPosition, java.awt.geom.Point2D.Float defenderPosition, java.awt.geom.Point2D.Float forwardPosition, java.awt.geom.Rectangle2D.Float mySide, int y)
    {
        if(mySide.contains(ballPosition))
        {
            java.awt.geom.Point2D.Float estimate = estimateBallContact(defender, ball);
            java.awt.geom.Point2D.Float shootTarget = new java.awt.geom.Point2D.Float();
            shootTarget.setLocation(forwardPosition);
            int pDir = angleOf((float)(estimate.getX() - shootTarget.getX()), (float)(estimate.getY() - shootTarget.getY()));
            int fDir = angleOf((float)(defenderPosition.getX() - estimate.getX()), (float)(defenderPosition.getY() - estimate.getY()));
            int tDir = angleAdd(fDir, angleDif(fDir, pDir));
            java.awt.geom.Point2D.Float defenderTo = new java.awt.geom.Point2D.Float((float)estimate.getX(), (float)estimate.getY());
            move(defenderTo, Math.max(0, Math.abs(angleDif(fDir, pDir)) - 30), tDir);
            defender.setLineOfSight(angleDif(defender.getLineOfSight(), defender.getAngleToPoint((float)defenderTo.getX(), (float)defenderTo.getY())));
            defender.setSpeed((100 - angleDif(fDir, pDir)) + (int)defenderPosition.distance(defenderTo));
            if(defender.canShoot())
                if(Math.abs(angleDif(defender.getAngleToPoint(ball.getX(), ball.getY()), defender.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()))) < 15)
                    defender.setShoot(defender.getAngleToPoint((float)shootTarget.getX(), (float)shootTarget.getY()), 75);
                else
                    defender.setShoot(defender.getAngleToPoint(ball.getX(), ball.getY()), 0);
        } else
        {
            defender.setLineOfSight(angleDif(defender.getLineOfSight(), defender.getAngleToPoint(450 + (left ? -150 : '\226'), 250F)));
            defender.setSpeed(Math.max(0, (int)defenderPosition.distance(450 + (left ? -150 : '\226'), 250D) + y));
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

    protected float cos(int arc)
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

    private final java.awt.geom.Point2D.Float GOALKEEPER_LEFT = new java.awt.geom.Point2D.Float(15F, 250F);
    private final java.awt.geom.Point2D.Float GOALKEEPER_RIGHT = new java.awt.geom.Point2D.Float(887F, 250F);
    private boolean left;
    private boolean bApo;
}
