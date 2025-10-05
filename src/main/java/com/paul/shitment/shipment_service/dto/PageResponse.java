package com.paul.shitment.shipment_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int pageNo;
    private int size;
    private Long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
