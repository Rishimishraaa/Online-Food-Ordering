package com.nomzila.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomzila.config.JwtProvider;
import com.nomzila.model.Cart;
import com.nomzila.model.USER_ROLE;
import com.nomzila.model.User;
import com.nomzila.repository.CartRepository;
import com.nomzila.repository.UserRepository;
import com.nomzila.request.LoginRequest;
import com.nomzila.response.AuthResponse;
import com.nomzila.service.CustomeUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private CustomeUserDetailsService userDetailsService;
	@Autowired
	private CartRepository cartRepository;
	
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception{
		
		User isUserExists = userRepository.findByEmail(user.getEmail());
		
		if(isUserExists!=null) {
			throw new Exception("Email is already exists");
		}
		
		
		
		 User newUser = User.builder().fullName(user.getFullName())
				         .email(user.getEmail())
				         .password(encoder.encode(user.getPassword()))
				         .role(user.getRole())
				         .build();
		 
		
		User saveUser= userRepository.save(newUser);
		
		Cart cart = new Cart();
		cart.setCustomer(saveUser);
		cartRepository.save(cart);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register Success");
		authResponse.setRole(saveUser.getRole());
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin (@RequestBody LoginRequest loginRequest){
		
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
		Authentication authentication = authenticate(username,password);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String role = authorities.isEmpty()?null : authorities.iterator().next().getAuthority();
		
	String jwt = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Login Success");
			authResponse.setRole(USER_ROLE.valueOf(role));
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);

	}


	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid Username");
		}
		
		if(!encoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				}
}
