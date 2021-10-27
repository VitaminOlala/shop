package com.shopme.admin.apiresponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.ResponseError;

@Component
public class ApiResponse {
	ResponseError responseError;
	
    public ResponseEntity<?> errorResponse(String message, HttpStatus httpStatus, boolean success){
        responseError = new ResponseError(success, message);
        return new ResponseEntity<>(responseError, httpStatus);
    }
}
