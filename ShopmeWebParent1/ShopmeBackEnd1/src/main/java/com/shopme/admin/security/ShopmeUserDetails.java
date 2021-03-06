package com.shopme.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

//Principal
public class ShopmeUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

	public ShopmeUserDetails(User user) {
		this.user = user;
	}

	//Phan quyen cho cac Role  //goi bang principal. se return ve 1 list Role Name 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authories = new ArrayList<>();
		
		for(Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authories;
	}
	//Lay pass tu db (xac dinh la pass)
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {   
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	//tai khoan ko bi het han
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnable();
	}

	public String getFullname() { 
		return this.user.getFirstName() + " " + this.user.getLastName();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}
}
