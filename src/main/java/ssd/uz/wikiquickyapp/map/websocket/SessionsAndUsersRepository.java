package ssd.uz.wikiquickyapp.map.websocket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionsAndUsersRepository extends JpaRepository<PrincipleAndUsers, Long> {

    int deleteSessionsAndUsersByPrinciple(String principle);

    PrincipleAndUsers findSessionsAndUsersByUserId(Long userId);

    @Query(value = "select * from principle_and_users where principle=:name",nativeQuery = true)
    PrincipleAndUsers selectByPrinciple(@Param("name") String name);
}
