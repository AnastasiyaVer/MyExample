package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.ServiceStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStationRepository extends JpaRepository<ServiceStation,Long> {

}
