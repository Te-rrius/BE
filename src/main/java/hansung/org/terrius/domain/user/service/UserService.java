package hansung.org.terrius.domain.user.service;

import hansung.org.terrius.domain.user.web.dto.UserRes;

public interface UserService {
    UserRes getUserInfo(Long userId);
}
