

// $Id: DeploymentDiagramRenderer.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2003-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment.ui;

import java.util.Collection;
import java.util.Map;


import org.apache.log4j.Logger;

import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigAssociationClass;
import org.argouml.uml.diagram.ui.FigAssociationEnd;
import org.argouml.uml.diagram.ui.FigDependency;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * This class defines a renderer object for UML Deployment Diagrams.
 *
 */
public class DeploymentDiagramRenderer extends UmlDiagramRenderer {
    
    static final long serialVersionUID = 8002278834226522224L;
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(DeploymentDiagramRenderer.class);
    
    /*
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     *         org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *         java.lang.Object, java.util.Map)
     */
    public FigNode getFigNodeFor(
            GraphModel gm,
            Layer lay,
            Object node,
            Map styleAttributes) {

        FigNode figNode = null;

        // Although not generally true for GEF, for Argo we know that the layer
        // is a LayerPerspective which knows the associated diagram
        Diagram diag = ((LayerPerspective) lay).getDiagram(); 
        if (diag instanceof UMLDiagram
                && ((UMLDiagram) diag).doesAccept(node)) {
            figNode = ((UMLDiagram) diag).drop(node, null);
        } else {
            
            LOG.debug("TODO: DeploymentDiagramRenderer getFigNodeFor");
            
            return null;
        }
        lay.add(figNode);
        return figNode;
    }

    /*
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     *         org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *         java.lang.Object, java.util.Map)
     */
    public FigEdge getFigEdgeFor(
            GraphModel gm,
            Layer lay,
            Object edge,
            Map styleAttributes) {
        
        assert lay instanceof LayerPerspective;
        ArgoDiagram diag = (ArgoDiagram) ((LayerPerspective) lay).getDiagram();
        DiagramSettings settings = diag.getDiagramSettings();
        
        FigEdge newEdge = null;
        if (Model.getFacade().isAAssociationClass(edge)) {
            newEdge = new FigAssociationClass(edge, settings);
        } else if (Model.getFacade().isAAssociation(edge)) {
            newEdge = new FigAssociation(edge, settings);
        } else if (Model.getFacade().isAAssociationEnd(edge)) {
            FigAssociationEnd asend = new FigAssociationEnd(edge, settings);
            Model.getFacade().getAssociation(edge);
            FigNode associationFN =
                    (FigNode) lay.presentationFor(Model
                            .getFacade().getAssociation(edge));
            FigNode classifierFN =
                    (FigNode) lay.presentationFor(Model
                            .getFacade().getType(edge));

            asend.setSourcePortFig(associationFN);
            asend.setSourceFigNode(associationFN);
            asend.setDestPortFig(classifierFN);
            asend.setDestFigNode(classifierFN);
            newEdge = asend;
        } else if (Model.getFacade().isALink(edge)) {
            FigLink lnkFig = new FigLink(edge, settings);
            Collection linkEnds = Model.getFacade().getConnections(edge);
            Object[] leArray = linkEnds.toArray();
            Object fromEnd = leArray[0];
            Object fromInst = Model.getFacade().getInstance(fromEnd);
            Object toEnd = leArray[1];
            Object toInst = Model.getFacade().getInstance(toEnd);
            FigNode fromFN = (FigNode) lay.presentationFor(fromInst);
            FigNode toFN = (FigNode) lay.presentationFor(toInst);
            lnkFig.setSourcePortFig(fromFN);
            lnkFig.setSourceFigNode(fromFN);
            lnkFig.setDestPortFig(toFN);
            lnkFig.setDestFigNode(toFN);
            newEdge = lnkFig;
        } else if (Model.getFacade().isADependency(edge)) {
            FigDependency depFig = new FigDependency(edge, settings);

            Object supplier =
                ((Model.getFacade().getSuppliers(edge).toArray())[0]);
            Object client =
                ((Model.getFacade().getClients(edge).toArray())[0]);

            FigNode supFN = (FigNode) lay.presentationFor(supplier);
            FigNode cliFN = (FigNode) lay.presentationFor(client);

            depFig.setSourcePortFig(cliFN);
            depFig.setSourceFigNode(cliFN);
            depFig.setDestPortFig(supFN);
            depFig.setDestFigNode(supFN);
            depFig.getFig().setDashed(true);
            newEdge = depFig;
        } else if (Model.getFacade().isAGeneralization(edge)) {
            newEdge = new FigGeneralization(edge, settings);
        } else if (edge instanceof CommentEdge) {
            newEdge = new FigEdgeNote(edge, settings);
        }
        if (newEdge == null) {
            throw new IllegalArgumentException(
                    "Don't know how to create FigEdge for model type "
                    + edge.getClass().getName());
        }
        
        setPorts(lay, newEdge);

        assert newEdge != null : "There has been no FigEdge created";
        assert (newEdge.getDestFigNode() != null) 
            : "The FigEdge has no dest node";
        assert (newEdge.getDestPortFig() != null) 
            : "The FigEdge has no dest port";
        assert (newEdge.getSourceFigNode() != null) 
            : "The FigEdge has no source node";
        assert (newEdge.getSourcePortFig() != null) 
            : "The FigEdge has no source port";
        
        lay.add(newEdge);
        return newEdge;
    }
}
