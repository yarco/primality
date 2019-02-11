package com.example.primecheck.service;

import com.example.primecheck.TestValuesBootstrapper;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigInteger;

@RunWith(MockitoJUnitRunner.class) // gives the capacity to use mocks, if needed
public class PrimeCheckerServiceImplTest extends TestValuesBootstrapper {

    private UnsignedPrimeCheckerService unsignedPrimeCheckerService;

    @Before
    public void setUp() throws IOException, JSONException {
        super.setUp();
        unsignedPrimeCheckerService = new UnsignedPrimeCheckerServiceImpl(new PrimeCheckerImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenNegativeInput_ThenException() {
        unsignedPrimeCheckerService.isUnsignedPrime(BigInteger.valueOf(-1));
    }
}
