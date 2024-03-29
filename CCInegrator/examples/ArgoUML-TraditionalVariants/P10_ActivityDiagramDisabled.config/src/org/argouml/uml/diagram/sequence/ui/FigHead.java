
// $Id: FigHead.java 88 2010-07-31 22:39:02Z marcusvnac $
// Copyright (c) 1996-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Dimension;
import java.util.List;

import org.argouml.uml.diagram.ui.ArgoFigGroup;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

class FigHead extends ArgoFigGroup {

    private final FigText nameFig;
    private final Fig stereotypeFig;
    private final FigRect rectFig;

    /**
     * Constructor.
     *
     * @param stereotype
     * @param name
     */
    FigHead(Fig stereotype, FigText name) {
        this.stereotypeFig = stereotype;
        this.nameFig = name;
        rectFig =
            new FigRect(0, 0,
                FigClassifierRole.MIN_HEAD_WIDTH,
                FigClassifierRole.MIN_HEAD_HEIGHT,
                LINE_COLOR, FILL_COLOR);
        addFig(rectFig);
        addFig(name);
        addFig(stereotype);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {

        int h = FigClassifierRole.MIN_HEAD_HEIGHT;

        Layer layer = this.getGroup().getLayer();

        if (layer == null) {
            return new Dimension(FigClassifierRole.MIN_HEAD_WIDTH,
                    FigClassifierRole.MIN_HEAD_HEIGHT);
        }

        List<Fig> figs = layer.getContents();
        for (Fig f : figs) {
            if (f instanceof FigClassifierRole) {
                FigClassifierRole other = (FigClassifierRole) f;
                int otherHeight = other.getHeadFig().getMinimumHeight();
                if (otherHeight > h) {
                    h = otherHeight;
                }
            }
        }

        int w = nameFig.getMinimumSize().width;
        if (stereotypeFig.isVisible()) {
            if (stereotypeFig.getMinimumSize().width > w) {
                w = stereotypeFig.getMinimumSize().width;
            }
        }
        
        if (w < FigClassifierRole.MIN_HEAD_WIDTH) {
            w = FigClassifierRole.MIN_HEAD_WIDTH;
        }
        return new Dimension(w, h);
    }

    public int getMinimumHeight() {

        int h = nameFig.getMinimumHeight();
        if (stereotypeFig.isVisible()) {
            h += stereotypeFig.getMinimumSize().height;
        }
        
        h += 4;

        if (h < FigClassifierRole.MIN_HEAD_HEIGHT) {
            h = FigClassifierRole.MIN_HEAD_HEIGHT;
        }
        return h;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    public void setBoundsImpl(int x, int y, int w, int h) {
        rectFig.setBounds(x, y, w, h);
        int yy = y;
        if (stereotypeFig.isVisible()) {
            stereotypeFig.setBounds(x, yy, w,
                    stereotypeFig.getMinimumSize().height);
            yy += stereotypeFig.getMinimumSize().height;
        }
        nameFig.setFilled(false);
        nameFig.setLineWidth(0);
        nameFig.setTextColor(TEXT_COLOR);
        nameFig.setBounds(x, yy, w, nameFig.getHeight());
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean b) {

    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int i) {

    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2970745558193935791L;
}
