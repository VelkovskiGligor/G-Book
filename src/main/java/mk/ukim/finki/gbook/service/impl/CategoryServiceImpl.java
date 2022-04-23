package mk.ukim.finki.gbook.service.impl;

import mk.ukim.finki.gbook.models.Category;
import mk.ukim.finki.gbook.repository.jpa.CategoryRepository;
import mk.ukim.finki.gbook.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findByOrderByCategoryNameAsc();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return this.categoryRepository.findById(id);
    }
}
