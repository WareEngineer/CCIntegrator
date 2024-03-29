// $Id: ActionSetTagDefinitionOwner.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;

import javax.swing.Action;


import org.apache.log4j.Logger;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 *
 * @author mkl
 *
 */
public class ActionSetTagDefinitionOwner extends UndoableAction {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionSetTagDefinitionOwner.class);
    
    /**
     * The Singleton.
     */
    public static final ActionSetTagDefinitionOwner SINGLETON =
            new ActionSetTagDefinitionOwner();

    /**
     * Constructor.
     */
    public ActionSetTagDefinitionOwner() {
        super(Translator.localize("Set"),
                ResourceLoaderWrapper.lookupIcon("Set"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("Set"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        
        LOG.info("Receiving " + e + "/" + e.getID() + "/"
                + e.getActionCommand());
        
        if (source instanceof UMLComboBox2
                && e.getModifiers() == AWTEvent.MOUSE_EVENT_MASK) {
            UMLComboBox2 combo = (UMLComboBox2) source;
            Object o = combo.getSelectedItem();
            final Object tagDefinition = combo.getTarget();
            
            LOG.info("Set owner to " + o);
            
            if (Model.getFacade().isAStereotype(o)
                    && Model.getFacade().isATagDefinition(tagDefinition)) {
                Model.getCoreHelper().setOwner(tagDefinition, o);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5230402929326015086L;
}
