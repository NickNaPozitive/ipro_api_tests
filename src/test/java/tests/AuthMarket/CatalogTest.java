package tests.AuthMarket;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String urlForCatalog = "md6J4_hh4kG6YXLcEw99d";
    String idGoods = "2415539";

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.MINOR)
    @Description("Это тест проверяет добавление в избранное на аккаунте market")
    @DisplayName("Проверка добавления в избранное из каталога")
    public void testAddingToFavoriteFromCatalog() {

        //LOGIN

        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");
        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        //CHECK CNT
        Response responseGetCntFavorites = apiCoreRequests.
                makeGetRequestWithNoBody("https://idev.etm.ru/_next/data/" + urlForCatalog + "/catalog.json?my_fav=1");
        String cnt = getSingleHeaderFromJson(responseGetCntFavorites, "pageProps", ".data.records");
        System.out.println(cnt);


        //ADDING
        Response responseAddProduct = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/goods/" + idGoods + "/add_to_fav?session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseAddProduct, 200);
        Assertions.assertNotJsonByName(responseAddProduct, "data", ".cnt", cnt);

        //DELETING
        Response responseDelProduct = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/goods/" + idGoods + "/del_from_fav?session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseDelProduct, 200);
        Assertions.assertJsonByName(responseDelProduct, "data", ".cnt", cnt);
    }

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.MINOR)
    @Description("Это тест проверяет отрабатываение функционала замены для определенного товара")
    @DisplayName("Проверка функционала замены")
    public void testZamenaCatalog() {

        //LOGIN
        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");
        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        String city = getSingleHeaderFromJson(responseGetAuth, "data", ".city");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        //CHECK ZAMENA
        Response responseGetListOfZamena = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/catalog?page=1&rows=20&cls=101010&zamena=" + idGoods +
                        "&hide_no_post=1&city=" + city + "&session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseGetListOfZamena, 200);

    }

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.MINOR)
    @Description("Это тест проверяет отрабатываение функционала Доступности")
    @DisplayName("Проверка функционала Доступности")
    public void testRemainsCatalog() {

        //LOGIN
        String idGoods = "34031304";

        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");
        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        String city = getSingleHeaderFromJson(responseGetAuth, "data", ".city");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        //TAKE DATA FROM CATALOG
        Response responseGetDataRem = apiCoreRequests.
                makeGetRequestWithNoBody("https://idev.etm.ru/_next/data/" + urlForCatalog + "/catalog/101010_kabeli_s_mednoy_tokoprovodjaschey_zhiloy.json?nameCat=101010_kabeli_s_mednoy_tokoprovodjaschey_zhiloy");

        List rems = getSingleValueFromJson(responseGetDataRem, "pageProps", ".data.rows");
        String rem = "";
        for (int i = 0; i < rems.toArray().length - 1; i++) {
            String data = getSingleHeaderFromJson(responseGetDataRem, "pageProps", ".data.rows" + "[" + i + "]" + ".id");

            if (data.equals(idGoods + "-0-0")) {
                rem = getSingleHeaderFromJson(responseGetDataRem, "pageProps", ".data.rows" + "[" + i + "]" + ".rem");
            }
        }

        //COMPARE DATA W/T REMAINS\
        Response responseGetDataRemains = apiCoreRequests.
                makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/goods/" + idGoods + "/remains?city=" + city + "&session-id=" + sessionId);

        String remains = getSingleHeaderFromJson(responseGetDataRemains, "data", ".InfoStores[0].StoreQuantRem");
        Assertions.assertResponseTextEqualsToAnother(responseGetAuth, rem, remains);
    }

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.CRITICAL)
    @Description("Это тест входит в аккаунт market, добавляет товар, проверяет в корзине, удаляет и затем опять удаляет")
    @DisplayName("Проверка работы корзины с добавлением товара")
    public void testCheckBasketAddingAndDeleting() {
        String valAdding = "1";
        String gdsNum = "4541870";

        //LOGIN
        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");

        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


        //ADDING
        Response responseAddProduct = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/basket/add?val=" + valAdding + "&gds=" + gdsNum + "&session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseAddProduct, 200);
        Assertions.assertJsonByName(responseAddProduct, "data", ".basket_last_line_num", valAdding);

        //CHECK BASKET AFTER ADDING
        Response responseCheckBasket = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/basket?city=78&skl=4280&session-id=" + sessionId);
        Assertions.assertNotJsonByName(responseCheckBasket, "data", ".sum", "0.0");

        //CLEAR BASKET
        Response responseClearAllBasket = apiCoreRequests
                .makePostRequestNoBody("https://idev.etm.ru/api/ipro/basket/clear?session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseClearAllBasket, 200);
        Assertions.assertJsonByName(responseCheckBasket, "status", ".code", "200");

        //CHECK BASKET CLEARING
        Response responseCheckBasketAfterCleaning = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/basket?city=78&skl=4280&session-id=" + sessionId);
        Assertions.assertJsonByName(responseCheckBasketAfterCleaning, "data", ".sum", "0.0");

    }

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.CRITICAL)
    @Description("Это тест входит в аккаунт market, добавляет товар, проверяет в корзине, удаляет и затем опять удаляет")
    @DisplayName("Проверка работы корзины с добавлением товара")
    public void testAddToCompare() {
        String from_rus = "1";
        String spec = "1";



        //LOGIN
        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");

        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        //TAKE DATA FROM CATALOG
        Response responseGetData = apiCoreRequests.
                makeGetRequestWithNoBody("https://idev.etm.ru/_next/data/" + urlForCatalog +
                        "/catalog/101010_kabeli_s_mednoy_tokoprovodjaschey_zhiloy.json?from_rus=" +
                        from_rus + "&spec=" + spec + "&rows=20&page=1&nameCat=101010_kabeli_s_mednoy_tokoprovodjaschey_zhiloy");


    }


}