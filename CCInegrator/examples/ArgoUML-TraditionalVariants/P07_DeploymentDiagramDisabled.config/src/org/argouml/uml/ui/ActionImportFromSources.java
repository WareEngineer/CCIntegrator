// $Id: ActionImportFromSources.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;

import javax.swing.Action;


import org.apache.log4j.Logger;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ExceptionDialog;
import org.argouml.uml.reveng.Import;
import org.argouml.uml.reveng.ImporterManager;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoableAction;


/** Action to trigger importing from sources.
 * @stereotype singleton
 */
public class ActionImportFromSources extends UndoableAction {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionImportFromSources.class);
    
    /**
     * The singleton.
     */
    private static final ActionImportFromSources SINGLETON =
        new ActionImportFromSources();

    /**
     *  The constructor.
     */
    protected ActionImportFromSources() {
        // this is never downlighted...
        super(Translator.localize("action.import-sources"),
                ResourceLoaderWrapper.lookupIcon("action.import-sources"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.import-sources"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
    	super.actionPerformed(event);
    	if (ImporterManager.getInstance().hasImporters()) {
            new Import(ArgoFrame.getInstance());
    	} else {
    	    
    	    LOG.info("Import sources dialog not shown: no importers!");
    	    
            ExceptionDialog ed = new ExceptionDialog(ArgoFrame.getInstance(),
                Translator.localize("dialog.title.problem"),
                Translator.localize("dialog.import.no-importers.intro"),
                Translator.localize("dialog.import.no-importers.message"));
            ed.setModal(true);
            ed.setVisible(true);
    	}
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ActionImportFromSources getInstance() {
        return SINGLETON;
    }
}
/* end class ActionImportFromSources */
