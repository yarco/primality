package com.example.primecheck;

import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class TestValuesBootstrapper {

    protected ArrayList<Map<String, BigInteger>> testValues;
    protected final int SMALL_TEST_SIZE = 5;
    protected final int MEDIUM_TEST_SIZE = 15;

    @Before
    public void setUp() throws IOException, JSONException {
        testValues = new ArrayList<>();
        String json = Files.lines(Paths.get(new File("src/test/resources/testValues.json").toURI()))
                .parallel()
                .collect(Collectors.joining());
        assertThat(json).isNotEmpty();

        AtomicReference<JSONArray> arrayRef = new AtomicReference<>();

        Assertions.assertThatCode(()->arrayRef.set(new JSONArray(json))).doesNotThrowAnyException();

        JSONArray jsonArray = arrayRef.get();

        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, BigInteger> map = new HashMap<>();

            assertThat(jsonArray.getJSONObject(i).has("length")).isTrue();
            String length = jsonArray.getJSONObject(i).getString("length");
            map.put("length", new BigInteger(length));

            assertThat(jsonArray.getJSONObject(i).has("prime")).isTrue();
            String prime = jsonArray.getJSONObject(i).getString("prime");
            map.put("prime", new BigInteger(prime));

            assertThat(jsonArray.getJSONObject(i).has("nonprime")).isTrue();
            String nonprime = jsonArray.getJSONObject(i).getString("nonprime");
            map.put("nonprime", new BigInteger(nonprime));
            testValues.add(map);
        }
    }
}
