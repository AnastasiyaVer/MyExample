package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.Detail;
import myexample.bikesmanagement.entity.Repair;
import myexample.bikesmanagement.entity.ServiceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DetailsRepository extends JpaRepository<Detail,Long> {

}
