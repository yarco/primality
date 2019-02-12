package com.example.primecheck.web;

import com.example.primecheck.message.PrimalityResult;
import com.example.primecheck.service.UnsignedPrimeCheckerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Validated
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class PrimeCheckerController {

    protected UnsignedPrimeCheckerService unsignedPrimeCheckerService;

    @GetMapping(value = "/checkprime")
    @ResponseBody
    public PrimalityResult checkUnsignedPrime(
            @Valid()
            @NotNull
            @Min(value = 0L, message = "number must be positive")
            @RequestParam("input") BigInteger number) {
        boolean isPrime = unsignedPrimeCheckerService.isUnsignedPrime(number);
        return new PrimalityResult(number.toString(), String.valueOf(isPrime));
    }


    @ExceptionHandler(value = TypeMismatchException.class)
    public ResponseEntity<PrimalityResult> handleNotANumberFailure(TypeMismatchException e) {
        PrimalityResult result = new PrimalityResult(
                e.getValue().toString(), "input must be a number"
        );
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<PrimalityResult> handleValidationFailure(ConstraintViolationException e) {
        // show one validation error to user
        ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        PrimalityResult result = new PrimalityResult(
                violation.getInvalidValue() == null
                        ? "null"
                        : violation.getInvalidValue().toString(),
                violation.getMessage()
        );
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleValidationFailure(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}