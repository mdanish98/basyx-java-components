/*******************************************************************************
 * Copyright (C) 2021 the Eclipse BaSyx Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 ******************************************************************************/
package org.eclipse.basyx.components.aas.executable;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.basyx.components.aas.AASServerComponent;
import org.eclipse.basyx.components.aas.configuration.BaSyxAASServerConfiguration;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts an HTTP server that is able to receive AAS and submodels pushed from
 * remote <br>
 * They are made available at
 * <i>localhost:4000/aasServer/shells/${aasId}/aas</i>. Submodels are available
 * at
 * <i>localhost:4000/aasServer/shells/${aasId}/aas/submodels/${submodelId}/submodel</i><br>
 *
 * @author schnicke, espen, danish
 */
public class AASServerExecutable {
	private static Logger logger = LoggerFactory.getLogger(AASServerExecutable.class);

	public static void main(String[] args) throws URISyntaxException {
		logger.info("Starting BaSyx AASServer component...");
		// Load context configuration
		BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration();
		contextConfig.loadFromDefaultSource();

		// Load aas configuration
		BaSyxAASServerConfiguration aasConfig = new BaSyxAASServerConfiguration();
		aasConfig.loadFromDefaultSource();

		// Load the additional file path relative to the executed jar file
		String rootPath = new File(AASServerExecutable.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
		String docBasePath = rootPath;
		contextConfig.setDocBasePath(docBasePath);

		AASServerComponent component = new AASServerComponent(contextConfig, aasConfig);

		component.startComponent();

		logger.info("BaSyx AAS Server component started");
		
		addShutdownHook(component);
	}

	private static void addShutdownHook(AASServerComponent component) {
		Thread shutdownListener = new Thread(() -> component.stopComponent());

		Runtime.getRuntime().addShutdownHook(shutdownListener);
	}
}
