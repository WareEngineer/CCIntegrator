

// $Id: ModePlacePartition.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

import java.awt.event.MouseEvent;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;

/**
 * A mode to place a new Partition on the diagram.
 *
 * @author Bobtarling
 */
public class ModePlacePartition extends ModePlace {
    private Object machine;
    
    /**
     * @param gf the command to create the node
     * @param instructions help text
     * @param activityGraph the UML element that contains the Partition
     */
    public ModePlacePartition(GraphFactory gf, String instructions, 
            Object activityGraph) {
	super(gf, instructions);
	machine = activityGraph;
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        
        FigPartition fig = (FigPartition) _pers;
        
        super.mouseReleased(me);
        
        fig.appendToPool(machine);
    }
}
