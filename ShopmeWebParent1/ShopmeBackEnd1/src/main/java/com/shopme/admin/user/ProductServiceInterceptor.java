package com.shopme.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ProductServiceInterceptor implements HandlerInterceptor{
//	@Override
//	   public boolean preHandle(
//	      HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//			System.out.println("Request URL: " + request.getRequestURL());
//			System.out.println("Start Time: "+System.currentTimeMillis());
//			return true;
//	   }
//	   @Override
//	   public void postHandle(
//	      HttpServletRequest request, HttpServletResponse response, Object handler, 
//	      ModelAndView modelAndView) throws Exception {
//		   
//	        System.out.println("Doan chay 1");
//	      
//	   }
//	   
//	   @Override
//	   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
//	      Object handler, Exception exception) throws Exception {
//		   
//	    
//	        System.out.println("Doan chay 2");
//	    
//	   }
}
