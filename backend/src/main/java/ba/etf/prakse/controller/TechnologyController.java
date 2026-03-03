package ba.etf.prakse.controller;

import ba.etf.prakse.model.Technology;
import ba.etf.prakse.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyRepository technologyRepository;

    @GetMapping
    public ResponseEntity<List<Technology>> getAll() {
        return ResponseEntity.ok(technologyRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Technology> create(@RequestBody Technology technology) {
        return ResponseEntity.ok(technologyRepository.save(technology));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        technologyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
