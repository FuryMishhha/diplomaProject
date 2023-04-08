package com.example.backend.Config;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.AbstractBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class BeanConfig {
    @Bean
    CaptchaService captchaService() {
        WordGenerator wordGen = new RandomWordGenerator("0123456789");

        FontGenerator fontGenRandom = new RandomFontGenerator(12, 14,
                new Font[]{
                        new Font("Arial", Font.PLAIN, 10),
                        new Font("Tahoma", Font.PLAIN, 10),
                        new Font("Verdana", Font.PLAIN, 10),
                        new Font("Comic sans MS", Font.PLAIN, 10),
                        new Font("Lucida console", Font.PLAIN, 10),
                });
        Color white = new Color(255, 255, 255);
        Color black = new Color(0, 0, 0);
        ColorGenerator whiteGen = new SingleColorGenerator(white);
        ColorGenerator blackGen = new SingleColorGenerator(black);
        AbstractBackgroundGenerator abstractBackgroundGenerator = new FunkyBackgroundGenerator(120, 30, blackGen, whiteGen, whiteGen, blackGen, 0.1F);

        TextPaster simpleColoredPaster = new RandomTextPaster(4, 6, new SingleColorGenerator(white), true);

        ComposedWordToImage wordToImage = new ComposedWordToImage(
                fontGenRandom,
                abstractBackgroundGenerator,
                simpleColoredPaster);

        CaptchaFactory captchaFactory = new GimpyFactory(wordGen, wordToImage);
        CaptchaEngine imageEngine = new GenericCaptchaEngine(new CaptchaFactory[]{captchaFactory});

        return new GenericManageableCaptchaService(imageEngine, 300, 10000);
    }
}
