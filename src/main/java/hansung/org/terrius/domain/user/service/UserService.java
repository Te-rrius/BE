package hansung.org.terrius.domain.user.service;

import hansung.org.terrius.domain.user.web.dto.LoginRes;

public interface UserService {
    LoginRes login(String code);
}
