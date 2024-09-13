package org.huayunaurora.loginAndRegistration.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.huayunaurora.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAuthenticationProvider implements AuthenticationProvider {

//    @Value("${auth.baseurl}")
//    private String authBaseurl;
//
//    @Value("${auth.login.endpoint}")
//    private String authLoginEndpoint;

    private final JwtUtils jwtUtils;

    private final AccountDetailsService accountDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String useNameOrEmail = authentication.getName();
        final String password = authentication.getCredentials().toString();

            var userDetails = accountDetailsService.loadUserByUsername(useNameOrEmail);
            if (!bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }
            log.info("Login Success");

            Authentication authenticationToken =new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpSession session = request.getRequest().getSession();

            String accessToken = jwtUtils.generateToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            session.setAttribute("accessToken", accessToken);
            session.setAttribute("refreshToken", refreshToken);

            Date jwtExpiration = jwtUtils.extractExpiration(accessToken);

            long sessionTimeoutInMillis = jwtExpiration.getTime() - System.currentTimeMillis();
            long sessionExpiration = sessionTimeoutInMillis / 1000;
            session.setMaxInactiveInterval((int) sessionExpiration);

            return authenticationToken;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
