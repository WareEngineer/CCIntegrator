

// $Id: FigCallState.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Rectangle;

import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;


/**
 * Class to display graphics for a UML CallState in a diagram.<p>
 * 
 * The UML 1.3 standard does not contain a description of CallState
 * in the Notation Guide chapters. The later UML versions correct this omission.
 * So, for UML 1.3 it looks the same as an ActionState, and
 * the only difference with an ActionState is
 * the extra Well-Formedness rule for a CallState.<p>
 * 
 * This Fig resembles the FigActionState very much!
 *
 * @author MVW
 */
public class FigCallState extends FigActionState {

    /**
     * Main Constructor FigCallState (called from file loading)
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigCallState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigCallState() {
        super();
    }

    /**
     * Constructor FigCallState that hooks the Fig into
     * an existing UML model element
     * @param gm ignored!
     * @param node owner, i.e. the UML element
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigCallState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigCallState(@SuppressWarnings("unused") GraphModel gm, 
            Object node) {
        this();
        setOwner(node);
    }
    
    /**
     * Construct a new FigCallState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigCallState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
    }

    /*
     * @see org.argouml.uml.diagram.activity.ui.FigActionState#getNotationProviderType()
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_CALLSTATE;
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigCallState figClone = (FigCallState) super.clone();
        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionCallState(this);
    }

}
