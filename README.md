# A Primality Checker Service

## Methodology
To check if the number is prime, we use the [Lucas–Lehmer primality test](https://en.wikipedia.org/wiki/Lucas%E2%80%93Lehmer_primality_test) together with the [Miller–Rabin primality test](https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test). 


If the number is a [Mersenne prime](https://en.wikipedia.org/wiki/Mersenne_prime), we use the deterministic Lucas–Lehmer primality test. 
Otherwise, we fall back on using the probabilistic Miller–Rabin primality test.
 

## Quick start

Minimal requirements:
- Java 1.8+
- Maven 3.3+


To start the application, from the project directory run:
```
mvn spring-boot:run
```
This will bootstrap the webapp on `http://localhost:8080`.

A rudimentary web interface can be used to interact with the web service. 

Alternatively, users can interact with the API directly. Below are a few examples of how the API endpoints work.

- Check if `5` is prime:

```
curl --request GET "http://127.0.0.1:8080/api/checkprime?input=5"
# {"input":"5", "status":"true"}
 
```

- Check if `6` is prime:
```
curl --request GET "http://127.0.0.1:8080/api/checkprime?input=6"
# {"input":"6", "status":"false"}
```

- Check if `'wine'` is prime:
```
curl --request GET "http://127.0.0.1:8080/api/checkprime?input=wine"
# {"input":"wine","status":"input must be a number"}
```

## Testing

To test the web application, run:
```
mvn test
```

## Coverage

To generate a coverage report (provided by [jacoco](https://github.com/jacoco/jacoco)), run:
```
mvn test jacoco:report # creates target/site/jacoco/index.html
```

