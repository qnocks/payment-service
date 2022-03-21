package com.itransition.payment.auth.security;

import com.itransition.payment.auth.repository.UserRepository;
import com.itransition.payment.auth.type.UserStatus;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ExceptionMessageResolver exceptionMessageResolver;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        exceptionMessageResolver.getMessage("auth.username-not-found", username)));

        return UserDetailsImpl.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.getStatus().equals(UserStatus.ACTIVE))
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()))
                .build();
    }
}
