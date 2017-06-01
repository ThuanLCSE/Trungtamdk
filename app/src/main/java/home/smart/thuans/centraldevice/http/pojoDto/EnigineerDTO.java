package home.smart.thuans.centraldevice.http.pojoDto;

import java.sql.Date;

/**
 * Created by Sam on 4/14/2017.
 */
public class EnigineerDTO {
    private int id;
    private String username;
    private String password;
    private String email;
    private Date birthday;
    private String fullname;
    private Date joinDay;

    public EnigineerDTO() {
    }

    public EnigineerDTO(int id, String username, String password, String email, Date birthday, String fullname, Date joinDay) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.fullname = fullname;
        this.joinDay = joinDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
