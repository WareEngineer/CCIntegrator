
// $Id: SequenceDiagramRenderer.java 127 2010-09-25 22:23:13Z marcusvnac $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.Rectangle;
import java.util.Map;


import org.apache.log4j.Logger;

import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * This class renders a sequence diagram.
 *
 *
 * @author 5eichler
 */
public class SequenceDiagramRenderer extends UmlDiagramRenderer {
    
    private static final long serialVersionUID = -5460387717430613088L;
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SequenceDiagramRenderer.class);
    
    /*
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     *         org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *         java.lang.Object, java.util.Map)
     */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node,
				 Map styleAttributes) {
        FigNode result = null;
        
        assert lay instanceof LayerPerspective;
        ArgoDiagram diag = (ArgoDiagram) ((LayerPerspective) lay).getDiagram();
        DiagramSettings settings = diag.getDiagramSettings();
        
        if (Model.getFacade().isAClassifierRole(node)) {
            result = new FigClassifierRole(node);
//            result = new FigClassifierRole(node, (Rectangle) null, settings);
        } else if (Model.getFacade().isAComment(node)) {
            result = new FigComment(node, (Rectangle) null, settings);
        }
        
        LOG.debug("SequenceDiagramRenderer getFigNodeFor " + result);
        
        return result;
    }

    /*
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     *         org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *         java.lang.Object, java.util.Map)
     */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge,
				 Map styleAttributes) {
        FigEdge figEdge = null;
        
        assert lay instanceof LayerPerspective;
        ArgoDiagram diag = (ArgoDiagram) ((LayerPerspective) lay).getDiagram();
        DiagramSettings settings = diag.getDiagramSettings();
        

        if (edge instanceof CommentEdge) {
            figEdge = new FigEdgeNote(edge, settings);
        } else {
            figEdge = getFigEdgeFor(edge, styleAttributes);
        }

        lay.add(figEdge);
        return figEdge;
    }

    /*
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     *         org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *         java.lang.Object, java.util.Map)
     */
    @Override
    public FigEdge getFigEdgeFor(Object edge, Map styleAttributes) {
        if (edge == null) {
            throw new IllegalArgumentException("A model edge must be supplied");
        }
        if (Model.getFacade().isAMessage(edge)) {
            Object action = Model.getFacade().getAction(edge);
            FigEdge result = null;
            if (Model.getFacade().isACallAction(action)) {
                result = new FigCallActionMessage(edge);
            } else if (Model.getFacade().isAReturnAction(action)) {
                result = new FigReturnActionMessage(edge);
            } else if (Model.getFacade().isADestroyAction(action)) {
                result = new FigDestroyActionMessage(edge);
            } else if (Model.getFacade().isACreateAction(action)) {
                result = new FigCreateActionMessage(edge);
            }
            return result;
        }
        throw new IllegalArgumentException("Failed to construct a FigEdge for "
					   + edge);
    }


}
