package ba.etf.prakse.service;

import ba.etf.prakse.model.Company;
import ba.etf.prakse.model.User;
import ba.etf.prakse.repository.CompanyRepository;
import ba.etf.prakse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<Company> getAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public Page<Company> searchByName(String name, Pageable pageable) {
        return companyRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Optional<Company> getById(Long id) {
        return companyRepository.findById(id);
    }

    public Optional<Company> getByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    @Transactional
    public Company create(Company company, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("COMPANY");
        user.setActive(true);
        userRepository.save(user);

        company.setUser(user);
        company.setActive(true);
        return companyRepository.save(company);
    }

    @Transactional
    public Company update(Long id, Company updated) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kompanija nije pronađena"));

        company.setName(updated.getName());
        company.setAddress(updated.getAddress());
        company.setContactEmail(updated.getContactEmail());
        company.setDescription(updated.getDescription());

        return companyRepository.save(company);
    }

    @Transactional
    public Company setActive(Long id, boolean active) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kompanija nije pronađena"));

        company.setActive(active);
        company.getUser().setActive(active);
        userRepository.save(company.getUser());

        return companyRepository.save(company);
    }

    @Transactional
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }
}
