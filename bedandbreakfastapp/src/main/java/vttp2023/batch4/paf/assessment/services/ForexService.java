package vttp2023.batch4.paf.assessment.services;

import java.io.StringReader;
import java.math.BigDecimal;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch4.paf.assessment.Constants.Url;

@Service
public class ForexService {


	// TODO: Task 5 
	public float convert(String from, String to, float amount) {

		try {
			// // Consider using exchange
			// RestTemplate restTemplate = new RestTemplate();
			// String response = restTemplate.getForObject(Url.currencyBaseAudUrl, String.class);

			// System.out.println(">>> Response: " + response);

			// JsonReader jReader = Json.createReader(new StringReader(response));
			// JsonObject jObj = jReader.readObject();

			// JsonObject jRates = jObj.getJsonObject("rates");
			// float conversionRate = (float) jRates.getJsonNumber("SGD").doubleValue();

			// // double dConversionRate = jRates.getJsonNumber("SGD").doubleValue();
			// // float fConversionRate = BigDecimal.valueOf(conversionRate).floatValue();

			// System.out.println(">>> conversionRate: " + conversionRate);

			// return conversionRate;


			String url = UriComponentsBuilder.fromUriString(Url.currencyBaseUrl).queryParam("base", from).queryParam("symbols", to).toUriString();

			System.out.println(">>> URL: " + url);
			// request entity
			RequestEntity<Void> request = RequestEntity.get(url)
												// .header(to, null) // add apiKey sometime in headers need
												// header use for timestamp and version condiional updates
												.build();
			
			
			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> response = restTemplate.exchange(request, String.class);
			 // void because request not giving api anything back

			String jResponseStr = response.getBody();

			JsonReader jReader = Json.createReader(new StringReader(jResponseStr));
			JsonObject jObj = jReader.readObject();

			JsonObject jRates = jObj.getJsonObject("rates");
			float conversionRate = (float) jRates.getJsonNumber(to.toUpperCase()).doubleValue(); // to = "sgd" -> "SGD"

			// double dConversionRate = jRates.getJsonNumber("SGD").doubleValue();
			// float fConversionRate = BigDecimal.valueOf(conversionRate).floatValue();
			System.out.println(">>> conversionRate: " + conversionRate);

			float convertedAmount = amount * conversionRate;
			System.out.println(">>> conversionRate: " + convertedAmount);

			return convertedAmount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1000f; // if any error, return -1000 as converted price
		}		
	}
}
