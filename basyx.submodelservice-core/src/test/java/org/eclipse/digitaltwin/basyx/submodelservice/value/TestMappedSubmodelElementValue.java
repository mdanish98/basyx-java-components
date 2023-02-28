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
package org.eclipse.digitaltwin.basyx.submodelservice.value;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AnnotatedRelationshipElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Blob;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.Entity;
import org.eclipse.digitaltwin.aas4j.v3.model.EntityType;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.Key;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.LangString;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Range;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.RelationshipElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.basyx.submodelservice.SubmodelServiceHelper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.AnnotatedRelationshipElementValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.BlobValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.EntityValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.FileValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.MultiLanguagePropertyValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.PropertyValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.RangeValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.ReferenceElementValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.RelationshipElementValueMapper;
import org.eclipse.digitaltwin.basyx.submodelservice.value.mapper.ValueMapper;
import org.junit.Test;

/**
 * Tests the mapped value from SubmodelElements
 * 
 * @author danish
 *
 */
public class TestMappedSubmodelElementValue {

	private List<ValueOnly> valueOnlies = Arrays.asList(
			new ValueOnly(SubmodelServiceHelper.SUBMODEL_TECHNICAL_DATA_PROPERTY_ID_SHORT, new PropertyValue("120")),
			new ValueOnly(SubmodelServiceHelper.SUBMODEL_TECHNICAL_DATA_RANGE_ID_SHORT, new RangeValue(20, 40)));
	private EntityType testEntityType = EntityType.CO_MANAGED_ENTITY;
	private ReferenceValue referenceValue_first = new ReferenceValue(ReferenceTypes.GLOBAL_REFERENCE,
			Arrays.asList(new DefaultKey.Builder().type(KeyTypes.CAPABILITY).value("CapabilityType").build()));
	private ReferenceValue referenceValue_second = new ReferenceValue(ReferenceTypes.MODEL_REFERENCE, Arrays
			.asList(new DefaultKey.Builder().type(KeyTypes.RELATIONSHIP_ELEMENT).value("RelationshipElement").build()));
	private List<SpecificAssetIdValue> specificAssetIdValues = Arrays
			.asList(new SpecificAssetIdValue("TestSpecificId", "TestSpecificAssetIdValue"));
	private List<Key> referenceKeys = Arrays
			.asList(new DefaultKey.Builder().type(KeyTypes.REFERENCE_ELEMENT).value("ReferenceElementKey").build());

	@Test
	public void mappedGetRangeValue() {
		Range expected = SubmodelServiceHelper.createRangeSubmodelElement();

		RangeValue retrievedValue = (RangeValue) new RangeValueMapper(expected).getValue();

		assertEquals(expected.getMin(), String.valueOf(retrievedValue.getMin()));

		assertEquals(expected.getMax(), String.valueOf(retrievedValue.getMax()));
	}

	@Test
	public void mappedSetRangeValue() {
		int expectedMin = 50;
		int expectedMax = 100;

		Range range = SubmodelServiceHelper.createRangeSubmodelElement();

		setRangeValue(expectedMin, expectedMax, range);

		assertEquals(Integer.valueOf(expectedMin), Integer.valueOf(range.getMin()));

		assertEquals(Integer.valueOf(expectedMax), Integer.valueOf(range.getMax()));
	}

	@Test
	public void mappedGetPropertyValue() {
		String expectedValue = "200";

		Property property = new DefaultProperty.Builder().value(expectedValue).valueType(DataTypeDefXsd.INTEGER)
				.build();

		ValueMapper<PropertyValue> rangeValueMapper = new PropertyValueMapper(property);

		assertEquals(expectedValue, rangeValueMapper.getValue().getValue());
	}

	@Test
	public void mappedSetPropertyValue() {
		String expectedValue = "5000";

		Property property = SubmodelServiceHelper.createPropertySubmodelElement();

		setPropertyValue(expectedValue, property);

		assertEquals(expectedValue, property.getValue());
	}

