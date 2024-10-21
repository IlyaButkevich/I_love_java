package com.example.demo20;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    @Query("SELECT p FROM Goods p WHERE CONCAT(p.goodname, '', p.contents, '', p.dateofshipment, '', p.shipmentcity, '', p.dateofarrival, '', p.arrivalcity) LIKE %?1%")
    List<Goods> searchGoods(String keyword);
}
