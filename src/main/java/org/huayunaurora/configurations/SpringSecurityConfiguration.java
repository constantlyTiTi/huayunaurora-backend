package org.huayunaurora.configurations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.huayunaurora.loginAndRegistration.services.AccountAuthenticationProvider;
import org.huayunaurora.loginAndRegistration.services.AccountDetailsService;
import org.huayunaurora.loginAndRegistration.services.JwtAthFilter;
import org.huayunaurora.loginAndRegistration.services.Oauth2OidcAccountService;
import org.huayunaurora.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SpringSecurityConfiguration {
    private final AccountAuthenticationProvider accountAuthenticationProvider;
    private final JwtAthFilter jwtAthFilter;
    private final Oauth2OidcAccountService oauth2AccountService;
    private final JwtUtils jwtUtils;
    private final String AUTHORIZATION_HEADER = "Authorization";

//    @Value("${auth.login.front-end-url}")
//    private String loginFrontEndUrl;
//
//    @Value("${auth.login.endpoint}")
//    private String loginEndPoint;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, AccountDetailsService userDetailService)
            throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(accountAuthenticationProvider)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/graphql").permitAll()
                        .anyRequest()
                        .authenticated())
//                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login((oauth2Login) -> oauth2Login
                        .userInfoEndpoint((userInfo) -> userInfo
                                .oidcUserService(this.oauth2AccountService))
                        .authorizationEndpoint(endPoint -> endPoint.authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository()))
                        .successHandler(oidcAuthenticationSuccessHandler()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .formLogin(fl -> fl.successHandler(authenticationSuccessHandler())
                        .failureUrl("/login.html?error=true"))
                .rememberMe(rm -> rm.key("uniqueAndSecret").tokenValiditySeconds(86400))
                .logout(logout -> logout.deleteCookies("JSESSIONID"))
                .addFilterBefore(jwtAthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            log.info("onAuthenticationSuccess pending to redirect to different page logic");
//            response.setHeader(AUTHORIZATION_HEADER, "Bearer " + jwtUtils.generateToken(userDetails));
//            redirectHandler.onAuthenticationSuccess(request,response,authentication);

            SavedRequestAwareAuthenticationSuccessHandler redirectHandler = new SavedRequestAwareAuthenticationSuccessHandler(){
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer " + jwtUtils.generateToken(userDetails));
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            };
            redirectHandler.setUseReferer(false);
            redirectHandler.setDefaultTargetUrl("/loginSuccess");

            log.info("onAuthenticationSuccess pending to redirect to different page logic" );

            redirectHandler.onAuthenticationSuccess(request,response,authentication);
        };
    }

    @Bean
    public AuthenticationSuccessHandler oidcAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            SavedRequestAwareAuthenticationSuccessHandler redirectHandler = new SavedRequestAwareAuthenticationSuccessHandler(){
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    OidcUser oidUserDetails = (OidcUser) authentication.getPrincipal();
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer " + jwtUtils.generateTokenByOidcUser(oidUserDetails));
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            };
//            redirectHandler.setUseReferer(false);
//            redirectHandler.setDefaultTargetUrl("/loginSuccess");
//            redirectHandler.

            log.info("OIDC onAuthenticationSuccess pending to redirect to different page logic: " );

            redirectHandler.onAuthenticationSuccess(request,response,authentication);
        };
    }


//    @Bean
//    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
//        return (authorities) -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//            authorities.forEach((authority) -> {
//                GrantedAuthority mappedAuthority;
//
//                if (authority instanceof OidcUserAuthority) {
//                    OidcUserAuthority userAuthority = (OidcUserAuthority) authority;
//                    mappedAuthority = new OidcUserAuthority(
//                            "OIDC_USER", userAuthority.getIdToken(), userAuthority.getUserInfo());
//                } else if (authority instanceof OAuth2UserAuthority) {
//                    OAuth2UserAuthority userAuthority = (OAuth2UserAuthority) authority;
//                    mappedAuthority = new OAuth2UserAuthority(
//                            "OAUTH2_USER", userAuthority.getAttributes());
//                } else {
//                    mappedAuthority = authority;
//                }
//
//                mappedAuthorities.add(mappedAuthority);
//            });
//
//            return mappedAuthorities;
//        };
//    }

//    @Bean
//    public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
//                               OAuth2AuthorizedClientRepository authorizedClientRepository) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
//                        authorizedClientRepository);
//        oauth2.setDefaultOAuth2AuthorizedClient(true);
//        return WebClient.builder()
//                .apply(oauth2.oauth2Configuration())
//                .build();
//    }

}

