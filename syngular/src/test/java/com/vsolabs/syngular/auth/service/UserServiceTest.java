package com.vsolabs.syngular.auth.service;

import com.vsolabs.syngular.auth.model.Role;
import com.vsolabs.syngular.auth.model.User;
import com.vsolabs.syngular.auth.repository.RoleRepository;
import com.vsolabs.syngular.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = Role.builder()
                .id(1)
                .name("USER")
                .build();

        testUser = User.builder()
                .username("user_test")
                .email("user.test@vsolabs.com")
                .passwordHash("hashedPassword123")
                .isActive(true)
                .build();
    }

    @Test
    void registerNewUser_Success() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1);
            return savedUser;
        });

        User result = userService.registerNewUser(testUser, "USER");

        assertNotNull(result.getId());
        assertEquals(testRole, result.getRole());
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void registerNewUser_RoleNotFound_ThrowsException() {

        when(roleRepository.findByName("INVALID_ROLE")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerNewUser(testUser, "INVALID_ROLE");
        });

        assertTrue(exception.getMessage().contains("no existe en la base de datos"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void existsByEmail_ReturnsTrue_WhenEmailExists() {

        when(userRepository.findByEmail("user_test@vsolabs.com")).thenReturn(Optional.of(testUser));

        boolean exists = userService.existsByEmail("user_test@vsolabs.com");

        assertTrue(exists);
        verify(userRepository, times(1)).findByEmail("user_test@vsolabs.com");
    }
}