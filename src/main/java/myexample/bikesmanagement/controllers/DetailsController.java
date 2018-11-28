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
    private final ServiceStationController serviceStationController;

    @Autowired
    public DetailsController(DetailsRepository detailsRepository,ServiceStationController serviceStationController) {
        this.detailsRepository = detailsRepository;
        this.serviceStationController = serviceStationController;
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
        if(detail.getServiceStation()!=null){
            serviceStationController.createServiceStation(detail.getServiceStation());
        }
        return detailsRepository.save(detail);
    }

    @PutMapping("{id}")
    public Detail updateDetail(@PathVariable("id") Detail detailFromDb, @RequestBody Detail detail){
        if(detail.getServiceStation()!=null){
            serviceStationController.createServiceStation(detail.getServiceStation());
        }
        BeanUtils.copyProperties(detail,detailFromDb,"id");
        return detailsRepository.save(detailFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteDetail(@PathVariable("id") Detail detail){
        detailsRepository.delete(detail);
    }

}
