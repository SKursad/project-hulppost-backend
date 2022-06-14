/**
 * @Author - S.K. Dursun
 * @Version - 0.1 / 27-04-2022
 *
 * Copyright (c) Novi University, Edu.
 *
 * This is an Entity Class of a User_Account
 */

package nl.novi.hulppost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import nl.novi.hulppost.model.enums.Gender;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@Table(name = "user_accounts")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(unique = true, nullable = false)
    private Long id;
    @Column( name = "first_name")
    private String firstName;
    @Column(name = "surname")
    private String surname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "birthday")
    private String birthday;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "tel_number")
    private String telNumber;
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL ,mappedBy = "account")
    @PrimaryKeyJoinColumn(referencedColumnName = "user_id")
    @JoinColumn( name = "account_Id")
//    @JsonIgnore
    private User user;

    public Account() {
    }

    public Account(Long id, String firstName, String surname, Gender gender, String birthday, String zipCode, String telNumber, User user) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.birthday = birthday;
        this.zipCode = zipCode;
        this.telNumber = telNumber;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
