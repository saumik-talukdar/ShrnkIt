package bd.pro.saumik.shrnkit.domain.user.controller;

import bd.pro.saumik.shrnkit.domain.auth.dto.request.RefreshTokenRequest;
import bd.pro.saumik.shrnkit.domain.user.dto.ChangePasswordRequest;
import bd.pro.saumik.shrnkit.domain.user.entity.User;
import bd.pro.saumik.shrnkit.domain.user.service.UserService;
import bd.pro.saumik.shrnkit.security.AppUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/me/change-password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        User user = ((AppUserDetails) authentication.getPrincipal()).getUser();
        userService.changePassword(
                user,
                changePasswordRequest.currentPassword(),
                changePasswordRequest.newPassword()
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest,
            Authentication authentication
    ){
        User user = ((AppUserDetails) authentication.getPrincipal()).getUser();;
        userService.logout(user.getId(), refreshTokenRequest.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/logout-all")
    public ResponseEntity<Void> logoutAll(
            Authentication authentication
    ){
        User user = ((AppUserDetails) authentication.getPrincipal()).getUser();
        userService.logoutAll(user.getId());
        return ResponseEntity.noContent().build();
    }
}
