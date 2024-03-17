package ru.gav19770210.stage2task05.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CreateAccountResponse {
    private ResponseData data = new ResponseData();
    private String errorText;

    @ToString
    @Getter
    @Setter
    public static class ResponseData {
        private String accountId;
    }
}
