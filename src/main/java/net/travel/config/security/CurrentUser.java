package net.travel.config.security;

import lombok.Getter;
import lombok.Setter;
import net.travel.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    @Setter
    @Getter
    private User user;

    public CurrentUser(User user, boolean isActive) {
        super(user.getEmail(), user.getPassword(), true, true, true, isActive, AuthorityUtils.createAuthorityList(user.getUserType().name()));
        this.user = user;
    }
}