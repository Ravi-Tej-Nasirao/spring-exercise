package uk.co.lexisnexis.risk.search.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CompanySearchControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("search.company", wireMockServer::baseUrl);
        System.out.println(wireMockServer.baseUrl());
    }

    @Test
    @DisplayName("Get companySearchResponse when either CompanyName Or CompanyNumber are provided")
    void shouldGetCompanySearchResponseWhenEitherCompanyNameOrCompanyNumberAreProvided() throws Exception {
        String companyNumber = "06500244";
        wireMockServer.stubFor(WireMock.get(urlMatching("/Companies/v1/.*"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/vnd.company.app-v1+json")
                                .withBody("""
                                                                          {
                                                                              "total_results": 1,
                                                                              "items": [
                                                                                  {
                                                                                      "company_number": "06500244",
                                                                                      "company_type": "ltd",
                                                                                      "title": "BBC LIMITED",
                                                                                      "company_status": "active",
                                                                                      "date_of_creation": "2008-02-11",
                                                                                      "address": {
                                                                                          "premises": "Boswell Cottage Main Street",
                                                                                          "locality": "Retford",
                                                                                          "country": "England",
                                                                                          "postal_code": "DN22 0AD",
                                                                                          "address_line_1": "North Leverton"
                                                                                      },
                                                                                      "officers": [
                                                                                          {
                                                                                              "name": "BOXALL, Sarah Victoria",
                                                                                              "officer_role": "secretary",
                                                                                              "appointed_on": "2008-02-11",
                                                                                              "address": {
                                                                                                  "premises": "5",
                                                                                                  "locality": "London",
                                                                                                  "country": "England",
                                                                                                  "postal_code": "SW20 0DP",
                                                                                                  "address_line_1": "Cranford Close"
                                                                                              }
                                                                                          },
                                                                                          {
                                                                                              "name": "BRAY, Simon Anton",
                                                                                              "officer_role": "director",
                                                                                              "appointed_on": "2008-02-11",
                                                                                              "address": {
                                                                                                  "premises": "5",
                                                                                                  "locality": "London",
                                                                                                  "country": "England",
                                                                                                  "postal_code": "SW20 0DP",
                                                                                                  "address_line_1": "Cranford Close"
                                                                                              }
                                                                                          }
                                                                                      ]
                                                                                  }
                                                                              ]
                                                                          }
                                        """.formatted(06500244))));

        CompanySearchRequest companySearchRequest = new CompanySearchRequest();
        companySearchRequest.setCompanyName("BBC LIMITED");
        companySearchRequest.setCompanyNumber("06500244");

        this.mockMvc.perform(
                post("/company", companyNumber)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(companySearchRequest))
                        .header("x-api-key" , "API_KEY")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_results", is(1)))
                .andExpect(jsonPath("$.items[0].company_number", is("06500244")))
                .andExpect(jsonPath("$.items[0].title", is("BBC LIMITED")))
                .andExpect(jsonPath("$.items[0].officers[0].name", is("BOXALL, Sarah Victoria")))
                .andExpect(jsonPath("$.items[0].address.country", is("England")));
    }


    @Test
    @DisplayName("Get BadRequest Message when either CompanyName Or CompanyNumber are not provided")
    void shouldGetBadRequestMessageWhenEitherCompanyNameOrCompanyNumberAreNotProvided() throws Exception {
        String companyNumber = "06500244";
        wireMockServer.stubFor(WireMock.get(urlMatching("/Companies/v1/.*"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/vnd.company.app-v1+json")
                                .withBody("""
                                                                          {
                                                                              "total_results": 1,
                                                                              "items": [
                                                                                  {
                                                                                      "company_number": "06500244",
                                                                                      "company_type": "ltd",
                                                                                      "title": "BBC LIMITED",
                                                                                      "company_status": "active",
                                                                                      "date_of_creation": "2008-02-11",
                                                                                      "address": {
                                                                                          "premises": "Boswell Cottage Main Street",
                                                                                          "locality": "Retford",
                                                                                          "country": "England",
                                                                                          "postal_code": "DN22 0AD",
                                                                                          "address_line_1": "North Leverton"
                                                                                      },
                                                                                      "officers": [
                                                                                          {
                                                                                              "name": "BOXALL, Sarah Victoria",
                                                                                              "officer_role": "secretary",
                                                                                              "appointed_on": "2008-02-11",
                                                                                              "address": {
                                                                                                  "premises": "5",
                                                                                                  "locality": "London",
                                                                                                  "country": "England",
                                                                                                  "postal_code": "SW20 0DP",
                                                                                                  "address_line_1": "Cranford Close"
                                                                                              }
                                                                                          },
                                                                                          {
                                                                                              "name": "BRAY, Simon Anton",
                                                                                              "officer_role": "director",
                                                                                              "appointed_on": "2008-02-11",
                                                                                              "address": {
                                                                                                  "premises": "5",
                                                                                                  "locality": "London",
                                                                                                  "country": "England",
                                                                                                  "postal_code": "SW20 0DP",
                                                                                                  "address_line_1": "Cranford Close"
                                                                                              }
                                                                                          }
                                                                                      ]
                                                                                  }
                                                                              ]
                                                                          }
                                        """.formatted(06500244))));

        CompanySearchRequest companySearchRequest = new CompanySearchRequest();

        this.mockMvc.perform(
                        post("/company", companyNumber)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(companySearchRequest))
                                .header("x-api-key" , "API_KEY")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Either Company Name or Number must be provided.")));
    }

    @Test
    @DisplayName("Get Internal Server Error message when wrong url is formed for remote api call.")
    void shouldGetInternalServerErrorMessageWhenWrongUrlIsFormed() throws Exception {
        String companyNumber = "0650024411";
        wireMockServer.stubFor(WireMock.get(urlMatching("/Search*"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/vnd.company.app-v1+json")
                                .withBody("""
                                                                          {
                                                                               "page_number": 1,
                                                                               "kind": "search#companies"
                                                                           }
                                        """.formatted(0650024411))));

        CompanySearchRequest companySearchRequest = new CompanySearchRequest();
        companySearchRequest.setCompanyName("BBC LIMITED");
        companySearchRequest.setCompanyNumber("0650024411");

        this.mockMvc.perform(
                        post("/company", companyNumber)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(companySearchRequest))
                                .header("x-api-key" , "API_KEY")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Internal server exception occurred. Please try after sometime.")));

    }

}