	@Test
	public void mappedGetMultiLanguagePropertyValue() {
		List<LangString> expectedValue = Arrays.asList(new DefaultLangString("Hello", "en"),
				new DefaultLangString("Hallo", "de"));

		MultiLanguageProperty multiLanguageProperty = SubmodelServiceHelper.createMultiLanguagePropertySubmodelElement();

		MultiLanguagePropertyValueMapper multiLanguagePropertyValueMapper = new MultiLanguagePropertyValueMapper(
				multiLanguageProperty);

		assertEquals(expectedValue.get(0).getLanguage(),
				((MultiLanguagePropertyValue) multiLanguagePropertyValueMapper.getValue()).getValue().get(0)
						.getLanguage());
	}

	@Test
	public void mappedSetMultiLanguagePropertyValue() {
		List<LangString> expectedValue = Arrays.asList(new DefaultLangString("Bonjour", "fr"),
				new DefaultLangString("Hola", "es"));

		MultiLanguageProperty multiLanguageProperty = SubmodelServiceHelper.createMultiLanguagePropertySubmodelElement();

		setMultiLanguagePropertyValue(expectedValue, multiLanguageProperty);

		assertEquals(expectedValue, multiLanguageProperty.getValue());
	}

	@Test
	public void mappedGetFileValue() {
		String expectedValue = SubmodelServiceHelper.SUBMODEL_TECHNICAL_DATA_FILE_VALUE;

		File file = SubmodelServiceHelper.createFileSubmodelElement();

		FileValueMapper fileValueMapper = new FileValueMapper(file);

		assertEquals(expectedValue, ((FileBlobValue) fileValueMapper.getValue()).getValue());
	}

	@Test
	public void mappedSetFileValue() {
		String expectedContentType = "application/pdf";
		String expectedValue = "someTestFile.pdf";

		File file = SubmodelServiceHelper.createFileSubmodelElement();

		setFileValue(expectedContentType, expectedValue, file);

		assertEquals(expectedContentType, file.getContentType());

		assertEquals(expectedValue, file.getValue());
	}
	
	@Test
	public void mappedGetBlobValue() {
		String expectedValue = SubmodelServiceHelper.SUBMODEL_TECHNICAL_DATA_BLOB_VALUE;

		Blob blob = SubmodelServiceHelper.createBlobSubmodelElement();

		BlobValueMapper fileValueMapper = new BlobValueMapper(blob);

		assertEquals(expectedValue, ((FileBlobValue) fileValueMapper.getValue()).getValue());
	}

	@Test
	public void mappedSetBlobValue() {
		String expectedContentType = "application/xml";
		String expectedValue = "This is the test content of the xml file";

		Blob blob = SubmodelServiceHelper.createBlobSubmodelElement();

		setBlobValue(expectedContentType, expectedValue, blob);

		assertEquals(expectedContentType, blob.getContentType());

		assertEquals(expectedValue, new String(blob.getValue(), StandardCharsets.UTF_8));
	}

	@Test
	public void mappedGetEntityValue() {
		Entity expected = SubmodelServiceHelper.createEntitySubmodelElement();

		EntityValueMapper fileValueMapper = new EntityValueMapper(expected);

		assertEqualsEntityValue(expected, fileValueMapper);
	}

	@Test
	public void mappedSetEntityValue() {
		Entity entity = SubmodelServiceHelper.createEntitySubmodelElement();
		
		EntityValue entityValue = new EntityValue(valueOnlies, testEntityType, referenceValue_first,
				specificAssetIdValues);

		setReferenceElementValue(entity, entityValue);

		assertEqualsSetEntityValue(valueOnlies, testEntityType, referenceValue_first, specificAssetIdValues, entity);
	}

