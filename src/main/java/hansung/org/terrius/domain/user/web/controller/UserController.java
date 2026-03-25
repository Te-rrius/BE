package hansung.org.terrius.domain.user.web.controller;


import hansung.org.terrius.domain.user.service.UserService;
import hansung.org.terrius.domain.user.web.dto.LoginRes;
import hansung.org.terrius.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("kakao/login")
    public ResponseEntity<SuccessResponse<LoginRes>> login(
            @RequestParam("code") String code){
        LoginRes res = userService.login(code);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(res));
    }
}