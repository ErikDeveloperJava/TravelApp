package net.travel.service.impl;

import net.travel.config.security.CurrentUser;
import net.travel.model.User;
import net.travel.repository.UserRepository;
import net.travel.service.UserService;
import net.travel.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUtil imageUtil;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void add(User user, MultipartFile image) {
        userRepository.save(user);
        if(image != null && !image.isEmpty()){
            String userDir = "user/" +  String.valueOf(user.getId());
            imageUtil.createDir(userDir);
            String imageName = userDir + "/" + System.currentTimeMillis() + image.getOriginalFilename().substring(image.getOriginalFilename().length() - 5);
            try {
                imageUtil.save(imageName,image);
                user.setImgUrl(imageName);
                userRepository.flush();
            }catch (RuntimeException e){
                imageUtil.delete(userDir);
                throw e;
            }
        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isNotNull(CurrentUser currentUser) {
        return currentUser != null;
    }

    @Transactional
    public void deleteUserImage(CurrentUser currentUser) {
        User user = userRepository.findById(currentUser.getUser().getId()).get();
        imageUtil.delete("user/" + user.getId());
        user.setImgUrl(null);
        userRepository.flush();
        currentUser.setUser(user);
    }

    @Transactional
    public String changeUserImage(CurrentUser currentUser, MultipartFile multipartFile) {
        User user = userRepository.findById(currentUser.getUser().getId()).get();
        if(user.getImgUrl() != null){
            imageUtil.delete(user.getImgUrl());
        }
        String dir = "user/" + user.getId();
        imageUtil.createDir(dir);
        String imageName = dir + "/" + System.currentTimeMillis() +
                multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().length() - 5);
        try {
            imageUtil.save(imageName,multipartFile);
            user.setImgUrl(imageName);
            currentUser.setUser(user);
            userRepository.flush();
        }catch (RuntimeException e){
            imageUtil.delete(dir);
            throw e;
        }
        return imageName;
    }
}