package ru.gav19770210.stage2task05.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateProductResponse {
    private ResponseData data = new ResponseData();
    private String errorText;

    @Getter
    @Setter
    @ToString
    public static class ResponseData {
        private Long instanceId;
        private List<Long> registerId = new ArrayList<>();
        private List<Long> supplementaryAgreementId = new ArrayList<>();
    }
}
