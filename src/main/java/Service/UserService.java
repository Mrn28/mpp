package Service;

import Domain.User;
import Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
        logger.info("User added successfully: {}", user.getUsername());
    }

    public void updateUser(User user) {
        userRepository.update(user);
        logger.info("User updated successfully: {}", user.getUsername());
    }

    public List<User> getAllUsers() {
        logger.info("Retrieving all users...");
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        logger.info("Retrieving user with id: {}", id);
        return userRepository.findById(id);
    }

    public void deleteUserById(Integer id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public boolean login(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        return userRepository.login(username, password);
    }
}
