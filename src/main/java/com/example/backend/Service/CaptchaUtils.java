package com.example.backend.Service;

import com.octo.captcha.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CaptchaUtils {

    @Autowired
    private CaptchaService captchaService;

    public ResponseEntity<?> captcha(HttpServletResponse response) throws IOException {
        String captchaId = generateCode();
        BufferedImage challenge = (BufferedImage) captchaService.getChallengeForID(captchaId);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ImageIO.write(challenge, "PNG", byteOutputStream);

        ResponseEntity<?> mutableHttpResponse = new ResponseEntity(byteOutputStream.toByteArray(), HttpStatus.OK);
        response.addCookie(new Cookie("JCaptcha", captchaId));
        return mutableHttpResponse;
    }

    private String generateCode() {
        SecureRandom secureRandom = new SecureRandom();
        AtomicReference<String> res = new AtomicReference<>("");
        secureRandom.ints(48, 122)
                .filter(i -> (i > 48 && i < 57) || (i > 65 && i < 90) || (i > 97 && i < 122))//0-9 || A-Z || a-z
                .mapToObj(i -> (char) i)
                .limit(64).forEach(c -> res.updateAndGet(v -> v + c));
        return res.toString();
    }

    public boolean silentlyValidateCaptcha(String session, String code) {
        try {
            return captchaService.validateResponseForID(session, code);
        } catch (Exception e) {
            return false;
        }
    }
}
