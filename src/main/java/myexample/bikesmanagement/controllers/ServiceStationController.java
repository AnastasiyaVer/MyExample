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
    public List<ServiceStation> serviceList(){  // возвращает список ремонтов(наименование и стоимость ремонта,
                                                // информация по велосипеду, в которую входит инф-ия по владельцу велосипеда,
        return serviceStationRepository.findAll();  //  информация по ремонтируемой детали)
    }

    @GetMapping("{id}")
    public ServiceStation getOneService(@PathVariable("id") ServiceStation serviceStation){ //возвращает информацию по конкретному ремонту (по id)
        return serviceStation;
    }

    @PostMapping
    public ServiceStation createService(@RequestBody ServiceStation serviceStation){ //создание нового ремонта
        if(serviceStation.getBike()!= null){ // проверяем, есть ли в запросе информация по велосипеду, если есть, то создаем новую запись
                bikesController.createBike(serviceStation.getBike()); // по велосипеду, если мы введем существующий id велосипеда, то запись будет отредактирована
        }
        if(serviceStation.getDetail()!=null){ // то же самое, что и выше только насчет записей о детали
            detailsController.createDetail(serviceStation.getDetail());
        }
        serviceStation.setCost(serviceStation.getCost()/100*serviceStation.getBike().getCost()// считаем стоимость ремонта
                +serviceStation.getDetail().getCost());//берем процент стоимости велосипеда и прибавляем стоимость детали
        return serviceStationRepository.save(serviceStation);
    }

    @PutMapping("{id}")
    public ServiceStation updateService(@PathVariable("id") ServiceStation serviceStationFromDb,// редактирование записи о ремонте
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
    public void deleteService(@PathVariable("id") ServiceStation serviceStation){// удаление записи
        serviceStationRepository.delete(serviceStation);
    }
}
