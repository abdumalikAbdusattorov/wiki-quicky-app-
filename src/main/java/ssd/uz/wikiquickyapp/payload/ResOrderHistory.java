package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResOrderHistory {
    private Long allCount;
    private Long allPages;
    private Integer currentPage;
    private Double sumAllOrder;
    private List<ResOrder> list;
}
