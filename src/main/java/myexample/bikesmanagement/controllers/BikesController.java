package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Bike;
import myexample.bikesmanagement.repository.BikesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bikes")
public class BikesController {
   private final BikesRepository bikesRepository;

   @Autowired
   public BikesController(BikesRepository bikesRepository) {
        this.bikesRepository = bikesRepository;
    }

    @GetMapping
    public List<Bike> list(){
        return bikesRepository.findAll();
    }

    @GetMapping("{id}")
    public Bike getOne(@PathVariable("id") Bike bike){
        return bike;
    }

    @PostMapping
    public Bike create(@RequestBody Bike bike){
        return bikesRepository.save(bike);
    }

    @PutMapping("{id}")
    public Bike update(@PathVariable("id") Bike bikeFromDb, @RequestBody Bike bike){
        BeanUtils.copyProperties(bike,bikeFromDb,"id");
        return bikesRepository.save(bikeFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Bike bike){
        bikesRepository.delete(bike);
    }

}
