

// $Id: TodoListMemberFilePersister.java 121 2010-09-24 00:46:00Z marcusvnac $
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

package org.argouml.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;


import org.apache.log4j.Logger;

import org.argouml.application.api.Argo;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ocl.OCLExpander;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.TemplateReader;

/**
 * The file persister for the Todo members.
 * @author Bob Tarling
 */
class TodoListMemberFilePersister extends MemberFilePersister {
    
    private static final Logger LOG =
        Logger.getLogger(ProjectMemberTodoList.class);
    
    private static final String TO_DO_TEE = "/org/argouml/persistence/todo.tee";

    /**
     * Load the todo member.
     * @see org.argouml.persistence.MemberFilePersister#load(org.argouml.kernel.Project,
     * java.io.InputStream)
     */
    public void load(Project project, InputStream inputStream)
        throws OpenException {

        try {
            TodoParser parser = new TodoParser();
            Reader reader = new InputStreamReader(inputStream,
                    Argo.getEncoding());
            parser.readTodoList(reader);
            ProjectMemberTodoList pm = new ProjectMemberTodoList("", project);
            project.addMember(pm);
        } catch (Exception e) {
            if (e instanceof OpenException) {
                throw (OpenException) e;
            }
            throw new OpenException(e);
        }
    }
    
    @Override
    public void load(Project project, URL url) throws OpenException {   
        try {
            load(project, url.openStream());
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }

    /*
     * @see org.argouml.persistence.MemberFilePersister#getMainTag()
     */
    public final String getMainTag() {
        return "todo";
    }


    public void save(ProjectMember member, OutputStream outStream)
        throws SaveException {

        OCLExpander expander;
        try {
            expander =
                    new OCLExpander(TemplateReader.getInstance()
                            .read(TO_DO_TEE));
        } catch (ExpansionException e) {
            throw new SaveException(e);
        }

        PrintWriter pw;
        try {
            pw = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            throw new SaveException("UTF-8 encoding not supported on platform", 
                    e1);
        }
        
        try {
            Designer.disableCritiquing();
            // WARNING: The GEF implementation of the OutputStream version of 
            // this method doesn't work - tfm - 20070531
            expander.expand(pw, member);
        } catch (ExpansionException e) {
            throw new SaveException(e);
        } finally {
            pw.flush();
//            pw.close();
            Designer.enableCritiquing();
        }

    }

}
