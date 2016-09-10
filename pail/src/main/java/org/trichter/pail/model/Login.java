package org.trichter.pail.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;

/**
 * Created by guy on 9/10/16.
 */
public class Login implements Serializable {

    private static final long serialVersionUID = -5633092014800844240L;

    private String userName;
    private long timestamp;

    public Login(){}
    public Login(String userName) {
        this.userName = userName;
        this.timestamp = DateTime.now(DateTimeZone.UTC).getMillis();
    }
    public Login(String userName, long timestamp) {
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login = (Login) o;

        if (timestamp != login.timestamp) return false;
        return userName.equals(login.userName);

    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Login{" +
                "userName='" + userName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
