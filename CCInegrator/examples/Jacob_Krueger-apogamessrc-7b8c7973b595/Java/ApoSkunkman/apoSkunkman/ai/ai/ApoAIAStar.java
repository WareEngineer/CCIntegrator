// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoAIAStar.java

package apoSkunkman.ai.ai;

import apoSkunkman.ai.ApoSkunkmanAILevel;
import apoSkunkman.ai.ApoSkunkmanAILevelSkunkman;
import java.awt.Point;
import java.util.ArrayList;

// Referenced classes of package apoSkunkman.ai.ai:
//            ApoAIAStarSquare, ApoAIAstarHelp

public class ApoAIAStar
{

    public ApoAIAStar()
    {
        opened = new ArrayList();
        closed = new ArrayList();
        bestList = new ArrayList();
    }

    public ApoAIAstarHelp getBestWay(ApoSkunkmanAILevel skunkmanLevel, Point start, Point goal)
    {
        return getBestWay(skunkmanLevel, start, goal, true);
    }

    private ApoAIAstarHelp getBestWay(ApoSkunkmanAILevel skunkmanLevel, Point start, Point goal, boolean bRepeat)
    {
        byte level[][] = skunkmanLevel.getLevelAsByte();
        this.goal = (Point)goal.clone();
        this.start = (Point)start.clone();
        this.level = new ApoAIAStarSquare[level.length][level[0].length];
        for(int y = 0; y < this.level.length; y++)
        {
            for(int x = 0; x < this.level[0].length; x++)
            {
                int costToFinish = getCostToGoal(x, y);
                if(level[y][x] == 0)
                    this.level[y][x] = new ApoAIAStarSquare(x, y, 1, costToFinish);
                else
                if(level[y][x] == 2)
                    this.level[y][x] = new ApoAIAStarSquare(x, y, 40000, costToFinish);
                else
                if(level[y][x] == 4)
                    this.level[y][x] = new ApoAIAStarSquare(x, y, 200, costToFinish);
                else
                if(level[y][x] == 3)
                    this.level[y][x] = new ApoAIAStarSquare(x, y, 1, costToFinish);
            }

        }

        for(int y = 0; y < this.level.length; y++)
        {
            for(int x = 0; x < this.level[0].length; x++)
                if(level[y][x] == 4)
                {
                    int width = skunkmanLevel.getSkunkman(y, x).getSkunkWidth();
                    setCostWithSkunk(x, y, -1, 0, width);
                    setCostWithSkunk(x, y, 1, 0, width);
                    setCostWithSkunk(x, y, 0, 1, width);
                    setCostWithSkunk(x, y, 0, -1, width);
                }

        }

        boolean bBack = false;
        if(bRepeat)
        {
            if(this.level[this.start.y][this.start.x] != null)
            {
                if(this.level[this.start.y][this.start.x].getMyCost() > 100)
                    return findNewGoal(skunkmanLevel, start);
                findBestWay();
                addDirectionToBestWay(this.goal.x, this.goal.y);
            }
        } else
        {
            bBack = true;
            findBestWay();
            addDirectionToBestWay(this.goal.x, this.goal.y);
        }
        ApoAIAstarHelp help = new ApoAIAstarHelp(bestList, bBack, this.level[this.goal.y][this.goal.x]);
        return help;
    }

    private ApoAIAstarHelp findNewGoal(ApoSkunkmanAILevel skunkmanLevel, Point start)
    {
        ArrayList aroundList = new ArrayList();
        int max = 8;
        int startX = start.x - max;
        if(startX <= 0)
            startX = 0;
        int endX = start.x + max;
        if(endX >= level[0].length)
            endX = level[0].length - 1;
        int startY = start.y - max;
        if(startY <= 0)
            startY = 0;
        int endY = start.y + max;
        if(endY >= level.length)
            endY = level.length - 1;
        for(int y = startY; y <= endY; y++)
        {
            for(int x = startX; x <= endX; x++)
                if(level[y][x] != null && (this.start.x != x || this.start.y != y))
                {
                    ApoAIAStar aStar = new ApoAIAStar();
                    ApoAIAstarHelp help = aStar.getBestWay(skunkmanLevel, start, new Point(x, y), false);
                    if(help.getGoalSquare().getMyCost() < 200 && help.getBestWay().size() > 0)
                        if(aroundList.size() <= 0)
                        {
                            aroundList.add(help);
                        } else
                        {
                            boolean bBreak = false;
                            for(int h = 0; h < aroundList.size(); h++)
                            {
                                if(((ApoAIAstarHelp)aroundList.get(h)).getGoalSquare().getAllCosts() == help.getGoalSquare().getAllCosts())
                                {
                                    if(getCostToGoal(((ApoAIAstarHelp)aroundList.get(h)).getGoalSquare().getX(), ((ApoAIAstarHelp)aroundList.get(h)).getGoalSquare().getY()) <= getCostToGoal(help.getGoalSquare().getX(), help.getGoalSquare().getY()))
                                        continue;
                                    aroundList.add(h, help);
                                    bBreak = true;
                                    break;
                                }
                                if(((ApoAIAstarHelp)aroundList.get(h)).getGoalSquare().getAllCosts() <= help.getGoalSquare().getAllCosts())
                                    continue;
                                aroundList.add(h, help);
                                bBreak = true;
                                break;
                            }

                            if(!bBreak)
                                aroundList.add(help);
                        }
                }

        }

        if(aroundList.size() > 0)
        {
            ((ApoAIAstarHelp)aroundList.get(0)).setBack(true);
            return (ApoAIAstarHelp)aroundList.get(0);
        } else
        {
            return null;
        }
    }

