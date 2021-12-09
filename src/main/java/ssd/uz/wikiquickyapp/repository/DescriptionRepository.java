package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.uz.wikiquickyapp.collection.UsersCol;
import ssd.uz.wikiquickyapp.entity.Description;

import java.util.List;
import java.util.Optional;

public interface DescriptionRepository extends JpaRepository<Description, Long> {
    Optional<Description> findByAnswerAndUserId(boolean answer, Long userId);

    @Query(value = "select d.id from description d where d.user_id =:userId", nativeQuery = true)
    List<Long> getDescriptionIds(@Param(value = "userId") Long userId);
}
