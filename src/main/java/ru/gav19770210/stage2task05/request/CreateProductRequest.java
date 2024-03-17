package ru.gav19770210.stage2task05.request;

import com.networknt.schema.JsonSchema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.gav19770210.stage2task05.json.JsonSchemaValidator;

import java.util.Date;

@Getter
@Setter
public class CreateProductRequest {
    @Getter
    private static final JsonSchema jsonSchema = JsonSchemaValidator.getJsonSchema(CreateProductRequest.class);

    private Long instanceId;
    private String productType;
    private String productCode;
    private String registerType;
    private String mdmCode;
    private String contractNumber;
    private Date contractDate;
    private Integer priority;
    private Double interestRatePenalty;
    private Double minimalBalance;
    private Double thresholdAmount;
    private String accountingDetails;
    private String rateType;
    private Double taxPercentageRate;
    private Double technicalOverdraftLimitAmount;
    private Long contractId;
    private String isoCurrencyCode;
    private String branchCode;
    private AdditionalPropertiesVip additionalPropertiesVip;
    private Agreement[] instanceAgreement;

    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    public static class AdditionalPropertiesVip {
        private Data[] data;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Data {
        private String key;
        private String value;
        private String name;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Agreement {
        private String generalAgreementId;
        private String supplementaryAgreementId;
        private String arrangementType;
        private Long schedulerJobId;
        private String number;
        private Date openingDate;
        private Date closingDate;
        private Date cancelDate;
        private Long validityDuration;
        private String cancellationReason;
        private String status;
        private Date interestCalculationDate;
        private Double interestRate;
        private Double coefficient;
        private String coefficientAction;
        private Double minimumInterestRate;
        private Double minimumInterestRateCoefficient;
        private String minimumInterestRateCoefficientAction;
        private Double maximalInterestRate;
        private Double maximalInterestRateCoefficient;
        private String maximalInterestRateCoefficientAction;
    }
}
