package com.forgerock.openbanking.exercise.tpp.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.exercise.tpp.api.registration.TPPRegistrationController;
import com.forgerock.openbanking.exercise.tpp.configuration.TppConfiguration;
import com.forgerock.openbanking.exercise.tpp.ui.SeleniumConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class PostOnboardTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;
    @Autowired
    public TppConfiguration tppConfiguration;
    @Autowired
    public ObjectMapper mapper;

    public MockMvc mockMvcForDocs;
    public MockMvc mockMvcForSettingUpTest;
    public String aspspConfigId = null;
    public SeleniumConfig config;

    @Before
    public void setUp() throws Exception {
        this.mockMvcForSettingUpTest = MockMvcBuilders.webAppContextSetup(this.context).build();
        this.mockMvcForDocs = MockMvcBuilders.webAppContextSetup(this.context)
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
                .alwaysDo(document("{ClassName}/{method-name}/{step}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
        unregisterAspsp();
        this.aspspConfigId = onboard(mockMvcForSettingUpTest);
        this.tppConfiguration.getRedirectUris().setAisp("https://localhost");
        this.tppConfiguration.getRedirectUris().setPisp("https://localhost");
        this.config = new SeleniumConfig();
    }


    @After
    public void unsetup() throws Exception {
        unregisterAspsp();
        config.getDriver().close();
    }

    private void unregisterAspsp() throws Exception {
        mockMvcForSettingUpTest.perform(delete("/api/registration/aspsp")
                .header("as_discovery_endpoint", tppConfiguration.getAspsp().getAsDiscoveryEndpoint()));
    }

    public String onboard(MockMvc mockMvc) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/api/registration/aspsp")
                        .header("financial_id", tppConfiguration.getAspsp().getFinancialId())
                        .header("as_discovery_endpoint", tppConfiguration.getAspsp().getAsDiscoveryEndpoint())
                        .header("rs_discovery_endpoint", tppConfiguration.getAspsp().getRsDiscoveryEndpoint())
        )
                .andReturn();
        TPPRegistrationController.RegistrationResponse registrationResponse = mapper.readValue(result.getResponse().getContentAsString(),
                TPPRegistrationController.RegistrationResponse.class);
        return registrationResponse.getId();
    }

    public static MultiValueMap<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.add(name, value);
        }
        return map;
    }
}
