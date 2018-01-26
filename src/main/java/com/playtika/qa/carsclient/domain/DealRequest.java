package com.playtika.qa.carsclient.domain;

import lombok.Value;

@Value
public class DealRequest {
    private Client client;
    private double price;
}
