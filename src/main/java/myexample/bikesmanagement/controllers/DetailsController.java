package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Detail;
import myexample.bikesmanagement.repository.DetailsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("details")
public class DetailsController{
    private final DetailsRepository detailsRepository;

    @Autowired
    public DetailsController(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    @GetMapping
    public List<Detail> detailsList(){
        return detailsRepository.findAll();
    }

    @GetMapping("{id}")
    public Detail getOneDetail(@PathVariable("id") Detail detail){
        return detail;
    }

    @PostMapping
    public Detail createDetail(@RequestBody Detail detail){
        return detailsRepository.save(detail);
    }

    @PutMapping("{id}")
    public Detail updateDetail(@PathVariable("id") Detail detailFromDb, @RequestBody Detail detail){
        BeanUtils.copyProperties(detail,detailFromDb,"id");
        return detailsRepository.save(detailFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteDetail(@PathVariable("id") Detail detail){
        detailsRepository.delete(detail);
    }

}
