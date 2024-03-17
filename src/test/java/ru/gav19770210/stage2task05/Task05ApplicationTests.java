package ru.gav19770210.stage2task05;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.gav19770210.stage2task05.json.JsonSchemaValidator;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class Task05ApplicationTests {
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");
    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void beforeEach(@Autowired Flyway flyway) {
        RestAssured.baseURI = "http://localhost:" + port;

        flyway.clean();
        flyway.migrate();
    }

    @Test
    @Disabled
    @DisplayName("Интеграционный тест сервисов создания ЭП, ПР и доп.соглашения")
    public void csiServiceTest() throws IOException {
        System.out.println("Интеграционный тест сервиса создания ЭП, ПР и ДС");

        var pointCounter = 1;
        System.out.println("--> Создание ЭП.");

        System.out.println("п." + (pointCounter++) + ". Получение тестового запроса на добавление ЭП.");
        var jsonCreateProduct = JsonSchemaValidator.fileToString("/json/createProductRequestNewInstance.json");

        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(jsonCreateProduct)
                .post("/corporate-settlement-instance/create")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("data.instanceId", Matchers.notNullValue())
                .body("data.registerId", Matchers.hasSize(1))
                .body("data.supplementaryAgreementId", Matchers.hasSize(0));

        System.out.println("--> Создание ПР к созданному ЭП. Повторная попытка создания ПР после создания ЭП.");

        System.out.println("п." + (pointCounter++) + ". Получение тестового запроса на добавление ПР.");
        var jsonCreateAccount = JsonSchemaValidator.fileToString("/json/createAccountRequestNewInstance.json");

        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(jsonCreateAccount)
                .post("/corporate-settlement-account/create")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(400) // HttpStatus.BAD_REQUEST
                .body("data.accountId", Matchers.nullValue())
                .body("errorText", Matchers.notNullValue());

        System.out.println("--> Создание ДС к созданному ЭП.");

        System.out.println("п." + (pointCounter++) + ". Получение тестового запроса на добавление ДС к созданному ЭП.");
        var jsonCreateAgreement = JsonSchemaValidator.fileToString("/json/createProductRequestNewAgreement.json");

        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(jsonCreateAgreement)
                .post("/corporate-settlement-instance/create")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("data.instanceId", Matchers.notNullValue())
                .body("data.registerId", Matchers.hasSize(0))
                .body("data.supplementaryAgreementId", Matchers.hasSize(1));
    }

    @Test
    @DisplayName("Проверка контроля валидации тестового запроса JSON на соответствие схеме создания ЭП")
    public void validateJsonTextCreateProduct() throws IOException {
        System.out.println("Получение тестового запроса JSON на соответствие схеме создания ЭП из ресурсов.");
        String jsonText = JsonSchemaValidator.fileToString("/json/createProductRequestMissedRequiredFields.json");

        System.out.println("Проверка тестового запроса JSON на соответствие схеме создания ЭП");
        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(jsonText)
                .post("/corporate-settlement-instance/create")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(400) // HttpStatus.BAD_REQUEST
                .body("data.instanceId", Matchers.nullValue())
                .body("errorText", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Проверка контроля валидации тестового запроса JSON на соответствие схеме создания ПР.")
    public void validateJsonTextCreateAccount() throws IOException {
        System.out.println("Получение тестового запроса JSON на соответствие схеме создания ПР из ресурсов.");
        String jsonText = JsonSchemaValidator.fileToString("/json/createAccountRequestMissedRequiredFields.json");

        System.out.println("Проверка тестового запроса JSON на соответствие схеме создания ПР.");
        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(jsonText)
                .post("/corporate-settlement-account/create")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(400) // HttpStatus.BAD_REQUEST
                .body("data.accountId", Matchers.nullValue())
                .body("errorText", Matchers.notNullValue());
    }
}
