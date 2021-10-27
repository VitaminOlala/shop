package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//Khai bao
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());  // Sét đặt dịch vụ để tìm kiếm User trong Database
		authProvider.setPasswordEncoder(passwordEncoder()); //spring security xac dinh Pass da dc encode o day la pass;
		
		return authProvider;
		
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    // Cấu hình concurrent session
//	    http.sessionManagement().sessionFixation().newSession()
//	    .invalidSessionUrl("/login?message=timeout")
//	    .maximumSessions(1).expiredUrl("/login?message=max_session").maxSessionsPreventsLogin(true);
		// Cau hinh cho form login

//		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/users/**").hasAuthority("Admin")
			.antMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
			.anyRequest().authenticated()
			.and()
			.formLogin()
//			.loginProcessingUrl("/j_spring_security_login")//
			.loginPage("/login").permitAll()
			.usernameParameter("email") 
			
			.permitAll()  
			
			
			.and().logout().logoutUrl("/j_spring_security_logout").logoutSuccessUrl("/login?message=logout")
		.and().rememberMe().key("AbcDefgHijKlmnOpqrs_1234567890")  
				.tokenValiditySeconds(30);
				//Set time tồn tai cho token
//				.and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	;			
//				http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}


	
}
