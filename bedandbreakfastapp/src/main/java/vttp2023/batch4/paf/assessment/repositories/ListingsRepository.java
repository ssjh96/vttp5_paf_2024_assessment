package vttp2023.batch4.paf.assessment.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.Constants.MongoParams;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;


	// TASK 3
	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *db.listings.aggregate([
		{ $match: {
			"address.country" : { $regex: "australia", $options: 'i'}, 
			"address.suburb": { $nin: ["", null] } } }, // Exclude empty and null String
		{ $group: { _id: "$address.suburb" } } // group by distinct suburbs
		]);
	 *
	 */
	public List<String> getSuburbs(String country) 
	{
		// Get non-empty and non-null suburbs for given country
		Criteria criteria = Criteria.where(MongoParams.L_F_COUNTRY).regex(country, "i")
									.and(MongoParams.L_F_SUBURBS).nin("", null);
		MatchOperation matchNonEmptyNullSuburbs = Aggregation.match(criteria);

		// Group by distinct sururbs 
		GroupOperation groupBySuburbs = Aggregation.group(MongoParams.L_F_SUBURBS);

		Aggregation pipeline = Aggregation.newAggregation(matchNonEmptyNullSuburbs, groupBySuburbs);

		AggregationResults<Document> results = template.aggregate(pipeline, MongoParams.C_LISTINGS, Document.class);

		List<Document>  suburbsListDoc = results.getMappedResults(); // Output > [{_id: "xyz"}, {_id: "abc"}]

		// results.getMappedResults().stream().map(doc -> doc.getString("_id")).toList();

		List<String> suburbsList = new ArrayList<>();
		for(Document d : suburbsListDoc)
		{
			suburbsList.add(d.getString(MongoParams.L_F_ID)); // get "_id"
		}

		return suburbsList;
		// return null; // returns a failure response for http://localhost:8080/api/suburbs:500 OK 
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *db.listings.find({
		'address.suburb' : {$regex: 'avalon', $options:'i'},
		price: { $gte: 50.00, $lte: 200.00},  
		accommodates: {$gte : 3},
		min_nights: {$lte : 2}
	})
	.projection({_id: 1, name: 1, price: 1, accommodates: 1})
	.sort({price: -1})
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) 
	{
		Criteria criteria = Criteria.where(
								MongoParams.L_F_SUBURBS).regex(suburb, "i") // match provided suburbs, case insensitive
								.and(MongoParams.L_F_PRICE).lte(priceRange) // results should be less than or equal to input price
								.and(MongoParams.L_F_ACCOMMODATES).gte(persons) // results should accomodate more than input number of person 
								.and(MongoParams.L_F_MIN_NIGHTS).lte(duration); // results should have min nights less than input duration

		// Criteria criteria = Criteria.where(
		// 							MongoParams.L_F_SUBURBS).regex(suburb, "i")
		// 							.andOperator(
		// 								Criteria.where(MongoParams.L_F_PRICE).lte(priceRange),
		// 								Criteria.where(MongoParams.L_F_ACCOMMODATES).gte(persons),
		// 								Criteria.where(MongoParams.L_F_MIN_NIGHTS).lte(duration));

		Query query = Query.query(criteria); // Find the criteria

		query.fields().include(MongoParams.L_F_ID, MongoParams.L_F_NAME, MongoParams.L_F_PRICE, MongoParams.L_F_ACCOMMODATES); // Project the required fields

		query.with(Sort.by(Sort.Direction.DESC, MongoParams.L_F_PRICE)); // Sort highest price first

		List<Document> resultsDoc = template.find(query, Document.class, MongoParams.C_LISTINGS);

		List<AccommodationSummary> accomSummaryList = new ArrayList<>();
		
		for(Document d : resultsDoc)
		{	
			AccommodationSummary as = new AccommodationSummary();
			as.setId(d.getString("_id"));
			as.setName(d.getString("name"));
			as.setAccomodates(d.getInteger("accommodates"));

			Float price  = d.get("price", Number.class).floatValue();
			as.setPrice(price);

			accomSummaryList.add(as);
		}	

		return accomSummaryList; // test at http://localhost:8080/api/search?suburb=Monterey&persons=2&duration=1&price_range=50

	}




	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
