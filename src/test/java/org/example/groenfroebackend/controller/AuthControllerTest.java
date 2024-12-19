package org.example.groenfroebackend.controller;

import org.example.groenfroebackend.model.Enums.JobTitle;
import org.example.groenfroebackend.security.AuthenticationRequest;
import org.example.groenfroebackend.security.JwtUtil;
import org.example.groenfroebackend.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void successfulAuthenticationReturnsJWTToken() throws Exception {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest("testuser", "password");
        JobTitle jobTitle = JobTitle.GLASS_SELLER;
        UserPrincipal userPrincipal = new UserPrincipal(
                1,
                "testuser",
                "password",
                jobTitle,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateToken(userPrincipal)).thenReturn("testToken");

        // Act
        String result = authController.loginUser(authRequest);

        // Assert
        assertEquals("testToken", result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(userPrincipal);

        Authentication securityContextAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(securityContextAuth);
        assertEquals(authentication, securityContextAuth);
    }

    @Test
    void failedAuthenticationThrowsException() {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest("testuser", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authController.loginUser(authRequest));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtUtil);

    }
}