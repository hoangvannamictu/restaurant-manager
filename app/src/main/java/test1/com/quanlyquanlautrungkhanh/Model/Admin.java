package test1.com.quanlyquanlautrungkhanh.Model;

public class Admin {
    private String emailAdmin;
    private String passwordAdmin;
    private String nameAdmin;
    private String phoneAdmin;

    public Admin() {
    }

    public Admin(String emailAdmin, String passwordAdmin, String nameAdmin, String phoneAdmin) {
        this.emailAdmin = emailAdmin;
        this.passwordAdmin = passwordAdmin;
        this.nameAdmin = nameAdmin;
        this.phoneAdmin = phoneAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public String getNameAdmin() {
        return nameAdmin;
    }

    public void setNameAdmin(String nameAdmin) {
        this.nameAdmin = nameAdmin;
    }

    public String getPhoneAdmin() {
        return phoneAdmin;
    }

    public void setPhoneAdmin(String phoneAdmin) {
        this.phoneAdmin = phoneAdmin;
    }
}
