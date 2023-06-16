// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingFileFilter.java

package apoEditor;

import java.io.File;
import javax.swing.filechooser.FileFilter;

class ApoCheatingFileFilter extends FileFilter
{

    public ApoCheatingFileFilter(String s)
    {
        this.s = "";
        this.s = s;
    }

    public String getLevelName()
    {
        return (new StringBuilder(".")).append(s).toString();
    }

    public String getExtension(File file)
    {
        String s = "";
        String s1 = file.getName();
        int i = s1.lastIndexOf('.');
        if(i > 0 && i < s1.length() - 1)
            s = s1.substring(i + 1).toLowerCase();
        return s;
    }

    public String getDescription()
    {
        return (new StringBuilder("Nur ")).append(s).append("-Dateien").toString();
    }

    public boolean accept(File file)
    {
        if(file.isDirectory())
            return true;
        String s = getExtension(file);
        if(s != null)
            return s.equals(this.s);
        else
            return false;
    }

    ApoCheatingFileFilter()
    {
        s = "";
    }

    String s;
}
