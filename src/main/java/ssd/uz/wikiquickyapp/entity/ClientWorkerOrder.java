package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ssd.uz.wikiquickyapp.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ClientWorkerOrder extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private WorkerActive worker;
    @OneToOne
    private Users client;
    @OneToOne
    private Order order;
    private Boolean isUsed;

    public ClientWorkerOrder(WorkerActive worker, Users client, Order order, Boolean isUsed) {
        this.worker = worker;
        this.client = client;
        this.order = order;
        this.isUsed = isUsed;
    }

    public ClientWorkerOrder(WorkerActive worker, Users client, Order order) {
        this.worker = worker;
        this.client = client;
        this.order = order;
    }
}
