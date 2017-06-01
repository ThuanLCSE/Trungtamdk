package home.smart.thuans.centraldevice.http.pojoDto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Thuans on 4/16/2017.
 */
public class EngineerDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private Date birthday;
    private String fullname;
    private Date joinDay;
    private String managedById;
    private String telephone;
    private String role;

    public String getManagedById() {
        return managedById;
    }

    public void setManagedById(String managedById) {
        this.managedById = managedById;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getJoinDay() {
        return joinDay;
    }

    public void setJoinDay(Date joinDay) {
        this.joinDay = joinDay;
    }
}
