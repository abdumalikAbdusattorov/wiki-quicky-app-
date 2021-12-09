package ssd.uz.wikiquickyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.uz.wikiquickyapp.entity.StoredOrder;

public interface StoredOrderRepository extends JpaRepository<StoredOrder, Long> {
//    List<SoC> findAllByClientAndDeleted(Users users,boolean deleted);


}
