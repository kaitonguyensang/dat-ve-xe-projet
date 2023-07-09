package com.example.datvexe.utils;

import java.io.IOException;

import com.example.datvexe.common.GooglePojo;
import com.example.datvexe.common.Provider;
import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.config.CustomTaiKhoanDetails;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.SignUpService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.example.datvexe.common.Constant.PASS_GOOGLE_ACOUT;

@Component
public class GoogleUtils {

    @Autowired
    SignUpService signUpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private Environment env;
    @Autowired
    private NhaXeRepository nhaXeRepository;

    public String getToken(final String code) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.token");
        System.out.println(1);
        System.out.println(code);
        System.out.println( env.getProperty("google.app.id"));
        System.out.println( env.getProperty("google.app.secret"));
        System.out.println( env.getProperty("google.redirect.uri"));
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
        ObjectMapper mapper = new ObjectMapper();
        GooglePojo googlePojo = mapper.readValue(response, GooglePojo.class);
        System.out.println(googlePojo);
        return googlePojo;
    }

    public CustomTaiKhoanDetails buildUser(GooglePojo googlePojo) {
        if (!googlePojo.isVerified_email()) {
            User user = new User();
            if (taiKhoanRepository.findTaiKhoanByNhaXe_Email(googlePojo.getEmail()) != null
                    || taiKhoanRepository.findTaiKhoanByUser_Email(googlePojo.getEmail()) != null
                    || taiKhoanRepository.findTaiKhoanByAdmin_Email(googlePojo.getEmail()) != null) {
                return null;
            }
            user.setEmail(googlePojo.getEmail());
            user.setHoTen(googlePojo.getName());
            TaiKhoan taiKhoanAdd = new TaiKhoan(googlePojo.getEmail(), PASS_GOOGLE_ACOUT, Role.USER, Provider.GOOGLE, TrangThai.ACTIVE);
            taiKhoanRepository.save(taiKhoanAdd);
            TaiKhoan taiKhoanMatch = taiKhoanRepository.findTaiKhoanByAdmin_Email(googlePojo.getEmail());
            user.setTaiKhoan(taiKhoanMatch);
            userRepository.save(user);
        }
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByAdmin_Email(googlePojo.getEmail());
        return new CustomTaiKhoanDetails(taiKhoan);
    }
}
