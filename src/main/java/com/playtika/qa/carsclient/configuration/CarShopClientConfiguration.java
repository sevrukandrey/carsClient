package com.playtika.qa.carsclient.configuration;

import com.playtika.qa.carsclient.CarShopClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = CarShopClient.class)
@EnableAutoConfiguration
public class CarShopClientConfiguration {


}