package myexample.bikesmanagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@ToString(of = {"id","name","bike","detail","cost","serviceStation"})
@EqualsAndHashCode(of = {"id"})
public class Repair {       // класс, из которго можно узнать всю информацию
    @Id                             // по ремонту велосипедов
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bike_id")
    private Bike bike;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "detail_id")
    private Detail detail;

    private Double cost= 0.0;

    @Column(name = "date_repair")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime localDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private ServiceStation serviceStation;


    public Repair() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost){
        this.cost = cost;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public ServiceStation getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(ServiceStation serviceStation) {
        this.serviceStation = serviceStation;
    }
}
