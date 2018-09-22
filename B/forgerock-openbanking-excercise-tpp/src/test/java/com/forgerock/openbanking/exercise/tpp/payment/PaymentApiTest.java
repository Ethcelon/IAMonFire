package com.forgerock.openbanking.exercise.tpp.payment;

import com.forgerock.openbanking.exercise.tpp.template.PostOnboardTest;
import com.forgerock.openbanking.exercise.tpp.ui.view.AMLoginView;
import com.forgerock.openbanking.exercise.tpp.ui.view.RCSPaymentConsentView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmissionResponse1;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentApiTest extends PostOnboardTest {

    @Test
    public void initiatePayment() throws Exception {
        //Initiate payment request
        String redirectedUrl = this.mockMvcForDocs.perform(
                post("/api/open-banking/payment-requests/initiate")
                        .param("aspspId", aspspConfigId)
        )
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn().getResponse().getContentAsString();

        //Login to the ASPSP
        AMLoginView amLoginView = new AMLoginView(config);
        amLoginView.navigate(redirectedUrl);
        amLoginView.login(tppConfiguration.getDirectory().getUser().getUsername(), tppConfiguration.getDirectory().getUser().getPassword());

        //Accept payment
        RCSPaymentConsentView paymentConsentView = new RCSPaymentConsentView(config);
        paymentConsentView.allow();
        paymentConsentView.submit(0);

        //Simulate the javascript redirect, as we are limited by what we can simulate
        MultiValueMap<String, String> queryMap = getQueryMap(new URI(config.getDriver().getCurrentUrl()).getFragment());
        String obPaymentSubmissionResponseSerialised = this.mockMvcForDocs.perform(
                get("/api/open-banking/payment-requests/exchange_code")
                        .params(queryMap)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        OBPaymentSubmissionResponse1 obPaymentSubmissionResponse = mapper.readValue(obPaymentSubmissionResponseSerialised, OBPaymentSubmissionResponse1.class);

        assertThat(obPaymentSubmissionResponse.getData().getStatus()).isEqualTo(OBTransactionIndividualStatus1Code.ACCEPTED_SETTLEMENT_IN_PROCESS);
    }
}
