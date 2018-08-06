package com.lubanjianye.biaoxuntong.eventbus;


public class EventMessage {

    public static final String LOGIN_SUCCSS = "login_success";
    public static final String LOGIN_OUT = "login_out";
    public static final String CLICK_FAV = "click_fav";
    public static final String TAB_CHANGE = "tab_change";
    public static final String BIND_MOBILE_SUCCESS = "mobile";
    public static final String BIND_COMPANY_SUCCESS = "company";
    public static final String TOKEN_FALSE = "token_false";

    public static final String LOCA_AREA = "loca_area";
    public static final String LOCA_AREA_CODE = "loca_area_code";

    public static final String LOCA_AREA_CHANGE = "loca_area_change";

    public static final String USER_INFO_CHANGE = "user_info_change";
    public static final String READ_STATUS = "read_status";
    public static final String NO_CHANGE_AREA = "no_change_area";
    public static final String IF_ASK_LOCATION = "if_ask_location";

    public static final String DOUBLE_CLICK_EXIT = "double_click_exit";

    public static final String LEFT_BACK = "left_back";


    public final String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
