package io.mkknowledge.managerportal.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.mkknowledge.managerportal.models.ERole;
import io.mkknowledge.managerportal.models.Manager;
import io.mkknowledge.managerportal.models.Role;
import io.mkknowledge.managerportal.payload.request.LoginRequest;
import io.mkknowledge.managerportal.payload.request.SignupRequest;
import io.mkknowledge.managerportal.payload.response.JwtResponse;
import io.mkknowledge.managerportal.payload.response.MessageResponse;
import io.mkknowledge.managerportal.repository.ManagerRepository;
import io.mkknowledge.managerportal.repository.RoleRepository;

import io.mkknowledge.managerportal.security.jwt.JwtUitls;
import io.mkknowledge.managerportal.security.services.ManagerDetailsImpl;


public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	ManagerRepository managerRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUitls jwtUtils;
	
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		ManagerDetailsImpl userDetails = (ManagerDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		
		if(managerRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if(managerRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		
		// Create new Manager's account
		Manager manager = new Manager(signUpRequest.getUsername(),
										signUpRequest.getEmail(),
										signUpRequest.getFirstName(),
										signUpRequest.getLastName(),
										encoder.encode(signUpRequest.getPassword()),
										signUpRequest.getAddress(),
										signUpRequest.getDob(),
										signUpRequest.getCompany());
		
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_MANAGER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "employee":
					Role adminRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
					
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		
		manager.setRoles(roles);
		managerRepository.save(manager);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		
	}
}
