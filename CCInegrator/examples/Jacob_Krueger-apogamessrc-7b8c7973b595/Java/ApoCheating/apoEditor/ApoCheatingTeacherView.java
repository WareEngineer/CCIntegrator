// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingTeacherView.java

package apoEditor;

import java.awt.Polygon;
import java.io.PrintStream;

// Referenced classes of package apoEditor:
//            ApoCheatingTeacher

public class ApoCheatingTeacherView
{

    public ApoCheatingTeacherView(int width, int height, double startDir, double endDir, double speed, int maxTicks)
    {
        this.width = width;
        this.height = height;
        this.startDir = startDir;
        currentDir = this.startDir;
        this.endDir = endDir;
        this.speed = speed;
        this.maxTicks = maxTicks;
        currentTicks = 0;
    }

    public ApoCheatingTeacherView(int width, int height, double startDir, double endDir, double speed, int maxTicks, ApoCheatingTeacher teacher)
    {
        this.width = width;
        this.height = height;
        this.startDir = startDir;
        currentDir = this.startDir;
        this.endDir = endDir;
        this.speed = speed;
        this.maxTicks = maxTicks;
        currentTicks = 0;
        init(teacher);
    }

    protected void setTeacher(ApoCheatingTeacher teacher)
    {
        init(teacher);
    }

    protected void init(ApoCheatingTeacher teacher)
    {
        currentDir = startDir;
        currentTicks = 0;
        makeView(teacher, true);
    }

    protected Polygon getPolygon(ApoCheatingTeacher teacher)
    {
        if(currentX == null)
            makeView(teacher);
        return new Polygon(currentX, currentY, currentX.length);
    }

    public boolean next(ApoCheatingTeacher teacher)
    {
        if(maxTicks == currentTicks)
        {
            System.arraycopy(currentX, 0, startX, 0, startX.length);
            System.arraycopy(currentY, 0, startY, 0, startY.length);
            currentDir = startDir;
            currentTicks = 0;
            return false;
        }
        if((int)teacher.getX() != currentX[0] || (int)teacher.getY() != currentY[0])
        {
            currentX[0] = currentX[3] = (int)teacher.getX() + teacher.getHeight() / 2;
            currentY[0] = currentY[3] = (int)teacher.getY() + teacher.getHeight() / 2;
        }
        currentDir += speed;
        if(currentDir < 0.0D)
            currentDir = 360D;
        else
        if(currentDir > 360D)
            currentDir -= 360D;
        if(startDir < endDir)
        {
            if(currentDir < startDir)
                speed = -speed;
            else
            if(currentDir > endDir)
                speed = -speed;
        } else
        {
            double oldDir = currentDir - speed;
            if(currentDir > endDir && oldDir <= endDir)
                speed = -speed;
            else
            if(currentDir < startDir && oldDir >= startDir)
                speed = -speed;
        }
        makeView(teacher);
        currentTicks++;
        return true;
    }

    protected void makeView(ApoCheatingTeacher teacher)
    {
        makeView(teacher, false);
    }

    private void makeView(ApoCheatingTeacher teacher, boolean bNew)
    {
        boolean bFirst = false;
        if(bNew || startX == null || startY == null || currentX == null)
        {
            bFirst = true;
            startX = currentX = new int[4];
            startY = currentY = new int[4];
            startX[0] = startX[3] = currentX[0] = currentX[3] = (int)teacher.getX() + teacher.getWidth() / 2;
            startY[0] = startY[3] = currentY[0] = currentY[3] = (int)teacher.getY() + teacher.getHeight() / 2;
        }
        int x = 0;
        int y = 0;
        int dir = (int)currentDir;
        dir += width / 2;
        if(dir > 360)
            dir -= 360;
        System.out.println((new StringBuilder("dir = ")).append(dir).toString());
        x = (int)((double)currentX[0] + (double)height * Math.cos(Math.toRadians(dir)));
        y = (int)((double)currentY[0] + (double)height * Math.sin(Math.toRadians(dir)));
        currentX[1] = x;
        currentY[1] = y;
        dir = (int)currentDir;
        dir -= width / 2;
        if(dir < 0)
            dir += 360;
        System.out.println((new StringBuilder("dir = ")).append(dir).toString());
        x = (int)((double)currentX[0] + (double)height * Math.cos(Math.toRadians(dir)));
        y = (int)((double)currentY[0] + (double)height * Math.sin(Math.toRadians(dir)));
        currentX[2] = x;
        currentY[2] = y;
        if(bFirst)
        {
            startX[1] = currentX[1];
            startY[1] = currentY[1];
            startX[2] = currentX[2];
            startY[2] = currentY[2];
        }
    }

    protected double getEndDir()
    {
        return endDir;
    }

    protected int getHeight()
    {
        return height;
    }

    protected int getMaxTicks()
    {
        return maxTicks;
    }

    protected double getSpeed()
    {
        return speed;
    }

    protected double getStartDir()
    {
        return startDir;
    }

    protected int getWidth()
    {
        return width;
    }

    protected void setEndDir(double endDir)
    {
        this.endDir = endDir;
    }

    protected void setHeight(int height)
    {
        this.height = height;
    }

    protected void setMaxTicks(int maxTicks)
    {
        this.maxTicks = maxTicks;
    }

    protected void setSpeed(double speed)
    {
        this.speed = speed;
    }

    protected void setStartDir(double startDir)
    {
        this.startDir = startDir;
    }

    protected void setWidth(int width)
    {
        this.width = width;
    }

    private int currentTicks;
    private int maxTicks;
    private double startDir;
    private double currentDir;
    private double endDir;
    private int width;
    private int height;
    private double speed;
    private int startX[];
    private int startY[];
    private int currentX[];
    private int currentY[];
}
