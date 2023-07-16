package com.example.datvexe.utils;

import java.io.IOException;

import com.example.datvexe.common.GooglePojo;
import com.example.datvexe.common.Provider;
import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.config.CustomTaiKhoanDetails;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.SignUpService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import static com.example.datvexe.constants.Constants.PASS_GOOGLE_ACCOUNT;

@Component
@Service
public class GoogleUtils {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SignUpService signUpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private Environment env;

    public String getToken(final String code) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.token");
        String response = Request.Post(link)
                .bodyForm(Form.form().add("client_id", env.getProperty("google.app.id"))
                        .add("client_secret", env.getProperty("google.app.secret"))
                        .add("redirect_uri", env.getProperty("google.redirect.uri")).add("code", code)
                        .add("grant_type", "authorization_code").build())
                .execute().returnContent().asString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }

    public GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.user_info") + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        GooglePojo googlePojo = new Gson().fromJson(response, GooglePojo.class);
        return googlePojo;
    }

    public CustomTaiKhoanDetails buildUser(GooglePojo googlePojo) {
        if (taiKhoanRepository.findTaiKhoanByNhaXe_Email(googlePojo.getEmail()) != null
                || taiKhoanRepository.findTaiKhoanByAdmin_Email(googlePojo.getEmail()) != null) {
            return null;
        } else if (taiKhoanRepository.findTaiKhoanByUser_Email(googlePojo.getEmail()) == null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setProvider(Provider.GOOGLE);
            taiKhoan.setUsername(googlePojo.getId() + googlePojo.getEmail());
            taiKhoan.setVerifyEmail(true);
            taiKhoan.setPassword(passwordEncoder.encode(PASS_GOOGLE_ACCOUNT + googlePojo.getEmail()));
            taiKhoan.setRole(Role.USER);
            taiKhoan.setTrangThaiHoatDong(TrangThai.ACTIVE);
            User user = new User();
            user.setPicture(googlePojo.getPicture());
            user.setEmail(googlePojo.getEmail());
            user.setHoTen(googlePojo.getFamily_name() + " " + googlePojo.getName());
            userRepository.save(user);
            user = userRepository.findUserByEmail(googlePojo.getEmail());
            taiKhoan.setUser(user);
            taiKhoanRepository.save(taiKhoan);
            return new CustomTaiKhoanDetails(taiKhoan);
        } else  {
            TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByUser_Email(googlePojo.getEmail());
            if (!Provider.GOOGLE.equals(taiKhoan.getProvider())) {
                return null;
            }
            return new CustomTaiKhoanDetails(taiKhoan);
        }
    }
}
