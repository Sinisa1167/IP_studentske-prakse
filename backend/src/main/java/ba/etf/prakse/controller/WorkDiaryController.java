package ba.etf.prakse.controller;

import ba.etf.prakse.model.WorkDiary;
import ba.etf.prakse.service.WorkDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class WorkDiaryController {

    private final WorkDiaryService workDiaryService;

    @GetMapping("/student/{studentId}/internship/{internshipId}")
    public ResponseEntity<List<WorkDiary>> getEntries(@PathVariable Long studentId,
                                                       @PathVariable Long internshipId) {
        return ResponseEntity.ok(workDiaryService.getByStudentAndInternship(studentId, internshipId));
    }

    @PostMapping("/student/{studentId}/internship/{internshipId}")
    public ResponseEntity<WorkDiary> create(@PathVariable Long studentId,
                                             @PathVariable Long internshipId,
                                             @RequestBody WorkDiary entry) {
        return ResponseEntity.ok(workDiaryService.save(studentId, internshipId, entry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkDiary> update(@PathVariable Long id,
                                             @RequestBody WorkDiary entry) {
        return ResponseEntity.ok(workDiaryService.update(id, entry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workDiaryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
