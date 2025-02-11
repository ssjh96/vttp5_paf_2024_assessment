package vttp2023.batch4.paf.assessment.bootstrap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp2023.batch4.paf.assessment.models.AccommodationSummary;
import vttp2023.batch4.paf.assessment.repositories.BookingsRepository;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;
import vttp2023.batch4.paf.assessment.services.ForexService;

@Component
public class TestQuery implements CommandLineRunner
{
    @Autowired
    private ListingsRepository listingsRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private ForexService forexService;

    @Override
    public void run(String... args) throws Exception
    {
        // TESTING TASK 3
        System.out.println(">>>> Testing task 3");
        System.out.println(listingsRepository.getSuburbs("australia"));

        // TESTING TASK 4
        System.out.println(">>>> Testing task 4");
        List<AccommodationSummary> accomSumList = listingsRepository.findListings("avalon", 2, 3, 200);

        int i = 1;
        for (AccommodationSummary as : accomSumList)
        {
            System.out.println(">>> " + i);
            System.out.println(">>> as id: " + as.getId());
            System.out.println(">>> as name: " + as.getName());
            System.out.println(">>> as price: " + as.getPrice());
            System.out.println(">>> as accomodates: " + as.getAccomodates());
            System.out.println();
            i++;
        }

        // TESTING TASK 5
        System.out.println(">>> TEST 5: " + forexService.convert("aud", "sgd", 100f));
    }

}
