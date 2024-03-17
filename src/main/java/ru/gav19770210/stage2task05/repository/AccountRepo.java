package ru.gav19770210.stage2task05.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.stage2task05.entity.AccountEntity;

@Repository
public interface AccountRepo extends CrudRepository<AccountEntity, Long> {
    AccountEntity getFirstByAccountPoolIdAndBusy(Long accountPoolId, boolean busy);
}
