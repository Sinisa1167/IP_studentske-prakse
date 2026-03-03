package ba.etf.prakse.controller;

import ba.etf.prakse.model.Evaluation;
import ba.etf.prakse.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Evaluation>> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(evaluationService.getByApplicationId(applicationId));
    }

    @PostMapping("/application/{applicationId}")
    public ResponseEntity<Evaluation> create(@PathVariable Long applicationId,
                                              @RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.save(applicationId, evaluation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> update(@PathVariable Long id,
                                              @RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.update(id, evaluation));
    }
}
