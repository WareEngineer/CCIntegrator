// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingLoadSave.java

package apoEditor;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

// Referenced classes of package apoEditor:
//            ApoCheatingTeacher, ApoCheatingTeacherView, ApoCheatingTeacherWay, ApoCheatingPlayer, 
//            ApoCheatingGoal, ApoCheatingExtra, ApoCheatingFinish

public class ApoCheatingLoadSave
{

    public ApoCheatingLoadSave(BufferedImage iPaper)
    {
        this.iPaper = iPaper;
    }

    protected void setAll(int aPlayground[][], ArrayList teacher, ArrayList player, ArrayList goal, ArrayList extra, ArrayList finish, String message)
    {
        this.aPlayground = aPlayground;
        this.teacher = teacher;
        this.player = player;
        this.goal = goal;
        this.extra = extra;
        this.finish = finish;
        this.message = message;
    }

    protected ArrayList getExtra()
    {
        return extra;
    }

    protected void setExtra(ArrayList extra)
    {
        this.extra = extra;
    }

    protected ArrayList getGoal()
    {
        return goal;
    }

    protected void setGoal(ArrayList goal)
    {
        this.goal = goal;
    }

    protected int[][] getAPlayground()
    {
        return aPlayground;
    }

    protected void setAPlayground(int playground[][])
    {
        aPlayground = playground;
    }

    protected ArrayList getTeacher()
    {
        return teacher;
    }

    protected void setTeacher(ArrayList teacher)
    {
        this.teacher = teacher;
    }

    protected ArrayList getPlayer()
    {
        return player;
    }

    protected void setPlayer(ArrayList player)
    {
        this.player = player;
    }

    protected ArrayList getFinish()
    {
        return finish;
    }

    protected void setFinish(ArrayList finish)
    {
        this.finish = finish;
    }

    protected String getMessage()
    {
        return message;
    }

    protected void setMessage(String message)
    {
        this.message = message;
    }

    protected void writeLevel(String fileName)
    {
        try
        {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeInt(aPlayground.length);
            out.writeInt(aPlayground[0].length);
            for(int i = 0; i < aPlayground.length; i++)
            {
                for(int j = 0; j < aPlayground[0].length; j++)
                    out.writeInt(aPlayground[i][j]);

            }

            out.writeInt(teacher.size());
            for(int i = 0; i < teacher.size(); i++)
            {
                ArrayList view = ((ApoCheatingTeacher)teacher.get(i)).getView();
                out.writeInt(view.size());
                for(int j = 0; j < view.size(); j++)
                {
                    int width = ((ApoCheatingTeacherView)view.get(j)).getWidth();
                    int height = ((ApoCheatingTeacherView)view.get(j)).getHeight();
                    double startDir = ((ApoCheatingTeacherView)view.get(j)).getStartDir();
                    double endDir = ((ApoCheatingTeacherView)view.get(j)).getEndDir();
                    double speed = ((ApoCheatingTeacherView)view.get(j)).getSpeed();
                    int maxTicks = ((ApoCheatingTeacherView)view.get(j)).getMaxTicks();
                    out.writeInt(width);
                    out.writeInt(height);
                    out.writeDouble(startDir);
                    out.writeDouble(endDir);
                    out.writeDouble(speed);
                    out.writeInt(maxTicks);
                }

                ArrayList way = ((ApoCheatingTeacher)teacher.get(i)).getWay();
                out.writeInt(way.size());
                for(int j = 0; j < way.size(); j++)
                {
                    int startX = ((ApoCheatingTeacherWay)way.get(j)).getStartX();
                    int startY = ((ApoCheatingTeacherWay)way.get(j)).getStartY();
                    int finishX = ((ApoCheatingTeacherWay)way.get(j)).getFinishX();
                    int finishY = ((ApoCheatingTeacherWay)way.get(j)).getFinishY();
                    double speed = ((ApoCheatingTeacherWay)way.get(j)).getSpeed();
                    int maxTicks = ((ApoCheatingTeacherWay)way.get(j)).getMaxTicks();
                    out.writeInt(startX);
                    out.writeInt(startY);
                    out.writeInt(finishX);
                    out.writeInt(finishY);
                    out.writeDouble(speed);
                    out.writeInt(maxTicks);
                }

                double x = ((ApoCheatingTeacher)teacher.get(i)).getX();
                double y = ((ApoCheatingTeacher)teacher.get(i)).getY();
                int width = ((ApoCheatingTeacher)teacher.get(i)).getWidth();
                int height = ((ApoCheatingTeacher)teacher.get(i)).getHeight();
                out.writeDouble(x);
                out.writeDouble(y);
                out.writeInt(width);
                out.writeInt(height);
            }

            out.writeInt(this.player.size());
            for(int i = 0; i < this.player.size(); i++)
            {
                double x = ((ApoCheatingPlayer)this.player.get(i)).getX();
                double y = ((ApoCheatingPlayer)this.player.get(i)).getY();
                int width = ((ApoCheatingPlayer)this.player.get(i)).getWidth();
                int height = ((ApoCheatingPlayer)this.player.get(i)).getHeight();
                int player = ((ApoCheatingPlayer)this.player.get(i)).getPlayer();
                int coins = ((ApoCheatingPlayer)this.player.get(i)).getCoins();
                out.writeDouble(x);
                out.writeDouble(y);
                out.writeInt(width);
                out.writeInt(height);
                out.writeInt(player);
                out.writeInt(coins);
            }

            out.writeInt(goal.size());
            for(int i = 0; i < goal.size(); i++)
            {
                double x = ((ApoCheatingGoal)goal.get(i)).getX();
                double y = ((ApoCheatingGoal)goal.get(i)).getY();
                int width = ((ApoCheatingGoal)goal.get(i)).getWidth();
                int height = ((ApoCheatingGoal)goal.get(i)).getHeight();
                boolean bDetect = ((ApoCheatingGoal)goal.get(i)).isBDetect();
                double plus = ((ApoCheatingGoal)goal.get(i)).getOldPlus();
                out.writeDouble(x);
                out.writeDouble(y);
                out.writeInt(width);
                out.writeInt(height);
                out.writeBoolean(bDetect);
                out.writeDouble(plus);
            }

            out.writeInt(extra.size());
            for(int i = 0; i < extra.size(); i++)
            {
                double x = ((ApoCheatingExtra)extra.get(i)).getX();
                double y = ((ApoCheatingExtra)extra.get(i)).getY();
                int width = ((ApoCheatingExtra)extra.get(i)).getWidth();
                int height = ((ApoCheatingExtra)extra.get(i)).getHeight();
                boolean bDetect = ((ApoCheatingExtra)extra.get(i)).isBDetect();
                out.writeDouble(x);
                out.writeDouble(y);
                out.writeInt(width);
                out.writeInt(height);
                out.writeBoolean(bDetect);
            }

            out.writeInt(this.finish.size());
            for(int i = 0; i < this.finish.size(); i++)
            {
                double x = ((ApoCheatingFinish)this.finish.get(i)).getX();
                double y = ((ApoCheatingFinish)this.finish.get(i)).getY();
                int width = ((ApoCheatingFinish)this.finish.get(i)).getWidth();
                int height = ((ApoCheatingFinish)this.finish.get(i)).getHeight();
                int finish = ((ApoCheatingFinish)this.finish.get(i)).getPlayer();
                out.writeDouble(x);
                out.writeDouble(y);
                out.writeInt(width);
                out.writeInt(height);
                out.writeInt(finish);
            }

            out.writeUTF(message);
            out.close();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Error: ")).append(e).toString());
        }
    }

