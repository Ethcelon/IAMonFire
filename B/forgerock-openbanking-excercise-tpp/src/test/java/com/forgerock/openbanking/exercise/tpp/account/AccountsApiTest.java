package com.forgerock.openbanking.exercise.tpp.account;

import com.forgerock.openbanking.exercise.tpp.template.PostAISPAccessTokenTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.datamodel.account.OBReadAccount2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsApiTest extends PostAISPAccessTokenTest {

    @Ignore("Fix me by implementing get account logic in AccountsAPIController")
    @Test
    public void getAccounts() throws Exception {
        String obPaymentSubmissionResponseSerialised = this.mockMvcForDocs.perform(
                get("/api/open-banking/accounts/")
                        .param("aspspId", aspspConfigId)
                        .header("accessToken", accessToken)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OBReadAccount2 accounts = mapper.readValue(obPaymentSubmissionResponseSerialised, OBReadAccount2.class);

        assertThat(accounts).isNotNull();
        assertThat(accounts.getData().getAccount()).isNotEmpty();
    }
}
