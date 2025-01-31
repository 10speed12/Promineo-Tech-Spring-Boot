package pet.store.controller.error;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String,String> handleNoSuchElementException(
			NoSuchElementException ex){
		Map<String,String> map = new HashMap<String,String>();
		log.info("NoSuchElementException occured.");
		map.put("message",ex.toString());
		return map;
	}
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	public Map<String,String> handleUnsupportedOperationException(UnsupportedOperationException ex) {
		Map<String,String> map = new HashMap<String,String>();
		log.info("UnsupportedOperationException occured.");
		map.put("message",ex.toString());
		return map;
	}
	/*
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String,String> handleException(Exception ex) {
		Map<String,String> map = new HashMap<String,String>();
		log.info("Unchecked Exception occured.");
		map.put("message",ex.toString());
		return map;
	}
	*/
}
