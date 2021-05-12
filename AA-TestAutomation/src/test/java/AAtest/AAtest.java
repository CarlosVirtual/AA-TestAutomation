//***********************************************************************
//
// Copyright (c) 2018 Microsoft Corporation. All rights reserved.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//
//**********************************************************************
package AAtest;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import org.json.simple.JSONObject;


public class AAtest {
	//////////////////////////////////////////////
	public static String redirectUri = "https://oauth.pstmn.io/v1/browser-callback";
	public static String scope = "https://graph.microsoft.com/.default";
	public static String username = "myAAuser@xxx.com";
	public static String password = "MyPwd";

	public static Response response;
	public static String userAdminClientId = "241xxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
	public static String userAdminClientSecret  = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	public static String TenantID = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";

	/*
	private String oauth2Payload = "{\n" +
			"  \"client_id\": \"" + userAdminClientId + "\",\n" +
			"  \"client_secret\": \"" + userAdminClientSecret + "\",\n" +
			"  \"audience\": \"https://some-url.com/user\",\n" +
			"  \"grant_type\": \"client_credentials\",\n" +
			"  \"scope\": \"https://graph.microsoft.com/.default\" \n}";
	 */

	private static HashMap<String, String> oauth2Payload;
	static {
		oauth2Payload = new HashMap<String, String>();
		oauth2Payload.put("client_id", userAdminClientId);
		oauth2Payload.put("client_secret", userAdminClientSecret);
		oauth2Payload.put("grant_type", "client_credentials");
		oauth2Payload.put("scope", "https://graph.microsoft.com/.default");		
	}		

	private static JSONObject oauth2PayloadJSON;
	static {
		oauth2PayloadJSON = new JSONObject();
		oauth2PayloadJSON.put("client_id", userAdminClientId);
		oauth2PayloadJSON.put("client_secret", userAdminClientSecret);
		oauth2PayloadJSON.put("grant_type", "client_credentials");
		oauth2PayloadJSON.put("scope", "https://graph.microsoft.com/.default");		
	}
/*
	private static String createUserPayload = "{\n" +
			"  \"username\": \"api-user\",\n" +
			"  \"email\": \"api-user@putsbox.com\",\n" +
			"  \"password\": \"Passw0rd123!\",\n" +
			"  \"firstName\": \"my-first-name\",\n" +
			"  \"lastName\": \"my-last-name\",\n" +
			"  \"roles\": [\"read\"] \n}";
*/			
	//----------------------------------------------------------------------
	public void userAdminConfigSetup() {
		requestSpecification = given().auth().oauth2(getAccessToken(oauth2Payload))
				.header("Accept", ContentType.JSON.getAcceptHeader())
				.contentType(ContentType.JSON);
	}
	//----------------------------------------------------------------------
	public String getAccessToken(HashMap<String, String> payload) {

		System.out.println("payload");
		System.out.println(payload.toString());

		Response rp = 
				given()
				.contentType(ContentType.URLENC)
				.formParams(payload)
				.post("https://login.microsoftonline.com/"+ TenantID+ "/oauth2/V2.0/token")
				.then().extract().response();

		System.out.println("reponse");
		System.out.println(rp.getBody().asPrettyString());

		return rp.jsonPath().getString("access_token");
	}
	//----------------------------------------------------------------------

	@Test(dataProvider = "dp")
	public void f(Integer n, String s) {
	}
	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}


	@DataProvider
	public Object[][] dp() {
		return new Object[][] {
			new Object[] { 1, "a" },
			new Object[] { 2, "b" },
		};
	}
	@BeforeClass
	public void beforeClass() {
	}

	@AfterClass
	public void afterClass() {
	}

	@BeforeTest
	public void beforeTest() {
		//RestAssured.baseURI = "https://login.microsoftonline.com/"+ TenantID ;
	}

	@AfterTest
	public void afterTest() {
	}

	@BeforeSuite
	public void beforeSuite() {
	}

	@AfterSuite
	public void afterSuite() {
	}

	//----------------------------------------------------------------------
	@Test
	public void test_ConfigSetup() {
		userAdminConfigSetup();
	}
	//----------------------------------------------------------------------
	@Test
	public void test_getTeamsUserActivityUserDetail() {		
		System.out.println("2");

		Response response = given(requestSpecification)
				.get("https://graph.microsoft.com/v1.0/reports/getTeamsUserActivityUserDetail(period='D180')");

		System.out.println("response");
		System.out.println(response.getBody().asPrettyString());
		
		System.out.println(response.getStatusCode());
		System.out.println(response.getTime());
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		System.out.println(response.getHeader("content-type"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	//----------------------------------------------------------------------
	@Test
	public void test_getTeamsUserActivityCounts() {		
//		Response response = 
				given(requestSpecification).
				when().
					get("https://graph.microsoft.com/v1.0/reports/getTeamsUserActivityCounts(period='D180')").
				then().
					statusCode(200)
					.log().all();
	}
	//----------------------------------------------------------------------
}
