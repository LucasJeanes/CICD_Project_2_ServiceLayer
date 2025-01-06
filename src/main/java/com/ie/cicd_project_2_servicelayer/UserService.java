package com.ie.cicd_project_2_servicelayer;

import com.ie.cicd_project_2_servicelayer.repository.NotificationLayerClient;
import com.ie.cicd_project_2_servicelayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final NotificationLayerClient notificationLayerClient;

    @Autowired
    public UserService(UserRepository userRepository,
                       NotificationLayerClient notificationLayerClient) {
        this.userRepository = userRepository;
        this.notificationLayerClient = notificationLayerClient;
    }

    public String registerUser(User user) {
        User savedUser = userRepository.save(user);
        return notificationLayerClient.notifyUserCreated(savedUser);
    }

    public String getAllUsers() {
        List<User> users = userRepository.findAll();
        return notificationLayerClient.notifyAllUsers(users);
    }

    public String getUserById(Long id) {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return notificationLayerClient.notifyUserById(userById);
    }

    public String updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());

        User updatedUser = userRepository.save(user);
        return notificationLayerClient.notifyUserUpdated(updatedUser);
    }

    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return notificationLayerClient.notifyUserDeleted(id);
    }
}
