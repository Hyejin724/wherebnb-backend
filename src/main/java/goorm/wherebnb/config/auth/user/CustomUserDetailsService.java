package goorm.wherebnb.config.auth.user;

import com.amazonaws.services.kms.model.NotFoundException;
import goorm.wherebnb.domain.dao.User;
import goorm.wherebnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> oUser = userRepository.findByEmail(email);

        User user = oUser
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email :" + email));
        return CustomUserDetails.create(user);
    }

    @Transactional(readOnly = true)
    public User selcetUser(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow(() -> new NotFoundException("유저없음"));
        user.getWishList().forEach(Long::getClass); // @OneToMany 상태 가지고올때 필요함
        return user;
    }

}
