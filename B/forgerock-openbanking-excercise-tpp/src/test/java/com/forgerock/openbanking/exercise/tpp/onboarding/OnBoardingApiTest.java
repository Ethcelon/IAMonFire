package com.forgerock.openbanking.exercise.tpp.onboarding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.exercise.tpp.api.registration.TPPRegistrationController;
import com.forgerock.openbanking.exercise.tpp.configuration.TppConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnBoardingApiTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TppConfiguration tppConfiguration;

    private MockMvc mockMvc;

    private String aspspConfigId = null;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)
                         .uris()
                                .withScheme("https")
                                .withHost("localhost")
                                .withPort(8080)
                                .and().snippets()
                                .withDefaults(CliDocumentation.curlRequest(),
                                        HttpDocumentation.httpRequest(),
                                        HttpDocumentation.httpResponse()
                                )
                )
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("https")
                        .withHost("localhost")
                        .withPort(7777)
                        .and().snippets()
                        .withDefaults(CliDocumentation.curlRequest(),
                                HttpDocumentation.httpRequest(),
                                HttpDocumentation.httpResponse()
                        ))
                .build();
    }

    @Test
    public void onBoarding() throws Exception {
        //On-board TPP to ForgeRock ASPSP
        MvcResult result = this.mockMvc.perform(
                post("/api/registration/aspsp")
                        .header("financial_id", tppConfiguration.getAspsp().getFinancialId())
                        .header("as_discovery_endpoint", tppConfiguration.getAspsp().getAsDiscoveryEndpoint())
                        .header("rs_discovery_endpoint", tppConfiguration.getAspsp().getRsDiscoveryEndpoint())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
        .andDo(
                document("on-boarding",
                        responseFields(
                                fieldWithPath("id").description("The internal ID of this on-boarding")
                        )
                )
        ).andReturn()
        ;
        TPPRegistrationController.RegistrationResponse registrationResponse = mapper.readValue(result.getResponse().getContentAsString(), TPPRegistrationController.RegistrationResponse.class);
        aspspConfigId = registrationResponse.getId();
    }

    @After
    public void unsetup() throws Exception {
        MockMvcBuilders.webAppContextSetup(this.context).build().perform(delete("/api/registration/aspsp/{aspspId}", aspspConfigId));
    }
}
