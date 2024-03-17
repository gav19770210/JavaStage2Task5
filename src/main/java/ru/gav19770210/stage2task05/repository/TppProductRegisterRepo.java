package ru.gav19770210.stage2task05.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.stage2task05.entity.TppProductEntity;
import ru.gav19770210.stage2task05.entity.TppProductRegisterEntity;

@Repository
public interface TppProductRegisterRepo extends CrudRepository<TppProductRegisterEntity, Long> {
    boolean existsByProductIdAndRegisterType(TppProductEntity productId, String registerType);

    TppProductRegisterEntity getByProductIdAndRegisterType(TppProductEntity productId, String registerType);
}
