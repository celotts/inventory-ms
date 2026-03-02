package com.celotts.authservice.infrastructure.controller;

import com.celotts.authservice.application.jwt.JwtUtils;
import com.celotts.authservice.application.service.UserDetailsImpl;
import com.celotts.authservice.domain.exception.EmailAlreadyExistsException;
import com.celotts.authservice.domain.exception.RoleNotFoundException;
import com.celotts.authservice.domain.exception.UsernameAlreadyExistsException;
import com.celotts.authservice.domain.model.ERole;
import com.celotts.authservice.domain.model.Role;
import com.celotts.authservice.domain.model.User;
import com.celotts.authservice.domain.repository.RoleRepository;
import com.celotts.authservice.domain.repository.UserRepository;
import com.celotts.authservice.infrastructure.common.ApiResult;
import com.celotts.authservice.infrastructure.dto.JwtResponse;
import com.celotts.authservice.infrastructure.dto.LoginRequest;
import com.celotts.authservice.infrastructure.dto.MessageResponse;
import com.celotts.authservice.infrastructure.dto.SignupRequest;
import com.celotts.authservice.infrastructure.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "${swagger.auth.api.name}", description = "${swagger.auth.api.desc}")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final MessageSource messageSource;

    @Operation(summary = "${swagger.auth.login.summary}", description = "${swagger.auth.login.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResult<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);

        return ApiResult.toResponseEntity(
                jwtResponse, 
                "Login successful", 
                HttpStatus.OK
        );
    }

    @Operation(summary = "${swagger.auth.register.summary}", description = "${swagger.auth.register.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Username or Email already in use", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResult<UserResponseDto>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(signUpRequest.getUsername());
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExistsException(signUpRequest.getEmail());
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleNotFoundException("ROLE_ADMIN"));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RoleNotFoundException("ROLE_MODERATOR"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        String successMessage = messageSource.getMessage("auth.register.success", null, LocaleContextHolder.getLocale());
        
        UserResponseDto responseDto = new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet())
        );

        return ApiResult.toResponseEntity(
                responseDto, 
                successMessage, 
                HttpStatus.CREATED
        );
    }
}
