package ru.gav19770210.stage2task05.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gav19770210.stage2task05.entity.AccountEntity;
import ru.gav19770210.stage2task05.repository.AccountPoolRepo;
import ru.gav19770210.stage2task05.repository.AccountRepo;

import javax.persistence.NoResultException;

@Service
public class GetAccountNumberServiceImp implements GetAccountNumberService {
    private AccountPoolRepo accountPoolRepo;
    private AccountRepo accountRepo;

    @Autowired
    public void setAccountRepo(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Autowired
    public void setAccountPoolRepo(AccountPoolRepo accountPoolRepo) {
        this.accountPoolRepo = accountPoolRepo;
    }

    @Override
    @Transactional
    public AccountEntity getAccountNumber(String branchCode, String currencyCode, String mdmCode,
                                          String priorityCode, String registerTypeCode) {
        var accountPoolEntity = accountPoolRepo.getByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                branchCode,
                currencyCode,
                mdmCode,
                priorityCode,
                registerTypeCode
        );

        if (accountPoolEntity == null) {
            throw new NoResultException("Не найден пул счетов с параметрами branchCode=<" + branchCode
                    + ">, currencyCode=<" + currencyCode
                    + ">, mdmCode=<" + mdmCode
                    + ">, priorityCode=<" + priorityCode
                    + ">, registerTypeCode=<" + registerTypeCode + ">.");
        }

        var accountEntity = accountRepo.getFirstByAccountPoolIdAndBusy(accountPoolEntity.getId(), false);
        if (accountEntity == null) {
            throw new NoResultException("В пуле счетов закончились счета.");
        }
        accountEntity.setBusy(true);

        accountRepo.save(accountEntity);

        return accountEntity;
    }
}
