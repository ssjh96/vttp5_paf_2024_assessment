package vttp2023.batch4.paf.assessment.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.models.User;

@Repository
public class BookingsRepository {
	
	// You may add additional dependency injections

	public static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email like ?";

	public static final String SQL_NEW_USER = """
			INSERT INTO users (email, name)
				VALUES(?, ?)
			""";
	
	public static final String SQL_NEW_BOOKING = """
			INSERT INTO bookings(booking_id, listing_id, duration, email)
				VALUES(?, ?, ?, ?)
			""";

	@Autowired
	private JdbcTemplate template;

	// You may use this method in your task
	public Optional<User> userExists(String email) {
		SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
		if (!rs.next())
			return Optional.empty();

		return Optional.of(new User(rs.getString("email"), rs.getString("name")));
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newUser(User user) 
	{
		template.update(SQL_NEW_USER, user.email(), user.name());
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newBookings(Bookings bookings) 
	{
		template.update(SQL_NEW_BOOKING, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail());
	}
}
