package webproject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Login {

    @Id
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
