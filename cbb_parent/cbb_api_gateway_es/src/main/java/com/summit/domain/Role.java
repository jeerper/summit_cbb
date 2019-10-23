package com.summit.domain;

import java.io.Serializable;

/**
 * @author yt
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String note;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Role [code=" + code + ", name=" + name + ", note=" + note + "]";
    }

}
