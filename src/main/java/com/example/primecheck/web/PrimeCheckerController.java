package com.example.primecheck.web;

import com.example.primecheck.message.PrimalityResult;
import com.example.primecheck.service.UnsignedPrimeCheckerService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigInteger;

@Validated
@Controller
@RequestMapping("/api")
public class PrimeCheckerController {

    protected UnsignedPrimeCheckerService unsignedPrimeCheckerService;

    public PrimeCheckerController(UnsignedPrimeCheckerService unsignedPrimeCheckerService) {
        this.unsignedPrimeCheckerService = unsignedPrimeCheckerService;
    }

    @GetMapping(value = "/checkprime")
    @ResponseBody
    public PrimalityResult checkUnsignedPrime(
            @Valid()
            @Min(value = 0L, message = "number must be positive")
            @RequestParam("input") BigInteger number) {

        boolean isPrime = unsignedPrimeCheckerService.isUnsignedPrime(number);
        return new PrimalityResult(number.toString(), String.valueOf(isPrime));
    }


    @ExceptionHandler(value = TypeMismatchException.class)
    public ResponseEntity<PrimalityResult> handleNotANumberFailure(TypeMismatchException e, WebRequest request) {
        PrimalityResult result = new PrimalityResult(
                e.getValue().toString(), "input must be a number"
        );
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<PrimalityResult> handleValidationFailure(ConstraintViolationException e,
                                                                   WebRequest request) {
        // show one validation error to user
        ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        PrimalityResult result = new PrimalityResult(
                violation.getInvalidValue().toString(),
                violation.getMessage()
        );
        return ResponseEntity.badRequest().body(result);
    }
}