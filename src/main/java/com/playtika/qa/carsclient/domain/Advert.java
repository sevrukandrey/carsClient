package com.playtika.qa.carsclient.domain;

import lombok.Value;

@Value
public class Advert {
    private Long id;
    private CarResponse car;
    private Client client;
    private Long dealId;
    private double price;
    private AdvertStatus status;
}
