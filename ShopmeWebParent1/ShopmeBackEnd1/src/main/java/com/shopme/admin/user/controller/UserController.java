package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserRepository;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import feign.Param;

import org.springframework.util.StringUtils;


@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	@Autowired
	UserRepository customerRepo;

	//Khi chua phan trang
//	@GetMapping("/users")
//	public String listAll(Model model) {
//		List<User> listUser = service.listAll();
//		model.addAttribute("listUser", listUser);
//		return "users";
//	}
//	@GetMapping("/users/modelandViews")
//	public ModelAndView listAll3() {
//		List<User> listUser = service.listAll();
//		ModelAndView modelAndView = new ModelAndView("users","listUser",listUser);
//		return modelAndView;
//	}
	@PostMapping("/customers")
	public ResponseEntity<User> saveCustomer(@RequestBody User customer) {
		try {
			return new ResponseEntity<>(customerRepo.save(customer), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);  //Field trong User class, not db
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
					@Param("sortField") String sortField, @Param("sortDir") String sortDir,
					@Param("keyword") String keyword) {
//		System.out.println("Sort Field : " +sortField);
//		System.out.println("Sort Order : " +sortDir);
		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		
		List<User> listUsers = page.getContent();
		
//		System.out.println("Pagenum= " +pageNum);
//		System.out.println("Total elements= " +page.getTotalElements());
//		System.out.println("Total pages= " +page.getTotalPages());
		
		long startCount = (pageNum - 1) * UserService.USER_PER_PAGE + 1;    //Count tu dau den ng dau tien page User chon
		long endCount = startCount + UserService.USER_PER_PAGE -1 ;			//Count tu dau den ng cuoi page User chon
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
			
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUser", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "users/users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnable(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}
	
	@PostMapping("/users/save") //RequestParam: tim gia tri image trong User
	public String saveUser(User user ,RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {     //Multipart : for uploaded file
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); //Chon anh tu file máy tinh
			user.setPhotos(fileName);
			User saveUser = service.save(user);
			
			String uploadDir = "user-photos/" +saveUser.getId();
			System.out.println("Pagenum= " +multipartFile);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);			
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		

		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		
		//Sau khi edit thi hien thi moi User dc edit
		return getRedirectURLtoAffectedUser(user);
	
}
	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "users/user_form";
		}catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
			
		
	}
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);;
			
			redirectAttributes.addFlashAttribute("message", "The user ID " + id +" has been deleted successfully");

		}catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			
		}		
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){
		service.updateUserEnableStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " +id+ " has been " +status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
//	 @RequestMapping(value = { "/login" })
//	  public String login(@RequestParam(required=false) String message, final Model model) {
//	    if (message != null && !message.isEmpty()) {
//	      if (message.equals("timeout")) {
//	        model.addAttribute("message", "Time out");
//	      }
//	      if (message.equals("max_session")) {
//	        model.addAttribute("message", "This accout has been login from another device!");
//	      }
//	      if (message.equals("logout")) {
//	        model.addAttribute("message", "Logout!");
//	      }
//	      if (message.equals("error")) {
//	        model.addAttribute("message", "Login Failed!");
//	      }
//	    }
//	    return "login";
//	  }
}
