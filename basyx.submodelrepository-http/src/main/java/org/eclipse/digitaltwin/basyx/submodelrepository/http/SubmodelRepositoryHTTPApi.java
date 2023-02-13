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

/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.29).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.eclipse.digitaltwin.basyx.submodelrepository.http;

import java.util.List;

import javax.validation.Valid;

import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.basyx.http.Base64UrlEncodedIdentifier;
import org.eclipse.digitaltwin.basyx.submodelservice.value.PropertyValue;
import org.eclipse.digitaltwin.basyx.submodelservice.value.SubmodelElementValue;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-01-10T15:59:05.892Z[GMT]")
@Validated
public interface SubmodelRepositoryHTTPApi {
	@Operation(summary = "Returns all Submodels", description = "", tags = { "Asset Administration Shell Repository" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Requested Submodels", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Submodel.class)))) })
	@RequestMapping(value = "/submodels", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Submodel>> getAllSubmodels(
			@Parameter(in = ParameterIn.QUERY, description = "The value of the semantic id reference (BASE64-URL-encoded)", schema = @Schema()) @Valid @RequestParam(value = "semanticId", required = false) String semanticId,
			@Parameter(in = ParameterIn.QUERY, description = "The Submodel’s idShort", schema = @Schema()) @Valid @RequestParam(value = "idShort", required = false) String idShort);

	@Operation(summary = "Returns the Submodel", description = "", tags = { "Asset Administration Shell Repository" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Requested Submodel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Submodel.class))) })
	@RequestMapping(value = "/submodels/{submodelIdentifier}/submodel", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<Submodel> getSubmodelSubmodelRepo(
			@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("submodelIdentifier") Base64UrlEncodedIdentifier submodelIdentifier,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content", schema = @Schema(allowableValues = { "deep",
					"core" }, defaultValue = "deep")) @Valid @RequestParam(value = "level", required = false, defaultValue = "deep") String level,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource", schema = @Schema(allowableValues = { "normal", "trimmed", "value", "reference",
					"path" }, defaultValue = "normal")) @Valid @RequestParam(value = "content", required = false, defaultValue = "normal") String content,
			@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized", schema = @Schema(allowableValues = { "withBlobValue",
					"withoutBlobValue" })) @Valid @RequestParam(value = "extent", required = false) String extent);

	@Operation(summary = "Updates the Submodel", description = "", tags = { "Asset Administration Shell Repository" })
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Submodel updated successfully") })
	@RequestMapping(value = "/submodels/{submodelIdentifier}/submodel", consumes = { "application/json" }, method = RequestMethod.PUT)
	ResponseEntity<Void> putSubmodelSubmodelRepo(
			@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("submodelIdentifier") Base64UrlEncodedIdentifier submodelIdentifier,
			@Parameter(in = ParameterIn.DEFAULT, description = "Submodel object", required = true, schema = @Schema()) @Valid @RequestBody Submodel body,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content", schema = @Schema(allowableValues = { "deep",
					"core" }, defaultValue = "deep")) @Valid @RequestParam(value = "level", required = false, defaultValue = "deep") String level,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource", schema = @Schema(allowableValues = { "normal", "trimmed", "value", "reference",
					"path" }, defaultValue = "normal")) @Valid @RequestParam(value = "content", required = false, defaultValue = "normal") String content,
			@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized", schema = @Schema(allowableValues = { "withBlobValue",
					"withoutBlobValue" })) @Valid @RequestParam(value = "extent", required = false) String extent);

	@Operation(summary = "Creates a new Submodel", description = "", tags = { "Asset Administration Shell Repository" })
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Submodel created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Submodel.class))) })
	@RequestMapping(value = "/submodels", produces = { "application/json" }, consumes = { "application/json" }, method = RequestMethod.POST)
	ResponseEntity<Submodel> postSubmodel(@Parameter(in = ParameterIn.DEFAULT, description = "Submodel object", required = true, schema = @Schema()) @Valid @RequestBody Submodel body);
	
    @Operation(summary = "Returns all submodel elements including their hierarchy", description = "", tags={ "Asset Administration Shell Repository" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "List of found submodel elements", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SubmodelElement.class)))) })
    @RequestMapping(value = "/submodels/{submodelIdentifier}/submodel/submodel-elements",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
	ResponseEntity<List<SubmodelElement>> getAllSubmodelElementsSubmodelRepo(
			@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("submodelIdentifier") Base64UrlEncodedIdentifier submodelIdentifier,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content", schema = @Schema(allowableValues = { "deep", "core" }
, defaultValue="deep")) @Valid @RequestParam(value = "level", required = false, defaultValue="deep") String level, @Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "trimmed", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content, @Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized" ,schema=@Schema(allowableValues={ "withBlobValue", "withoutBlobValue" }
)) @Valid @RequestParam(value = "extent", required = false) String extent);

	@Operation(summary = "Returns a specific submodel element from the Submodel at a specified path", description = "", tags = { "Asset Administration Shell Repository" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Requested submodel element", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubmodelElement.class))) })
	@RequestMapping(value = "/submodels/{submodelIdentifier}/submodel/submodel-elements/{idShortPath}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<Object> getSubmodelElementByPathSubmodelRepo(
			@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("submodelIdentifier") Base64UrlEncodedIdentifier submodelIdentifier,
			@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required = true, schema = @Schema()) @PathVariable("idShortPath") String idShortPath,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content", schema = @Schema(allowableValues = { "deep",
					"core" }, defaultValue = "deep")) @Valid @RequestParam(value = "level", required = false, defaultValue = "deep") String level,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource", schema = @Schema(allowableValues = { "normal", "trimmed", "value", "reference",
					"path" }, defaultValue = "normal")) @Valid @RequestParam(value = "content", required = false, defaultValue = "normal") String content,
			@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized", schema = @Schema(allowableValues = { "withBlobValue",
					"withoutBlobValue" })) @Valid @RequestParam(value = "extent", required = false) String extent);

	@Operation(summary = "Updates an existing submodel element at a specified path within submodel elements hierarchy", description = "", tags = { "Asset Administration Shell Repository" })
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Submodel element updated successfully") })
	@RequestMapping(value = "/submodels/{submodelIdentifier}/submodel/submodel-elements/{idShortPath}", consumes = { "application/json" }, method = RequestMethod.PUT)
	ResponseEntity<Void> putSubmodelElementByPathSubmodelRepo(
			@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("submodelIdentifier") Base64UrlEncodedIdentifier submodelIdentifier,
			@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required = true, schema = @Schema()) @PathVariable("idShortPath") String idShortPath,
			@Parameter(in = ParameterIn.DEFAULT, description = "Requested submodel element", required = true, schema = @Schema()) @Valid @RequestBody SubmodelElementValue body,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content", schema = @Schema(allowableValues = { "deep",
					"core" }, defaultValue = "deep")) @Valid @RequestParam(value = "level", required = false, defaultValue = "deep") String level,
			@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource", schema = @Schema(allowableValues = { "normal", "trimmed", "value", "reference",
					"path" }, defaultValue = "normal")) @Valid @RequestParam(value = "content", required = false, defaultValue = "normal") String content,
			@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized", schema = @Schema(allowableValues = { "withBlobValue",
					"withoutBlobValue" })) @Valid @RequestParam(value = "extent", required = false) String extent);
}
