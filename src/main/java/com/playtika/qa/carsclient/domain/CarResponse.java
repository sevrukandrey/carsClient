package com.playtika.qa.carsclient.domain;

import lombok.Value;

@Value
public class CarResponse {
    private String brand;
    private String model;
    private String plateNumber;
    private String color;
    private int year;
}
