package com.pmp.noteapp.exception;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pmp.noteapp.dto.ApiResponse;
import com.pmp.noteapp.utils.NoteAppConstants;

@RestControllerAdvice
class NoteAppControllerAdvice {

	
	
	@ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<?>  handleNoSuchElementException(final NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.noDataFound());
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?>  handleInvalidArguementException(MethodArgumentNotValidException e){
		String errorMsg = e.getBindingResult().getFieldErrors().stream()
							.map(fe->new StringBuilder(fe.getField())
														.append(NoteAppConstants.SPACE)
														.append(fe.getDefaultMessage()
														.toString()))
							.collect(Collectors.joining(","));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.response(HttpStatus.BAD_REQUEST.value(), errorMsg));

	}
	
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?>  handleInvalidArguementException(HttpMessageNotReadableException e){
		String errorMsg = e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.response(HttpStatus.BAD_REQUEST.value(), errorMsg));

	}
}