	@Test
	public void mappedGetReferenceElementValue() {
		ReferenceElement expected = SubmodelServiceHelper.createReferenceElementSubmodelElement();

		ReferenceElementValueMapper referenceValueMapper = new ReferenceElementValueMapper(expected);

		assertEquals(expected.getValue().getType(), referenceValueMapper.getValue().getReferenceValue().getType());

		assertEquals(expected.getValue().getKeys(), referenceValueMapper.getValue().getReferenceValue().getKeys());
	}

	@Test
	public void mappedSetReferenceElementValue() {
		ReferenceElement entity = SubmodelServiceHelper.createReferenceElementSubmodelElement();

		ReferenceValue referenceValue = new ReferenceValue(ReferenceTypes.GLOBAL_REFERENCE, referenceKeys);

		ReferenceElementValue referenceElementValue = new ReferenceElementValue(referenceValue);

		setReferenceElementValue(entity, referenceElementValue);

		assertEquals(ReferenceTypes.GLOBAL_REFERENCE, entity.getValue().getType());

		assertEquals(referenceKeys, entity.getValue().getKeys());
	}

	@Test
	public void mappedGetRelationshipElementValue() {
		RelationshipElement expected = SubmodelServiceHelper.createRelationshipElementSubmodelElement();

		RelationshipElementValueMapper referenceValueMapper = new RelationshipElementValueMapper(expected);

		assertEqualsRelationshipElementValue(expected, referenceValueMapper);
	}

	@Test
	public void mappedSetRelationshipElementValue() {
		RelationshipElement relationshipElement = SubmodelServiceHelper.createRelationshipElementSubmodelElement();

		RelationshipElementValue relationshipElementValue = new RelationshipElementValue(referenceValue_first,
				referenceValue_second);

		setRelationshipElementValue(relationshipElement, relationshipElementValue);

		assertEqualsSetRelationshipElementValue(referenceValue_first, referenceValue_second, relationshipElement);
	}

	@Test
	public void mappedGetAnnotatedRelationshipElementValue() {
		AnnotatedRelationshipElement expected = SubmodelServiceHelper.createAnnotatedRelationshipElementSubmodelElement();

		AnnotatedRelationshipElementValueMapper relationshipElementValueMapper = new AnnotatedRelationshipElementValueMapper(
				expected);

		assertEqualsAnnotatedRelationshipElementValue(expected, relationshipElementValueMapper);
	}

	@Test
	public void mappedSetAnnotatedRelationshipElementValue() {
		AnnotatedRelationshipElement annotatedRelationshipElement = SubmodelServiceHelper
				.createAnnotatedRelationshipElementSubmodelElement();

		AnnotatedRelationshipElementValue relationshipElementValue = new AnnotatedRelationshipElementValue(
				referenceValue_first, referenceValue_second, valueOnlies);

		setReferenceElementValue(annotatedRelationshipElement, relationshipElementValue);

		assertEqualsSetAnnotatedRelationshipElementValue(valueOnlies, referenceValue_first, referenceValue_second, annotatedRelationshipElement);
	}

	private static void assertEqualsSetAnnotatedRelationshipElementValue(List<ValueOnly> expectedValueOnlies,
			ReferenceValue expectedFirst, ReferenceValue expectedSecond,
			AnnotatedRelationshipElement annotatedRelationshipElement) {
		assertEquals(((PropertyValue) expectedValueOnlies.get(0).getSubmodelElementValue()).getValue(),
				((Property) annotatedRelationshipElement.getAnnotations().get(0)).getValue());

		assertEquals(((RangeValue) expectedValueOnlies.get(1).getSubmodelElementValue()).getMin(),
				Integer.parseInt(((Range) annotatedRelationshipElement.getAnnotations().get(1)).getMin()));

		assertEquals(((RangeValue) expectedValueOnlies.get(1).getSubmodelElementValue()).getMax(),
				Integer.parseInt(((Range) annotatedRelationshipElement.getAnnotations().get(1)).getMax()));

		assertEquals(expectedFirst.getKeys(), annotatedRelationshipElement.getFirst().getKeys());
		assertEquals(expectedFirst.getType(), annotatedRelationshipElement.getFirst().getType());

		assertEquals(expectedSecond.getKeys(), annotatedRelationshipElement.getSecond().getKeys());
		assertEquals(expectedSecond.getType(), annotatedRelationshipElement.getSecond().getType());
	}

