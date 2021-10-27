
package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager; //So huu cac cau query (Do bang thu 3 dc tao boi 2 bang)
 	
	@Test  //dung trong jnuit test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userTuanNVA = new User("tuandonald99@gmail.com", "tuan123", "Tuan", "Nguyen Vu Anh");
		userTuanNVA.addRole(roleAdmin);
		
		User savedUser = repo.save(userTuanNVA);  //implement spring data JPA trong Runtime
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test  //dung trong jnuit test
	public void testCreateUserWithTwoRole() {
		Role roleEditor = new Role(3);
		Role roleAssitant = new Role(5);
		User userPhongHL = new User("hieuphong99@gmail.com", "phong123", "Tuan", "Nguyen Vu Anh");
		userPhongHL.addRole(roleEditor);
		userPhongHL.addRole(roleAssitant);
		
		User savedUser = repo.save(userPhongHL);  //implement spring data JPA trong Runtime
		
		assertThat(savedUser.getId()).isGreaterThan(0); //la 1 api
	}
	
	@Test
	public void testListAllUsers() {
		List<User> listUsers = (List<User>) repo.findAll();
		listUsers.forEach(user -> System.out.print(user));	
//		assertThat(listUsers.s)
	}
	
	@Test
	public void testGetUserById() {
		User userTuan = repo.findById(1).get();
		System.out.print(userTuan);
		assertThat(userTuan).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetail() {
		User userTuan = repo.findById(1).get();
		userTuan.setEnable(true);
		userTuan.setEmail("anhtuan2641999@gmail.com");
		
		repo.save(userTuan);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userPhong = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		userPhong.getRoles().remove(roleEditor);
		userPhong.addRole(roleSalesperson);
		
		repo.save(userPhong);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "tuandonald929@gmail.com";
		User user = repo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 3;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
		
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "Minh";
		int pageNumber = 0;
		int pageSize = 3;		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
	
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	
}
