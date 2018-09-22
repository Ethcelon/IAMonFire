package com.forgerock.openbanking.exercise.tpp.api.balances;

import com.forgerock.openbanking.exercise.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.exercise.tpp.repository.AspspConfigurationRepository;
import com.forgerock.openbanking.exercise.tpp.services.aspsp.rs.RSAccountAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Controller
public class BalanceAPIController implements BalanceAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceAPIController.class);

    @Autowired
    private RSAccountAPIService rsAccountAPIService;
    @Autowired
    private AspspConfigurationRepository aspspConfigurationRepository;


    @Override
    public ResponseEntity readBalance(
            @RequestParam(value = "aspspId") String aspspId,
            @RequestParam(value = "accountId") String accountId,
            @RequestHeader(value = "accessToken") String accessToken) {
        //TODO: exercise: retrieve the aspsp configuration

        AspspConfiguration aspspConfiguration = aspspConfigurationRepository.findById(aspspId).get();

        //TODO exercise: call the RS-ASPSP accounts endpoint via the service
        try {
            return ResponseEntity.ok(rsAccountAPIService.readBalance(aspspConfiguration, accountId, accessToken));




        }  catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("Couldn't read the error returned by the RS", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error("An error happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
