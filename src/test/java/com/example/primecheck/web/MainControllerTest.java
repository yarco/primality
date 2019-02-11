package com.example.primecheck.web;

import com.example.primecheck.TestValuesBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MainControllerTest extends TestValuesBootstrapper {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getMainPage_checkIfSameHtmlAsSource() throws Exception {
        File main = new File("src/main/resources/templates/index.html");


        String html = new String(Files.readAllBytes(main.toPath()));

        mockMvc.perform(get("/")
                .param("input", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(html))
                .andReturn();
    }


}
