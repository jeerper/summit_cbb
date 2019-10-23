package com.summit.domain;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户实体
 *
 * @author Administrator
 */

public class User implements UserDetails, Serializable {


    private String username;

    private String password;

    private String email;

    private Collection<GrantedAuthority> authorities;

    private String name;
    private String phoneNumber;
    private Integer isEnabled;
    private Timestamp lastUpdateTime;
    private Integer state;
    private String note;

    public User() {

    }

    public User(String username, String password, String email) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public User(String username, String password, String email, Collection<GrantedAuthority> authorities, String name,
                String phoneNumber, Integer isEnabled, Timestamp lastUpdateTime, Integer state, String note) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isEnabled = isEnabled;
        this.lastUpdateTime = lastUpdateTime;
        this.state = state;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 密碼BCrypt加密
     *
     * @param password
     */
    public void setPassword(String password) {
        /*PasswordEncoder  encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(password);*/
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        //重載默認是false,我們要改成true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //重載默認是false,我們要改成true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //重載默認是false,我們要改成true
        return true;
    }

    @Override
    public boolean isEnabled() {
        //重載默認是false,我們要改成true
        return true;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", email=" + email
                + ", authorities=" + authorities + ", name=" + name + ", phoneNumber=" + phoneNumber + ", isEnabled="
                + isEnabled + ", lastUpdateTime=" + lastUpdateTime + ", state=" + state + ", note=" + note + "]";
    }


}
