package com.cubanengineer.test.sharefiles.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.cubanengineer.test.sharefiles.payload.request.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cubanengineer.test.sharefiles.models.ERole;
import com.cubanengineer.test.sharefiles.models.Role;
import com.cubanengineer.test.sharefiles.models.User;
import com.cubanengineer.test.sharefiles.payload.request.LoginRequest;
import com.cubanengineer.test.sharefiles.payload.request.SignupRequest;
import com.cubanengineer.test.sharefiles.payload.response.UserInfoResponse;
import com.cubanengineer.test.sharefiles.payload.response.MessageResponse;
import com.cubanengineer.test.sharefiles.repository.RoleRepository;
import com.cubanengineer.test.sharefiles.repository.UserRepository;
import com.cubanengineer.test.sharefiles.security.jwt.JwtUtils;
import com.cubanengineer.test.sharefiles.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest ploginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(ploginRequest.getUsername(), ploginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest psignUpRequest) {
    if (userRepository.existsByUsername(psignUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(psignUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(psignUpRequest.getUsername(),
                         psignUpRequest.getEmail(),
                         encoder.encode(psignUpRequest.getPassword()));

    Set<String> strRoles = psignUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/changePassword")
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest pchangePasswordRequest) {
    if (!userRepository.existsByUsername(pchangePasswordRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: User don't exist!"));
    }

    // Create new user's account
    User user = userRepository.findByUsername(pchangePasswordRequest.getUsername()).get();
    if(user.getPassword()==pchangePasswordRequest.getCurrentPassword())
    {
      if(pchangePasswordRequest.getNewPassword()== pchangePasswordRequest.getRepeatNewPasswordPassword())
      {
        user.setPassword(encoder.encode(pchangePasswordRequest.getNewPassword()));
      }
      else {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: The new password not match!"));
      }
    }
    
    else {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: The current password it's not correct!"));
    }
    
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Password changed successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
