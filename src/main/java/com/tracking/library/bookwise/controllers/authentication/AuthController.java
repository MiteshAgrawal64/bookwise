package com.tracking.library.bookwise.controllers.authentication;

import com.tracking.library.bookwise.config.jwtConfig.JwtUtil;
import com.tracking.library.bookwise.controllers.authentication.dto.AuthRequest;
import com.tracking.library.bookwise.controllers.authentication.dto.AuthResponse;
import com.tracking.library.bookwise.servicelayer.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("==> Entered login method");
        logger.info("Login attempt for user: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            logger.info("Authentication successful for user: {}", request.getUsername());

            UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails);

            logger.info("JWT token generated for user: {}", request.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", request.getUsername());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
