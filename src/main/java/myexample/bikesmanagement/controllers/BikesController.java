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
   private final OwnerController ownerController;

   @Autowired
   public BikesController(BikesRepository bikesRepository, OwnerController ownerController) {
        this.bikesRepository = bikesRepository;
        this.ownerController = ownerController;
    }

    @GetMapping
    public List<Bike> bikesList(){
        return bikesRepository.findAll();
    }

    @GetMapping("{id}")
    public Bike getOneBike(@PathVariable("id") Bike bike){
        return bike;
    }

    @PostMapping
    public Bike createBike(@RequestBody Bike bike){
        if(bike.getOwner()!= null){
            ownerController.createOwner(bike.getOwner());
        }
       return bikesRepository.save(bike);
    }

    @PutMapping("{id}")
    public Bike updateBike(@PathVariable("id") Bike bikeFromDb, @RequestBody Bike bike){
        if(bike.getOwner()!= null){
            ownerController.createOwner(bike.getOwner());
        }
        BeanUtils.copyProperties(bike,bikeFromDb,"id");
        return bikesRepository.save(bikeFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteBike(@PathVariable("id") Bike bike){
        bikesRepository.delete(bike);
    }

    @GetMapping("{name}")
    public Bike findByNameBike(@PathVariable("name") String name){
       return bikesRepository.findByName(name);
    }

}
