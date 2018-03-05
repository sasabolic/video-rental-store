package com.example.videorentalstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractWebIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    WebApplicationContext context;

    protected MockMvc mockMvc;

    @Before
    public void init() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/").locale(Locale.US))
                .build();
    }

    public String createURIWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    public String json(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());

        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();

        ObjectMapper mapper = new ObjectMapper();
        Object jsonObject = mapper.readValue(data.toString(), Object.class);
        String json = mapper.writeValueAsString(jsonObject);
        System.out.println(json);

        return json;
    }
}
