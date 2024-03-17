package ru.gav19770210.stage2task05.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Setter
public class CreateProductResponse {
    @Getter
    private ResponseData data = new ResponseData();
    @Getter
    private String errorText;

    @ToString
    @Setter
    public static class ResponseData {
        @Getter
        private Long instanceId;
        @Getter
        private List<Long> registerId = new ArrayList<>();
        @Getter
        private List<Long> supplementaryAgreementId = new ArrayList<>();
    }
}
