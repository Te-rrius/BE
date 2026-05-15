package hansung.org.terrius.domain.user.web.controller;

import hansung.org.terrius.domain.user.service.UserService;
import hansung.org.terrius.domain.user.web.dto.UserRes;
import hansung.org.terrius.global.jwt.CustomUserDetails;
import hansung.org.terrius.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<UserRes>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        UserRes res = userService.getUserInfo(customUserDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.from(res));
    }
}
