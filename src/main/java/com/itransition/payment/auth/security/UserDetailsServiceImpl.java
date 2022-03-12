package com.itransition.payment.auth.security;

import com.itransition.payment.auth.repository.UserRepository;
import com.itransition.payment.auth.type.UserStatus;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: add custom exception message
        val user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("lol kek lol"));

        // TODO: extract to method
        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                user.getStatus().equals(UserStatus.ACTIVE),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }
}
