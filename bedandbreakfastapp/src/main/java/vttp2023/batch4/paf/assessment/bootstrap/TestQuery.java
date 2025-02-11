package vttp2023.batch4.paf.assessment.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp2023.batch4.paf.assessment.repositories.BookingsRepository;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;

@Component
public class TestQuery implements CommandLineRunner
{
    @Autowired
    private ListingsRepository listingsRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Override
    public void run(String... args) throws Exception
    {
        // TESTING TASK 3
        System.out.println(">>>> Testing task 3");
        System.out.println(listingsRepository.getSuburbs("australia"));
    }

}
