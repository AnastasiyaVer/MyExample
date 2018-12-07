package myexample.bikesmanagement.controllers;

import myexample.bikesmanagement.entity.Purchase;
import myexample.bikesmanagement.exceptions.NotFoundException;
import myexample.bikesmanagement.exceptions.RestIsZeroException;
import myexample.bikesmanagement.repository.PurchaseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public Purchase createPurchase(@RequestBody Purchase purchase) throws RestIsZeroException {// создаем новую запись о закупке
        purchase.setLocalDateTime(LocalDateTime.now());
        if(purchase.getDetail()!= null){//проверяем наличие информации о детали
            detailsController.createDetail(purchase.getDetail());
        }
        if(purchase.getId()==null){ //проверяем наличие ввода id
            purchase.setRest(purchase.getNumber());//устанавливаем остаток деталей, равный количеству закупленных
        }else {
            if(purchaseRepository.findRest(purchase.getId())==null) throw new NotFoundException();
            purchase.setRest(purchaseRepository.findRest(purchase.getId()));//устанавливаем остаток деталей, равный остатку в найденной записи
        }
        if(purchase.getRest()==0) throw new RestIsZeroException("Rest = 0. Please select other id");
        purchase.setCost(purchase.getDetail().getCost());//устанавливаем стоимость детали в закупке, равную стоимости детали в справочнике Детали
        purchase.setSum();//считаем общую сумму закупки
        return purchaseRepository.save(purchase);
    }

    @PutMapping("{id}")//редактирование информации по заданной закупке
    public Purchase updatePurchase(@PathVariable("id") Purchase purchaseFromDb, @RequestBody Purchase purchase)
            throws RestIsZeroException {
        purchase.setLocalDateTime(LocalDateTime.now());
        if(purchase.getDetail()!= null){//проверяем наличие информации о детали
            detailsController.createDetail(purchase.getDetail());
        }
        if(purchase.getId()==null){
            purchase.setRest(purchase.getNumber());

        }else {
            purchase.setRest(purchaseRepository.findRest(purchase.getId()));
        }
        if(purchase.getRest()==0) throw new RestIsZeroException("Rest = 0. Please select other id");
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

   public void lessRest(@RequestBody Purchase purchase){//уменьшает остаток деталей на 1
        purchase.setRest(purchase.getRest()-1);
        purchaseRepository.save(purchase);
    }

    public void checkRest(@RequestBody Purchase purchase){//проверяет остаток деталей, и если осталось меньше 1, то создает новую закупку 5 деталей
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
