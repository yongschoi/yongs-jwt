package yongs.temp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yongs.temp.service.TokenService;
import yongs.temp.vo.User;

@RestController
@RequestMapping("/jwt")
public class JwtTokenController {
	private Logger logger = LoggerFactory.getLogger(JwtTokenController.class);
	@Autowired
    private TokenService service;
   
	// access-token을 리턴한다.
	@PostMapping("/create") 
    public String create(@RequestBody User user) {
    	logger.debug("yongs-jwt|JwtTokenController|create({})", user);
    	return service.create(user);
    }

	// access-token을 리턴한다. but refresh-token이 유효하지 않으면 "REFRESH-TOKEN-NOT-VALID" 리턴  
	@PostMapping("/recreate") 
    public ResponseEntity<String> recreate(@RequestBody User user) {
    	logger.debug("yongs-jwt|JwtTokenController|recreate({})", user);
    	HttpStatus status = null;
    	String newAccessToken = service.recreate(user);
    	if(!newAccessToken.equals("REFRESH-TOKEN-NOT-VALID")) {
    		status = HttpStatus.OK;
    		return new ResponseEntity<String>(newAccessToken, status);
    	} else {
    		status = HttpStatus.UNAUTHORIZED;
    		return new ResponseEntity<String>(status);
    	}
    }
}
