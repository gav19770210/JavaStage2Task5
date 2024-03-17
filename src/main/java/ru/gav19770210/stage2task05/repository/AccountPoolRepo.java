package ru.gav19770210.stage2task05.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.stage2task05.entity.AccountPoolEntity;

@Repository
public interface AccountPoolRepo extends CrudRepository<AccountPoolEntity, Long> {
    AccountPoolEntity getByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
            String branchCode,
            String currencyCode,
            String mdmCode,
            String priorityCode,
            String registerTypeCode);
}
