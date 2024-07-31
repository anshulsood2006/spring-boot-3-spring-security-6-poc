package com.arsoft.projects.thevibgyor.backend.controller.guest;

import com.arsoft.projects.thevibgyor.backend.constant.HttpStatus;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.security.auth.token.model.TokenRequest;
import com.arsoft.projects.thevibgyor.backend.security.auth.token.model.TokenResponse;
import com.arsoft.projects.thevibgyor.backend.security.auth.token.service.JwtService;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/auth/get-token")
    public TokenResponse getToken(@RequestBody TokenRequest tokenRequest, @RequestAttribute("user") User user, @RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request) {
        log.info("Request has reached login controller now");
        ZonedDateTime startZonedDateTime = null;
        Header header;
        String token = null;
        Error error = null;
        int httpStatus = -1;
        String url = HttpRequestUtil.getFullURL(request);
        try {
            if (requestTime == null) {
                startZonedDateTime = ZonedDateTime.now();
            } else {
                startZonedDateTime = requestTime;
            }
            token = jwtService.generateAccessToken(user, tokenRequest.getTtl());
            httpStatus = HttpStatus.OK.getValue();
        } catch (Exception e) {
            log.error("Exception found while getting token " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.getValue();
            error = new Error(e.getMessage());
        }
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
        header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, httpStatus);

        return new TokenResponse(header, token, error);
    }
}
