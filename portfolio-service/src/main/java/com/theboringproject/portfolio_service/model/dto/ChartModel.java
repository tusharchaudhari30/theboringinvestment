package com.theboringproject.portfolio_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartModel {
    private String name;
    private Double value;
}