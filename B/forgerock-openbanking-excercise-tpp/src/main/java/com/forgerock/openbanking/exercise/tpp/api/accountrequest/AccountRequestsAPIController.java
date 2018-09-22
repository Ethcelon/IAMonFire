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
package com.forgerock.openbanking.exercise.tpp.api.accountrequest;

import com.forgerock.openbanking.exercise.tpp.configuration.TppConfiguration;
import com.forgerock.openbanking.exercise.tpp.constants.OpenBankingConstants;
import com.forgerock.openbanking.exercise.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.exercise.tpp.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.exercise.tpp.model.oidc.OIDCState;
import com.forgerock.openbanking.exercise.tpp.repository.AspspConfigurationRepository;
import com.forgerock.openbanking.exercise.tpp.repository.OIDCStateRepository;
import com.forgerock.openbanking.exercise.tpp.services.aspsp.as.AspspAsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import uk.org.openbanking.datamodel.account.OBReadResponse1;

import java.util.Arrays;
import java.util.List;


@Controller
public class AccountRequestsAPIController implements AccountRequestsAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestsAPIController.class);

    private static final List<String> AISP_SCOPES = Arrays.asList(OpenBankingConstants.Scope.ACCOUNTS,
            OpenBankingConstants.Scope.OPENID, OpenBankingConstants.Scope.PAYMENTS);

    @Autowired
    private AspspAsService aspspAsService;
    @Autowired
    private com.forgerock.openbanking.exercise.tpp.services.aspsp.rs.RSAccountAPIService RSAccountAPIService;
    @Autowired
    private TppConfiguration tppConfiguration;
    @Autowired
    private AspspConfigurationRepository aspspConfigurationRepository;
    @Autowired
    private OIDCStateRepository oidcStateRepository;

    /**
     * The initiate payment as defined by the OpenBanking standard.
     *
     * @return redirect to the authorization endpoint of the AS, as described by the hybrid flow
     */
    public ResponseEntity<String> initiateAccountRequest(
            @RequestParam(value = "aspspId") String aspspId
            ) {

        LOGGER.debug("Start initiateAccountRequest with aspspId {}", aspspId);

        AspspConfiguration aspspConfiguration = aspspConfigurationRepository.findById(aspspId).get();

        LOGGER.debug("We generate an OIDC state, where we save the request parameters");
        OIDCState oidcState = oidcStateRepository.save(new OIDCState()
                .aspspId(aspspId)
        );

        String state = oidcState.getState();
        String nonce = state;
        LOGGER.debug("The state id is {}", state);

        //Step 2 get an access token via the client grant flow
        try {
            LOGGER.debug("Get an access token via the client credential flow");
            //TODO for this demo, we generate an access token each time. It can actually be re-used until expiration.
            AccessTokenResponse accessTokenResponse = aspspAsService.clientCredential(aspspConfiguration, AISP_SCOPES);
            LOGGER.debug("Received access token '{}'", accessTokenResponse.access_token);

            LOGGER.debug("Register the payment to the RS");
            OBReadResponse1 accountRequestResponse = RSAccountAPIService.createAccountRequest(aspspConfiguration, accessTokenResponse);
            String accountRequestID = accountRequestResponse.getData().getAccountRequestId();
            //We tag the payment ID to the request so we can track requests associated with a payment ID.
            LOGGER.debug("The received account Request ID '{}'", accountRequestID);

            oidcState.intentId(accountRequestID);
            oidcStateRepository.save(oidcState);

            //create the request parameter
            String requestParameter = aspspAsService.generateRequestParameter(aspspConfiguration, accountRequestID,
                    state, nonce, tppConfiguration.getRedirectUris().getAisp(), AISP_SCOPES);
            LOGGER.debug("Start the hybrid flow with the following request param '{}'", requestParameter);

            //redirect to the authorization page with the request parameter.
            return new ResponseEntity<>(aspspAsService.hybridFlow(aspspConfiguration, state, nonce, requestParameter,
                    tppConfiguration.getRedirectUris().getAisp(), AISP_SCOPES), HttpStatus.FOUND);
        }  catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("Couldn't read the error returned by the RS", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error("An error happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Exchange code endpoint, to complete the hybrid flow. The AS will call this endpoint via a redirection of the
     * user to it. It will send us the authorization code which will allow us to get an access token. The state and
     * the id token are here for extra validation. As the request go through the user, the OpenBanking standard
     * expects we validate the consistency of the information received with the initial request.
     *  @param code    the authorization code
     * @param idToken the ID token
     * @param state   the state
     */
    public ResponseEntity<String> exchangeCode(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "id_token") String idToken,
            @RequestParam(value = "state") String state) {
        LOGGER.debug("Received authorization code '{}', ID token '{}' and state '{}'", code, idToken, state);
        OIDCState oidcState = oidcStateRepository.findById(state).get();

        try {
            AspspConfiguration aspspConfiguration = aspspConfigurationRepository.findById(oidcState.getAspspId()).get();
            LOGGER.debug("Exchange the code '{}' to an access token.", code);
            AccessTokenResponse accessTokenResponse = aspspAsService.exchangeCode(code,
                    aspspConfiguration, tppConfiguration.getRedirectUris().getAisp());
            LOGGER.debug("Received an access token '{}' in exchange of the code.", accessTokenResponse.access_token);
            return ResponseEntity.ok(accessTokenResponse.getAccessTokenJWT().serialize());

        }  catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("Couldn't read the error returned by the RS", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error("An error happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
