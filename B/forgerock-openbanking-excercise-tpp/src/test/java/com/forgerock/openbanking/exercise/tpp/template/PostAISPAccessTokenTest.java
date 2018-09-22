package com.forgerock.openbanking.exercise.tpp.template;

import com.forgerock.openbanking.exercise.tpp.ui.view.AMLoginView;
import com.forgerock.openbanking.exercise.tpp.ui.view.RCSAccountsConsentView;
import org.junit.After;
import org.junit.Before;
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
public abstract class PostAISPAccessTokenTest extends PostOnboardTest {

    public String accessToken;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.accessToken = getAccessToken();
    }

    private String getAccessToken() throws Exception {
        //Initiate account request
        String redirectedUrl = this.mockMvcForSettingUpTest.perform(
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
        String accessToken = this.mockMvcForSettingUpTest.perform(
                get("/api/open-banking/account-requests/exchange_code")
                        .params(queryMap)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(accessToken).isNotEmpty();
        return accessToken;
    }

    @After
    public void unsetup() throws Exception {
        super.unsetup();
    }
}
