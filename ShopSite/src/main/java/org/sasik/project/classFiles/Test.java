package org.sasik.project.classFiles;
import io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

//import org.sasik.project.pojoclasses.AddProduct;
import org.sasik.project.pojoclasses.Login;

public class Test {
	
	public static void main(String[] args) {
		/* Login Details Setting Here */
		Login login = new Login();
		login.setUserEmail("sasikiran2213@gmail.com");
		login.setUserPassword("Sasikiran@2213");
	RequestSpecification	requestSession=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
		.setContentType(ContentType.JSON).build();
	
	ResponseSpecification responseSession = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
			.expectStatusCode(200).build();
/* For Secure Authorization of Login Purpose */	
String	responseSessions=given().log().all().spec(requestSession).body(login).when().post("api/ecom/auth/login")
	.then().log().all().spec(responseSession).extract().response().asString();
/* For Secure Authorization of Login Purpose extracting Token Key */
JsonPath js = new JsonPath(responseSessions);
String token = js.get("token");
String userId = js.get("userId");
System.out.println(token);

RequestSpecification addproductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
.addHeader("Authorization", token).build();

RequestSpecification reqAddProduct = given().log().all().spec(addproductBaseReq).param("productName", "Sasik1")
.param("productAddedBy",userId)
.param("productCategory", "fashion")
.param("productSubCategory","shirts")
.param("productPrice","1200")
.param("productDescription","Hello")
.param("productFor","men")
.multiPart("productImage",new File("\\Users\\kakar\\git\\repository19\\ShopSite\\mysign.jpg"));
String addproductResponse = reqAddProduct.when().post("api/ecom/product/add-product")
.then().log().all().extract().response().asString();
JsonPath js1 = new JsonPath(addproductResponse);
String productId = js.getString("productId");



		
	}

}
