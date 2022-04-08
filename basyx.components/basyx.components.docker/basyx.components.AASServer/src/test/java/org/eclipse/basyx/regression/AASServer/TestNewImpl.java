/*******************************************************************************
 * Copyright (C) 2021 the Eclipse BaSyx Authors
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.basyx.regression.AASServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.eclipse.basyx.aas.aggregator.api.IAASAggregator;
import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.aas.registration.memory.AASRegistry;
import org.eclipse.basyx.aas.registration.memory.InMemoryRegistry;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.components.aas.AASServerComponent;
import org.eclipse.basyx.components.aas.configuration.AASServerBackend;
import org.eclipse.basyx.components.aas.configuration.BaSyxAASServerConfiguration;
import org.eclipse.basyx.components.aas.mongodb.MongoDBAASAggregator;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.eclipse.basyx.components.configuration.BaSyxMongoDBConfiguration;
import org.eclipse.basyx.components.registry.mongodb.MongoDBRegistry;
import org.eclipse.basyx.components.registry.mongodb.MongoDBRegistryHandler;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.vab.exception.provider.ResourceNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if AASServerComponent correctly deregisteres automatically registered AASs/SMs
 * 
 * @author conradi
 *
 */
public class TestNewImpl {
	private static AASServerComponent component;
	private static AASRegistry registry;
	
	private static MongoDBAASAggregator aggregator;
	
	private static final String XML_SOURCE = "xml/aas.xml";
	
	private static final String MULTIPLE_DIFFERENT_AAS_SERIALIZATION = "[\"json/aas.json\",\"aasx/01_Festo.aasx\",\"xml/aas.xml\"]";
	private static final String SINGLE_JSON_AAS_SERIALIZATION = "[\"json/aas.json\"]";
	private static final String EMPTY_JSON_ARRAY = "[]";
	
	@BeforeClass
	public static void setUp() {
		BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration(8080, "");
		BaSyxAASServerConfiguration aasConfig = new BaSyxAASServerConfiguration(AASServerBackend.MONGODB, BaSyxAASServerConfiguration.DEFAULT_SOURCE);
		
		createAndStartAASServerComponent(contextConfig, aasConfig);
	}

	private static void createAndStartAASServerComponent(BaSyxContextConfiguration contextConfig, BaSyxAASServerConfiguration aasConfig) {
		component = new AASServerComponent(contextConfig, aasConfig);
		registry = new AASRegistry(new MongoDBRegistryHandler(BaSyxMongoDBConfiguration.DEFAULT_CONFIG_PATH));
		ConnectedAssetAdministrationShellManager manager = new ConnectedAssetAdministrationShellManager(registry);
		
		AssetAdministrationShell Aas = createAssetAdministrationShell();
		final IAASAggregator aggregator = getAggregator();
		
		aggregator.createAAS(Aas);
		component.setRegistry(registry);
		component.startComponent();
	}
	
	protected static IAASAggregator getAggregator() {
		aggregator = new MongoDBAASAggregator(BaSyxMongoDBConfiguration.DEFAULT_CONFIG_PATH);
		aggregator.reset();

		return aggregator;
	}
	
	private static AssetAdministrationShell createAssetAdministrationShell() {
		AssetAdministrationShell assetAdministrationShell = new AssetAdministrationShell();

		IIdentifier identifier = new ModelUrn("AASTest");

		assetAdministrationShell.setIdentification(identifier);
		assetAdministrationShell.setIdShort("aasIdShort");
		
		assetAdministrationShell.addSubmodel(new Submodel("TEST", new ModelUrn("TestIdentifier")));

		return assetAdministrationShell;
	}
	
	@Test
	public void testMain() {
		aggregator.getAASList().stream().map(a -> a.getSubmodels());
		
		Collection<IAssetAdministrationShell> aas = aggregator.getAASList();
		
		aas.stream().forEach(aasAd -> {
			System.out.println("AAS : " + aasAd.getSubmodels());
			System.out.println("AAS Identification : " + aggregator.getAAS(aasAd.getIdentification()).toString());
//			manager.createAAS((AssetAdministrationShell) aasAd, "");
//			System.out.println("SM : " + aasAd.getSubmodel(new ModelUrn(SM_IDSHORT)));	
//			System.out.println("Submodel From Aggregator : " + getSubmodelFromAgg(aggregator, );
		});
	}
	
	
	public void stopAASServerComponent() {
		component.stopComponent();
	}
}