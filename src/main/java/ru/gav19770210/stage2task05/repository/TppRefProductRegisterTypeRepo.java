package ru.gav19770210.stage2task05.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.stage2task05.entity.TppRefProductRegisterTypeEntity;

import java.util.List;

@Repository
public interface TppRefProductRegisterTypeRepo extends CrudRepository<TppRefProductRegisterTypeEntity, Integer> {
    List<TppRefProductRegisterTypeEntity> findAllByProductClassCodeAndAccountType(String productClassCode,
                                                                                  String accountType);

    TppRefProductRegisterTypeEntity getByValue(String typeCode);
}
