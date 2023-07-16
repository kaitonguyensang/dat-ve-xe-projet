package com.example.datvexe.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String convertCurrency(Double money){
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(localeVN);
        return formatter.format(money);
    }
}
