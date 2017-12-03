package app.com.HungryEnglish.Util;

/**
 * Created by Rujul on 11/15/2017.
 */

public enum RequestParams {
    Lat("lat"),
    Lng("lng"),
    ActionName("action"),
    Role("role"),
    Student("student"),
    Teacher("teacher"),
    Status("status"),
    Title("title"),
    Link("link"),
    ActionAddress("get_address");

    private String value;

    public String getValue() {
        return value;
    }

    private RequestParams(String value) {
        this.value = value;
    }
}