	private static void assertEqualsSetEntityValue(List<ValueOnly> expectedValueOnlies, EntityType expectedEntityType,
			ReferenceValue expectedReferenceValue, List<SpecificAssetIdValue> expectedSpecificAssetIdValues,
			Entity entity) {
		assertEquals(((PropertyValue) expectedValueOnlies.get(0).getSubmodelElementValue()).getValue(),
				((Property) entity.getStatements().get(0)).getValue());

		assertEquals(((RangeValue) expectedValueOnlies.get(1).getSubmodelElementValue()).getMin(),
				Integer.parseInt(((Range) entity.getStatements().get(1)).getMin()));

		assertEquals(((RangeValue) expectedValueOnlies.get(1).getSubmodelElementValue()).getMax(),
				Integer.parseInt(((Range) entity.getStatements().get(1)).getMax()));

		assertEquals(expectedEntityType, entity.getEntityType());

		assertEquals(expectedReferenceValue.getKeys(), entity.getGlobalAssetId().getKeys());

		assertEquals(expectedReferenceValue.getType(), entity.getGlobalAssetId().getType());

		assertEquals(expectedSpecificAssetIdValues.get(0).getName(), entity.getSpecificAssetId().getName());

		assertEquals(expectedSpecificAssetIdValues.get(0).getValue(), entity.getSpecificAssetId().getValue());
	}

	private static void assertEqualsSetRelationshipElementValue(ReferenceValue first, ReferenceValue second,
			RelationshipElement relationshipElement) {
		assertEquals(first.getType(), relationshipElement.getFirst().getType());
		assertEquals(first.getKeys(), relationshipElement.getFirst().getKeys());

		assertEquals(second.getType(), relationshipElement.getSecond().getType());
		assertEquals(second.getKeys(), relationshipElement.getSecond().getKeys());
	}

	private static void assertEqualsRelationshipElementValue(RelationshipElement expected,
			RelationshipElementValueMapper referenceValueMapper) {
		assertEquals(expected.getFirst().getType(), referenceValueMapper.getValue().getFirst().getType());
		assertEquals(expected.getFirst().getKeys(), referenceValueMapper.getValue().getFirst().getKeys());

		assertEquals(expected.getSecond().getType(), referenceValueMapper.getValue().getSecond().getType());
		assertEquals(expected.getSecond().getKeys(), referenceValueMapper.getValue().getSecond().getKeys());
	}

	private static void assertEqualsAnnotatedRelationshipElementValue(AnnotatedRelationshipElement expected,
			AnnotatedRelationshipElementValueMapper relationshipElementValueMapper) {
		assertEquals(expected.getFirst().getType(), relationshipElementValueMapper.getValue().getFirst().getType());
		assertEquals(expected.getFirst().getKeys(), relationshipElementValueMapper.getValue().getFirst().getKeys());

		assertEquals(expected.getSecond().getType(), relationshipElementValueMapper.getValue().getSecond().getType());
		assertEquals(expected.getSecond().getKeys(), relationshipElementValueMapper.getValue().getSecond().getKeys());

		assertEquals(expected.getAnnotations().size(),
				relationshipElementValueMapper.getValue().getAnnotation().size());

		assertEquals(((Property) expected.getAnnotations().get(0)).getValue(),
				((PropertyValue) relationshipElementValueMapper.getValue().getAnnotation().get(0)
						.getSubmodelElementValue()).getValue());

		assertEquals(((Range) expected.getAnnotations().get(1)).getMin(),
				String.valueOf(((RangeValue) relationshipElementValueMapper.getValue().getAnnotation().get(1)
						.getSubmodelElementValue()).getMin()));

		assertEquals(((Range) expected.getAnnotations().get(1)).getMax(),
				String.valueOf(((RangeValue) relationshipElementValueMapper.getValue().getAnnotation().get(1)
						.getSubmodelElementValue()).getMax()));
	}

