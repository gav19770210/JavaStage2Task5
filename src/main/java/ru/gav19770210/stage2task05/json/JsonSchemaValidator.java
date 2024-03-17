package ru.gav19770210.stage2task05.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonSchemaValidator {
    public static InputStream getStreamFromResource(String path) {
        return JsonSchemaValidator.class.getResourceAsStream(path);
    }

    public static <T> JsonSchema getJsonSchema(Class<T> requestClass) {
        var resourceFileName = "/templates/" + requestClass.getSimpleName() + ".json";
        var factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        return factory.getSchema(getStreamFromResource(resourceFileName));
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        var stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static String fileToString(String fileName) throws IOException {
        return convertInputStreamToString(getStreamFromResource(fileName));
    }

    public static <T> T validateAndParseJson(String jsonText, JsonSchema jsonSchema, Class<T> jsonClass)
            throws JsonValidateException, JsonProcessingException {
        var objectMapper = new ObjectMapper();

        var validationMessages = jsonSchema.validate(objectMapper.readTree(jsonText));

        if (validationMessages.isEmpty()) {
            return objectMapper.readValue(jsonText, jsonClass);
        } else {
            var stringBuilder = new StringBuilder();
            stringBuilder.append("Ошибки разбора json").append(System.lineSeparator());
            for (ValidationMessage validationMessage : validationMessages) {
                stringBuilder.append(validationMessage).append(System.lineSeparator());
            }
            throw new JsonValidateException(stringBuilder.toString());
        }
    }
}
