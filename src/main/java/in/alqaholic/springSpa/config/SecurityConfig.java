package in.alqaholic.springSpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import in.alqaholic.springSpa.TokenService;

@Configuration
public class SecurityConfig {

    private final TokenService tokenService;

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN))
                            .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
                })
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers("/api/token").permitAll()
                            .pathMatchers("/api/**").authenticated()
                            .anyExchange().permitAll();
                })
                .oauth2ResourceServer(oauthResourceServer -> {
                    oauthResourceServer
                            .jwt(Customizer.withDefaults());
                })
                .oauth2Login(oauthLogin -> {
                    oauthLogin.authenticationSuccessHandler((webFilterExchange, authentication) -> {
                        ServerWebExchange exchange = webFilterExchange.getExchange();
                        String token = tokenService.generateToken(authentication);

                        // Handle URL redirect here
                        // The token: ?token=TOKEN
                        return new DefaultServerRedirectStrategy()
                                .sendRedirect(exchange,
                                        UriComponentsBuilder.fromPath("/")
                                                .queryParam("token", token).build()
                                                .toUri());
                    });
                })
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .build();
    }
}
