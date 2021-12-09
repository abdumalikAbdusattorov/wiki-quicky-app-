package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import ssd.uz.wikiquickyapp.entity.FeedBack;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    @Query(value = "select * from feed_back where worker=:workerId and from_who='worker'", nativeQuery = true)
    List<FeedBack> findFeedBack(@Param("workerId") Long workerId);


    @Query(value = "select * from feed_back where client=:clientId and from_who='client'", nativeQuery = true)
    List<FeedBack> findFeedBack1(@Param("clientId") Long clientId);

    @Query(value = "select AVG(rating) from feed_back f where f.worker=:workerId and from_who='client'", nativeQuery = true)
    Double point(@Param("workerId") Long workerId);

    @Query(value = "select AVG(rating) from feed_back f where f.client=:clientId and from_who='worker'", nativeQuery = true)
    Double pointr(@Param("clientId") Long clientId);


    @Query(value = "select AVG(rating) from feed_back f where from_who='worker'", nativeQuery = true)
    Long sumWorkerPoint();

    @Query(value = "select AVG(rating) from feed_back f where from_who='client'", nativeQuery = true)
    Long sumClientPoint();


}
