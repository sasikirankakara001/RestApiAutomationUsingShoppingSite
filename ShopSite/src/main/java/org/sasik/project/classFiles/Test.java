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
		login.setUserEmail("admin_123@gmail.com");
		login.setUserPassword("Admin@12345");
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
String productId = js1.get("productId");
System.out.println(productId);

RequestSpecification reqAddtoCart=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
.addHeader("Authorization", token)
.setContentType(ContentType.JSON)
.build();
ResponseSpecification respAddtoCart = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
.expectStatusCode(200).build();


given().log().all().spec(reqAddtoCart).body("{\r\n"
		+ "    \"_id\": \"64e71719753efa4657edee96\",\r\n"
		+ "    \"product\": {\r\n"
		+ "        \"_id\": \""+productId+"\",\r\n"
		+ "        \"productName\": \"Sasik\",\r\n"
		+ "        \"productCategory\": \"fashion\",\r\n"
		+ "        \"productSubCategory\": \"shirts\",\r\n"
		+ "        \"productPrice\": 1500,\r\n"
		+ "        \"productDescription\": \"Adidas Originals\",\r\n"
		+ "        \"productImage\": \"https://rahulshettyacademy.com/api/ecom/uploads/productImage_1695672677667.jpg\",\r\n"
		+ "        \"productRating\": \"0\",\r\n"
		+ "        \"productTotalOrders\": \"0\",\r\n"
		+ "        \"productStatus\": true,\r\n"
		+ "        \"productFor\": \"women\",\r\n"
		+ "        \"productAddedBy\": \"64e71719753efa4657edee96\",\r\n"
		+ "        \"__v\": 0\r\n"
		+ "    }\r\n"
		+ "}").when().post("api/ecom/user/add-to-cart")
.then().log().all().spec(respAddtoCart).extract().response().asString();

RequestSpecification createOrderProduct = 
new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
.addHeader("Authorization", token)
.setContentType(ContentType.JSON)
.build();
ResponseSpecification responseOrderProduct = new ResponseSpecBuilder()
.expectContentType(ContentType.JSON)
.expectStatusCode(200).build();

given().log().all().spec(createOrderProduct).body("{\r\n"
		+ "    \"orders\": [\r\n"
		+ "        {\r\n"
		+ "            \"country\": \"India\",\r\n"
		+ "            \"productOrderedId\": \""+productId+"\"\r\n"
		+ "        }\r\n"
		+ "    ]\r\n"
		+ "}").when().post("api/ecom/order/create-order")
.then().log().all().extract().response().asString();

RequestSpecification deleteOrderProduct = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
.addHeader("Authorization", token)
.setContentType(ContentType.JSON)
.build();

given().log().all().spec(deleteOrderProduct).when().delete("api/ecom/product/delete-product/"+productId+"")
.then().log().all().extract().response().asString();




		
	}

}
