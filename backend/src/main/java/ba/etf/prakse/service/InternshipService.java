package ba.etf.prakse.service;

import ba.etf.prakse.model.Company;
import ba.etf.prakse.model.Internship;
import ba.etf.prakse.model.Technology;
import ba.etf.prakse.repository.CompanyRepository;
import ba.etf.prakse.repository.InternshipRepository;
import ba.etf.prakse.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final CompanyRepository companyRepository;
    private final TechnologyRepository technologyRepository;

    public Page<Internship> getAll(Pageable pageable) {
        return internshipRepository.findByActive(true, pageable);
    }

    public Page<Internship> searchByTitle(String title, Pageable pageable) {
        return internshipRepository.findByTitleContainingIgnoreCaseAndActive(title, true, pageable);
    }

    public Page<Internship> getByCompany(Long companyId, Pageable pageable) {
        return internshipRepository.findByCompanyId(companyId, pageable);
    }

    public Page<Internship> getByTechnology(Long techId, Pageable pageable) {
        return internshipRepository.findByTechnologyId(techId, pageable);
    }

    public Optional<Internship> getById(Long id) {
        return internshipRepository.findById(id);
    }

    public List<Internship> getAllForAi() {
        return internshipRepository.findAll();
    }

    @Transactional
    public Internship create(Long companyId, Internship internship, List<Long> technologyIds) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Kompanija nije pronađena"));

        internship.setCompany(company);
        internship.setActive(true);
        internship.setTechnologies(resolveTechnologies(technologyIds));

        return internshipRepository.save(internship);
    }

    @Transactional
    public Internship update(Long id, Internship updated, List<Long> technologyIds) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Praksa nije pronađena"));

        internship.setTitle(updated.getTitle());
        internship.setDescription(updated.getDescription());
        internship.setStartDate(updated.getStartDate());
        internship.setEndDate(updated.getEndDate());
        internship.setConditions(updated.getConditions());
        internship.setTechnologies(resolveTechnologies(technologyIds));

        return internshipRepository.save(internship);
    }

    @Transactional
    public Internship setActive(Long id, boolean active) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Praksa nije pronađena"));
        internship.setActive(active);
        return internshipRepository.save(internship);
    }

    @Transactional
    public void delete(Long id) {
        internshipRepository.deleteById(id);
    }

    private List<Technology> resolveTechnologies(List<Long> ids) {
        if (ids == null) return new ArrayList<>();
        List<Technology> technologies = new ArrayList<>();
        for (Long techId : ids) {
            technologyRepository.findById(techId).ifPresent(technologies::add);
        }
        return technologies;
    }
}
