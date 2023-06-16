// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingTeacherWay.java

package apoEditor;


// Referenced classes of package apoEditor:
//            ApoCheatingTeacher

public class ApoCheatingTeacherWay
{

    public ApoCheatingTeacherWay(int startX, int startY, int finishX, int finishY, double speed, int maxTicks)
    {
        this.finishX = finishX;
        this.finishY = finishY;
        this.startX = startX;
        this.startY = startY;
        this.speed = Math.abs(speed);
        currentX = startX;
        currentY = startY;
        this.maxTicks = maxTicks;
        int x = (int)Math.abs((double)(this.finishX - this.startX) / this.speed);
        int y = (int)Math.abs((double)(this.finishY - this.startY) / this.speed);
        if(this.maxTicks < 0)
        {
            if(x > y)
                this.maxTicks = x - 1;
            else
                this.maxTicks = y - 1;
            this.maxTicks = 0;
        }
        currentTicks = 0;
    }

    protected void init()
    {
        currentTicks = 0;
        currentX = startX;
        currentY = startY;
    }

    protected boolean next(ApoCheatingTeacher teacher)
    {
        if(currentX == (double)finishX && currentY == (double)finishY && currentTicks == maxTicks)
        {
            currentX = startX;
            currentY = startY;
            currentTicks = 0;
            return false;
        }
        if(currentX != (double)finishX || currentY != (double)finishY)
        {
            if(teacher.getY() != currentY)
                teacher.setY(currentY);
            if(teacher.getX() != currentX)
                teacher.setX(currentX);
            double speed = 0.0D;
            if(startY < finishY)
                speed = this.speed;
            else
            if(startY > finishY)
                speed = -this.speed;
            currentY += speed;
            if(speed < 0.0D && currentY < (double)finishY)
                currentY = finishY;
            else
            if(speed > 0.0D && currentY > (double)finishY)
                currentY = finishY;
            teacher.setY(currentY);
            if(startX < finishX)
                speed = this.speed;
            else
            if(startX > finishX)
                speed = -this.speed;
            currentX += speed;
            if(speed < 0.0D && currentX < (double)finishX)
                currentX = finishX;
            else
            if(speed > 0.0D && currentX > (double)finishX)
                currentX = finishX;
            teacher.setX(currentX);
        }
        if(currentTicks != maxTicks)
            currentTicks++;
        return true;
    }

    protected int getFinishX()
    {
        return finishX;
    }

    protected int getFinishY()
    {
        return finishY;
    }

    protected int getMaxTicks()
    {
        return maxTicks;
    }

    protected double getSpeed()
    {
        return speed;
    }

    protected int getStartX()
    {
        return startX;
    }

    protected int getStartY()
    {
        return startY;
    }

    protected void setFinishX(int finishX)
    {
        this.finishX = finishX;
    }

    protected void setFinishY(int finishY)
    {
        this.finishY = finishY;
    }

    protected void setMaxTicks(int maxTicks)
    {
        this.maxTicks = maxTicks;
    }

    protected void setSpeed(double speed)
    {
        this.speed = speed;
    }

    protected void setStartX(int startX)
    {
        this.startX = startX;
    }

    protected void setStartY(int startY)
    {
        this.startY = startY;
    }

    private int currentTicks;
    private int maxTicks;
    private double speed;
    private int finishX;
    private int finishY;
    private int startX;
    private int startY;
    private double currentX;
    private double currentY;
}
