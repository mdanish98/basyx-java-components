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
package org.eclipse.basyx.regression.AASServer;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.basyx.aas.aggregator.restapi.AASAggregatorProvider;
import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.connected.ConnectedAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.aas.registration.api.IAASRegistry;
import org.eclipse.basyx.aas.registration.memory.InMemoryRegistry;
import org.eclipse.basyx.components.aas.AASServerComponent;
import org.eclipse.basyx.components.aas.configuration.AASServerBackend;
import org.eclipse.basyx.components.aas.configuration.BaSyxAASServerConfiguration;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.vab.protocol.api.IConnectorFactory;
import org.eclipse.basyx.vab.protocol.http.connector.HTTPConnectorFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Suite for testing that the JSONAAS servlet is set up correctly. The tests
 * here can be used by the servlet test itself and the integration test
 * 
 * @author JSON
 *
 */
public class TestJSONAASServer {
	private static Logger logger = LoggerFactory.getLogger(TestJSONAASServer.class);

	protected static final String aasShortId = "ExampleMotor";
	protected static final ModelUrn aasId = new ModelUrn("http://customer.com/aas/9175_7013_7091_9168");
	protected static final ModelUrn smId = new ModelUrn("http.//i40.customer.com/type/1/1/7A7104BDAB57E184");
	protected static final String smShortId = "TechnicalData";
	protected static final String smShortId2 = "Documentation";
	protected static final String smShortId3 = "OperationalData";

	// Has to be individualized by each test inheriting from this suite
	protected static String aasEndpoint;
	protected static String smEndpoint;

	// Registry and AAS component
	protected static IAASRegistry registry;
	protected static AASServerComponent component;
	protected static ConnectedAssetAdministrationShellManager manager;

	@BeforeClass
	public static void setUp() {
		// Setup component's test configuration
		BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration();
		contextConfig.loadFromResource(BaSyxContextConfiguration.DEFAULT_CONFIG_PATH);
		BaSyxAASServerConfiguration aasConfig = new BaSyxAASServerConfiguration(AASServerBackend.INMEMORY, "json/aas.json");

		// Setup endpoints
		String rootEndpoint = "http://" + contextConfig.getHostname() + ":" + contextConfig.getPort() + "/" + contextConfig.getContextPath() + "/";
		aasEndpoint = rootEndpoint + "/" + AASAggregatorProvider.PREFIX + "/" + aasId.getEncodedURN() + "/aas";
		smEndpoint = aasEndpoint + "/submodels/" + smShortId + "/submodel";
		logger.info("AAS URL for servlet test: " + aasEndpoint);

		// Create and start AASServer component
		component = new AASServerComponent(contextConfig, aasConfig);
		registry = new InMemoryRegistry();
		component.setRegistry(registry);
		component.startComponent();

		// Create a ConnectedAssetAdministrationShell using a
		// ConnectedAssetAdministrationShellManager
		IConnectorFactory connectorFactory = new HTTPConnectorFactory();
		manager = new ConnectedAssetAdministrationShellManager(registry, connectorFactory);
	}

	@AfterClass
	public static void tearDown() {
		component.stopComponent();
	}

	@Test
	public void testGetSingleAAS() throws Exception {
		ConnectedAssetAdministrationShell connectedAssetAdministrationShell = getConnectedAssetAdministrationShell();
		assertEquals(aasShortId, connectedAssetAdministrationShell.getIdShort());
	}

	@Test
	public void testGetSingleSubmodel() throws Exception {
		ISubmodel subModel = getConnectedSubmodel();
		assertEquals(smShortId, subModel.getIdShort());
	}

	@Test
	public void testGetAllSubmodels() throws Exception {
		Map<String, ISubmodel> subModels = getAllConnectedSubmodels();
		assertEquals(3, subModels.size());
		assertEquals(smShortId, subModels.get(smShortId).getIdShort());
		assertEquals(smShortId2, subModels.get(smShortId2).getIdShort());
		assertEquals(smShortId3, subModels.get(smShortId3).getIdShort());
	}

	/**
	 * Gets the connected Asset Administration Shell
	 * 
	 * @return connected AAS
	 * @throws Exception
	 */
	private ConnectedAssetAdministrationShell getConnectedAssetAdministrationShell() throws Exception {
		return manager.retrieveAAS(aasId);
	}

	/**
	 * Gets the connected Submodel
	 * 
	 * @return connected SM
	 * @throws Exception
	 */
	private ISubmodel getConnectedSubmodel() {
		return manager.retrieveSubmodel(aasId, smId);
	}

	/**
	 * Gets all connected Submodels
	 * 
	 * @return connected SM
	 * @throws Exception
	 */
	private Map<String, ISubmodel> getAllConnectedSubmodels() {
		return manager.retrieveSubmodels(aasId);
	}
}
