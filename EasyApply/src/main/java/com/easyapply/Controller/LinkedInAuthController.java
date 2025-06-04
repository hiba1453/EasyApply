package com.easyapply.Controller;

import com.easyapply.Service.AuthService;
import com.easyapply.Service.JwtService;
import com.easyapply.entity.Candidat;
import com.easyapply.Repository.CandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/linkedin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LinkedInAuthController {

    @Value("${linkedin.client-id}")
    private String clientId;

    @Value("${linkedin.client-secret}")
    private String clientSecret;

    @Value("${linkedin.redirect-uri}")
    private String redirectUri;

    @Value("${linkedin.token-url}")
    private String tokenUrl;

    @Value("${linkedin.user-info-url}")
    private String userInfoUrl;

    @Autowired
    private CandidatRepository candidatRepository;

    private final AuthService authService;
    private final JwtService jwtService;

    public LinkedInAuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleLinkedInCallback(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state) {
        try {
            // Step 1: Exchange authorization code for access token
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("code", code);
            body.add("redirect_uri", redirectUri);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", "http://localhost:5173/login?error=" + URLEncoder.encode("Échec de l'obtention du jeton d'accès", StandardCharsets.UTF_8.toString()))
                        .build();
            }

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            // Step 2: Fetch user info from LinkedIn
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

            if (!userInfoResponse.getStatusCode().is2xxSuccessful() || userInfoResponse.getBody() == null) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", "http://localhost:5173/login?error=" + URLEncoder.encode("Échec de la récupération des informations utilisateur", StandardCharsets.UTF_8.toString()))
                        .build();
            }

            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            String givenName = (String) userInfo.get("given_name");
            String familyName = (String) userInfo.get("family_name");
            String linkedInId = (String) userInfo.get("sub");

            // Step 3: Check if user exists in your database, or create a new one
            Candidat candidat = candidatRepository.findByEmail(email).orElse(null);
            if (candidat == null) {
                candidat = new Candidat();
                candidat.setEmail(email);
                candidat.setNom(givenName + " " + familyName);
                candidat.setLinkedinToken(linkedInId);
                candidat.setMotDePasse("linkedin-auth-" + linkedInId);
                candidat = candidatRepository.save(candidat);
            }

            // Step 4: Generate JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", candidat.getId());
            claims.put("email", email);
            claims.put("role", "CANDIDAT");

            String jwtToken = jwtService.generateToken(claims, email);

            // Step 5: Set JWT in an HttpOnly cookie and redirect to dashboard
            ResponseCookie cookie = ResponseCookie.from("jwtToken", jwtToken)
                    .httpOnly(true)
                    .secure(false) // Set to true in production with HTTPS
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 days expiration for persistence
                    .build();

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Set-Cookie", cookie.toString())
                    .header("Location", "http://localhost:5173/dashboard/candidate/jobs")
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:5173/login?error=" + URLEncoder.encode("Erreur lors de l'authentification LinkedIn: " + e.getMessage(), StandardCharsets.UTF_8))
                    .build();
        }
    }
}