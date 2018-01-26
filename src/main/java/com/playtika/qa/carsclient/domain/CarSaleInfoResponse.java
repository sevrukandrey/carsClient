package com.playtika.qa.carsclient.domain;

import lombok.Value;

@Value
public class CarSaleInfoResponse {
    private long id;
    private CarRequest car;
    private SaleInfo saleInfo;
}
