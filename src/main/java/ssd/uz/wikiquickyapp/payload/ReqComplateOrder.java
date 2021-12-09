package ssd.uz.wikiquickyapp.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ssd.uz.wikiquickyapp.entity.enums.OrderStatus;
import ssd.uz.wikiquickyapp.entity.enums.WorkerLocation;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ReqComplateOrder {
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private WorkerLocation workerLocation;
    private Long waitingTime;
    private String endTime;
    private Boolean paid;
}
