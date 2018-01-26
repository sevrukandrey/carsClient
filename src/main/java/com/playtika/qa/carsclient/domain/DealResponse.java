package com.playtika.qa.carsclient.domain;

import lombok.Value;

@Value
public class DealResponse {
    private Long id;
    private Client client;
    private double price;
    private Advert advert;
    private DealStatus status;
}
