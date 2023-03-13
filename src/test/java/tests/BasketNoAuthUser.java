package tests;

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

import java.util.HashMap;
import java.util.Map;

//import java.util.HashMap;
//import java.util.Map;


public class BasketNoAuthUser extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.CRITICAL)
    @Description("Это тест входит в аккаунт market, добавляет товар, проверяет в корзине, удаляет и затем опять удаляет")
    @DisplayName("Проверка работы корзины с добавлением товара")
    public void testCheckBasketAddingAndDeleting() {

        //LOGIN and ADDING
        String valAdding = "1";

        Map<String, String> authData = new HashMap<>();
        authData.put("val", valAdding);
        authData.put("gds", "4541870");

        Response responseGetAuth = apiCoreRequests.
                makePostRequestNoBody("https://idev.etm.ru/api/ipro/user/login?log=9692161158&pwd=20101999d");

        String sessionId = getSingleHeaderFromJson(responseGetAuth, "data", ".session");
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        authData.put("session-id", sessionId);

        Response responseAddProduct = apiCoreRequests
                .makeGetRequestWithNoBody("https://idev.etm.ru/api/ipro/basket/add?val=" + valAdding + "&gds=4541870&session-id=" + sessionId);
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


}