	private static void assertEqualsEntityValue(Entity expected, EntityValueMapper fileValueMapper) {
		assertEquals(expected.getStatements().size(), fileValueMapper.getValue().getStatements().size());

		assertEquals(((Property) expected.getStatements().get(0)).getValue(),
				String.valueOf(
						((PropertyValue) fileValueMapper.getValue().getStatements().get(0).getSubmodelElementValue())
								.getValue()));

		assertEquals(((Range) expected.getStatements().get(1)).getMin(), String.valueOf(
				((RangeValue) fileValueMapper.getValue().getStatements().get(1).getSubmodelElementValue()).getMin()));

		assertEquals(((Range) expected.getStatements().get(1)).getMax(), String.valueOf(
				((RangeValue) fileValueMapper.getValue().getStatements().get(1).getSubmodelElementValue()).getMax()));
	}

	private void setReferenceElementValue(AnnotatedRelationshipElement annotatedRelationshipElement,
			AnnotatedRelationshipElementValue annotatedrelationshipElementValue) {
		AnnotatedRelationshipElementValueMapper annotatedRelationshipElementValueMapper = new AnnotatedRelationshipElementValueMapper(
				annotatedRelationshipElement);
		annotatedRelationshipElementValueMapper.setValue(annotatedrelationshipElementValue);
	}

	private void setRelationshipElementValue(RelationshipElement relationshipElement,
			RelationshipElementValue relationshipElementValue) {
		RelationshipElementValueMapper referenceValueMapper = new RelationshipElementValueMapper(relationshipElement);
		referenceValueMapper.setValue(relationshipElementValue);
	}

	private void setReferenceElementValue(ReferenceElement entity, ReferenceElementValue referenceElementValue) {
		ReferenceElementValueMapper referenceValueMapper = new ReferenceElementValueMapper(entity);
		referenceValueMapper.setValue(referenceElementValue);
	}

	private void setReferenceElementValue(Entity entity, EntityValue entityValue) {
		EntityValueMapper entityValueMapper = new EntityValueMapper(entity);
		entityValueMapper.setValue(entityValue);
	}

	private void setFileValue(String expectedContentType, String expectedValue, File file) {
		FileBlobValue fileValue = new FileBlobValue(expectedContentType, expectedValue);

		FileValueMapper fileValueMapper = new FileValueMapper(file);
		fileValueMapper.setValue(fileValue);
	}
	
	private void setBlobValue(String expectedContentType, String expectedValue, Blob blob) {
		FileBlobValue blobValue = new FileBlobValue(expectedContentType, expectedValue);

		BlobValueMapper blobValueMapper = new BlobValueMapper(blob);
		blobValueMapper.setValue(blobValue);
	}

	private void setRangeValue(int expectedMin, int expectedMax, Range range) {
		RangeValue rangeValue = new RangeValue(expectedMin, expectedMax);

		RangeValueMapper rangeValueMapper = new RangeValueMapper(range);
		rangeValueMapper.setValue(rangeValue);
	}

	private void setPropertyValue(String expectedValue, Property property) {
		PropertyValue propertyValue = new PropertyValue(expectedValue);

		PropertyValueMapper propertyValueMapper = new PropertyValueMapper(property);
		propertyValueMapper.setValue(propertyValue);
	}

	private void setMultiLanguagePropertyValue(List<LangString> valueToWrite,
			MultiLanguageProperty multiLanguageProperty) {
		MultiLanguagePropertyValue multiLanguagePropertyValue = new MultiLanguagePropertyValue(valueToWrite);

		MultiLanguagePropertyValueMapper multiLanguagePropertyValueMapper = new MultiLanguagePropertyValueMapper(multiLanguageProperty);
		multiLanguagePropertyValueMapper.setValue(multiLanguagePropertyValue);
	}
}
