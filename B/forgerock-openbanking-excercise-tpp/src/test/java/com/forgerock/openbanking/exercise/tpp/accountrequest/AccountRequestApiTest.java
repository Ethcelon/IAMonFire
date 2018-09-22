package com.forgerock.openbanking.exercise.tpp.accountrequest;

import com.forgerock.openbanking.exercise.tpp.template.PostOnboardTest;
import com.forgerock.openbanking.exercise.tpp.ui.view.AMLoginView;
import com.forgerock.openbanking.exercise.tpp.ui.view.RCSAccountsConsentView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRequestApiTest extends PostOnboardTest {

    @Test
    public void initiateAccountRequest() throws Exception {
        //Initiate account request
        String redirectedUrl = this.mockMvcForDocs.perform(
                post("/api/open-banking/account-requests/initiate")
                        .param("aspspId", aspspConfigId)
        )
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn().getResponse().getContentAsString();

        //Login to the ASPSP
        AMLoginView amLoginView = new AMLoginView(config);
        amLoginView.navigate(redirectedUrl);
        amLoginView.login(tppConfiguration.getDirectory().getUser().getUsername(), tppConfiguration.getDirectory().getUser().getPassword());

        //Accept account sharing consent
        RCSAccountsConsentView accountsConsentView = new RCSAccountsConsentView(config);
        accountsConsentView.allow();

        //Simulate the javascript redirect, as we are limited by what we can simulate
        MultiValueMap<String, String> queryMap = getQueryMap(new URI(config.getDriver().getCurrentUrl()).getFragment());
        String accessToken = this.mockMvcForDocs.perform(
                get("/api/open-banking/account-requests/exchange_code")
                        .params(queryMap)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(accessToken).isNotEmpty();
    }
}
