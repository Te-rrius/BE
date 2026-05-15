package hansung.org.terrius.domain.user.service;

import hansung.org.terrius.domain.user.entity.User;
import hansung.org.terrius.domain.user.exception.UserErrorCode;
import hansung.org.terrius.domain.user.exception.UserException;
import hansung.org.terrius.domain.user.repository.UserRepository;
import hansung.org.terrius.domain.user.web.dto.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserRes getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return UserRes.from(user);
    }
}
