import io.qameta.allure.Description;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;


public class ApiTests {

    @Test
    @Description(value = "Позитивный тест GetConversionRate")
    public void getConversionRateSuccessTest() throws Exception{
        RequestSpecification getSpec = Specification.requestSpec();
        ResponseSpecification checkSpec = Specification.responseSpec(200);
        Specification.installSpec(getSpec,checkSpec);
        Steps.getConversionRate(".//GetConversionRate.xml","GetConversionRateResult",200);
    }

    @Test
    @Description(value = "Негативный тест GetConversionRate, где RateDate не заполнено")
    public void getConversionRateNullRateDateTest() throws Exception{
        RequestSpecification getSpec = Specification.requestSpec();
        ResponseSpecification checkSpec = Specification.responseSpec(500);
        Specification.installSpec(getSpec,checkSpec);
        Steps.getConversionRate(".//GetConversionRateNullRateDate.xml","faultstring",500);
    }

    @Test
    @Description(value = "Позитивный тест GetCurrencies")
    public void getCurrenciesSuccessTest() throws Exception{
        RequestSpecification getSpec = Specification.requestSpec();
        ResponseSpecification checkSpec = Specification.responseSpec(200);
        Specification.installSpec(getSpec,checkSpec);
        Steps.getCurrencies(".//GetCurrencies.xml","GetCurrenciesResult",200);
    }

}
