/**
 * @Author - S.K. Dursun
 * @Version - 0.1 / 27-04-2022
 *
 * Copyright (c) Novi University, Edu.
 *
 * This is an Entity Class of a User_Account
 */

package nl.novi.hulppost.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import javax.persistence.*;
import java.util.Date;

@Builder
@Table(name = "user_accounts")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( name = "first_name")
    private String firstName;
    @Column(name = "surname")
    private String surname;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birthday")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(referencedColumnName = "id")
    @JoinColumn( name = "id")
//    @JsonIgnore
    private User user;

    public Account() {
    }

    public Account(Long id, String firstName, String surname, String gender, Date birthday, String zipCode, User user) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.birthday = birthday;
        this.zipCode = zipCode;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
