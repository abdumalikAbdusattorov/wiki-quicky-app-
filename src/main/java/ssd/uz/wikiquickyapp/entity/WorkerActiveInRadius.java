package ssd.uz.wikiquickyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ssd.uz.wikiquickyapp.entity.template.AbsNameEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class WorkerActiveInRadius extends AbsNameEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private WorkerActive workerActiveId;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderActive orderActive;
    private Double distance;
    private boolean isUsed;

    public WorkerActiveInRadius(WorkerActive workerActiveId, OrderActive orderActive, Double distance) {
        this.workerActiveId = workerActiveId;
        this.orderActive = orderActive;
        this.distance = distance;
    }

    public WorkerActiveInRadius(WorkerActive workerActiveId, OrderActive orderActive, Double distance, boolean isUsed) {
        this.workerActiveId = workerActiveId;
        this.orderActive = orderActive;
        this.distance = distance;
        this.isUsed = isUsed;
    }
}
