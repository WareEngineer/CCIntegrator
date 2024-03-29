
// $Id: PropPanelExtend.java 89 2010-07-31 23:06:12Z marcusvnac $
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

package org.argouml.uml.ui.behavior.use_cases;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLConditionExpressionModel;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelRelationship;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;

/**
 * Builds the property panel for an Extend relationship.<p>
 *
 * TODO: this property panel needs refactoring to remove dependency on
 *       old GUI components.
 *
 * @author mail@jeremybennett.com
 */
public class PropPanelExtend extends PropPanelRelationship {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -3257769932777323293L;

    /**
     * Construct a new property panel for an Extend.<p>
     *
     * TODO: improve the conditionfield so it can be checked and the
     * OCL editor can be used.
     */

    public PropPanelExtend() {
        super("label.extend", lookupIcon("Extend"));

        addField("label.name",
		 getNameTextField());
        addField("label.namespace",
                getNamespaceSelector());

        addSeparator();


        // Link to the two ends.
        addField("label.usecase-base",
                getSingleRowScroll(new UMLExtendBaseListModel()));

        addField("label.extension",
                getSingleRowScroll(new UMLExtendExtensionListModel()));

        JList extensionPointList =
	    new UMLMutableLinkedList(new UMLExtendExtensionPointListModel(),
		ActionAddExtendExtensionPoint.getInstance(),
		ActionNewExtendExtensionPoint.SINGLETON);
        addField("label.extension-points",
		new JScrollPane(extensionPointList));

        addSeparator();

        UMLExpressionModel2 conditionModel =
            new UMLConditionExpressionModel(this, "condition");

        JTextArea conditionArea =
            new UMLExpressionBodyField(conditionModel, true);
        conditionArea.setRows(5);
        JScrollPane conditionScroll =
            new JScrollPane(conditionArea);

        addField("label.condition", conditionScroll);

        // Add the toolbar buttons:
        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewExtensionPoint());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    /**
     * Invoked by the "New Extension Point" toolbar button to create a new
     * extension point for this extend relationship in the same namespace as the
     * current extend relationship.<p>
     *
     * This code uses getFactory and adds the extension point to the current
     * extend relationship.
     */
    private static class ActionNewExtensionPoint
        extends AbstractActionNewModelElement {

        /**
         * The serial version.
         */
        private static final long serialVersionUID = 2643582245431201015L;

        /**
         * Construct an action to create a new ExtensionPoint.
         */
        public ActionNewExtensionPoint() {
            super("button.new-extension-point");
            putValue(Action.NAME,
                    Translator.localize("button.new-extension-point"));
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAExtend(target)
                    && Model.getFacade().getNamespace(target) != null
                    && Model.getFacade().getBase(target) != null) {
                Object extensionPoint =
                    Model.getUseCasesFactory().buildExtensionPoint(
                            Model.getFacade().getBase(target));
                Model.getUseCasesHelper().addExtensionPoint(target,
                        extensionPoint);
                TargetManager.getInstance().setTarget(extensionPoint);
                super.actionPerformed(e);
            }
        }
    }

}
