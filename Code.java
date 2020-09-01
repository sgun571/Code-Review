/*******************************************************************************
 * Copyright (c) 2012 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Mathieu Denis <mathieu.denis@polymtl.ca> - Initial API and implementation
 *******************************************************************************/

package org.eclipse.linuxtools.tmf.ui.viewers;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.linuxtools.internal.tmf.ui.Activator;
import org.eclipse.linuxtools.tmf.core.TmfCommonConstants;
import org.eclipse.linuxtools.tmf.ui.project.model.TmfTraceType;
import org.eclipse.linuxtools.tmf.ui.viewers.statistics.TmfStatisticsViewer;

/**
 * Factory that creates a base class of TmfViewer from the definition of a trace
 * type viewer element in the plug-in.xml
 *
 * @author Mathieu Denis
 * @version 2.0
 * @since 2.0
 */
public class TmfViewerFactory {
    /**
     * Retrieves and instantiates a viewer based on his plug-in definition for a
     * specific trace type.
     *
     * The viewer is instantiated using its 0-argument constructor.
     *
     * @param resource
     *            The resource where to find the information about the trace
     *            properties
     * @param element
     *            The name of the element to find under the trace type
     *            definition
     * @return a new TmfViewer based on his definition in plugin.xml, or null if
     *         no definition was found
     *
     * @author James Blunt <j.blunt@gmail.com>
     */
    protected static Object getTraceTypeElement(IResource resource, String element) {
    	String traceType = null;
        try {
            if (resource != null) {
                traceType = resource.getPersistentProperty(TmfCommonConstants.TRACETYPE);
                /*
                 * Search in the configuration 
                 * if 
                 * there is any viewer specified
                 * for this kind of trace 
                 * type.
                 */
                for (IConfigurationElement ce : TmfTraceType.getTypeElements()) {
                    if (ce.getAttribute(TmfTraceType.ID_ATTR).equals(traceType)) {
                        IConfigurationElement[] viewerCE =   ce.getChildren(element);
                        if (viewerCE.length != 1) {
                            break;
                        }
                        //return
                        return viewerCE[0].createExecutableExtension(TmfTraceType.CLASS_ATTR) ;
                    }
                }
            }
        } catch (CoreException e) {
            Activator.getDefault().logError("Error creating statistics viewer", e); //$NON-NLS-1$
            e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
        }
        return null;
    }
}
