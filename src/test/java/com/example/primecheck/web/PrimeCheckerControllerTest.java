package com.example.primecheck.web;

import com.example.primecheck.TestValuesBootstrapper;
import com.example.primecheck.service.UnsignedPrimeCheckerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigInteger;
import java.util.HashMap;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PrimeCheckerController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrimeCheckerControllerTest extends TestValuesBootstrapper {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    protected UnsignedPrimeCheckerService unsignedPrimeCheckerService;

    @Before
    public void init(){
        HashMap<String, BigInteger> map = testValues.get(0);
        BigInteger nonprime = map.get("nonprime");
        given(unsignedPrimeCheckerService.isUnsignedPrime(nonprime))
                .willReturn(false);
        BigInteger prime = map.get("prime");
        given(unsignedPrimeCheckerService.isUnsignedPrime(prime))
                .willReturn(true);
    }

    @Test
    public void checkNonPrimes_mvcMethodReturnsFalseForAll() throws Exception {
        String input = testValues.get(0).get("nonprime").toString();
        ResultActions actions = getResultActions(input);
        actions.
                andExpect(status().isOk()).
                andExpect(
                        jsonPath("$.status").
                                value("false")).
                andReturn();
    }

    @Test
    public void whenInvalidApiArgument_thenServiceUnavailable() throws Exception {
        mockMvc.perform(get("/api/checkprime"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("service unavailable"))
                .andReturn();
    }

    @Test
    public void whenEmptyApiArgument_thenBadRequestWithErrorMessageInJson() throws Exception {
        mockMvc.perform(get("/api/checkprime").param("input", "''"))
                .andExpect(status().isBadRequest()).
                andExpect(
                        jsonPath("$.status").
                                value("input must be a number")).
                andReturn();
    }


    @Test
    public void whenMissingApiArgument_thenServiceUnavailable() throws Exception {
        mockMvc.perform(get("/api/checkprime").param("input", ""))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("service unavailable"))
                .andReturn();
    }

    @Test
    public void whenValidPrimes_mvcMethodTrueForAll() throws Exception {
        String input = testValues.get(0).get("prime").toString();
        ResultActions actions = getResultActions(input);
        actions.
                andExpect(status().isOk()).
                andExpect(
                        jsonPath("$.status").
                                value("true")).
                andReturn();
    }

    @Test
    public void whenNegativeInt_mvcMethodReturnsError() throws Exception {
        String input = "-1";
        ResultActions actions = getResultActions(input);
                actions.
                        andExpect(status().isBadRequest()).
                        andExpect(
                                jsonPath("$.status").
                                        value("number must be positive")).
                        andReturn();
    }

    @Test
    public void whenAlphaNumericInput_mvcMethodReturnsError() throws Exception {
        String input = "a5";
        ResultActions actions = getResultActions(input);
        actions.
                andExpect(status().isBadRequest()).
                andExpect(
                        jsonPath("$.status").value("input must be a number")).
                andReturn();
    }

    private ResultActions getResultActions(String input) throws Exception {

        ResultActions actions = mockMvc.perform(get("/api/checkprime")
                .param("input", input))
                //.andDo(print()) // add this to make output verbose, it needed
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.input").exists())
                .andExpect(jsonPath("$.status").exists());

        return actions;
    }
}
