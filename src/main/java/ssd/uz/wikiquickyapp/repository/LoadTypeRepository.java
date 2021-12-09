package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.LoadType;

public interface LoadTypeRepository extends JpaRepository<LoadType, Long> {
    String findByName(String name);

    boolean existsAllByName(String name);

    LoadType findLoadTypeByName(String name);
}
