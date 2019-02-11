package com.example.primecheck.service;

import com.example.primecheck.TestValuesBootstrapper;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


@RunWith(MockitoJUnitRunner.class) // gives the capacity to use mocks, if needed
public class PrimeCheckerImplTest extends TestValuesBootstrapper {

    private PrimeCheckerImpl primeChecker;
    private ExecutorService executor;

    private List<BigInteger> lukasMersennePrimesList;
    private List<BigInteger> nonLukasMersennePrimesList;

    @Before
    public void setUp() throws IOException, JSONException {
        super.setUp();
        executor = Executors.newWorkStealingPool();
        primeChecker = new PrimeCheckerImpl();

        lukasMersennePrimesList = Arrays.asList("131071", "524287", "2147483647").
                stream().
                map(BigInteger::new).
                collect(Collectors.toList());
        nonLukasMersennePrimesList = Arrays.asList("11", "23", "29").
                stream().
                map(BigInteger::new).
                collect(Collectors.toList());
    }

    @After
    public void cleanup() {
        executor.shutdownNow();
    }

    @Test
    public void whenNegativePrimeInput_thenTrue() {
        assertThat(primeChecker.isPrime(new BigInteger("-3"))).isTrue();
        assertThat(primeChecker.isPrime(new BigInteger("-5"))).isTrue();
    }

    @Test
    public void whenNegativeNonPrimeInput_thenFalse() {
        assertThat(primeChecker.isPrime(new BigInteger("-6"))).isFalse();
        assertThat(primeChecker.isPrime(new BigInteger("-8"))).isFalse();
    }


    @Test
    public void checkLucasLehmerValidNumbers_thenTrue() {
        lukasMersennePrimesList.
                forEach((prime) ->
                        assertThat(primeChecker.testLucasLehmer(prime)).isTrue()
                );
    }

    @Test
    public void checkLucasLehmerInvalidNumbers_thenFalse() {
        nonLukasMersennePrimesList.
                forEach((nonPrime) ->
                        assertThat(primeChecker.testLucasLehmer(nonPrime)).isFalse()
                );
    }

    @Test
    public void checkMersenneTestValidNumbers_thenTrue() {
        lukasMersennePrimesList.
                forEach((prime) ->
                        assertThat(primeChecker.isMersenne(prime)).isTrue()
                );
    }

    @Test
    public void checkMersenneTestInvalidNumbers_thenFalse() {
        nonLukasMersennePrimesList.
                forEach((nonPrime) ->
                        assertThat(primeChecker.isMersenne(nonPrime)).isFalse()
                );
    }

    @Test
    public void generateProbablePrimesAndNonPrimes_compareResultToExpectedResult() {
        Random random = new Random();
        BigInteger two = new BigInteger("2");

        HashSet<Future> futures = new HashSet<>();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Future future = executor.submit(() -> {
                for (int j = 0; j < 400; j++) {
                    BigInteger probablePrime = BigInteger.probablePrime(500, random);
                    assertThat(primeChecker.isPrime(probablePrime)).isTrue();
                    BigInteger nonprime = probablePrime.multiply(two);
                    assertThat(primeChecker.isPrime(nonprime)).isFalse();
                }
            });
            futures.add(future);
        }


        futures.forEach(f -> {
            try {
                f.get(); // ~ futures.join()
            } catch (InterruptedException | ExecutionException e) {
                fail("Future.get() failed");
            }
        });
    }

    @Test()
    public void checkJsonInputNumbers_compareResultToExpectedResult() {

        HashSet<Future> futures = new HashSet<>();
        for (int i = 0; i < testValues.size(); i++) {
            HashMap<String, BigInteger> map = testValues.get(i);


            BigInteger length = map.get("length");
            BigInteger prime = map.get("prime");
            BigInteger nonprime = map.get("nonprime");

            Future f = executor.submit(() -> {
                PrimeChecker primeChecker = new PrimeCheckerImpl();
                assertThat(primeChecker.isPrime(prime)).isTrue();
                assertThat(primeChecker.isPrime(nonprime)).isFalse();
            });
            futures.add(f);
        }
        futures.forEach(f -> {
            try {
                f.get(); // ~ futures.join()
            } catch (InterruptedException | ExecutionException e) {
                fail("Future.get() failed");
            }
        });
    }

}
