import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static io.restassured.RestAssured.given;
public class ApiTests {

    @ParameterizedTest
    //@CsvSource({"USD,RUB,2020-04-10,100", "RUB,USD,2020-04-15,200"})
    @CsvFileSource(resources = "/ppp.csv")
    public void getConversionAmountTest(String currencyFrom, String currencyTo, String rateDate, String amount) throws Exception{
        String filePath = ".//GetConversionAmount.xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(filePath);
        Node nodeCurrencyFrom = doc.getElementsByTagName("tem:CurrencyFrom").item(0);
        Node nodeCurrencyTo = doc.getElementsByTagName("tem:CurrencyTo").item(0);
        Node nodeDate = doc.getElementsByTagName("tem:RateDate").item(0);
        Node nodeAmount = doc.getElementsByTagName("tem:Amount").item(0);
        nodeCurrencyFrom.setTextContent(currencyFrom);
        nodeCurrencyTo.setTextContent(currencyTo);
        nodeDate.setTextContent(rateDate);
        nodeAmount.setTextContent(amount);
        String xmlToRequest = CustimUtils.documentToString(doc);
        System.out.println(xmlToRequest);
        Response response=
                given()
                        .header("Content-Type", "text/xml")
                        .and()
                        .body(xmlToRequest)
                        .when()
                        .post("http://currencyconverter.kowabunga.net/converter.asmx")
                        .then()
                        .statusCode(200)
                        .and()
                        .log().all().extract().response();
        XmlPath xmlPath = new XmlPath(response.asString());
        String rate = xmlPath.getString("GetConversionAmountResult");
        Assertions.assertFalse(xmlPath.get("GetConversionAmountResult").toString().isEmpty(),"rate is NULL");
        System.out.println("rate is "+rate);
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
