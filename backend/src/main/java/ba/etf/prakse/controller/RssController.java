package ba.etf.prakse.controller;

import ba.etf.prakse.model.Internship;
import ba.etf.prakse.repository.InternshipRepository;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rss")
@RequiredArgsConstructor
public class RssController {

    private final InternshipRepository internshipRepository;

    @GetMapping(value = "/internships", produces = MediaType.APPLICATION_XML_VALUE)
    public String getRssFeed() throws Exception {
        List<Internship> internships = internshipRepository.findAll()
                .stream().filter(Internship::getActive).toList();

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("Studentske prakse – ETF Banja Luka");
        feed.setLink("http://localhost:8080/api/rss/internships");
        feed.setDescription("Lista dostupnih studentskih praksi");

        List<SyndEntry> entries = new ArrayList<>();

        for (Internship internship : internships) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(internship.getTitle() + " – " + internship.getCompany().getName());
            entry.setLink("http://localhost:8080/api/internships/" + internship.getId());
            entry.setPublishedDate(new Date());

            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");

            StringBuilder sb = new StringBuilder();
            sb.append(internship.getDescription() != null ? internship.getDescription() : "");
            if (internship.getStartDate() != null) {
                sb.append(" | Period: ").append(internship.getStartDate()).append(" - ").append(internship.getEndDate());
            }
            if (!internship.getTechnologies().isEmpty()) {
                sb.append(" | Tehnologije: ");
                internship.getTechnologies().forEach(t -> sb.append(t.getName()).append(", "));
            }

            description.setValue(sb.toString());
            entry.setDescription(description);
            entries.add(entry);
        }

        feed.setEntries(entries);

        SyndFeedOutput output = new SyndFeedOutput();
        return output.outputString(feed);
    }
}
