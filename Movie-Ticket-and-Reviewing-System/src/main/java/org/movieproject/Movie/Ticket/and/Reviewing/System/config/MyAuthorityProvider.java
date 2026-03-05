package org.movieproject.Movie.Ticket.and.Reviewing.System.config;

import org.movieproject.Movie.Ticket.and.Reviewing.System.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MyAuthorityProvider implements AuthenticationProvider {

    /**
     * Authentication here is giving the details of creds entered by user
     */

    @Autowired
    UserAuthService myUserService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String userPassword = authentication.getCredentials().toString();
        UserDetails myDbUser = myUserService.loadUserByUsername(username);
        if(myDbUser!=null && passwordEncoder.matches(userPassword,myDbUser.getPassword()))
            return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), myDbUser.getAuthorities());
        throw new BadCredentialsException("Invalid Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
