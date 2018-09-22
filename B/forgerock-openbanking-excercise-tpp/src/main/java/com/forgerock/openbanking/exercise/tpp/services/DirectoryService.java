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
package com.forgerock.openbanking.exercise.tpp.services;

import com.forgerock.openbanking.exercise.tpp.configuration.TppConfiguration;
import com.forgerock.openbanking.exercise.tpp.model.directory.SoftwareStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DirectoryService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DirectoryService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TppConfiguration tppConfiguration;

    public String getSSA() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.postForObject(tppConfiguration.getDirectory().getEndpoint() + "/api/software-statement/current/ssa", request, String.class);
    }

    public SoftwareStatement getSoftwareStatement() {
        return restTemplate.getForEntity(tppConfiguration.getDirectory().getEndpoint() + "/api/software-statement/current/", SoftwareStatement.class).getBody();
    }

    public String testmatls() {
        return restTemplate.getForEntity(tppConfiguration.getDirectory().getEndpoint() + "/api/matls/test", String.class).getBody();

    }
}
