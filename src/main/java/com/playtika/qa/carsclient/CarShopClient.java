package com.playtika.qa.carsclient;

import com.playtika.qa.carsclient.configuration.CarShopClientConfiguration;
import com.playtika.qa.carsclient.domain.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "CarShop",
    configuration = CarShopClientConfiguration.class)
public interface CarShopClient {

    @RequestMapping(method = POST, value = "/cars")
    long addCar(@RequestBody CarRequest car,
                @RequestParam("price") double price,
                @RequestParam("ownerContacts") String ownerPhone);

    @RequestMapping(method = GET, value = "/cars")
    List<CarSaleInfoResponse> getAllCars();

    @RequestMapping(method = DELETE, value = "/cars/{id}")
    void deleteCar(@PathVariable("id") long id);

    @RequestMapping(method = GET, value = "/cars/{id}")
    SaleInfo getCarDetails(@PathVariable("id") long id);

    @RequestMapping(method = POST, value = "/rejectDeal")
    void rejectDeal(@RequestParam("dealId") long id);

    @RequestMapping(method = PUT, value = "/car")
    long putCarToSale(@RequestBody CarOnSaleRequest carOnSaleRequest);

    @RequestMapping(method = GET, value = "/bestDeal")
    long chooseBestDeal(@RequestParam("advertId") long advertId);

    @RequestMapping(method = POST, value = "/deal")
    long createDeal(@RequestParam("advertId") long advertId,
                    @RequestBody DealRequest dealRequest);

    @RequestMapping(method = GET, value = "/advertByCarId")
    long getAdvertIdByCarId(@RequestParam("carId") long carId);

    @RequestMapping(method = GET, value = "/carByAdvertId")
    CarResponse getCarByAdvertId(@RequestParam("advertId") long advertId);

    @RequestMapping(method = GET, value = "/dealById")
    DealResponse dealById(@RequestParam("dealId") long dealId);

}

