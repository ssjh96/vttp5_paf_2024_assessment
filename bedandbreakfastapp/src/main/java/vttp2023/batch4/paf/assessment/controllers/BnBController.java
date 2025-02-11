package vttp2023.batch4.paf.assessment.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.services.ListingsService;
import vttp2023.batch4.paf.assessment.Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BnBController {

	// You may add additional dependency injections

	@Autowired
	private ListingsService listingsSvc;



	// TASK 3 : http://localhost:8080/api/suburbs
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/suburbs")
	@ResponseBody
	public ResponseEntity<String> getSuburbs() {
		List<String> suburbs = listingsSvc.getAustralianSuburbs();
		JsonArray result = Json.createArrayBuilder(suburbs).build();
		return ResponseEntity.ok(result.toString());
	}
	

	// Task 4 - Return an array of accomodation listing summary
	// Error: Http failure response for http://localhost:8080/api/search?suburb=Forest%20Lodge&persons=1&price_range=100&duration=1: 500 OK
	// [INPUT] suburb, persons, price_range, duration [FIND]
	// [OUTPUT] id, name, price, accomodates [project] 
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<String> search(@RequestParam MultiValueMap<String, String> params) {

		String suburb = params.getFirst("suburb");
		int persons = Integer.parseInt(params.getFirst("persons"));
		int duration = Integer.parseInt(params.getFirst("duration"));
		float priceRange = Float.parseFloat(params.getFirst("price_range"));

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		listingsSvc.findAccommodatations(suburb, persons, duration, priceRange)
			.stream()
			.forEach(acc -> 
				arrBuilder.add(
					Json.createObjectBuilder()
						.add("id", acc.getId())
						.add("name", acc.getName())
						.add("price", acc.getPrice())
						.add("accommodates", acc.getAccomodates())
						.build()
				)
			);

		return ResponseEntity.ok(arrBuilder.build().toString());
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/accommodation/{id}")
	@ResponseBody
	public ResponseEntity<String> getAccommodationById(@PathVariable String id) {

		Optional<Accommodation> opt = listingsSvc.findAccommodatationById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(Utils.toJson(opt.get()).toString());
	}

	// TODO: Task 6
	@PostMapping(path = "/accommodation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postAccomodation(@RequestBody String payload) 
	{
		System.out.println(">>> payload: " + payload);

		if (payload == null || payload.isBlank()) {
			return ResponseEntity.badRequest().body("Request body cannot be empty");
		}

		JsonReader jReader = Json.createReader(new StringReader(payload));
		JsonObject jObj = jReader.readObject();
		
		System.out.println(">>> jObj: " + jObj.toString());

		Bookings bookings = new Bookings();
		bookings.setName(jObj.getString("name"));
		bookings.setEmail(jObj.getString("email"));
		bookings.setDuration(jObj.getInt("nights"));
		bookings.setListingId(jObj.getString("id"));

		// >>> payload: {"name":"hello","email":"hello@email.com","nights":2,"id":"9599384"}
		// >>> jObj: {"name":"hello","email":"hello@email.com","nights":2,"id":"9599384"}

		try {

			JsonObject jResponse = Json.createObjectBuilder().build();

			listingsSvc.createBooking(bookings);

			return ResponseEntity.ok("{}"); //  alr stated produces json
			// return ResponseEntity.ok(jResponse.toString());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			JsonObject jError = Json.createObjectBuilder()
								.add("message", e.getMessage())
								.build();

			return ResponseEntity.status(500).body(jError.toString());
		}
	}
	
}
