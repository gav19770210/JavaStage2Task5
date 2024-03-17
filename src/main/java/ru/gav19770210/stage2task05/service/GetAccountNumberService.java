package ru.gav19770210.stage2task05.service;

import ru.gav19770210.stage2task05.entity.AccountEntity;

public interface GetAccountNumberService {
    AccountEntity getAccountNumber(String branchCode, String currencyCode, String mdmCode,
                                   String priorityCode, String registerTypeCode);
}
