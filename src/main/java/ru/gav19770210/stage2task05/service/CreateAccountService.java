package ru.gav19770210.stage2task05.service;

import ru.gav19770210.stage2task05.request.CreateAccountRequest;
import ru.gav19770210.stage2task05.response.CreateAccountResponse;

public interface CreateAccountService {
    CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);
}
