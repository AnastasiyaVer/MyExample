package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.Repair;
import myexample.bikesmanagement.entity.ServiceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairsRepository extends JpaRepository<Repair,Long> {
    @Query("select sum (u.cost) from Repair u where u.serviceStation = :serviceStation ")//считает сумму стоимости всех ремонтов
    Double sumCostRepair(@Param("serviceStation") ServiceStation serviceStation);//заданного сервиса

    List<Repair> findAllByServiceStation(ServiceStation serviceStation);//возвращает список всех ремонтов заданного сервиса

     List<Repair> findAllByLocalDateTimeBetween(LocalDateTime start,LocalDateTime end);//возвращает список всех ремонтов за последние 30 дней

}
