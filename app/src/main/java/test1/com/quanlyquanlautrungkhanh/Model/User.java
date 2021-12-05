package test1.com.quanlyquanlautrungkhanh.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String keyUser;
    private String emailUser;
    private String passwordUser;
    private String imgAvtUser;

    private String nameImgAvtUser;
    private String nameUser;
    private String dateCreatedUser;
    private String addressUser;
    private String phoneUser;
    private String ageUser;
    private String IdentityCardUser;
    private int statusUser;

    public User() {
    }

    public User(String emailUser, String passwordUser, String imgAvtUser, String nameUser, String dateCreatedUser, String addressUser, String phoneUser, String ageUser, String identityCardUser, int statusUser) {
        this.emailUser = emailUser;
        this.passwordUser = passwordUser;
        this.imgAvtUser = imgAvtUser;
        this.nameUser = nameUser;
        this.dateCreatedUser = dateCreatedUser;
        this.addressUser = addressUser;
        this.phoneUser = phoneUser;
        this.ageUser = ageUser;
        this.IdentityCardUser = identityCardUser;
        this.statusUser = statusUser;
    }

    public String getNameImgAvtUser() {
        return nameImgAvtUser;
    }

    public void setNameImgAvtUser(String nameImgAvtUser) {
        this.nameImgAvtUser = nameImgAvtUser;
    }
    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getAgeUser() {
        return ageUser;
    }

    public void setAgeUser(String ageUser) {
        this.ageUser = ageUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getImgAvtUser() {
        return imgAvtUser;
    }

    public void setImgAvtUser(String imgAvtUser) {
        this.imgAvtUser = imgAvtUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getDateCreatedUser() {
        return dateCreatedUser;
    }

    public void setDateCreatedUser(String dateCreatedUser) {
        this.dateCreatedUser = dateCreatedUser;
    }

    public String getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(String addressUser) {
        this.addressUser = addressUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getIdentityCardUser() {
        return IdentityCardUser;
    }

    public void setIdentityCardUser(String identityCardUser) {
        IdentityCardUser = identityCardUser;
    }

    public int getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(int statusUser) {
        this.statusUser = statusUser;
    }
}
