package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Detail;
import myexample.bikesmanagement.entity.Owner;
import myexample.bikesmanagement.entity.Repair;
import myexample.bikesmanagement.entity.ServiceStation;
import myexample.bikesmanagement.repository.DetailsRepository;
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
    private final DetailsRepository detailsRepository;
    private final RepairsRepository repairsRepository;

    @Autowired
    public ServiceStationController(ServiceStationRepository serviceStationRepository,DetailsRepository detailsRepository,
                                    RepairsRepository repairsRepository) {
        this.serviceStationRepository = serviceStationRepository;
        this.detailsRepository = detailsRepository;
        this.repairsRepository = repairsRepository;
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

    @GetMapping("{id}/details")//вовращает информацию о закупленных всех деталях заданного сервиса
    public List<Detail> listDetail(@PathVariable("id")ServiceStation serviceStation){
        return detailsRepository.findAllByServiceStation(serviceStation);
    }

    @GetMapping("{id}/income")//считает всю чистую прибыль заданного сервиса
    public Double income(@PathVariable("id") ServiceStation serviceStation){
        Double inc;
        return inc = repairsRepository.sumCostRepair(serviceStation)-detailsRepository.sumCostDetails(serviceStation);
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

    @GetMapping("{id}/detailsMonth")//вовращает информацию о закупленных деталях заданного сервиса за последние 30 дней
    public List<Detail> listDetailForMonth(@PathVariable("id")ServiceStation serviceStation){
        List<Repair> repairs = repairsRepository.findAllByLocalDateTimeBetween(LocalDateTime.now()
                .minusDays(30),LocalDateTime.now());
        List<Detail> details = new ArrayList<>();
        for (Repair repair:repairs) {
            if(detailsRepository.findAllByServiceStation(serviceStation).contains(repair.getDetail())) {
                details.add(repair.getDetail());
            }
        }
        return details;
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
        Double inc = 0.0;
        List<Repair> repairs = repairsRepository.findAllByLocalDateTimeBetween(LocalDateTime.now()
                .minusDays(30),LocalDateTime.now());
        for (Repair repair:repairs) {
            if(repairsRepository.findAllByServiceStation(serviceStation).contains(repair)&&
                    detailsRepository.findAllByServiceStation(serviceStation).contains(repair.getDetail())) {
                inc = repair.getCost()-repair.getDetail().getCost();
            }
        }
        return inc;
    }

}
