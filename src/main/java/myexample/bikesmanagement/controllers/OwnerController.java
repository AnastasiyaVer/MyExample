package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Owner;
import myexample.bikesmanagement.repository.OwnerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("owner")
public class OwnerController {
    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @GetMapping
    public List<Owner> bikesList(){
        return ownerRepository.findAll();
    }

    @GetMapping("{id}")
    public Owner getOneOwner(@PathVariable("id")  Owner owner){
        return owner;
    }

    @PostMapping
    public Owner createOwner(@RequestBody Owner owner){
        return ownerRepository.save(owner);
    }

    @PutMapping("{id}")
    public Owner updateOwner(@PathVariable("id") Owner ownerFromDb, @RequestBody Owner owner){
        BeanUtils.copyProperties(owner,ownerFromDb,"id");
        return ownerRepository.save(ownerFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteOwner(@PathVariable("id") Owner owner){
        ownerRepository.delete(owner);
    }

}
