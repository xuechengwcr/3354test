package cn.projects.team.demo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class Contact {
    @Id(autoincrement = true)
    private Long id ;
    private String name;
    private String phone1;
    private String phone2;
    private String phone3;
    private String icon;
    private String letters;
    private Integer groupId;
    private Boolean isBlack;
    @Generated(hash = 761606915)
    public Contact(Long id, String name, String phone1, String phone2,
            String phone3, String icon, String letters, Integer groupId,
            Boolean isBlack) {
        this.id = id;
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.icon = icon;
        this.letters = letters;
        this.groupId = groupId;
        this.isBlack = isBlack;
    }
    @Generated(hash = 672515148)
    public Contact() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone1() {
        return this.phone1;
    }
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }
    public String getPhone2() {
        return this.phone2;
    }
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }
    public String getPhone3() {
        return this.phone3;
    }
    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getLetters() {
        return this.letters;
    }
    public void setLetters(String letters) {
        this.letters = letters;
    }
    public Integer getGroupId() {
        return this.groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public Boolean getIsBlack() {
        return this.isBlack;
    }
    public void setIsBlack(Boolean isBlack) {
        this.isBlack = isBlack;
    }
    


}
