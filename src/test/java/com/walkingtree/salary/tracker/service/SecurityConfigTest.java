//package com.walkingtree.salary.tracker.service;
//
//import com.walkingtree.salary.tracker.model.AppUser;
//import com.walkingtree.salary.tracker.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class SecurityConfigTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private SecurityConfig securityConfig;
//
//    private DefaultOAuth2UserService delegate;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        delegate = mock(DefaultOAuth2UserService.class);
//    }
//
//    @Test
//    public void testOauth2UserService_NewUser() throws Exception {
//        OAuth2UserRequest request = mock(OAuth2UserRequest.class);
//
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("email", "newuser@example.com");
//        attributes.put("name", "New User");
//        attributes.put("sub", "12345");
//
//        OAuth2User oauth2User = new DefaultOAuth2User(
//                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
//                attributes,
//                "sub"
//        );
//
//        // Delegate returns oauth2User
//        when(delegate.loadUser(request)).thenReturn(oauth2User);
//
//        // UserRepository returns empty - new user scenario
//        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
//
//        // UserRepository.save returns new AppUser
//        AppUser savedUser = new AppUser();
//        savedUser.setEmail("newuser@example.com");
//        savedUser.setName("New User");
//        savedUser.getRoles().add("ROLE_USER");
//
//        when(userRepository.save(any(AppUser.class))).thenReturn(savedUser);
//
//        // Use the actual oauth2UserService from securityConfig
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = securityConfig.oauth2UserService();
//
//        OAuth2User resultUser = oauth2UserService.loadUser(request);
//
//        assertNotNull(resultUser);
//        assertTrue(resultUser.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
//        verify(userRepository).save(any(AppUser.class));
//    }
//
//    @Test
//    public void testOauth2UserService_ExistingUser() throws Exception {
//        OAuth2UserRequest request = mock(OAuth2UserRequest.class);
//
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("email", "existing@example.com");
//        attributes.put("name", "Existing User");
//        attributes.put("sub", "abcde");
//
//        OAuth2User oauth2User = new DefaultOAuth2User(
//                Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")),
//                attributes,
//                "sub"
//        );
//
//        when(delegate.loadUser(request)).thenReturn(oauth2User);
//
//        AppUser existingUser = new AppUser();
//        existingUser.setEmail("existing@example.com");
//        existingUser.setName("Existing User");
//        existingUser.getRoles().add("ROLE_ADMIN");
//
//        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));
//
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = securityConfig.oauth2UserService();
//
//        OAuth2User resultUser = oauth2UserService.loadUser(request);
//
//        assertNotNull(resultUser);
//        assertTrue(resultUser.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
//        verify(userRepository, never()).save(any(AppUser.class));
//    }
//}
//