    private void setCostWithSkunk(int x, int y, int changeX, int changeY, int width)
    {
        for(int i = 1; i <= width; i++)
        {
            if(level[y + changeY * i][x + changeX * i] == null)
                return;
            if(level[y + changeY * i][x + changeX * i].getMyCost() == 40000)
            {
                level[y + changeY * i][x + changeX * i].setMyCost(200 + 40000);
                return;
            }
            level[y + changeY * i][x + changeX * i].setMyCost(200);
        }

    }

    private void findBestWay()
    {
        opened = new ArrayList();
        closed = new ArrayList();
        bestList = new ArrayList();
        opened.add(level[start.y][start.x]);
        ApoAIAStarSquare square;
        int x;
        int y;
        for(; opened.size() > 0; checkNewSquare(square, x, y, 0, 1))
        {
            square = (ApoAIAStarSquare)opened.get(0);
            opened.remove(square);
            closed.add(square);
            x = square.getX();
            y = square.getY();
            if(goal.x == x && goal.y == y)
                return;
            checkNewSquare(square, x, y, -1, 0);
            checkNewSquare(square, x, y, 1, 0);
            checkNewSquare(square, x, y, 0, -1);
        }

    }

    private void addDirectionToBestWay(int x, int y)
    {
        if(x < 0 || y < 0)
            return;
        ApoAIAStarSquare square = level[y][x];
        if(start == null || square == null || x == start.x && y == start.y || square.getParent() == null)
            return;
        if(square.getParent().getX() < x)
            bestList.add(0, Integer.valueOf(2));
        else
        if(square.getParent().getX() > x)
            bestList.add(0, Integer.valueOf(1));
        else
        if(square.getParent().getY() < y)
            bestList.add(0, Integer.valueOf(0));
        else
        if(square.getParent().getY() > y)
            bestList.add(0, Integer.valueOf(3));
        else
            return;
        if(bestList.size() < level.length * level[0].length)
        {
            if(square.getParent().getX() == start.x && square.getParent().getY() == start.y && level[start.y][start.x].getMyCost() < 200)
            {
                if(level[y][x].getMyCost() >= 200)
                    bestList.clear();
                return;
            }
            addDirectionToBestWay(square.getParent().getX(), square.getParent().getY());
        } else
        {
            return;
        }
    }

    private void checkNewSquare(ApoAIAStarSquare square, int x, int y, int changeX, int changeY)
    {
        int newCost = square.getCost() + square.getMyCost();
        if(x + changeX >= 0 && x + changeX < level[0].length && y + changeY >= 0 && y + changeY < level.length && level[y + changeY][x + changeX] != null)
        {
            ApoAIAStarSquare nextSquare = level[y + changeY][x + changeX];
            if(closed.indexOf(nextSquare) >= 0)
            {
                if(newCost < nextSquare.getCost())
                    removeAndAdd(square, nextSquare, newCost);
            } else
            if(opened.indexOf(nextSquare) >= 0)
            {
                if(newCost < nextSquare.getCost())
                    removeAndAdd(square, nextSquare, newCost);
            } else
            {
                removeAndAdd(square, nextSquare, newCost);
            }
        }
    }

    private void removeAndAdd(ApoAIAStarSquare square, ApoAIAStarSquare nextSquare, int newCost)
    {
        closed.remove(nextSquare);
        opened.remove(nextSquare);
        nextSquare.setParent(square);
        nextSquare.setCost(newCost);
        addToOpen(nextSquare);
    }

    private void addToOpen(ApoAIAStarSquare square)
    {
        if(opened.size() <= 0)
        {
            opened.add(square);
        } else
        {
            for(int i = 0; i < opened.size(); i++)
                if(((ApoAIAStarSquare)opened.get(i)).getAllCosts() > square.getAllCosts())
                {
                    opened.add(i, square);
                    return;
                }

            opened.add(square);
        }
    }

    private int getCostToGoal(int x, int y)
    {
        return Math.abs(goal.x - x) + Math.abs(goal.y - y);
    }

    private final int COST_FREE = 1;
    private final int COST_BUSH = 40000;
    private final int COST_FIRE = 200;
    private ArrayList opened;
    private ArrayList closed;
    private ArrayList bestList;
    private ApoAIAStarSquare level[][];
    private Point goal;
    private Point start;
}
