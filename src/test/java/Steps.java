import io.qameta.allure.Step;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.apache.tika.io.IOUtils;
import org.junit.jupiter.api.Assertions;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class Steps {

    @Step("Проверка getCurrencies")
    public static String getCurrencies(String converterSoap, String result, int code) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(converterSoap);
        Response response =
                given()
                        .contentType("text/xml")
                        .and()
                        .body(IOUtils.toString(fileInputStream, "UTF-8"))
                        .when()
                        .post("/converter.asmx")
                        .then()
                        .statusCode(code)
                        .and()
                        .log().all().extract().response();
        XmlPath xmlPath = new XmlPath(response.asString());
        String resp = xmlPath.getString(result);
        xmlPath.getList(result).forEach(x -> Assertions.assertFalse(x.toString().isEmpty(), "currency is null"));
        List<String> list = xmlPath.getList(result);
        List<String> sorted = list.stream().sorted().collect(Collectors.toList());
        Assertions.assertEquals(sorted, list);
        return resp;
    }

}
