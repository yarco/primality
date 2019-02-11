package com.example.primecheck.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service("prototype")
public class UnsignedPrimeCheckerServiceImpl implements UnsignedPrimeCheckerService {

    private PrimeChecker primeChecker;

    public UnsignedPrimeCheckerServiceImpl(PrimeChecker primeChecker) {
        this.primeChecker = primeChecker;
    }

    @Override
    public boolean isUnsignedPrime(BigInteger num) {
        if (num.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException();
        } else {
            return primeChecker.isPrime(num);
        }
    }
}
