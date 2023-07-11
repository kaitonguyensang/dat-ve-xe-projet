package com.example.datvexe.constants;

import org.hibernate.engine.config.spi.ConfigurationService;

public class Constants {
    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final Integer MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final String NO_ANY_ACCOUNT = "Not find any account.";
    public static final String NO_ANY_BUS_STATION = "Not find any bus station.";
    public static final String NO_ACCOUNT = "Not find account.";
    public static final String EXIST_PHONE_NUMBER = "Phone number has existed.";
    public static final String EXIST_BUS_NAME = "Name of bus garage has existed.";
    public static final String EXIST_EMAIL = "Email has existed.";
    public static final String EXIST_IDENTITY_CODE = "Identity card has existed.";
    public static final String NOT_FOUND = "Not found.";
    public static final String MISS_FIELD = "Missing field";

    public static final String PASS_GOOGLE_ACCOUNT = "Kaitooo";

    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_Returnurl = "https://cms-admin-management-project.vercel.app/";
    public static String vnp_TmnCode = "X50IF3JA";
    public static String vnp_HashSecret = "GKDRVXISPGPDFMMCGYQSSSHPNMZZVTSJ";
    public static String vnp_apiUrl="https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    private Constants(){
        throw new IllegalStateException("Utility class");
    }

}
