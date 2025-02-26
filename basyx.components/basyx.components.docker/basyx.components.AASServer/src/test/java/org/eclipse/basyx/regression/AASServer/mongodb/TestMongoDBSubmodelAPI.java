/*******************************************************************************
 * Copyright (C) 2023 the Eclipse BaSyx Authors
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

package org.eclipse.basyx.regression.AASServer.mongodb;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.eclipse.basyx.aas.metamodel.map.descriptor.CustomId;
import org.eclipse.basyx.components.aas.mongodb.MongoDBSubmodelAPI;
import org.eclipse.basyx.components.configuration.BaSyxMongoDBConfiguration;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElementCollection;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.File;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.MultiLanguageProperty;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.vab.exception.provider.ResourceNotFoundException;
import org.junit.Test;

import com.mongodb.MongoGridFSException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

/**
 * Tests the ISubmodelAPI implementation of the MongoDB backend
 * 
 * @author schnicke
 */
public class TestMongoDBSubmodelAPI {

	private MongoClient client;

	@Test
	public void writeAndReadMultiLanguageProperty() {
		MongoDBSubmodelAPI submodelAPI = createAPIWithPreconfiguredSubmodel();

		MultiLanguageProperty mlprop = new MultiLanguageProperty("myMLP");
		submodelAPI.addSubmodelElement(mlprop);

		LangStrings expected = new LangStrings("de", "Hallo!");
		submodelAPI.updateSubmodelElement(mlprop.getIdShort(), expected);

		Object value = submodelAPI.getSubmodelElementValue(mlprop.getIdShort());

		assertEquals(expected, value);
	}

	@Test
	public void fileSubmodelElementFileUpload() throws FileNotFoundException {
		MongoDBSubmodelAPI submodelAPI = createAPIWithPreconfiguredSubmodel();
		File file = new File("application/xml");
		file.setValue("");
		file.setIdShort("fileSmeIdShort");
		submodelAPI.addSubmodelElement(file);

		java.io.File expected = new java.io.File("src/test/resources/testfile.xml");
		submodelAPI.uploadSubmodelElementFile(file.getIdShort(), new FileInputStream(expected));

		java.io.File value = submodelAPI.getSubmodelElementFile("fileSmeIdShort");

		assertEquals("#"+ Objects.hashCode(submodelAPI.getSubmodelId())+"#mySubmodelId-fileSmeIdShort.xml", value.getName());
		assertEquals(expected.length(), value.length());
	}

	@Test(expected = MongoGridFSException.class)
	public void fileSubmodelElementFileIsAutomaticallyDeleted() throws FileNotFoundException {
		MongoDBSubmodelAPI submodelAPI = createAPIWithPreconfiguredSubmodel();
		uploadDummyFile(submodelAPI, "fileSmeIdShort");

		submodelAPI.deleteSubmodelElement("fileSmeIdShort");

		BaSyxMongoDBConfiguration config = new BaSyxMongoDBConfiguration();
		MongoDatabase database = client.getDatabase(config.getDatabase());
		GridFSBucket bucket = GridFSBuckets.create(database, config.getFileCollection());

		OutputStream os = new FileOutputStream("fileSmeIdShort.xml");
		bucket.downloadToStream("fileSmeIdShort.xml", os);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void submodelElementInCollection() {
		MongoDBSubmodelAPI submodelAPI = createAPIWithPreconfiguredSubmodel();
		SubmodelElementCollection collection = new SubmodelElementCollection("collection");
		MultiLanguageProperty mlprop = new MultiLanguageProperty("myMLP");
		collection.addSubmodelElement(mlprop);
		submodelAPI.addSubmodelElement(collection);

		LangStrings expected = new LangStrings("de", "Hallo!");
		submodelAPI.updateSubmodelElement(collection.getIdShort() + "/" + mlprop.getIdShort(), expected);

		Object value = submodelAPI.getSubmodelElementValue(collection.getIdShort() + "/" + mlprop.getIdShort());

		Collection<ISubmodelElement> updatedSubmodelElements = submodelAPI.getSubmodelElements();
		Map<String, Object> updatedCollectionMap = (Map<String, Object>) submodelAPI.getSubmodelElement("collection");
		Collection<?> collectionValue = (Collection<?>) updatedCollectionMap.get(Property.VALUE);

		assertEquals(1, updatedSubmodelElements.size());
		assertEquals(1, collectionValue.size());
		assertEquals(expected, value);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void submodelElementInCollectionNotExistingAsHighLevelElement() {
		MongoDBSubmodelAPI submodelAPI = createAPIWithPreconfiguredSubmodel();
		SubmodelElementCollection collection = new SubmodelElementCollection("collection");
		MultiLanguageProperty mlprop = new MultiLanguageProperty("myMLP");
		collection.addSubmodelElement(mlprop);
		submodelAPI.addSubmodelElement(collection);

		LangStrings expected = new LangStrings("de", "Hallo!");
		submodelAPI.updateSubmodelElement(collection.getIdShort() + "/" + mlprop.getIdShort(), expected);

		submodelAPI.getSubmodelElementValue(mlprop.getIdShort());
	}

	private void uploadDummyFile(MongoDBSubmodelAPI submodelAPI, String idShort) throws FileNotFoundException {
		File file = new File("application/xml");
		file.setValue("");
		file.setIdShort(idShort);
		submodelAPI.addSubmodelElement(file);

		java.io.File dummyFile = new java.io.File("src/test/resources/testfile.xml");
		submodelAPI.uploadSubmodelElementFile(idShort, new FileInputStream(dummyFile));
	}

	private MongoDBSubmodelAPI createAPIWithPreconfiguredSubmodel() {
		client = MongoClients.create(new BaSyxMongoDBConfiguration().getConnectionUrl());
		MongoDBSubmodelAPI submodelAPI = new MongoDBSubmodelAPI("", client);

		Submodel mySM = new Submodel("mySubmodel", new CustomId("mySubmodelId"));

		submodelAPI.setSubmodel(mySM);
		return submodelAPI;
	}
}
