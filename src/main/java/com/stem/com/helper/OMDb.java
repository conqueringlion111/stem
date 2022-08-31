package com.stem.com.helper;

import com.stem.com.properties.AppConstants;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class OMDb {

	public static Response searchOMDbByStr(String accessToken, String searchParam, String searchStr, String url) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param(searchParam, searchStr)
				.when()
				.get(url)
				.then()
				.log().ifValidationFails()
				.assertThat().statusCode(200)
				.extract().response();
	}
	
	public static Response searchOMDbByStringAndPage(String accessToken, String searchParam, String searchStr, String pageNumber,
													 String url) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param(searchParam, searchStr)
				.param("page", pageNumber)
				.when()
				.get(url)
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("$", hasKey("Search"))
				.body("$", hasKey("totalResults"))
				.body("$", hasKey("Response"))
				.body("Search[0]", hasKey("Title"))
				.body("Search[1]", hasKey("Title"))
				.body("Search[0].Title", is(notNullValue()))
				.extract().response();
	}
	
	public static Response searchOMDbById(String accessToken, String identifier, String url) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param("i", identifier)
				.when()
				.get(url)
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().response();
	}
	
	public static Response searchOMDbByTitle(String accessToken, String param, String title, String url) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param(param, title)
				.when()
				.get(url)
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().response();
	}
	
	public int getTotalPageCount(int displayedTotal, int totalObj) {
		int totalPages = displayedTotal/totalObj;
		if(displayedTotal % totalObj > 0) {
			totalPages ++;
		}
		return totalPages;
		
	}

}
