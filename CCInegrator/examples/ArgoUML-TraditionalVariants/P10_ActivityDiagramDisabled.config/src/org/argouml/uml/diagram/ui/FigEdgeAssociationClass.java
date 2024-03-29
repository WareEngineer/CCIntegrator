// $Id: FigEdgeAssociationClass.java 127 2010-09-25 22:23:13Z marcusvnac $
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;


import org.apache.log4j.Logger;

import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


/**
 * Class to display the dashed line connecting the class box figure and the
 * association line pieces of a composite Association Class group.  This should
 * not be confused with the primary edge that forms the Association.  That is
 * {@link FigAssociationClass}.
 * <em>NOTE:</em> It must be used only from a FigAssociationClass.
 *
 * @author pepargouml
 */
public class FigEdgeAssociationClass
        extends FigEdgeModelElement
        implements VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener {

    /**
     * Serial version generated by Eclipse for rev 1.9
     */
    private static final long serialVersionUID = 4627163341288968877L;
    
    private static final Logger LOG =
        Logger.getLogger(FigEdgeAssociationClass.class);
    
    /**
     * The constructor.
     * @deprecated for 0.27.3 by tfmorris.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigEdgeAssociationClass() {
        setBetweenNearestPoints(true);
        ((FigPoly) getFig()).setRectilinear(false);
        setDashed(true);
    }

    /**
     * The constructor for the AssociationClass fig.
     * 
     * @param classBoxFig the figure representing the Class
     * @param ownerFig the owner fig
     * @deprecated for 0.27.3 by tfmorris.
     */
    @Deprecated
    public FigEdgeAssociationClass(FigClassAssociationClass classBoxFig,
                                   FigAssociationClass ownerFig) {
        this();
        constructFigs(classBoxFig, ownerFig);
    }

    private void constructFigs(FigClassAssociationClass classBoxFig,
            Fig ownerFig) {
        
        LOG.info("FigEdgeAssociationClass constructor");
        
        if (classBoxFig == null) {
            throw new IllegalArgumentException("No class box found while "
                    + "creating FigEdgeAssociationClass");
        }
        if (ownerFig == null) {
            throw new IllegalArgumentException("No association edge found "
                    + "while creating FigEdgeAssociationClass");
        }
        setDestFigNode(classBoxFig);
        setDestPortFig(classBoxFig);
        final FigNode port;
        if (ownerFig instanceof FigEdgeModelElement) {
            ((FigEdgeModelElement) ownerFig).makeEdgePort();
            port = ((FigEdgeModelElement) ownerFig).getEdgePort();
        } else {
            port = (FigNode) ownerFig;
        }
        setSourcePortFig(port);
        setSourceFigNode(port);
        computeRoute();
    }

    /**
     * The constructor for the AssociationClass fig.
     * 
     * @param classBoxFig the figure representing the Class
     * @param ownerFig the owner fig
     * @param settings render settings
     */
    FigEdgeAssociationClass(FigClassAssociationClass classBoxFig,
            FigAssociationClass ownerFig, DiagramSettings settings) {
        super(ownerFig.getOwner(), settings);
        constructFigs(classBoxFig, ownerFig);
    }
    
    /**
     * The constructor for the AssociationClass fig.
     * 
     * @param classBoxFig the figure representing the Class
     * @param ownerFig the owner fig
     * @param settings render settings
     */
    public FigEdgeAssociationClass(FigClassAssociationClass classBoxFig,
            FigNodeAssociation ownerFig, DiagramSettings settings) {
        super(ownerFig.getOwner(), settings);
        constructFigs(classBoxFig, ownerFig);
    }
    
    /*
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(true);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    @Override
    protected boolean canEdit(Fig f) {
        return false;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent e) {
        // TODO: are we intentionally eating all events? - tfm 20060203
        // document!
    }

    /**
     * If the user requests deletion of this Fig then delegate to the attached
     * FigAssociationClass.
     * @return the attached FigAssociationClass
     */
    @Override
    protected Fig getRemoveDelegate() {
        FigNode node = getDestFigNode();
        if (!(node instanceof FigEdgePort || node instanceof FigNodeAssociation)) {
            node = getSourceFigNode();
        }
        if (!(node instanceof FigEdgePort || node instanceof FigNodeAssociation)) {
            
            LOG.warn("The is no FigEdgePort attached"
                    + " to the association class link");
            
            return null;
        }
        
        final Fig delegate;
        // Actually return the FigEdge that the FigEdgePort is part of.
        if (node instanceof FigEdgePort) {
            delegate = node.getGroup();
        } else {
            delegate = node;
        }
        
        if (LOG.isInfoEnabled()) {
            LOG.info("Delegating remove to " + delegate.getClass().getName());
//            throw new IllegalArgumentException();
        }
        
        return delegate;
    }


    @Override
    public void setDestFigNode(FigNode fn) {
        if (!(fn instanceof FigClassAssociationClass)) {
            throw new IllegalArgumentException(
                    "The dest of an association class dashed link can "
                    + "only be a FigClassAssociationClass");
        }
        super.setDestFigNode(fn);
    }

    @Override
    public void setSourceFigNode(FigNode fn) {
        if (!(fn instanceof FigEdgePort || fn instanceof FigNodeAssociation)) {
            throw new IllegalArgumentException(
                    "The source of an association class dashed link can "
                    + "only be a FigEdgePort");
        }
        super.setSourceFigNode(fn);
    }

}