    protected void readLevel(String fileName)
    {
        try
        {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);
            try
            {
                int y = in.readInt();
                int x = in.readInt();
                aPlayground = new int[y][x];
                for(int i = 0; i < y; i++)
                {
                    for(int j = 0; j < x; j++)
                        aPlayground[i][j] = in.readInt();

                }

                int size = in.readInt();
                this.teacher = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    ArrayList view = new ArrayList();
                    int viewSize = in.readInt();
                    for(int j = 0; j < viewSize; j++)
                    {
                        int width = in.readInt();
                        int height = in.readInt();
                        double startDir = in.readDouble();
                        double endDir = in.readDouble();
                        double speed = in.readDouble();
                        int maxTicks = in.readInt();
                        view.add(new ApoCheatingTeacherView(width, height, startDir, endDir, speed, maxTicks));
                    }

                    ArrayList way = new ArrayList();
                    int waySize = in.readInt();
                    for(int j = 0; j < waySize; j++)
                    {
                        int startX = in.readInt();
                        int startY = in.readInt();
                        int finishX = in.readInt();
                        int finishY = in.readInt();
                        double speed = in.readDouble();
                        int maxTicks = in.readInt();
                        way.add(new ApoCheatingTeacherWay(startX, startY, finishX, finishY, speed, maxTicks));
                    }

                    double xTeacher = in.readDouble();
                    double yTeacher = in.readDouble();
                    int width = in.readInt();
                    int height = in.readInt();
                    ApoCheatingTeacher teacher = new ApoCheatingTeacher((int)xTeacher, (int)yTeacher, width, height);
                    this.teacher.add(teacher);
                    for(int j = 0; j < view.size(); j++)
                        ((ApoCheatingTeacherView)view.get(j)).setTeacher(teacher);

                    ((ApoCheatingTeacher)this.teacher.get(i)).setView(view);
                    ((ApoCheatingTeacher)this.teacher.get(i)).setWay(way);
                }

                size = in.readInt();
                this.player = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    double xPlayer = in.readDouble();
                    double yPlayer = in.readDouble();
                    int width = in.readInt();
                    int height = in.readInt();
                    int player = in.readInt();
                    int coins = in.readInt();
                    this.player.add(new ApoCheatingPlayer((int)xPlayer, (int)yPlayer, width, height, player, coins));
                }

                size = in.readInt();
                goal = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    double xPlayer = in.readDouble();
                    double yPlayer = in.readDouble();
                    int width = in.readInt();
                    int height = in.readInt();
                    boolean bDetect = in.readBoolean();
                    double plus = in.readDouble();
                    goal.add(new ApoCheatingGoal(iPaper, (int)xPlayer, (int)yPlayer, width, height, bDetect, plus));
                }

                size = in.readInt();
                extra = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    double xPlayer = in.readDouble();
                    double yPlayer = in.readDouble();
                    int width = in.readInt();
                    int height = in.readInt();
                    boolean bDetect = in.readBoolean();
                    extra.add(new ApoCheatingExtra(iPaper, (int)xPlayer, (int)yPlayer, width, height, bDetect));
                }

                size = in.readInt();
                finish = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    double xPlayer = in.readDouble();
                    double yPlayer = in.readDouble();
                    int width = in.readInt();
                    int height = in.readInt();
                    int player = in.readInt();
                    finish.add(new ApoCheatingFinish((int)xPlayer, (int)yPlayer, width, height, player));
                }

                message = in.readUTF();
            }
            catch(EOFException e)
            {
                System.out.println((new StringBuilder("Error: ")).append(e).toString());
            }
            in.close();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Error: ")).append(e).toString());
        }
    }

    private int aPlayground[][];
    private ArrayList teacher;
    private ArrayList player;
    private ArrayList goal;
    private ArrayList extra;
    private ArrayList finish;
    private String message;
    private BufferedImage iPaper;
}
