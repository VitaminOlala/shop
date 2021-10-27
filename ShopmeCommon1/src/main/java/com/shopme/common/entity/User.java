package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String password;
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	@Column(length = 64)
	private String photos;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnable() {
		return enable;
	}

	public User() {

		// TODO Auto-generated constructor stub
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public User(String email, String password, String firstName, String lastName) {
	
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	private boolean enable;
	
	@ManyToMany(fetch = FetchType.EAGER) //Sinh ra 1 bang o giua
	@JoinTable(                                                  //Tu tao cho minh bang trung gian
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),    	  //con
			inverseJoinColumns = @JoinColumn(name = "role_id")    //cha
			) //Khi co su lien ket giua 2 bang nhung ta lai muon tach ra bang thu 3 o giua so huu khoa chinh va khoa phu
	private Set<Role> roles = new HashSet<>();
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	@Transient   //ko map voi bat cu truong nao trong db  //Lay anh mac dinh in ra trang users
	public String getPhotosImagePath() {
		if(id == null || photos == null) return "/images/guest-user.png";
		return "/user-photos/" +this.id+ "/" + this.photos;
	}
}
