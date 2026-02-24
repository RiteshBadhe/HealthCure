package healthcare_backend.com.example.demo.service;

import healthcare_backend.com.example.demo.model.User;
import healthcare_backend.com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {
    @Autowired UserRepository userRepository;
    
    public User registerUser(User user) {
        // check existing, encode password if needed
        return userRepository.save(user);
    }
}
