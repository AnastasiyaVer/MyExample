package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.Detail;
import myexample.bikesmanagement.entity.ServiceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailsRepository extends JpaRepository<Detail,Long> {

    List<Detail> findAllByServiceStation(ServiceStation serviceStation);//возвращает список всех деталей заданного сервиса

    @Query("select sum (u.cost) from Detail u where u.serviceStation = :serviceStation ")//считает сумму стоимости всех деталей
    Double sumCostDetails(@Param("serviceStation") ServiceStation serviceStation);//заданного сериса

}
