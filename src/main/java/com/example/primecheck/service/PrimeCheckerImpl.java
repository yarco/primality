package com.example.primecheck.service;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component()
public class PrimeCheckerImpl implements PrimeChecker {

    public boolean isPrime(BigInteger number) {
        BigInteger abs = number.abs();
        if (abs.equals(BigInteger.TWO)) { return true; }
        if (isMersenne(abs)) {
            return testLucasLehmer(abs);
        }
        return abs.testBit(0) && testMillerRabin(abs);
    }

    public boolean isMersenne(BigInteger number) {
        byte nBytes[] = number.toByteArray();
        byte b0 = nBytes[0];
        if (((b0 + 1) & b0 ) != 0) {
            return false;
        }
        for (int i = 1; i < nBytes.length; i++) {
            if (nBytes[i] != -1) {
                return false;
            }
        }
        return true;
    }

    public boolean testLucasLehmer(BigInteger number) {
        BigInteger four = BigInteger.valueOf(4);
        BigInteger two = BigInteger.valueOf(2);
        int bitLen = number.bitLength();
        if (bitLen <= 1) {
            return false;
        }
        if (bitLen <= 3) {
            return true;
        }
        BigInteger modPow = four;
        while (bitLen > 2) {
            modPow = modPow.modPow(two, number);
            modPow = modPow.subtract(two);
            bitLen--;
        }
        return modPow.signum() == 0;
    }

    public boolean testMillerRabin(BigInteger number) {
        BigInteger base=BigInteger.valueOf(2);
        BigInteger nMinusOne = number.subtract(BigInteger.ONE);
        BigInteger currMMinusOne = nMinusOne;
        int index = currMMinusOne.getLowestSetBit();
        currMMinusOne = currMMinusOne.shiftRight(index);
        int j = 0;
        BigInteger modPowNum = base.modPow(currMMinusOne, number);
        while (!modPowNum.equals(nMinusOne) && (j != 0 || !modPowNum.equals(BigInteger.ONE))) {
            if (++j == index) {
                return false;
            }
            modPowNum = modPowNum.modPow(base, number);
        }
        return true;
    }
}