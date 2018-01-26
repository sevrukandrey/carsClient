package com.playtika.qa.carsclient;

import com.playtika.qa.carsclient.configuration.CarShopClientConfiguration;
import com.playtika.qa.carsclient.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.playtika.qa.carsclient.domain.DealStatus.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = CarShopClientConfiguration.class)
@AutoConfigureWireMock
public class CarShopClientTest {

    @Autowired
    CarShopClient carShopClient;

    private CarRequest car;
    private Client client;
    private double price = 100;
    private String ownerContacts = "093";
    private long carId = 1L;
    private long dealId = 2L;
    private long advertId = 3L;
    private String carInJson = "{\"brand\": \"Ford\"," +
        "\"model\":\"fiesta\"," +
        "\"plateNumber\":\"10-10\"," +
        "\"color\":\"green\"," +
        "\"year\":2010}";

    @Before
    public void init() {
        car = new CarRequest("Ford", "fiesta", "10-10", "green", 2010);
        client = new Client("Andrey", "Sevruk", ownerContacts);
    }


    @Test
    public void shouldAddCar() {
        stubFor(post(urlEqualTo(format("/cars?price=%s&ownerContacts=%s", price, ownerContacts)))
            .withRequestBody(equalToJson(carInJson))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(String.valueOf(carId))));

        long carIdResponse = carShopClient.addCar(car, price, ownerContacts);

        assertThat(carIdResponse).isEqualTo(Long.valueOf(carId));
    }

    @Test
    public void shouldGetAllCars() {
        List<CarSaleInfoResponse> expectedCarSaleInfo =
            Collections.singletonList(new CarSaleInfoResponse(1L, car, new SaleInfo("093", 500.0)));

        stubFor(get(urlEqualTo("/cars"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody("[{\"id\":1,\"car\":{\"brand\":\"Ford\",\"model\":\"fiesta\",\"plateNumber\":\"10-10\",\"color\":\"green\",\"year\":2010}," +
                    "\"saleInfo\":{\"ownerContacts\":\"093\",\"price\":500.0}}]")));

        List<CarSaleInfoResponse> allCars = carShopClient.getAllCars();

        assertThat(allCars).isEqualTo(expectedCarSaleInfo);
    }

    @Test
    public void deleteCar() {
        stubFor(delete(urlEqualTo(format("/cars/%s", carId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)));

        carShopClient.deleteCar(carId);
    }

    @Test
    public void shouldRejectDeal() {
        stubFor(post(urlEqualTo(format("/rejectDeal?dealId=%s", dealId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)));

        assertThatCode(() -> carShopClient.rejectDeal(dealId)).doesNotThrowAnyException();
    }

    @Test
    public void shouldPutCatToSale() {
        CarOnSaleRequest carOnSaleRequest = new CarOnSaleRequest(car, client, price);

        stubFor(put(urlEqualTo("/car"))
            .withRequestBody(equalToJson("{\"car\":{\"brand\":\"Ford\",\"model\":\"fiesta\",\"plateNumber\":\"10-10\",\"color\":\"green\",\"year\":2010}," +
                "\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"},\"price\":100.0}"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(String.valueOf(advertId))));

        assertThat(carShopClient.putCarToSale(carOnSaleRequest)).isEqualTo(advertId);
    }

    @Test
    public void shouldChooseBestDeal() {
        stubFor(get(urlEqualTo(format("/bestDeal?advertId=%s", advertId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(String.valueOf(dealId))));

        assertThat(carShopClient.chooseBestDeal(advertId)).isEqualTo(dealId);
    }

    @Test
    public void shouldCreateDeal() {
        DealRequest dealRequest = new DealRequest(client, price);

        stubFor(post(urlEqualTo(format("/deal?advertId=%s", advertId)))
            .withRequestBody(equalToJson("{\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"},\"price\":100.0}"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(String.valueOf(dealId))));

        assertThat(carShopClient.createDeal(advertId, dealRequest)).isEqualTo(dealId);
    }

    @Test
    public void getAdvertIdByCarId() {
        stubFor(get(urlEqualTo(format("/advertByCarId?carId=%s", carId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(String.valueOf(advertId))));

        assertThat(carShopClient.getAdvertIdByCarId(carId)).isEqualTo(advertId);
    }

    @Test
    public void getCarByAdvertId() {
        stubFor(get(urlEqualTo(format("/carByAdvertId?advertId=%s", advertId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(carInJson)));

        assertThat(carShopClient.getCarByAdvertId(advertId)).isEqualToComparingFieldByField(car);
    }

    @Test
    public void dealById() {
        stubFor(get(urlEqualTo(format("/dealById?dealId=%s", dealId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody("{\"id\": \"1\"," +
                    "\"price\":\"200\"," +
                    "\"status\":\"ACTIVE\"}")));

        DealResponse dealResponse = new DealResponse(1L, null,200.0,null, ACTIVE );

        assertThat(carShopClient.dealById(dealId)).isEqualToIgnoringNullFields(dealResponse);
    }
}