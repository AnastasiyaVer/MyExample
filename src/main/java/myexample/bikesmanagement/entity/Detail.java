package myexample.bikesmanagement.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString(of = {"id","name","cost","serviceStation"})
@EqualsAndHashCode(of = {"id"})
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Double cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private ServiceStation serviceStation;

    public Detail() {
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public ServiceStation getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(ServiceStation serviceStation) {
        this.serviceStation = serviceStation;
    }
}
