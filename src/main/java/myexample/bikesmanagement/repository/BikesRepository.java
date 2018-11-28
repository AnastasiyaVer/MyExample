package myexample.bikesmanagement.repository;

import myexample.bikesmanagement.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikesRepository extends JpaRepository<Bike,Long> {
    Bike findByName(String name); //метод поиска велосипеда по имени
}
