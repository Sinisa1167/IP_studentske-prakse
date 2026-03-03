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
import java.util.Map;

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
            @RequestBody Map<String, Object> body) {

        Internship internship = new Internship();
        // Mapiranje se radi u servisu, ovdje samo prosljeđujemo
        return ResponseEntity.ok(internshipService.create(companyId, internship, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Internship> update(
            @PathVariable Long id,
            @RequestBody Internship internship,
            @RequestParam(required = false) List<Long> technologyIds) {
        return ResponseEntity.ok(internshipService.update(id, internship, technologyIds));
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
}
