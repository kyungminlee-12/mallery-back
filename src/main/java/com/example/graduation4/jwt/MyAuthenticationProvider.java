package com.example.graduation4.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider {

    // @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginId = (String) authentication.getPrincipal();
        String loginPass = (String) authentication.getCredentials();

        System.out.println(authentication);

        //(principal, credentials, authorities)
        return new UsernamePasswordAuthenticationToken(loginId, loginPass, null);
    }

    // @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
