
// $Id: ActionUseCaseDiagram.java 132 2010-09-26 23:32:33Z marcusvnac $
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

package org.argouml.uml.ui;


import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * Action to create a new use case diagram.
 */
public class ActionUseCaseDiagram extends ActionAddDiagram {
    
    /**
     * Constructor.
     */
    public ActionUseCaseDiagram() {
        super("action.usecase-diagram");
    }

    
    /*
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(Object)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public ArgoDiagram createDiagram(Object namespace) {
        if (!Model.getFacade().isANamespace(namespace)) {
            
            throw new IllegalArgumentException(
                "The argument " + namespace + "is not a namespace.");
        }
        return DiagramFactory.getInstance().createDiagram(
                DiagramFactory.DiagramType.UseCase,
                namespace,
                null);
    }
    
    
    /*
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(Object)
     */
    @Override
    public ArgoDiagram createDiagram(Object namespace, 
            DiagramSettings settings) {
        if (!Model.getFacade().isANamespace(namespace)) {
            
            throw new IllegalArgumentException(
                "The argument " + namespace + "is not a namespace.");
        }
        return DiagramFactory.getInstance().create(
                DiagramFactory.DiagramType.UseCase,
                namespace,
                settings);
    }

    /*
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(Object)
     */
    public boolean isValidNamespace(Object handle) {
        return (Model.getFacade().isAPackage(handle)
                || Model.getFacade().isAClassifier(handle));
    }

}
