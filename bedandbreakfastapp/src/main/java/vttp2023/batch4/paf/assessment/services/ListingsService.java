package vttp2023.batch4.paf.assessment.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;
import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.models.User;
import vttp2023.batch4.paf.assessment.repositories.BookingsRepository;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;

@Service
public class ListingsService {
	
	// You may add additional dependency injections

	@Autowired
	private ListingsRepository listingsRepo;
	
	@Autowired
	private BookingsRepository bookingsRepository;

	@Autowired
	private ForexService forexSvc;
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD.
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public List<String> getAustralianSuburbs() {
		return listingsRepo.getSuburbs("australia");
	}
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public List<AccommodationSummary> findAccommodatations(String suburb, int persons
			, int duration, float priceRange) {
		return listingsRepo.findListings(suburb, persons, duration, priceRange);
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Optional<Accommodation> opt = listingsRepo.findAccommodatationById(id);

		if (opt.isEmpty())
			return opt;

		Accommodation acc = opt.get();
		float sgd = forexSvc.convert("aud", "sgd", acc.getPrice());
		acc.setPrice(sgd);

		return opt;
	}

	// TODO: Task 6 
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add annotations and throw exceptions to this method
	public void createBooking(Bookings booking) {
		
		// Signature = return type and parameters
		// booking has 
		//		name -> for check user exist / no exist create new user 
		//		(booking repo) | (users table)
		// 		id -> for listing_id in bookings table
		//		(booking repo) | (bookings table)
		//		email -> email
		//		(booking repo) | (bookings table)
		// 		nights -> duration
		//		(booking repo) | (bookings table)

		// (bookings table)
		// booking_id - autogenerate when new Bookings() object constructed
		// listing_id - from booking object
		// duration - from booking object
		// email - from booking object
		// 
		// (users table)
		// name - from booking object
		// email - from booking object

		// Check if user exist
		String email = booking.getEmail();
		Optional<User> optUser = bookingsRepository.userExists(email);

		if(optUser.isEmpty())
		{
			User user = new User(email, booking.getName());
			bookingsRepository.newUser(user);
		}

		bookingsRepository.newBookings(booking);
	}

}
