package com.stem.com.test;

import com.stem.com.helper.OMDb;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testbase.APITestBase;

import java.util.Map;

public class OMDBTest extends APITestBase {
	
	@Test(groups = {"omdb"}, dataProvider = "dataProvider", description = "test coverage for 1. search items that match search string")
	public void searchItems(String searchParm, String searchStr, String titleOne, String titleTwo) {
		//initial search to get totalResults and total returned objects per page
		Response searchObj = OMDb.searchOMDbByStr(accessToken, searchParm, searchStr, baseURL);
		int totalResults = Integer.parseInt(searchObj.path("totalResults").toString());
		int totalObj = searchObj.path("Search.size()");
		boolean titleOneFound = false;
		boolean titleTwoFound = false;
		OMDb omd = new OMDb();
		//getting total pages we may need to navigate through to find the titles
		int totalPages = omd.getTotalPageCount(totalResults, totalObj);
		for(int i = 1; i < totalPages; ++i) {
			String pageNumber = String.valueOf(i);
			Response titleSearch = OMDb.searchOMDbByStringAndPage(accessToken, searchParm, searchStr, pageNumber, baseURL);
			int returnedObjCt = titleSearch.path("Search.size()");
			for(int j = 0; j <returnedObjCt; ++j) {
				Map<String, String> tempData = titleSearch.path("Search[" + j + "]");
				if(tempData.get("Title").equals(titleOne)) {
					titleOneFound = true;
				}
				if(tempData.get("Title").equals(titleTwo)) {
					titleTwoFound = true;
				}
				if(titleOneFound && titleTwoFound) {
					break;
				}
			}
			if(titleOneFound && titleTwoFound) {
				break;
			}
		}
		Assert.assertTrue(totalResults >= 30, "expected number of search objects not returned");
		Assert.assertTrue(titleOneFound, "title ".concat(titleOne).concat(" not found"));
		Assert.assertTrue(titleTwoFound, "title ".concat(titleTwo).concat(" not found"));	
	}
	
	@Test(groups = {"omdb"}, dataProvider = "dataProvider", description = "test coverage for 2. get imdbID validate details")
	public void getIMDBDetails(String searchParm, String searchStr, String searchTitle, String expectOne, String expectTwo) {
		Response searchObj = OMDb.searchOMDbByStr(accessToken, searchParm, searchStr, baseURL);
		int totalResults = 0;
		try {
			totalResults = Integer.parseInt(searchObj.path("totalResults").toString());
		} catch(Exception e) {
			Assert.fail("imdb search results objects not available");
		}
//		int totalResults = Integer.parseInt(searchObj.path("totalResults").toString());
		int totalObj = searchObj.path("Search.size()");
		boolean searchTitleFound = false;
		OMDb omd = new OMDb();
		//getting total pages we may need to navigate through to find the titles
		int totalPages = omd.getTotalPageCount(totalResults, totalObj);
		String imdbID = "";
		//parsing through title objects per page to search for the specific title
		for(int i = 1; i < totalPages; ++i) {
			String pageNumber = String.valueOf(i);
			Response titleSearch = OMDb.searchOMDbByStringAndPage(accessToken, searchParm, searchStr, pageNumber, baseURL);
			int returnedObjCt = titleSearch.path("Search.size()");
			for(int j = 0; j <returnedObjCt; ++j) {
				Map<String, String> tempData = titleSearch.path("Search[" + j + "]");
				if(tempData.get("Title").equals(searchTitle)) {
					imdbID = tempData.get("imdbID");
					searchTitleFound = true;
				}
				if(searchTitleFound) {
					break;
				}
			}
			if(searchTitleFound) {
				break;
			}
		}
		Response titleObj = OMDb.searchOMDbById(accessToken, imdbID, baseURL);
		Map<String, String> titleData = titleObj.path("$");
		//asserting the release data and director name
		Assert.assertEquals(titleData.get("Released"), expectOne, "expected release date ".concat(expectOne).concat(" not returned"));
		Assert.assertEquals(titleData.get("Director"), expectTwo, "expected director ".concat(expectTwo).concat(" not returned"));
	}
	
	@Test(groups = {"omdb"}, dataProvider = "dataProvider", description = "test coverage for 3. get item by title")
	public void getItemByTitle(String searchParam, String title, String expectOne, String expectTwo) {
		Response titleObj = OMDb.searchOMDbByTitle(accessToken, searchParam, title, baseURL);
		Map<String, String> titleData = titleObj.path("$");
		Assert.assertTrue(titleData.get("Plot").contains(expectOne.toLowerCase()), "expected string ".concat(expectOne).concat(" was not returned"));
		Assert.assertTrue(titleData.get("Runtime").contains(expectTwo), "expected runtime value ".concat(expectTwo).concat(" was not returned"));
	}

}
