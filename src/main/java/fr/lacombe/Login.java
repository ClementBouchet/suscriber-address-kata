package fr.lacombe;

import java.io.Serializable;

public class Login implements Serializable {
    private String advisorTestLogin;

    public Login(String advisorTestLogin) {
        this.advisorTestLogin = advisorTestLogin;
    }

    public String getAdvisorTestLogin() {
        return advisorTestLogin;
    }

    public void setAdvisorTestLogin(String advisorTestLogin) {
        this.advisorTestLogin = advisorTestLogin;
    }
}
