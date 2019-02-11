package com.example.primecheck;

import com.example.primecheck.message.PrimalityResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrimeCheckApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrimeCheckerIntegrationTest extends TestValuesBootstrapper {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void checkNonPrimes_apiReturnsFalseForAll() {
        for (int i = 0; i < SMALL_TEST_SIZE; i++) {
            BigInteger number = testValues.get(i).get("prime");
            ResponseEntity<PrimalityResult> response = getResponseEntity(number.toString());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getStatus()).isEqualTo("true");
        }
    }

    @Test
    public void checkPrimes_apiReturnsTrueForAll() {
        for (int i = 0; i < SMALL_TEST_SIZE; i++) {
            BigInteger number = testValues.get(i).get("nonprime");
            ResponseEntity<PrimalityResult> response = getResponseEntity(number.toString());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getStatus()).isEqualTo("false");
        }
    }

    @Test
    public void checkNegativeInt_apiReturnsError() {
        String input = "-1";
        ResponseEntity<PrimalityResult> response = getResponseEntity(input);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isNotEqualTo("false").isNotEqualTo("true");
    }

    @Test
    public void checkAlphaNumericInput_apiReturnsError() {
        String input = "a5";
        ResponseEntity<PrimalityResult> response = getResponseEntity(input);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isNotEqualTo("false").isNotEqualTo("true");
    }

    private ResponseEntity<PrimalityResult> getResponseEntity(String input) {
        ResponseEntity<PrimalityResult> response = restTemplate.
                getForEntity(String.format("/api/checkprime?input=%s", input), PrimalityResult.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInput()).isNotNull();
        assertThat(response.getBody().getStatus()).isNotNull();
        assertThat(response.getBody().getInput()).isEqualTo(input);
        return response;
    }
}