package ba.etf.prakse.service;

import ba.etf.prakse.model.Application;
import ba.etf.prakse.model.Evaluation;
import ba.etf.prakse.repository.ApplicationRepository;
import ba.etf.prakse.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ApplicationRepository applicationRepository;

    public List<Evaluation> getByApplicationId(Long applicationId) {
        return evaluationRepository.findByApplicationId(applicationId);
    }

    public Optional<Evaluation> getById(Long id) {
        return evaluationRepository.findById(id);
    }

    @Transactional
    public Evaluation save(Long applicationId, Evaluation evaluation) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Prijava nije pronađena"));

        // Provjeri da li ocjena već postoji za ovaj role
        evaluationRepository.findByApplicationIdAndEvaluatorRole(
                applicationId, evaluation.getEvaluatorRole())
                .ifPresent(e -> { throw new RuntimeException("Ocjena već postoji"); });

        evaluation.setApplication(application);
        return evaluationRepository.save(evaluation);
    }

    @Transactional
    public Evaluation update(Long id, Evaluation updated) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocjena nije pronađena"));
        evaluation.setGrade(updated.getGrade());
        evaluation.setComment(updated.getComment());
        return evaluationRepository.save(evaluation);
    }
}
