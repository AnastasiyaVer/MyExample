package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.ServiceStation;
import myexample.bikesmanagement.repository.ServiceStationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("service")
public class ServiceStationController {
    private final ServiceStationRepository serviceStationRepository;
    private final BikesController bikesController;
    private final DetailsController detailsController;

    @Autowired
    public ServiceStationController(ServiceStationRepository serviceStationRepository, BikesController bikesController,
                                    DetailsController detailsController) {
        this.serviceStationRepository = serviceStationRepository;
        this.bikesController = bikesController;
        this.detailsController = detailsController;
    }


    @GetMapping
    public List<ServiceStation> serviceList(){
        return serviceStationRepository.findAll();
    }

    @GetMapping("{id}")
    public ServiceStation getOneService(@PathVariable("id") ServiceStation serviceStation){
        return serviceStation;
    }

    @PostMapping
    public ServiceStation createService(@RequestBody ServiceStation serviceStation){
        if(serviceStation.getBike()!= null){
                bikesController.createBike(serviceStation.getBike());
        }
        if(serviceStation.getDetail()!=null){
            detailsController.createDetail(serviceStation.getDetail());
        }
        serviceStation.setCost(serviceStation.getCost()/100*serviceStation.getBike().getCost()
                +serviceStation.getDetail().getCost());
        return serviceStationRepository.save(serviceStation);
    }

    @PutMapping("{id}")
    public ServiceStation updateService(@PathVariable("id") ServiceStation serviceStationFromDb,
                                 @RequestBody ServiceStation serviceStation){
        if(serviceStation.getBike()!= null){
            bikesController.createBike(serviceStation.getBike());
        }
        if(serviceStation.getDetail()!=null){
            detailsController.createDetail(serviceStation.getDetail());
        }
        serviceStation.setCost(serviceStation.getCost()/100*serviceStation.getBike().getCost()
                +serviceStation.getDetail().getCost());
        BeanUtils.copyProperties(serviceStation,serviceStationFromDb,"id");
        return serviceStationRepository.save(serviceStationFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteService(@PathVariable("id") ServiceStation serviceStation){
        serviceStationRepository.delete(serviceStation);
    }
}
