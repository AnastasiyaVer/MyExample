package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsRepository extends JpaRepository<Detail,Long> {

}
