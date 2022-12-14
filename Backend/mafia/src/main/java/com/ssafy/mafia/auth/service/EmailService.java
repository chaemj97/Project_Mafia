package com.ssafy.mafia.auth.service;


import com.ssafy.mafia.Entity.RefreshToken;
import com.ssafy.mafia.Entity.User;
import com.ssafy.mafia.auth.controller.dto.TokenDto;
import com.ssafy.mafia.auth.controller.dto.UserRequestDto;
import com.ssafy.mafia.auth.jwt.TokenProvider;
import com.ssafy.mafia.auth.repository.RefreshTokenRepository;
import com.ssafy.mafia.auth.repository.UserRepository;
import com.ssafy.mafia.auth.util.makeSecretnumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EntityManager em;
    private final JavaMailSender mailSender;

    @Transactional
    public int sendEmail(String email) throws  Exception {
        int num = Integer.parseInt(makeSecretnumberUtil.numberGen(6,1));
        User user = em.createQuery("SELECT u FROM User u WHERE u.email like :email", User.class).setParameter("email", email).getSingleResult();
        User changeUser = em.find(User.class, user.getUserSeq());
        changeUser.setEmailCode(num);

        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");
            mimeMessageHelper.setFrom("socialable@naver.com");
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("?????????????????? ????????? ???????????? ?????????.");
            mimeMessageHelper.setText("<h1>[??????????????????]????????????</h1>" +
                    "<br/>????????????????????? ???????????????????????? ???????????????."+
                    "<br/>?????? ??????????????? ???????????? ???????????????."+
                    "<br/>-------------------------------------------------" +
                    "<br/><h2> ????????? ?????? ????????? :" + num + "?????????</h2>" +
                    "<br/>???????????????", true);
            mailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user.getUserSeq();
    }

    @Transactional
    public boolean emailValidationUser(int num, int userId) {
        User user = em.find(User.class, userId);
        if (user.getEmailCode() == num ) {
            user.setAuth(true);
            return true;
        } else {
            return false;
        }
    }

}
