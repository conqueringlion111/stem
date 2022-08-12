package com.stem.com.helper;

import com.stem.com.properties.AppConstants;
import io.restassured.response.Response;
import testbase.APITestBase;

import static io.restassured.RestAssured.given;

public class OMDb extends APITestBase {

	public static Response searchOMDbByStr(String searchParam, String searchStr) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param(searchParam, searchStr)
				.when()
				.get(baseURL)
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().response();
	}
	
	public static Response searchOMDbByStringAndPage(String searchParam, String searchStr, String pageNumber) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param(searchParam, searchStr)
				.param("page", pageNumber)
				.when()
				.get(baseURL)
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().response();
	}
	
	public static Response searchOMDbById(String identifier) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param("i", identifier)
				.when()
				.get(baseURL)
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().response();
	}
	
	public static Response searchOMDbByTitle(String param, String title) {
		return given()
				.header(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
				.param("apikey", accessToken)
				.param(param, title)
				.when()
				.get(baseURL)
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
