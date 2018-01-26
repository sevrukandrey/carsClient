package com.playtika.qa.carsclient.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSaleInfo {
    private CarRequest car;
    private String ownerContacts;
    private double price;


}
