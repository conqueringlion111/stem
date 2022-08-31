package testbase;

import com.stem.com.dataprovider.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class APITestBase {

	private static Properties apiTestConfig;
	protected static String baseURL = "";
	protected static String accessToken = "";
	protected static Logger LOGGER = null;

	@BeforeSuite(alwaysRun = true)
	public void setTestConfig() throws FileNotFoundException, IOException {
		apiTestConfig = new Properties();
		apiTestConfig.load(new FileInputStream("TestConfig.properties"));
		if (System.getProperty("test.env") != null) {
			baseURL = System.getProperty("test.env");
		} else {
			baseURL = apiTestConfig.getProperty("url");
		}
		if (System.getProperty("test.apiKey") != null) {
			accessToken = System.getProperty("test.apiKey");
		} else {
			accessToken = apiTestConfig.getProperty("apiKey");
		}
		System.setProperty("log4j.configurationFile","log4j2Config.xml");
        LOGGER = LogManager.getLogger();
	}

	@DataProvider(name = "dataProvider")
	public Object[][] passData(Method method) throws IOException {
		apiTestConfig = new Properties();
		apiTestConfig.load(new FileInputStream("TestConfig.properties"));
		String name = getClass().getName();
		String fileName = name.substring(name.lastIndexOf(".") + 1).trim();
		return JsonReader.getdata(apiTestConfig.getProperty("jsonDataLocation").concat(fileName).concat(".json"),
				method.getName());
	}

}
