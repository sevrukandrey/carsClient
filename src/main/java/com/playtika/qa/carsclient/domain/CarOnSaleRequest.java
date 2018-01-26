package com.playtika.qa.carsclient.domain;

import lombok.Value;

@Value
public class CarOnSaleRequest {
    private CarRequest car;
    private Client client;
    private double price;
}
