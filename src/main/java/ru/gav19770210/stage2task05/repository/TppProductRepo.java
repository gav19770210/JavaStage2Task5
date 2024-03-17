package ru.gav19770210.stage2task05.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.stage2task05.entity.TppProductEntity;

@Repository
public interface TppProductRepo extends CrudRepository<TppProductEntity, Long> {
    TppProductEntity getByNumber(String number);
}
