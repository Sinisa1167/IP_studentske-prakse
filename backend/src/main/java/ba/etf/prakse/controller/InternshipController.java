package ba.etf.prakse.controller;

import ba.etf.prakse.model.Internship;
import ba.etf.prakse.service.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
@RequiredArgsConstructor
public class InternshipController {

    private final InternshipService internshipService;

    @GetMapping
    public ResponseEntity<Page<Internship>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long techId) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        if (techId != null) {
            return ResponseEntity.ok(internshipService.getByTechnology(techId, pageable));
        }
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(internshipService.searchByTitle(search, pageable));
        }
        return ResponseEntity.ok(internshipService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Internship> getById(@PathVariable Long id) {
        return internshipService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<Internship>> getByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(internshipService.getByCompany(companyId, pageable));
    }

    @PostMapping("/company/{companyId}")
    public ResponseEntity<Internship> create(
            @PathVariable Long companyId,
            @RequestBody InternshipRequest request) {

        Internship internship = new Internship();
        internship.setTitle(request.getTitle());
        internship.setDescription(request.getDescription());
        internship.setStartDate(request.getStartDate());
        internship.setEndDate(request.getEndDate());
        internship.setConditions(request.getConditions());

        return ResponseEntity.ok(internshipService.create(companyId, internship, request.getTechnologyIds()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Internship> update(
            @PathVariable Long id,
            @RequestBody InternshipRequest request) {

        Internship internship = new Internship();
        internship.setTitle(request.getTitle());
        internship.setDescription(request.getDescription());
        internship.setStartDate(request.getStartDate());
        internship.setEndDate(request.getEndDate());
        internship.setConditions(request.getConditions());

        return ResponseEntity.ok(internshipService.update(id, internship, request.getTechnologyIds()));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Internship> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(internshipService.setActive(id, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        internshipService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Inner DTO klasa
    @lombok.Data
    public static class InternshipRequest {
        private String title;
        private String description;
        private java.time.LocalDate startDate;
        private java.time.LocalDate endDate;
        private String conditions;
        private List<Long> technologyIds;
    }
}