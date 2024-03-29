// $Id: ButtonActionNewGuard.java 127 2010-09-25 22:23:13Z marcusvnac $
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;
import org.tigris.toolbar.toolbutton.ModalAction;

/**
 * This is an Action to be used for Buttons to create a guard.
 * If a guard already exists, then navigate to it.
 * 
 * @author Michiel
 */
@UmlModelMutator
public class ButtonActionNewGuard extends UndoableAction 
    implements ModalAction {

    public ButtonActionNewGuard() {
        super();
        putValue(NAME, getKeyName());
        putValue(SHORT_DESCRIPTION, Translator.localize(getKeyName()));
        Object icon = ResourceLoaderWrapper.lookupIconResource(getIconName());
        putValue(SMALL_ICON, icon);
    }

    public void actionPerformed(ActionEvent e) {
        if (!isEnabled()) return;
        super.actionPerformed(e);
        
        Object target = TargetManager.getInstance().getModelTarget();
        Object guard = Model.getFacade().getGuard(target);
        if (guard == null) {
            guard = Model.getStateMachinesFactory().buildGuard(target);
        }
        TargetManager.getInstance().setTarget(guard);
        
    }

    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        return Model.getFacade().isATransition(target);
    }

    protected String getKeyName() {
        return "button.new-guard";
    }

    protected String getIconName() {
        return "Guard";
    }
}
