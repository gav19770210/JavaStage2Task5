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
import ru.gav19770210.stage2task05.request.CreateAccountRequest;
import ru.gav19770210.stage2task05.response.CreateAccountResponse;
import ru.gav19770210.stage2task05.service.CreateAccountService;

import javax.persistence.NoResultException;
import java.io.PrintWriter;
import java.io.StringWriter;

@RestController
public class CreateAccountController {
    private CreateAccountService createAccountService;

    @Autowired
    public void setCreateAccountService(CreateAccountService createAccountService) {
        this.createAccountService = createAccountService;
    }

    @PostMapping("corporate-settlement-account/create")
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody String requestJsonText) {
        CreateAccountResponse createAccountResponse;
        HttpStatus httpStatus;
        try {
            var createAccountRequest = JsonSchemaValidator.validateAndParseJson(requestJsonText,
                    CreateAccountRequest.getJsonSchema(),
                    CreateAccountRequest.class);
            createAccountResponse = createAccountService.createAccount(createAccountRequest);
            httpStatus = HttpStatus.OK;
        } catch (JsonValidateException | JsonProcessingException | IllegalArgumentException e) {
            createAccountResponse = new CreateAccountResponse();
            createAccountResponse.setErrorText(e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (NoResultException e) {
            createAccountResponse = new CreateAccountResponse();
            createAccountResponse.setErrorText(e.getMessage());
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            createAccountResponse = new CreateAccountResponse();
            var stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            createAccountResponse.setErrorText(e.getMessage() + System.lineSeparator() + stringWriter);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(createAccountResponse);
    }
}
