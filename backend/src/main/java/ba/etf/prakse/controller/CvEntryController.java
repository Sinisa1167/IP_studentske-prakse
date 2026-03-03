package ba.etf.prakse.controller;

import ba.etf.prakse.model.CvEntry;
import ba.etf.prakse.service.CvEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
public class CvEntryController {

    private final CvEntryService cvEntryService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CvEntry>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(cvEntryService.getByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/type/{type}")
    public ResponseEntity<List<CvEntry>> getByStudentAndType(@PathVariable Long studentId,
                                                              @PathVariable String type) {
        return ResponseEntity.ok(cvEntryService.getByStudentIdAndType(studentId, type));
    }

    @PostMapping("/student/{studentId}")
    public ResponseEntity<CvEntry> create(@PathVariable Long studentId,
                                           @RequestBody CvEntry entry) {
        return ResponseEntity.ok(cvEntryService.create(studentId, entry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CvEntry> update(@PathVariable Long id, @RequestBody CvEntry entry) {
        return ResponseEntity.ok(cvEntryService.update(id, entry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cvEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
