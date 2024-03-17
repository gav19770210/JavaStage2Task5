package ru.gav19770210.stage2task05.request;

import com.networknt.schema.JsonSchema;
import lombok.Getter;
import lombok.Setter;
import ru.gav19770210.stage2task05.json.JsonSchemaValidator;

@Getter
@Setter
public class CreateAccountRequest {
    @Getter
    private static final JsonSchema jsonSchema = JsonSchemaValidator.getJsonSchema(CreateAccountRequest.class);

    private Long instanceId;
    private String registryTypeCode;
    private String accountType;
    private String currencyCode;
    private String branchCode;
    private String priorityCode;
    private String mdmCode;
    private String clientCode;
    private String trainRegion;
    private String counter;
    private String salesCode;
}
