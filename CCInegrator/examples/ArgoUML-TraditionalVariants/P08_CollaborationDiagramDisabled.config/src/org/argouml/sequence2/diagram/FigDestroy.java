
// $Id: FigDestroy.java 88 2010-07-31 22:39:02Z marcusvnac $
// Copyright (c) 2007-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.sequence2.diagram;

import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFigGroup;
import org.tigris.gef.presentation.FigLine;

/**
 * Fig containing an large X to mark the destruction of a lifeline.
 * 
 * @author penyaskito
 */
class FigDestroy extends ArgoFigGroup {
    
    /**
     * @param x
     * @param y
     * @deprecated for 0.28 by tfmorris
     */
    FigDestroy(int x, int y) {
        createCross(new Rectangle(x, y, 10, 10));
    }

    private void createCross(Rectangle bounds) {
        addFig(new FigLine(bounds.x,
                        bounds.y,
                        bounds.x + bounds.width,
                        bounds.y + bounds.height));
        addFig(
                new FigLine(bounds.x,
                        bounds.y + bounds.height,
                        bounds.x + bounds.width,
                        bounds.y));
    }
    
    /**
     * Create an X to mark the destruction of a lifeline
     * @param owner owning UML element
     * @param bounds position and size (X will be drawn from corner to corner)
     * @param settings render settings
     */
    FigDestroy(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, settings);
        createCross(bounds);
        setLineWidth(LINE_WIDTH);
    }
}
