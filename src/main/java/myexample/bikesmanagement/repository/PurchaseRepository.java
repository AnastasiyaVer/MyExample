package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.Detail;
import myexample.bikesmanagement.entity.Purchase;
import myexample.bikesmanagement.entity.ServiceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    @Query("select sum (u.sum) from Purchase u where u.serviceStation = :serviceStation ")//считает сумму всех закупок
    Double sumPurchase(@Param("serviceStation") ServiceStation serviceStation);//заданного сервиса

    List<Purchase> findAllByServiceStation(ServiceStation serviceStation);//возвращает список всех закупок заданного сервиса

    List<Purchase> findAllByLocalDateTimeBetween(LocalDateTime start, LocalDateTime end);//возвращает список всех закупок за заданный период времени

    Purchase findById(Purchase purchase);

}
