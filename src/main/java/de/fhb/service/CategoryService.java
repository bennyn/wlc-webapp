package de.fhb.service;

import de.fhb.repository.CategoryRepository;
import de.fhb.entities.Category;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class CategoryService {

  @EJB
  private CategoryRepository repository;

  public CategoryService() {
  }

  public List<Category> getCategories() {
    return repository.findAll();
  }

  public List<Category> getCategoriesOrderedByTitle() {
    return repository.getCategoriesOrderedByTitle();
  }

//  public String getUrlSlug(Category category) {
//    return Slugify.slugify(category.getTitle());
//  }
}
