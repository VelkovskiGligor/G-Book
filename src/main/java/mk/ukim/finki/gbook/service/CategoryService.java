package mk.ukim.finki.gbook.service;

import mk.ukim.finki.gbook.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
}
