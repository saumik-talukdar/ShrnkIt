package bd.pro.saumik.shrnkit.domain.auth.controller;

import bd.pro.saumik.shrnkit.common.http.UserAgentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final UserAgentService userAgentService;

    @GetMapping("/me")
    public ResponseEntity<String> me(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String userAgent = request.getHeader("User-Agent");
        System.out.println(userAgentService.parse(
                userAgent
        ));
        return ResponseEntity.ok("ok");
    }
}