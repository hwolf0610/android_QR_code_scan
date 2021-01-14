package com.example.qr_code.utils;


public class AppConstants {
    public static String BaseURL = "https://www.scanthedoor.com/api";
    public static String LOGIN = BaseURL + "/user/generate_auth_cookie/";
    public static String GET_NONCE = BaseURL + "/get_nonce/?controller=user&method=register";
    public static String SIGNUP = "https://www.scanthedoor.com/wp-json/wp/v2/users/register";

    public static String firstname = "";
    public static String secondname = "";
}
