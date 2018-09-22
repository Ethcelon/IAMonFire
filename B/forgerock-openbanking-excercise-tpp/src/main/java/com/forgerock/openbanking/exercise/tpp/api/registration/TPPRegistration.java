/*
 * The contents of this file are subject to the terms of the Common Development and
 *  Distribution License (the License). You may not use this file except in compliance with the
 *  License.
 *
 *  You can obtain a copy of the License at https://forgerock.org/license/CDDLv1.0.html. See the License for the
 *  specific language governing permission and limitations under the License.
 *
 *  When distributing Covered Software, include this CDDL Header Notice in each file and include
 *  the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 *  Header, with the fields enclosed by brackets [] replaced by your own identifying
 *  information: "Portions copyright [year] [name of copyright owner]".
 *
 *  Copyright 2018 ForgeRock AS.
 *
 */
package com.forgerock.openbanking.exercise.tpp.api.registration;

import com.forgerock.openbanking.exercise.tpp.model.aspsp.AspspConfiguration;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Api(value = "aspsp", description = "Register the current TPP to an ASPSP")
@RequestMapping("/api/registration/")
public interface TPPRegistration {

    @ApiOperation(value = "Register the current TPP to an ASPSP", response = AspspConfiguration.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "TPP registered successfully to the ASPSP", response = AspspConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
        @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
        @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @RequestMapping(value = "/aspsp",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.POST)
    ResponseEntity<AspspConfiguration> registerToAspsp(
            @ApiParam(value = "The ASPSP financial ID", required = true)
            @RequestHeader(value = "financial_id") String financialID,

            @ApiParam(value = "The ASPSP-AS OIDC root endpoint", required = true)
            @RequestHeader("as_discovery_endpoint") String oidcRootEndpoint,

            @ApiParam(value = "The ASPSP-RS discovery endpoint", required = true)
            @RequestHeader("rs_discovery_endpoint") String discoveryEndpoint
    );

    @ApiOperation(value = "Unregister the current TPP to an ASPSP", response = AspspConfiguration.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "TPP unregistered successfully to the ASPSP", response = AspspConfiguration.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
            @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @RequestMapping(value = "/aspsp",
            method = RequestMethod.DELETE)
    ResponseEntity<AspspConfiguration> unregisterToAspsp(
            @ApiParam(value = "The ASPSP-AS OIDC root endpoint", required = true)
            @RequestHeader("as_discovery_endpoint") String oidcRootEndpoint
    );

    @RequestMapping(value = "/aspsp",
            method = RequestMethod.PUT)
    ResponseEntity<AspspConfiguration> loadAspspConfig(
            @ApiParam(value = "The ASPSP financial ID", required = true)
            @RequestHeader(value = "financial_id") String financialID,

            @ApiParam(value = "The ASPSP-AS OIDC root endpoint", required = true)
            @RequestHeader("as_discovery_endpoint") String oidcRootEndpoint,

            @ApiParam(value = "The ASPSP-RS discovery endpoint", required = true)
            @RequestHeader("rs_discovery_endpoint") String discoveryEndpoint
    ) throws Exception;
}
