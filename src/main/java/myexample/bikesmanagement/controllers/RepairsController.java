package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Repair;
import myexample.bikesmanagement.repository.RepairsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("repair")
public class RepairsController {
    private final RepairsRepository repairsRepository;
    private final BikesController bikesController;
    private final DetailsController detailsController;
    private final ServiceStationController serviceStationController;

    @Autowired
    public RepairsController(RepairsRepository repairsRepository, BikesController bikesController,
                             DetailsController detailsController,ServiceStationController serviceStationController) {
        this.repairsRepository = repairsRepository;
        this.bikesController = bikesController;
        this.detailsController = detailsController;
        this.serviceStationController = serviceStationController;
    }


    @GetMapping
    public List<Repair> serviceList(){  // возвращает список ремонтов(наименование и стоимость ремонта,
                                                // информация по велосипеду, в которую входит инф-ия по владельцу велосипеда,
        return repairsRepository.findAll();  //  информация по ремонтируемой детали)
    }

    @GetMapping("{id}")
    public Repair getOneService(@PathVariable("id") Repair repair){ //возвращает информацию по конкретному ремонту (по id)
        return repair;
    }

    @PostMapping
    public Repair createService(@RequestBody Repair repair){ //создание нового ремонта
        repair.setLocalDateTime(LocalDateTime.now());
        if(repair.getBike()!= null){ // проверяем, есть ли в запросе информация по велосипеду, если есть, то создаем новую запись
                bikesController.createBike(repair.getBike()); // по велосипеду, если мы введем существующий id велосипеда, то запись будет отредактирована
        }
        if(repair.getDetail()!=null){ // то же самое, что и выше только насчет записей о детали
            detailsController.createDetail(repair.getDetail());
        }
        if(repair.getServiceStation()!=null){
            serviceStationController.createServiceStation(repair.getServiceStation());
        }
        repair.setCost(0.2* repair.getBike().getCost()
                + repair.getDetail().getCost());//считаем стоимость ремонта
        return repairsRepository.save(repair);//берем 20 процентов стоимости велосипеда и прибавляем стоимость детали
    }

    @PutMapping("{id}")
    public Repair updateService(@PathVariable("id") Repair repairFromDb,// редактирование записи о ремонте
                                @RequestBody Repair repair){
        repair.setLocalDateTime(LocalDateTime.now());
        if(repair.getBike()!= null){
            bikesController.createBike(repair.getBike());
        }
        if(repair.getDetail()!=null){
            detailsController.createDetail(repair.getDetail());
        }
        if(repair.getServiceStation()!=null){
            serviceStationController.createServiceStation(repair.getServiceStation());
        }
        repair.setCost(0.2* repair.getBike().getCost()
                + repair.getDetail().getCost());
        BeanUtils.copyProperties(repair, repairFromDb,"id");
        return repairsRepository.save(repairFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteService(@PathVariable("id") Repair repair){// удаление записи
        repairsRepository.delete(repair);
    }

   @GetMapping("/month")//возвращает информацию о всех ремонтах за последние 30 дней
    public List<Repair> repairListForMonth(){
       return repairsRepository.findAllByLocalDateTimeBetween(LocalDateTime.now().minusDays(30),LocalDateTime.now());
   }

}
