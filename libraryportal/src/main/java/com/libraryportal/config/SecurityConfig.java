package com.libraryportal.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration class for defining security-related beans and configurations.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
	 /**
     * Configures the security filter chain for the application.
     *
     * @param httpSecurity the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring security
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		logger.info("Configuring security filter chain...");
		httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(c -> c.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/books", "api/borrowers").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(Customizer.withDefaults())
				.formLogin(form -> form.defaultSuccessUrl("/swagger-ui/index.html").permitAll());

		return httpSecurity.build();
	}

	/**
     * Configures CORS (Cross-Origin Resource Sharing) for the application.
     *
     * @return the CorsConfigurationSource configured with allowed origins, methods, headers, and exposed headers
     */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		 logger.info("Configuring CORS...");
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setExposedHeaders(Arrays.asList("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	 /**
     * Configures an in-memory user details service with a single user.
     *
     * @return the configured UserDetailsService
     */
	@Bean
	UserDetailsService userDetailsService() {
		 logger.info("Configuring user details service...");
		InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("password")).build();
		userDetailsService.createUser(user);
		return userDetailsService;
	}

	/**
     * Configures a BCrypt password encoder for encoding passwords.
     *
     * @return the configured BCryptPasswordEncoder
     */
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		logger.info("Configuring password encoder...");
		return new BCryptPasswordEncoder();
	}

}