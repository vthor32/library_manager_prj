package model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Patron implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("name")
    private String name;

    @SerializedName("contactInfo")
    private String contactInfo;

    public Patron(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    @Override
    public String toString() {
        return name + " (" + contactInfo + ")";
    }
}
