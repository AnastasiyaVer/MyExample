package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Repair;
import myexample.bikesmanagement.exceptions.RestIsZeroException;
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
    private final ServiceStationController serviceStationController;
    private final PurchaseController purchaseController;

    @Autowired
    public RepairsController(RepairsRepository repairsRepository, BikesController bikesController,
                             ServiceStationController serviceStationController,PurchaseController purchaseController) {
        this.repairsRepository = repairsRepository;
        this.bikesController = bikesController;
        this.serviceStationController = serviceStationController;
        this.purchaseController = purchaseController;
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
    public Repair createService(@RequestBody Repair repair) throws RestIsZeroException { //создание нового ремонта
        repair.setLocalDateTime(LocalDateTime.now());
        repair.getPurchase().setServiceStation(repair.getServiceStation());
        if (repair.getBike() != null) { // проверяем, есть ли в запросе информация по велосипеду, если есть, то создаем новую запись
            bikesController.createBike(repair.getBike()); // по велосипеду, если мы введем существующий id велосипеда, то запись будет отредактирована
        }
        if (repair.getServiceStation() != null) {
            serviceStationController.createServiceStation(repair.getServiceStation());

        }
        if (repair.getPurchase()!= null) {
            purchaseController.createPurchase(repair.getPurchase());
        }
        repair.setCost(0.2 * repair.getBike().getCost()//считаем стоимость ремонта
                + repair.getPurchase().getCost());//берем 20 процентов стоимости велосипеда и прибавляем стоимость детали
        purchaseController.lessRest(repair.getPurchase());//уменьшаем остаток деталей на 1
        purchaseController.checkRest(repair.getPurchase());//проверяем остаток деталей
        return repairsRepository.save(repair);
        }

    @PutMapping("{id}")
    public Repair updateService(@PathVariable("id") Repair repairFromDb,// редактирование записи о ремонте
                                @RequestBody Repair repair) throws RestIsZeroException {
        repair.setLocalDateTime(LocalDateTime.now());
        repair.getPurchase().setServiceStation(repair.getServiceStation());
        if (repair.getBike() != null) { // проверяем, есть ли в запросе информация по велосипеду, если есть, то создаем новую запись
            bikesController.createBike(repair.getBike()); // по велосипеду, если мы введем существующий id велосипеда, то запись будет отредактирована
        }
        if (repair.getServiceStation() != null) {
            serviceStationController.createServiceStation(repair.getServiceStation());

        }
        if (repair.getPurchase()!= null) {
            purchaseController.createPurchase(repair.getPurchase());
        }
        repair.setCost(0.2 * repair.getBike().getCost()//считаем стоимость ремонта
                + repair.getPurchase().getCost());//берем 20 процентов стоимости велосипеда и прибавляем стоимость детали
        purchaseController.lessRest(repair.getPurchase());
        purchaseController.checkRest(repair.getPurchase());
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
