package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Detail;
import myexample.bikesmanagement.entity.Purchase;
import myexample.bikesmanagement.repository.PurchaseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("purchase")
public class PurchaseController {
    private final PurchaseRepository purchaseRepository;
    private final DetailsController detailsController;

    @Autowired
    public PurchaseController(PurchaseRepository purchaseRepository,DetailsController detailsController) {
        this.purchaseRepository = purchaseRepository;
        this.detailsController = detailsController;
    }

    @GetMapping
    public List<Purchase> purchaseList(){
        return purchaseRepository.findAll();
    }//возвращает список всех закупок

    @GetMapping("{id}")//возвращает информацию по заданной закупке
    public Purchase getOnePurchase(@PathVariable("id") Purchase purchase){
        return purchase;
    }

    @PostMapping
    public Purchase createPurchase(@RequestBody Purchase purchase){// создаем новую запись о закупке
        purchase.setLocalDateTime(LocalDateTime.now());
        if(purchase.getDetail()!= null){//проверяем наличие информации о детали
            detailsController.createDetail(purchase.getDetail());
        }
        if(purchase.getId()==null){
            purchase.setRest(purchase.getNumber());
        }else {
            purchase.setRest(getOnePurchase(purchase).getRest());//????? НЕ РАБОТАЕТ
        }
        purchase.setCost(purchase.getDetail().getCost());
        purchase.setSum();
        return purchaseRepository.save(purchase);
    }

    @PutMapping("{id}")//редактирование информации по заданной закупке
    public Purchase updatePurchase(@PathVariable("id") Purchase purchaseFromDb, @RequestBody Purchase purchase){
        purchase.setLocalDateTime(LocalDateTime.now());
        if(purchase.getDetail()!= null){
           detailsController.createDetail(purchase.getDetail());
        }
        purchase.setRest(getOnePurchase(purchase).getRest());
        purchase.setCost(purchase.getDetail().getCost());
        purchase.setSum();
        BeanUtils.copyProperties(purchase,purchaseFromDb,"id");
        return purchaseRepository.save(purchaseFromDb);
    }

    @DeleteMapping("{id}")
    public void deletePurchase(@PathVariable("id") Purchase purchase){
        purchaseRepository.delete(purchase);
    }

    @GetMapping("/month")//возвращает информацию о всех закупках за последние 30 дней
    public List<Purchase> repairListForMonth(){
        return purchaseRepository.findAllByLocalDateTimeBetween(LocalDateTime.now().minusDays(30),LocalDateTime.now());
    }

   public void lessRest(@RequestBody Purchase purchase){
        purchase.setRest(purchase.getRest()-1);
        purchaseRepository.save(purchase);
    }

    public void checkRest(@RequestBody Purchase purchase){
        if(purchase.getRest()<1){
            Purchase purchaseNew = new Purchase();
            BeanUtils.copyProperties(purchase,purchaseNew,"id");
            purchaseNew.setNumber(5);
            purchaseNew.setSum();
            purchaseRepository.save(purchaseNew);
            purchaseNew.setRest(purchaseNew.getNumber());
            purchaseRepository.save(purchaseNew);
        }
    }

}
