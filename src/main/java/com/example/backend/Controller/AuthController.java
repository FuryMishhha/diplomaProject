package com.example.backend.Controller;

import com.example.backend.Entity.User;
import com.example.backend.Model.LoginInput;
import com.example.backend.Service.CaptchaUtils;
import com.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaUtils captchaUtils;

    @PostMapping("/registration")
    public String addUser(@RequestBody User userForm, @CookieValue(name = "JCaptcha") String session) {
        if(!captchaUtils.silentlyValidateCaptcha(session, userForm.getCaptcha())){
            return "Некорректный ввод капчи!";
        }
        userService.saveUser(userForm);
        return "redirect:/login";
    }

    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getCaptcha(HttpServletResponse response) throws IOException {
        return captchaUtils.captcha(response);
    }

    @PostMapping("/login")
    ResponseEntity<String> auth(@RequestBody LoginInput loginInput) {
        return userService.login(loginInput);
    }
}