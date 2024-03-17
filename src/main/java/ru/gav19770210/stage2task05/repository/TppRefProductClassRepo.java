package ru.gav19770210.stage2task05.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.stage2task05.entity.TppRefProductClassEntity;

@Repository
public interface TppRefProductClassRepo extends CrudRepository<TppRefProductClassEntity, Integer> {
    TppRefProductClassEntity getByValue(String productCode);
}
