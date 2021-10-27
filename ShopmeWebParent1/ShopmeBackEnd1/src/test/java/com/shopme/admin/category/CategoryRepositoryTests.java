package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

//Muon show ra cau query trong console kk
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)   //Thuc hien trong bang db thực
@Rollback(false)    //Giữ duy trì data trong db
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repo;
	
	//Test creating top-level(root) categories
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	//Test creating sub categories
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(5);
		Category subCategory = new Category("Memory", parent);
//		Category components = new Category("Smartphones", parent);
		Category savedCategory = repo.save(subCategory);
//		Category savedCategory = repo.save(subCategory);
//		repo.saveAll(List.of(subCategory, components));
//		repo.saveAll(List.of(cameras, smartphones));
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	//Test getting a category and its children
	@Test
	public void testGetCategory() {
		Category category = repo.findById(1).get();
		System.out.println(category.getName());
		
		Set<Category> children = category.getChildren();
		
		for(Category subCategory : children) {
			System.out.println(subCategory.getName());
		}
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repo.findAll();
		
		for(Category category : categories) {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildren();
				
				for(Category subCategory : children) {
					System.out.println("--" +subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for(Category subCategory : children) {
			for(int i = 0; i < newSubLevel; i++) {
				System.out.println("--");
			}
			
			System.out.println(subCategory.getName());
			
			printChildren(subCategory, newSubLevel);
		}
		
	}
}
