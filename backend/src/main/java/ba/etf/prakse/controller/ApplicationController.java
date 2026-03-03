package ba.etf.prakse.controller;

import ba.etf.prakse.model.Application;
import ba.etf.prakse.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Application>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(applicationService.getByStudentId(studentId));
    }

    @GetMapping("/internship/{internshipId}")
    public ResponseEntity<Page<Application>> getByInternship(
            @PathVariable Long internshipId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {

        PageRequest pageable = PageRequest.of(page, size);

        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(applicationService.getByInternshipIdAndStatus(internshipId, status, pageable));
        }
        return ResponseEntity.ok(applicationService.getByInternshipId(internshipId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getById(@PathVariable Long id) {
        return applicationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/student/{studentId}/internship/{internshipId}")
    public ResponseEntity<Application> apply(@PathVariable Long studentId,
                                              @PathVariable Long internshipId) {
        return ResponseEntity.ok(applicationService.apply(studentId, internshipId));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Application> accept(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.updateStatus(id, "ACCEPTED"));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Application> reject(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.updateStatus(id, "REJECTED"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
