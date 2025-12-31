package org.omer.connectfour;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Swagger UI and OpenAPI documentation.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SwaggerUITest {

    private static final String SWAGGER_UI_ENDPOINT = "/swagger-ui/index.html";
    private static final String SWAGGER_UI_CONTENT_STRING = "swagger-ui";
    private static final String API_DOCS_ENDPOINT = "/v3/api-docs";
    private static final String JSON_PATH_OPENAPI = "$.openapi";
    private static final String JSON_PATH_INFO = "$.info";
    private static final String JSON_PATH_INFO_TITLE = "$.info.title";
    private static final String SWAGGER_UI_SHORTCUT_URL = "/swagger-ui.html";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Swagger UI page should be accessible and return HTML")
    void swaggerUiShouldBeAccessible() throws Exception {
        mockMvc.perform(get(SWAGGER_UI_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(SWAGGER_UI_CONTENT_STRING)));
    }

    @Test
    @DisplayName("OpenAPI docs endpoint should return valid JSON schema")
    void openApiDocsShouldBeAccessible() throws Exception {
        mockMvc.perform(get(API_DOCS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_OPENAPI).exists())
                .andExpect(jsonPath(JSON_PATH_INFO).exists())
                .andExpect(jsonPath(JSON_PATH_INFO_TITLE).exists());
    }

    @Test
    @DisplayName("Swagger UI shortcut URL should redirect to full UI")
    void swaggerUiRedirectShouldWork() throws Exception {
        mockMvc.perform(get(SWAGGER_UI_SHORTCUT_URL))
                .andExpect(status().is3xxRedirection());
    }
}
