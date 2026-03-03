package ba.etf.prakse.controller;

import ba.etf.prakse.model.Company;
import ba.etf.prakse.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<Page<Company>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(companyService.searchByName(search, pageable));
        }
        return ResponseEntity.ok(companyService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) {
        return companyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Company> getByUserId(@PathVariable Long userId) {
        return companyService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Company> create(
            @RequestBody Company company,
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(companyService.create(company, username, password));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.update(id, company));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Company> activate(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.setActive(id, true));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Company> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.setActive(id, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
