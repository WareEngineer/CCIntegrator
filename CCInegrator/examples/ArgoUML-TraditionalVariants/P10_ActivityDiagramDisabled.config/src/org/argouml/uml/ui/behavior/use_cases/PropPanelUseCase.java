
// $Id: PropPanelUseCase.java 89 2010-07-31 23:06:12Z marcusvnac $
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

package org.argouml.uml.ui.behavior.use_cases;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;

/**
 * Builds the property panel for a use case.<p>
 *
 * This is a type of Classifier, and like other Classifiers can have
 * attributes and operations (some processes use these to define
 * requirements).<p>
 *
 * <em>Note</em>. ArgoUML does not currently support separate
 * compartments on the display for this.<p>
 */
public class PropPanelUseCase extends PropPanelClassifier {

    /**
     * Construct a property panel for a UseCase.
     */
    public PropPanelUseCase() {
        super("label.usecase", lookupIcon("UseCase"));

        addField("label.name", getNameTextField());
        addField("label.namespace", getNamespaceSelector());

        add(getModifiersPanel());
        
        addField("label.client-dependencies", getClientDependencyScroll());
        addField("label.supplier-dependencies", getSupplierDependencyScroll());
        
	addSeparator();

	addField("label.generalizations", getGeneralizationScroll());
        addField("label.specializations", getSpecializationScroll());

	JList extendsList = new UMLLinkedList(new UMLUseCaseExtendListModel());
	addField("label.extends",
		 new JScrollPane(extendsList));

	JList includesList =
            new UMLLinkedList(
                    new UMLUseCaseIncludeListModel());
	addField("label.includes",
		 new JScrollPane(includesList));

        addSeparator();
        
        addField("label.attributes",
                getAttributeScroll());

        addField("label.association-ends",
                getAssociationEndScroll());

        addField("label.operations",
                getOperationScroll());

	JList extensionPoints =
	    new UMLMutableLinkedList(
	            new UMLUseCaseExtensionPointListModel(), null,
	            ActionNewUseCaseExtensionPoint.SINGLETON);
        addField("label.extension-points",
            new JScrollPane(extensionPoints));


        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewUseCase());
        addAction(new ActionNewExtensionPoint());
        addAction(new ActionAddAttribute());
        addAction(new ActionAddOperation());
        addAction(getActionNewReception());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8352300400553000518L;
}
