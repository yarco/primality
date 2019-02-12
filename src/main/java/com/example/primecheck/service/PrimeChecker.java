package com.example.primecheck.service;

import java.math.BigInteger;

@FunctionalInterface
public interface PrimeChecker {
    boolean isPrime(BigInteger number);
}


