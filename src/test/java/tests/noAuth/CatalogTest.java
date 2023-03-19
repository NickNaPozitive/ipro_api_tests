package tests.noAuth;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CatalogTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Owner(value = "Дегтярёв Никита Витальевич")
    @Severity(value = SeverityLevel.MINOR)
    @Description("Это тест проверяет добавление в сравнение на аккаунте noAuth")
    @DisplayName("Проверка добавления в сравнение из каталога")
    public void testAddingToCompareFromCatalog() {

    }

}
