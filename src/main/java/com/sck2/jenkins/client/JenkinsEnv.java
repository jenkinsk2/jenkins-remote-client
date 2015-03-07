package com.sck2.jenkins.client;

import lombok.Getter;

import java.util.ResourceBundle;

/**
 * @author newlight77@gmail.com
 */
public class JenkinsEnv {
    public static final String HOST = "jenkins.host";
    public static final String USER = "jenkins.user";
    public static final String PASSWORD = "jenkins.password";
    public static final String BUILD_TOKEN = "jenkins.buildToken";

    private ResourceBundle resource = ResourceBundle.getBundle("jenkins");

    public JenkinsEnv() {
        host = getValue(HOST);
        user = getValue(USER);
        password = getValue(PASSWORD);
        buildToken = getValue(BUILD_TOKEN);
    }
    @Getter private String host;
    @Getter private String user;
    @Getter private String password;
    @Getter private String buildToken;

    private String getValue(String param) {
        return resource.getString(param);
    }
}
