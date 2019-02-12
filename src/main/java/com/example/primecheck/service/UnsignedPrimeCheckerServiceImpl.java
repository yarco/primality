package com.example.primecheck.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service("prototype")
@AllArgsConstructor
public class UnsignedPrimeCheckerServiceImpl implements UnsignedPrimeCheckerService {

    private PrimeChecker primeChecker;

    @Override
    public boolean isUnsignedPrime(BigInteger num) {
        if (num.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException();
        } else {
            return primeChecker.isPrime(num);
        }
    }
}
