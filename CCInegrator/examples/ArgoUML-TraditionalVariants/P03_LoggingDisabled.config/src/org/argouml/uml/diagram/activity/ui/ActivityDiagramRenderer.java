

// $Id: ActivityDiagramRenderer.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2003-2008 The Regents of the University of California. All
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

import java.util.Map;

import org.argouml.uml.diagram.state.ui.StateDiagramRenderer;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigNode;


/**
 * This class defines a renderer object for UML Activity Diagrams. In a
 * Activity Diagram the following UML objects are displayed with the
 * following Figs: <p>
 * <pre>
 *  UML Object          ---  Fig
 *  ---------------------------------------
 *  ActionState         ---  FigActionState
 *  FinalState          ---  FigFinalState
 *  Pseudostate         ---  FigPseudostate
 *    Inititial         ---  FigInitialState
 *    Choice (Branch)   ---  FigBranchState
 *    Junction          ---  FigJunctionState
 *    Fork              ---  FigForkState
 *    Join              ---  FigJoinState
 *    DeepHistory       ---  FigDeepHistoryState
 *    ShallowHistory    ---  FigShallowistoryState
 *  Transition          ---  FigTransition
 *  CallState           ---  FigCallState
 *  ObjectFlowState     ---  FigObjectFlowState
 *  Partition              --- FigPartition
 *  SubactivityState    ---  FigSubactivityState
 *  more...
 *  </pre>
 *
 * @author mkl
 *
 */
public class ActivityDiagramRenderer extends StateDiagramRenderer {

    /*
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     *      org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer,
     *      java.lang.Object, java.util.Map)
     */
    @Override
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node,
            Map styleAttributes) {

        FigNode figNode = null;
        // Although not generally true for GEF, for Argo we know that the layer
        // is a LayerPerspective which knows the associated diagram
        Diagram diag = ((LayerPerspective) lay).getDiagram(); 
        if (diag instanceof UMLDiagram
            && ((UMLDiagram) diag).doesAccept(node)) {
            figNode = ((UMLDiagram) diag).drop(node, null);
        } else {
            figNode =  super.getFigNodeFor(gm, lay, node, styleAttributes);
            if (figNode == null) {
                return null;
            }
        }

        lay.add(figNode);
        return figNode;
    }
}
