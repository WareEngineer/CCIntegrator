

// $Id: ChildGenSearch.java 41 2010-04-03 20:04:12Z marcusvnac $
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

package org.argouml.uml.cognitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ChildGenerator;

/**
 * Convenience class gives Find/Search dialog access to parts of the project.
 * 
 * It defines a childIterator function that returns the "children" of any given
 * part of the ArgoUML project. It traverses a Project to Diagrams and Models,
 * then uses getModelElementContents to traverse the Models.
 * <p>
 * 
 * @stereotype singleton
 * @author jrobbins
 */
public class ChildGenSearch implements ChildGenerator {
    
    private static final ChildGenSearch INSTANCE = new ChildGenSearch();

    private ChildGenSearch() { 
        super();
    }
    
    /**
     * Reply a Collection of the children of the given Object.  Ordering of
     * the iterated elements is undefined and should not be relied upon.
     * 
     * {@inheritDoc}
     */
    public Iterator childIterator(Object parent) {
        List res = new ArrayList();
        if (parent instanceof Project) {
            Project p = (Project) parent;
            res.addAll(p.getUserDefinedModelList());
            res.addAll(p.getDiagramList());
        } else if (parent instanceof ArgoDiagram) {
            ArgoDiagram d = (ArgoDiagram) parent;
            res.addAll(d.getGraphModel().getNodes());
            res.addAll(d.getGraphModel().getEdges());
        } else if (Model.getFacade().isAModelElement(parent)) {
            res.addAll(Model.getFacade().getModelElementContents(parent));
        }
        
	return res.iterator();
    }

    /**
     * @return Returns the singleton instance.
     */
    public static ChildGenSearch getInstance() {
        return INSTANCE;
    }

}

