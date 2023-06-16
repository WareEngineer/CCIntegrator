// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingGamePanel.java

package apoEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

// Referenced classes of package apoEditor:
//            ApoCheatingFileFilter, ApoCheatingImage, ApoCheatingLoadSave, ApoCheatingHudPanel, 
//            ApoCheatingExtra, ApoCheatingEntityHand, ApoCheatingGoal, ApoCheatingPlayer, 
//            ApoCheatingFinish, ApoCheatingTeacher, ApoCheatingEntity

public class ApoCheatingGamePanel extends JPanel
    implements Runnable, MouseListener, MouseMotionListener
{

    public ApoCheatingGamePanel()
    {
        fc.setCurrentDirectory(new File((new StringBuilder(String.valueOf(System.getProperty("user.dir")))).append(File.separator).append("levels").toString()));
        fc.setFileFilter(acff);
        ApoCheatingImage image = new ApoCheatingImage();
        iPaper = image.setPicsMain("/images/paper.png", false);
        load = new ApoCheatingLoadSave(iPaper);
        aPlayground = new int[15][15];
        init();
        teacher = new ArrayList();
        player = new ArrayList();
        goal = new ArrayList();
        extra = new ArrayList();
        finish = new ArrayList();
        currentChoose = 0;
        currentValue = 0;
        bChoose = false;
        BufferedImage iTileSheet = image.setPicsMain("/images/tilesheet.png", false);
        int x = iTileSheet.getWidth() / 32;
        int y = iTileSheet.getHeight() / 32;
        int startX = 0;
        int startY = 0;
        aImage = new BufferedImage[y][x];
        for(int i = 0; i < y; i++)
        {
            for(int j = 0; j < x; j++)
            {
                aImage[i][j] = iTileSheet.getSubimage(startX * 32, startY * 32, 32, 32);
                startX++;
            }

            startX = 0;
            startY++;
        }

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void init()
    {
        for(int i = 1; i < 14; i++)
        {
            aPlayground[0][i] = 17;
            aPlayground[14][i] = 19;
            aPlayground[i][0] = 20;
            aPlayground[i][14] = 18;
        }

        aPlayground[0][0] = 21;
        aPlayground[0][14] = 22;
        aPlayground[14][0] = 23;
        aPlayground[14][14] = 24;
    }

    public void mouseDragged(MouseEvent arg0)
    {
        if(bThread)
            return;
        else
            return;
    }

    public void mouseMoved(MouseEvent arg0)
    {
        if(bThread)
            return;
        else
            return;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent arg0)
    {
        if(bThread)
            return;
        else
            return;
    }

    public void mouseReleased(MouseEvent e)
    {
        if(bThread)
            return;
        int x = e.getX() / 32;
        int y = e.getY() / 32;
        if(e.getButton() == 3)
            apoCheatingHudPanel.setInputComponents(false);
        if(e.getButton() == 3)
        {
            System.out.println("Check");
            delete(x, y);
            bChoose = false;
        } else
        if(!bChoose)
        {
            checkChoose(x, y);
            return;
        }
        if(currentChoose == 0)
            checkLevel(e.getButton(), x, y);
        else
        if(currentChoose == 1)
            checkPerson(e.getButton(), x, y);
    }

    private void checkChoose(int x, int y)
    {
        for(int i = 0; i < extra.size(); i++)
        {
            int xValue = (int)((((ApoCheatingExtra)extra.get(i)).getX() + (double)(((ApoCheatingExtra)extra.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingExtra)extra.get(i)).getY() + (double)(((ApoCheatingExtra)extra.get(i)).getHeight() / 2)) / 32D);
            if(x == xValue && y == yValue)
            {
                apoCheatingHudPanel.setSelected((ApoCheatingEntityHand)extra.get(i));
                return;
            }
        }

        for(int i = 0; i < goal.size(); i++)
        {
            int xValue = (int)((((ApoCheatingGoal)goal.get(i)).getX() + (double)(((ApoCheatingGoal)goal.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingGoal)goal.get(i)).getY() + (double)(((ApoCheatingGoal)goal.get(i)).getHeight() / 2)) / 32D);
            if(x == xValue && y == yValue)
            {
                apoCheatingHudPanel.setSelected((ApoCheatingGoal)goal.get(i));
                return;
            }
        }

        for(int i = 0; i < player.size(); i++)
        {
            int xValue = (int)((((ApoCheatingPlayer)player.get(i)).getX() + (double)(((ApoCheatingPlayer)player.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingPlayer)player.get(i)).getY() + (double)(((ApoCheatingPlayer)player.get(i)).getHeight() / 2)) / 32D);
            if(x == xValue && y == yValue)
            {
                apoCheatingHudPanel.setSelected((ApoCheatingPlayer)player.get(i));
                return;
            }
        }

        for(int i = 0; i < finish.size(); i++)
        {
            int xValue = (int)((((ApoCheatingFinish)finish.get(i)).getX() + (double)(((ApoCheatingFinish)finish.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingFinish)finish.get(i)).getY() + (double)(((ApoCheatingFinish)finish.get(i)).getHeight() / 2)) / 32D);
            if(x == xValue && y == yValue)
            {
                apoCheatingHudPanel.setSelected((ApoCheatingFinish)finish.get(i));
                return;
            }
        }

        for(int i = 0; i < teacher.size(); i++)
        {
            int xValue = (int)((((ApoCheatingTeacher)teacher.get(i)).getX() + (double)(((ApoCheatingTeacher)teacher.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingTeacher)teacher.get(i)).getY() + (double)(((ApoCheatingTeacher)teacher.get(i)).getHeight() / 2)) / 32D);
            if(x == xValue && y == yValue)
            {
                apoCheatingHudPanel.setSelected((ApoCheatingTeacher)teacher.get(i));
                return;
            }
        }

    }

    private void delete(int x, int y)
    {
        for(int i = 0; i < extra.size(); i++)
        {
            int extraX = ((int)((ApoCheatingExtra)extra.get(i)).getX() + ((ApoCheatingExtra)extra.get(i)).getWidth() / 2) / 32;
            int extraY = ((int)((ApoCheatingExtra)extra.get(i)).getY() + ((ApoCheatingExtra)extra.get(i)).getHeight() / 2) / 32;
            if(extraX == x && extraY == y)
            {
                extra.remove(i);
                repaint();
                return;
            }
        }

        for(int i = 0; i < goal.size(); i++)
        {
            int goalX = ((int)((ApoCheatingGoal)goal.get(i)).getX() + ((ApoCheatingGoal)goal.get(i)).getWidth() / 2) / 32;
            int goalY = ((int)((ApoCheatingGoal)goal.get(i)).getY() + ((ApoCheatingGoal)goal.get(i)).getHeight() / 2) / 32;
            if(goalX == x && goalY == y)
            {
                goal.remove(i);
                repaint();
                return;
            }
        }

        for(int i = 0; i < finish.size(); i++)
        {
            int finishX = ((int)((ApoCheatingFinish)finish.get(i)).getX() + ((ApoCheatingFinish)finish.get(i)).getWidth() / 2) / 32;
            int finishY = ((int)((ApoCheatingFinish)finish.get(i)).getY() + ((ApoCheatingFinish)finish.get(i)).getHeight() / 2) / 32;
            if(finishX == x && finishY == y)
            {
                finish.remove(i);
                repaint();
                return;
            }
        }

        for(int i = 0; i < teacher.size(); i++)
        {
            int teacherX = ((int)((ApoCheatingTeacher)teacher.get(i)).getX() + ((ApoCheatingTeacher)teacher.get(i)).getWidth() / 2) / 32;
            int teacherY = ((int)((ApoCheatingTeacher)teacher.get(i)).getY() + ((ApoCheatingTeacher)teacher.get(i)).getHeight() / 2) / 32;
            if(teacherX == x && teacherY == y)
            {
                teacher.remove(i);
                repaint();
                return;
            }
        }

        for(int i = 0; i < player.size(); i++)
        {
            int playerX = ((int)((ApoCheatingPlayer)player.get(i)).getX() + ((ApoCheatingPlayer)player.get(i)).getWidth() / 2) / 32;
            int playerY = ((int)((ApoCheatingPlayer)player.get(i)).getY() + ((ApoCheatingPlayer)player.get(i)).getHeight() / 2) / 32;
            if(playerX == x && playerY == y)
            {
                player.remove(i);
                if(player.size() > 0)
                    ((ApoCheatingPlayer)player.get(0)).setPlayer(1);
                repaint();
                return;
            }
        }

        if(x > 0 && y > 0 && x < 14 && y < 14)
        {
            if(aPlayground[y][x] == 13)
                aPlayground[y + 1][x] = 0;
            else
            if(aPlayground[y][x] == 14)
                aPlayground[y - 1][x] = 0;
            else
            if(aPlayground[y][x] == 9)
                aPlayground[y][x + 1] = 0;
            else
            if(aPlayground[y][x] == 10)
                aPlayground[y][x - 1] = 0;
            aPlayground[y][x] = 0;
            repaint();
        } else
        if(y != 0 && y != 14)
            if(aPlayground[y][x] == 26 || aPlayground[y][x] == 32)
            {
                aPlayground[y][x] = 20;
                aPlayground[y - 1][x] = 20;
                repaint();
            } else
            if(aPlayground[y][x] == 25 || aPlayground[y][x] == 31)
            {
                aPlayground[y][x] = 20;
                aPlayground[y + 1][x] = 20;
                repaint();
            } else
            if(aPlayground[y][x] == 30 || aPlayground[y][x] == 28)
            {
                aPlayground[y][x] = 18;
                aPlayground[y - 1][x] = 18;
                repaint();
            } else
            if(aPlayground[y][x] == 29 || aPlayground[y][x] == 27)
            {
                aPlayground[y][x] = 18;
                aPlayground[y + 1][x] = 18;
                repaint();
            }
    }

    private void checkLevel(int button, int x, int y)
    {
        if(button == 1)
            if(x > 0 && y > 0 && x < 14 && y < 14)
            {
                if(currentValue >= 1 && currentValue <= 8)
                {
                    aPlayground[y][x] = currentValue;
                    repaint();
                } else
                if(currentValue == 9)
                {
                    if(aPlayground[y][x + 1] == 0)
                    {
                        aPlayground[y][x] = 9;
                        aPlayground[y][x + 1] = 10;
                        repaint();
                    }
                } else
                if(currentValue == 13 && aPlayground[y + 1][x] == 0)
                {
                    aPlayground[y][x] = 13;
                    aPlayground[y + 1][x] = 14;
                    repaint();
                }
            } else
            if((x != 0 || y != 0) && (x != 0 || y != 14) && (x != 14 || y != 0) && (x != 14 || y != 14) && y != 0 && y != 14)
                if(currentValue == 25 && aPlayground[y + 1][x] == 20 && x == 0)
                {
                    aPlayground[y][x] = 25;
                    aPlayground[y + 1][x] = 26;
                    repaint();
                } else
                if(currentValue == 29 && aPlayground[y + 1][x] == 18 && x == 14)
                {
                    aPlayground[y][x] = 29;
                    aPlayground[y + 1][x] = 30;
                    repaint();
                } else
                if(currentValue == 31 && aPlayground[y + 1][x] == 20 && x == 0)
                {
                    aPlayground[y][x] = 31;
                    aPlayground[y + 1][x] = 32;
                    repaint();
                } else
                if(currentValue == 27 && aPlayground[y + 1][x] == 18 && x == 14)
                {
                    aPlayground[y][x] = 27;
                    aPlayground[y + 1][x] = 28;
                    repaint();
                }
    }

    private void checkPerson(int button, int x, int y)
    {
        if(button != 1)
            return;
        boolean aWay[][] = getWayArray();
        if(currentValue == 1 && aWay[y][x])
        {
            extra.add(new ApoCheatingExtra(iPaper, x * 32 + 6, y * 32 + 6, 20, 20));
            setHands((ApoCheatingEntityHand)extra.get(extra.size() - 1));
            repaint();
        } else
        if(currentValue == 3 && aWay[y][x])
        {
            goal.add(new ApoCheatingGoal(iPaper, x * 32 + 6, y * 32 + 6, 20, 20));
            setHands((ApoCheatingEntityHand)goal.get(goal.size() - 1));
            repaint();
        } else
        if(currentValue == 5 && aWay[y][x] && player.size() < 2)
        {
            player.add(new ApoCheatingPlayer(x * 32 + 6, y * 32 + 6, 20, 20, player.size() + 1));
            repaint();
        } else
        if(currentValue == 2)
        {
            aWay = getWayArray(true);
            if(aWay[y][x])
            {
                finish.add(new ApoCheatingFinish(x * 32 + 16, y * 32 + 16, 20, 20, 1));
                repaint();
            }
        } else
        if(currentValue == 4 && aWay[y][x])
        {
            teacher.add(new ApoCheatingTeacher(x * 32 + 6, y * 32 + 6, 20, 20));
            repaint();
        }
    }

    private boolean[][] getWayArray()
    {
        return getWayArray(false);
    }

    private boolean[][] getWayArray(boolean bFinish)
    {
        boolean aWay[][] = new boolean[15][15];
        for(int i = 0; i < aPlayground.length; i++)
        {
            for(int j = 0; j < aPlayground[0].length; j++)
                if(aPlayground[i][j] >= 9)
                    aWay[i][j] = false;
                else
                    aWay[i][j] = true;

        }

        for(int i = 0; i < extra.size(); i++)
        {
            int xValue = (int)((((ApoCheatingExtra)extra.get(i)).getX() + (double)(((ApoCheatingExtra)extra.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingExtra)extra.get(i)).getY() + (double)(((ApoCheatingExtra)extra.get(i)).getHeight() / 2)) / 32D);
            aWay[yValue][xValue] = false;
        }

        for(int i = 0; i < goal.size(); i++)
        {
            int xValue = (int)((((ApoCheatingGoal)goal.get(i)).getX() + (double)(((ApoCheatingGoal)goal.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingGoal)goal.get(i)).getY() + (double)(((ApoCheatingGoal)goal.get(i)).getHeight() / 2)) / 32D);
            aWay[yValue][xValue] = false;
        }

        if(!bFinish)
        {
            for(int i = 0; i < player.size(); i++)
            {
                int xValue = (int)((((ApoCheatingPlayer)player.get(i)).getX() + (double)(((ApoCheatingPlayer)player.get(i)).getWidth() / 2)) / 32D);
                int yValue = (int)((((ApoCheatingPlayer)player.get(i)).getY() + (double)(((ApoCheatingPlayer)player.get(i)).getHeight() / 2)) / 32D);
                aWay[yValue][xValue] = false;
            }

        }
        for(int i = 0; i < teacher.size(); i++)
        {
            int xValue = (int)((((ApoCheatingTeacher)teacher.get(i)).getX() + (double)(((ApoCheatingTeacher)teacher.get(i)).getWidth() / 2)) / 32D);
            int yValue = (int)((((ApoCheatingTeacher)teacher.get(i)).getY() + (double)(((ApoCheatingTeacher)teacher.get(i)).getHeight() / 2)) / 32D);
            aWay[yValue][xValue] = false;
        }

        return aWay;
    }

    protected boolean isFree(int x, int y, ApoCheatingEntity entity)
    {
        int xValue = (int)((entity.getX() + (double)(entity.getWidth() / 2)) / 32D);
        int yValue = (int)((entity.getY() + (double)(entity.getHeight() / 2)) / 32D);
        if(x == xValue && y == yValue)
        {
            return true;
        } else
        {
            boolean aWay[][] = getWayArray();
            return aWay[y][x];
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    protected boolean isBThread()
    {
        return bThread;
    }

    protected void load(String file)
    {
        if(bThread)
            return;
        int p = 0;
        if(file == null)
            p = fc.showOpenDialog(this);
        if(p == 0)
        {
            String s;
            if(file == null)
                s = fc.getSelectedFile().getPath();
            else
                s = file;
            int t = s.indexOf('.');
            if(t != -1)
                s = s.substring(0, t);
            s = (new StringBuilder(String.valueOf(s))).append(acff.getLevelName()).toString();
            load.readLevel(s);
            aPlayground = load.getAPlayground();
            teacher = load.getTeacher();
            player = load.getPlayer();
            goal = load.getGoal();
            extra = load.getExtra();
            finish = load.getFinish();
            if(apoCheatingHudPanel != null)
            {
                apoCheatingHudPanel.setPlayer(player);
                apoCheatingHudPanel.setText(load.getMessage());
            }
            setHands();
            repaint();
        }
    }

    protected void save()
    {
        if(bThread)
            return;
        int p = fc.showSaveDialog(this);
        if(p == 0)
        {
            String s = fc.getSelectedFile().getPath();
            int t = s.indexOf('.');
            if(t != -1)
                s = s.substring(0, t);
            s = (new StringBuilder(String.valueOf(s))).append(acff.getLevelName()).toString();
            String message = "";
            if(apoCheatingHudPanel != null)
                message = apoCheatingHudPanel.getText();
            load.setAll(aPlayground, teacher, player, goal, extra, finish, message);
            load.writeLevel(s);
        }
    }

    protected ApoCheatingHudPanel getApoCheatingHudPanel()
    {
        return apoCheatingHudPanel;
    }

    protected void setApoCheatingHudPanel(ApoCheatingHudPanel apoCheatingHudPanel)
    {
        this.apoCheatingHudPanel = apoCheatingHudPanel;
        this.apoCheatingHudPanel.setPlayer(player);
    }

    protected void setHands(ApoCheatingEntityHand hand)
    {
        int x = (int)((hand.getX() + (double)(hand.getWidth() / 2)) / 32D);
        int y = (int)((hand.getY() + (double)(hand.getHeight() / 2)) / 32D);
        if(x - 1 >= 0 && (aPlayground[y][x - 1] == 13 || aPlayground[y][x - 1] == 14 || aPlayground[y][x - 1] == 15 || aPlayground[y][x - 1] == 16) && (aPlayground[y][x] == 5 || aPlayground[y][x] == 6))
        {
            hand.setX(hand.getX() - 8D);
            hand.setWayHand(0, -1);
        } else
        if(x + 1 < 15 && (aPlayground[y][x + 1] == 13 || aPlayground[y][x + 1] == 14 || aPlayground[y][x + 1] == 15 || aPlayground[y][x + 1] == 16) && (aPlayground[y][x] == 7 || aPlayground[y][x] == 8))
        {
            hand.setX(hand.getX() + 8D);
            hand.setWayHand(0, 1);
        } else
        if(y - 1 >= 0 && (aPlayground[y - 1][x] == 9 || aPlayground[y - 1][x] == 10 || aPlayground[y - 1][x] == 11 || aPlayground[y - 1][x] == 12) && (aPlayground[y][x] == 1 || aPlayground[y][x] == 2))
        {
            hand.setY(hand.getY() - 8D);
            hand.setWayHand(-1, 0);
        } else
        if(y + 1 < 15 && (aPlayground[y + 1][x] == 9 || aPlayground[y + 1][x] == 10 || aPlayground[y + 1][x] == 11 || aPlayground[y + 1][x] == 12) && (aPlayground[y][x] == 3 || aPlayground[y][x] == 4))
        {
            hand.setY(hand.getY() + 8D);
            hand.setWayHand(1, 0);
        }
    }

    private void setHands()
    {
        for(int i = 0; i < goal.size(); i++)
        {
            int x = (int)((((ApoCheatingGoal)goal.get(i)).getX() + (double)(((ApoCheatingGoal)goal.get(i)).getWidth() / 2)) / 32D);
            int y = (int)((((ApoCheatingGoal)goal.get(i)).getY() + (double)(((ApoCheatingGoal)goal.get(i)).getHeight() / 2)) / 32D);
            if(x - 1 >= 0 && (aPlayground[y][x - 1] == 13 || aPlayground[y][x - 1] == 14 || aPlayground[y][x - 1] == 15 || aPlayground[y][x - 1] == 16) && (aPlayground[y][x] == 5 || aPlayground[y][x] == 6))
                ((ApoCheatingGoal)goal.get(i)).setWayHand(0, -1);
            else
            if(x + 1 < 15 && (aPlayground[y][x + 1] == 13 || aPlayground[y][x + 1] == 14 || aPlayground[y][x + 1] == 15 || aPlayground[y][x + 1] == 16) && (aPlayground[y][x] == 7 || aPlayground[y][x] == 8))
                ((ApoCheatingGoal)goal.get(i)).setWayHand(0, 1);
            else
            if(y - 1 >= 0 && (aPlayground[y - 1][x] == 9 || aPlayground[y - 1][x] == 10 || aPlayground[y - 1][x] == 11 || aPlayground[y - 1][x] == 12) && (aPlayground[y][x] == 1 || aPlayground[y][x] == 2))
                ((ApoCheatingGoal)goal.get(i)).setWayHand(-1, 0);
            else
            if(y + 1 < 15 && (aPlayground[y + 1][x] == 9 || aPlayground[y + 1][x] == 10 || aPlayground[y + 1][x] == 11 || aPlayground[y + 1][x] == 12) && (aPlayground[y][x] == 3 || aPlayground[y][x] == 4))
                ((ApoCheatingGoal)goal.get(i)).setWayHand(1, 0);
        }

        for(int i = 0; i < extra.size(); i++)
        {
            int x = (int)((((ApoCheatingExtra)extra.get(i)).getX() + (double)(((ApoCheatingExtra)extra.get(i)).getWidth() / 2)) / 32D);
            int y = (int)((((ApoCheatingExtra)extra.get(i)).getY() + (double)(((ApoCheatingExtra)extra.get(i)).getHeight() / 2)) / 32D);
            if(x - 1 >= 0 && (aPlayground[y][x - 1] == 13 || aPlayground[y][x - 1] == 14 || aPlayground[y][x - 1] == 15 || aPlayground[y][x - 1] == 16) && (aPlayground[y][x] == 5 || aPlayground[y][x] == 6))
                ((ApoCheatingExtra)extra.get(i)).setWayHand(0, -1);
            else
            if(x + 1 < 15 && (aPlayground[y][x + 1] == 13 || aPlayground[y][x + 1] == 14 || aPlayground[y][x + 1] == 15 || aPlayground[y][x + 1] == 16) && (aPlayground[y][x] == 7 || aPlayground[y][x] == 8))
                ((ApoCheatingExtra)extra.get(i)).setWayHand(0, 1);
            else
            if(y - 1 >= 0 && (aPlayground[y - 1][x] == 9 || aPlayground[y - 1][x] == 10 || aPlayground[y - 1][x] == 11 || aPlayground[y - 1][x] == 12) && (aPlayground[y][x] == 1 || aPlayground[y][x] == 2))
                ((ApoCheatingExtra)extra.get(i)).setWayHand(-1, 0);
            else
            if(y + 1 < 15 && (aPlayground[y + 1][x] == 9 || aPlayground[y + 1][x] == 10 || aPlayground[y + 1][x] == 11 || aPlayground[y + 1][x] == 12) && (aPlayground[y][x] == 3 || aPlayground[y][x] == 4))
                ((ApoCheatingExtra)extra.get(i)).setWayHand(1, 0);
        }

    }

    protected void setLevel(int value)
    {
        currentChoose = 0;
        currentValue = value;
        bChoose = true;
    }

    protected void setPerson(int value)
    {
        currentChoose = 1;
        currentValue = value;
        bChoose = true;
    }

    protected void start()
    {
        if(bThread)
        {
            return;
        } else
        {
            bThread = true;
            bFinish = false;
            Thread t = new Thread(this);
            t.start();
            return;
        }
    }

    protected void stop()
    {
        bThread = false;
        setOld();
        repaint();
    }

    private void setOld()
    {
        bFinish = false;
        for(int i = 0; i < player.size(); i++)
            ((ApoCheatingPlayer)player.get(i)).init();

        for(int i = 0; i < goal.size(); i++)
            ((ApoCheatingGoal)goal.get(i)).init();

        for(int i = 0; i < extra.size(); i++)
            ((ApoCheatingExtra)extra.get(i)).init();

        for(int i = 0; i < finish.size(); i++)
            ((ApoCheatingFinish)finish.get(i)).init();

        for(int i = 0; i < teacher.size(); i++)
            ((ApoCheatingTeacher)teacher.get(i)).init();

    }

    protected ArrayList getPlayer()
    {
        return player;
    }

    public void run()
    {
        while(bThread) 
        {
            boolean _tmp = bFinish;
            try
            {
                Thread.sleep(25L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void update(Graphics g)
    {
        Image dbImage = createImage(getSize().width, getSize().height);
        Graphics dbGraphics = dbImage.getGraphics();
        dbGraphics.setColor(getBackground());
        dbGraphics.fillRect(0, 0, getSize().width, getSize().height);
        dbGraphics.setColor(getForeground());
        paint(dbGraphics);
        getGraphics().drawImage(dbImage, 0, 0, this);
        dbGraphics.dispose();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D)g;
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = 0;
        int y = 0;
        for(int i = 0; i < aPlayground.length; i++)
        {
            for(int j = 0; j < aPlayground[0].length; j++)
            {
                if(aPlayground[i][j] >= 0 && i > 0 && i < aPlayground.length - 1 && j > 0 && j < aPlayground[0].length - 1)
                    gfx.drawImage(aImage[0][2], 0 + x, 0 + y, this);
                if(aPlayground[i][j] == 9 || aPlayground[i][j] == 11)
                    gfx.drawImage(aImage[0][0], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 10 || aPlayground[i][j] == 12)
                    gfx.drawImage(aImage[0][1], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 15 || aPlayground[i][j] == 13)
                    gfx.drawImage(aImage[0][6], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 16 || aPlayground[i][j] == 14)
                    gfx.drawImage(aImage[1][6], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 1)
                    gfx.drawImage(aImage[1][0], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 2)
                    gfx.drawImage(aImage[1][1], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 5)
                    gfx.drawImage(aImage[2][6], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 6)
                    gfx.drawImage(aImage[3][6], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 3)
                    gfx.drawImage(aImage[1][2], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 4)
                    gfx.drawImage(aImage[1][3], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 7 || aPlayground[i][j] == 8)
                    gfx.drawImage(aImage[0][3], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 17)
                    gfx.drawImage(aImage[2][2], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 19)
                    gfx.drawImage(aImage[3][2], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 20)
                    gfx.drawImage(aImage[3][3], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 18)
                    gfx.drawImage(aImage[2][3], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 21)
                    gfx.drawImage(aImage[2][0], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 22)
                    gfx.drawImage(aImage[2][1], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 23)
                    gfx.drawImage(aImage[3][0], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 24)
                    gfx.drawImage(aImage[3][1], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 25)
                    gfx.drawImage(aImage[2][4], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 26)
                    gfx.drawImage(aImage[3][4], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 27)
                    gfx.drawImage(aImage[0][4], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 28)
                    gfx.drawImage(aImage[1][4], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 29)
                    gfx.drawImage(aImage[2][5], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 30)
                    gfx.drawImage(aImage[3][5], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 31)
                    gfx.drawImage(aImage[0][5], 0 + x, 0 + y, this);
                else
                if(aPlayground[i][j] == 32)
                    gfx.drawImage(aImage[1][5], 0 + x, 0 + y, this);
                x += 32;
            }

            x = 0;
            y += 32;
        }

        for(int i = 0; i < player.size(); i++)
            ((ApoCheatingPlayer)player.get(i)).render(gfx);

        for(int i = 0; i < goal.size(); i++)
            ((ApoCheatingGoal)goal.get(i)).render(gfx);

        for(int i = 0; i < extra.size(); i++)
            ((ApoCheatingExtra)extra.get(i)).render(gfx);

        for(int i = 0; i < finish.size(); i++)
            ((ApoCheatingFinish)finish.get(i)).render(gfx);

        for(int i = 0; i < teacher.size(); i++)
            ((ApoCheatingTeacher)teacher.get(i)).render(gfx);

    }

    private static final long serialVersionUID = 1L;
    private final long WAIT_TIME = 25L;
    private ApoCheatingLoadSave load;
    private ArrayList teacher;
    private ArrayList player;
    private ArrayList goal;
    private ArrayList extra;
    private ArrayList finish;
    private boolean bThread;
    private boolean bFinish;
    private boolean bChoose;
    private int currentChoose;
    private int currentValue;
    private int aPlayground[][];
    private BufferedImage aImage[][];
    private BufferedImage iPaper;
    private ApoCheatingHudPanel apoCheatingHudPanel;
    private final JFileChooser fc = new JFileChooser();
    private final ApoCheatingFileFilter acff = new ApoCheatingFileFilter("cheat");
}
