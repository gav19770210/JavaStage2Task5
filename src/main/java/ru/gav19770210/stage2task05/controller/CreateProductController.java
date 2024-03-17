package ru.gav19770210.stage2task05.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gav19770210.stage2task05.json.JsonSchemaValidator;
import ru.gav19770210.stage2task05.json.JsonValidateException;
import ru.gav19770210.stage2task05.request.CreateProductRequest;
import ru.gav19770210.stage2task05.response.CreateProductResponse;
import ru.gav19770210.stage2task05.service.CreateProductService;

import javax.persistence.NoResultException;
import java.io.PrintWriter;
import java.io.StringWriter;

@RestController
public class CreateProductController {
    private CreateProductService createProductService;

    @Autowired
    public void setCreateProductService(CreateProductService createProductService) {
        this.createProductService = createProductService;
    }

    @PostMapping("corporate-settlement-instance/create")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody String requestJsonText) {
        CreateProductResponse createProductResponse;
        HttpStatus httpStatus;
        try {
            var createProductRequest = JsonSchemaValidator.validateAndParseJson(requestJsonText,
                    CreateProductRequest.getJsonSchema(),
                    CreateProductRequest.class);
            createProductResponse = createProductService.createProduct(createProductRequest);
            httpStatus = HttpStatus.OK;
        } catch (JsonValidateException | JsonProcessingException | IllegalArgumentException e) {
            createProductResponse = new CreateProductResponse();
            createProductResponse.setErrorText(e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (NoResultException e) {
            createProductResponse = new CreateProductResponse();
            createProductResponse.setErrorText(e.getMessage());
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            createProductResponse = new CreateProductResponse();
            var stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            createProductResponse.setErrorText(e.getMessage() + System.lineSeparator() + stringWriter);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(createProductResponse);
    }
}
