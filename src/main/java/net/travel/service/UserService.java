package net.travel.service;

import net.travel.config.security.CurrentUser;
import net.travel.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    boolean existsByEmail(String email);

    void add(User user, MultipartFile image);

    Optional<User> getByEmail(String email);

    void update(User user);

    boolean isNotNull(CurrentUser currentUser);

    void deleteUserImage(CurrentUser currentUser);

    String changeUserImage(CurrentUser currentUser,MultipartFile multipartFile);
}