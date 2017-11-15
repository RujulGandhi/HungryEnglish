package app.com.HungryEnglish.Util;

/**
 * Created by good on 11/15/2017.
 */

public enum RequestParams {
    Lat("lat"),
    Lng("lng"),
    ActionName("action"),
    ActionAddress("get_address");

    private String value;

    public String getValue() {
        return value;
    }

    private RequestParams(String value) {
        this.value = value;
    }
}
