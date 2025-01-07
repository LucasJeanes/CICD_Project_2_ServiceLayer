package com.ie.cicd_project_2_servicelayer;

import com.ie.cicd_project_2_servicelayer.repository.NotificationLayerClient;
import com.ie.cicd_project_2_servicelayer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationLayerClient notificationLayerClient;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void openMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        User user = new User(1L,
                "test_user",
                "test@example.com",
                "testPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(notificationLayerClient.notifyUserCreated(any(User.class)))
                .thenReturn("User " + user.getUsername()
                        + " registered successfully. ID: " + user.getId());

        String result = userService.registerUser(user);

        assertEquals("User test_user registered successfully. ID: 1", result);

        verify(userRepository, times(1)).save(user);
        verify(notificationLayerClient, times(1)). notifyUserCreated(user);
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(notificationLayerClient.notifyAllUsers(any())).thenReturn("Retrieved all 1 users. \n"
                + Collections.singletonList(user));

        String result = userService.getAllUsers();

        assertEquals("Retrieved all 1 users. \n" + Collections.singletonList(user), result);
        verify(userRepository, times(1)).findAll();
        verify(notificationLayerClient, times(1)).notifyAllUsers(Collections.singletonList(user));
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(notificationLayerClient.notifyUserById(any(User.class)))
                    .thenReturn("Retrieved user with ID: 1\n" + user);

        String result = userService.getUserById(1L);

        assertEquals("Retrieved user with ID: 1\n" + user, result);
        verify(userRepository, times(1)).findById(1L);
        verify(notificationLayerClient, times(1)).notifyUserById(user);
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User(1L,
                "test_user",
                "test@example.com",
                "testPassword");

        User updatedUser = new User();
        updatedUser.setUsername("testUpdated_user");
        updatedUser.setEmail("updatedTest@example.com");
        updatedUser.setPassword("newTestPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            existingUser.setUsername(savedUser.getUsername());
            existingUser.setEmail(savedUser.getEmail());
            existingUser.setPassword(savedUser.getPassword());
            return existingUser;
        });

        when(notificationLayerClient.notifyUserUpdated(any(User.class)))
                .thenAnswer(invocation -> {
                    User notifiedUser = invocation.getArgument(0);
                    return "User with ID: " + notifiedUser.getId() + "\nUpdated: " + notifiedUser;
                });

        String result = userService.updateUser(1L, updatedUser);

        assertEquals("User with ID: " + existingUser.getId()
                + "\nUpdated: " + existingUser, result);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(notificationLayerClient, times(1)).notifyUserUpdated(existingUser);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        when(notificationLayerClient.notifyUserDeleted(userId))
                .thenReturn("User with ID: " + userId + " deleted successfully");

        String result = userService.deleteUser(userId);

        assertEquals("User with ID: " + userId + " deleted successfully", result);

        verify(userRepository, times(1)).deleteById(userId);
        verify(notificationLayerClient, times(1)).notifyUserDeleted(userId);
    }
}
