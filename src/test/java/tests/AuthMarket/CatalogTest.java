package tests.AuthMarket;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.MINOR)
    @Description("Это тест проверяет добавление в избранное на аккаунте market")
    @DisplayName("Проверка добавления в избранное из каталога")
    public void testAddingToFavoriteFromCatalog() {

        //LOGIN
        String idGoods = "2415539";

        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");
        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        //CHECK CNT
        Response responseGetCntFavorites = apiCoreRequests.
                makeGetRequestWithNoBody("https://idev.etm.ru/_next/data/HvB8kSbg34YbZlhTt2Inw/catalog.json?my_fav=1");
        String cnt = getSingleHeaderFromJson(responseGetCntFavorites, "pageProps", ".data.records");


        //ADDING
        Response responseAddProduct = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/goods/" + idGoods + "/add_to_fav?session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseAddProduct, 200);
        Assertions.assertNotJsonByName(responseAddProduct, "data", ".cnt", cnt);

        //DELETING
        Response responseDelProduct = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/goods/" + idGoods + "/del_from_fav?session-id=" + sessionId);
        Assertions.assertResponseCodeEquals(responseDelProduct, 200);
        Assertions.assertNotJsonByName(responseDelProduct, "data", ".cnt", cnt);
    }

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.MINOR)
    @Description("Это тест проверяет отрабатываение функционала замены для определенного товара")
    @DisplayName("Проверка функционала замены")
    public void testZamenaCatalog() {
        //LOGIN
        String idGoods = "2415539";

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
                makeGetRequestWithNoBody("https://idev.etm.ru/_next/data/HvB8kSbg34YbZlhTt2Inw/catalog/101010_kabeli_s_mednoy_tokoprovodjaschey_zhiloy.json?nameCat=101010_kabeli_s_mednoy_tokoprovodjaschey_zhiloy");

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


}
