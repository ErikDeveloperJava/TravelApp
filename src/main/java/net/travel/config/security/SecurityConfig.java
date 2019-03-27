package net.travel.config.security;

import net.travel.model.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/login","/register","/email-confirm/*/*","/forgot_password","/forgot_password/**")
                .anonymous()
                .antMatchers(HttpMethod.POST,"/register","/forgot_password","/forgot_password/**")
                .anonymous()
                .antMatchers("/region",
                        "/city/by/regionId/*","/hotels/search","/hotels",
                        "/places","/places/search","/hotel/detail/*","/hotel/room/id/*",
                        "/hotel/room/detail/*","/place/detail/*")
                .hasAnyAuthority("ROLE_ANONYMOUS",UserType.USER.name())
                .antMatchers("/admin","/admin/**")
                .hasAuthority(UserType.ADMIN.name())
                .antMatchers("/user/wish_list","/user/wish_list/*","/user/detail",
                        "/user/image/delete","/user/image/change",
                        "/model/review","/order","/order/**","/user/booking")
                .hasAuthority(UserType.USER.name())
        .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureHandler((request, response, e) -> {
                    if(e instanceof LockedException){
                        response.sendRedirect("/login?email-inactive");
                    }else {
                        response.sendRedirect("/login?data-error");
                    }
                })
                .usernameParameter("email")
        .and()
                .rememberMe()
                .userDetailsService(userDetailsService)
                .rememberMeCookieName("RM")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(100000000)
        .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("RM")
                .invalidateHttpSession(true)
        .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, e) -> response.sendRedirect("/"))
        .and()
                .csrf()
                .disable();
    }
}
