package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.*;
import myexample.bikesmanagement.repository.DetailsRepository;
import myexample.bikesmanagement.repository.PurchaseRepository;
import myexample.bikesmanagement.repository.RepairsRepository;
import myexample.bikesmanagement.repository.ServiceStationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("service")
public class ServiceStationController {
    private final ServiceStationRepository serviceStationRepository;
    private final RepairsRepository repairsRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public ServiceStationController(ServiceStationRepository serviceStationRepository,
                                    RepairsRepository repairsRepository,PurchaseRepository purchaseRepository) {
        this.serviceStationRepository = serviceStationRepository;
        this.repairsRepository = repairsRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @GetMapping
    public List<ServiceStation> serviceStationList(){
        return serviceStationRepository.findAll();
    }

    @GetMapping("{id}")
    public ServiceStation getOneServiceStation(@PathVariable("id")  ServiceStation serviceStation){
        return serviceStation;
    }

    @PostMapping
    public ServiceStation createServiceStation(@RequestBody ServiceStation serviceStation){
        return serviceStationRepository.save(serviceStation);
    }

    @PutMapping("{id}")
    public ServiceStation updateServiceStation(@PathVariable("id") ServiceStation serviceStationFromDb,
                                               @RequestBody ServiceStation serviceStation){
        BeanUtils.copyProperties(serviceStation,serviceStationFromDb,"id");
        return serviceStationRepository.save(serviceStationFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteServiceStation(@PathVariable("id") ServiceStation serviceStation){
        serviceStationRepository.delete(serviceStation);
    }

    @GetMapping("{id}/purchase")//вовращает информацию о всех закупках заданного сервиса
    public List<Purchase> listPurchase(@PathVariable("id")ServiceStation serviceStation){
        return purchaseRepository.findAllByServiceStation(serviceStation);
    }

    @GetMapping("{id}/allclients")//возвращает информацию о всех клиентах заданного сервиса
    public List<Owner> ownerList(@PathVariable("id") ServiceStation serviceStation){
        List<Repair> repairs;
        repairs = repairsRepository.findAllByServiceStation(serviceStation);
        List<Owner> owners = new ArrayList<>();
        for (Repair repair:repairs) {
            owners.add(repair.getBike().getOwner());
        }
        return owners;
    }

    @GetMapping("{id}/purchaseMonth")//вовращает информацию о закупках заданного сервиса за последние 30 дней
    public List<Purchase> listPurchaseForMonth(@PathVariable("id")ServiceStation serviceStation){
        List<Purchase> purchases = purchaseRepository.findAllByLocalDateTimeBetween(LocalDateTime.now()
                .minusDays(30),LocalDateTime.now());
        List<Purchase> purchaseForMonth = new ArrayList<>();
        for (Purchase purchase:purchases) {
            if(purchaseRepository.findAllByServiceStation(serviceStation).contains(purchase)){
                purchaseForMonth.add(purchase);
            }
        }
        return purchaseForMonth;
    }

    @GetMapping("{id}/clientsMonth")//возвращает информацию о всех клиентах заданного сервиса за последние 30дней
    public List<Owner> ownerListForMonth(@PathVariable("id") ServiceStation serviceStation){
        List<Repair> repairs = repairsRepository.findAllByLocalDateTimeBetween(LocalDateTime.now()
                .minusDays(30),LocalDateTime.now());
        List<Owner> owners = new ArrayList<>();
        for (Repair repair:repairs) {
            if(repairsRepository.findAllByServiceStation(serviceStation).contains(repair))
            owners.add(repair.getBike().getOwner());
        }
        return owners;
    }

    @GetMapping("{id}/incomeMonth")//считает всю чистую прибыль заданного сервиса за последние 30 дней
    public Double incomeForMonth(@PathVariable("id") ServiceStation serviceStation){
        Double incRepair = 0.0;
        List<Repair> repairs = repairsRepository.findAllByLocalDateTimeBetween(LocalDateTime.now()
                .minusDays(30),LocalDateTime.now());
        for (Repair repair:repairs) {
            if(repairsRepository.findAllByServiceStation(serviceStation).contains(repair)) {
                incRepair = incRepair + repair.getCost();
            }
        }
        Double incPurchase = 0.0;
        List<Purchase> purchases = purchaseRepository.findAllByLocalDateTimeBetween(LocalDateTime.now()
                .minusDays(30),LocalDateTime.now());
        for (Purchase purchase:purchases) {
            if(purchaseRepository.findAllByServiceStation(serviceStation).contains(purchase)){
                incPurchase = incPurchase + purchase.getSum();
            }
        }
        return incRepair - incPurchase;
    }

}
