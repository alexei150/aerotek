package com.swecor.aerotek.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//не просто юзер, а может дофига и создается админами, директорами
@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends EntityParent{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "company")
    private String company;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL,
                mappedBy = "employee")
    private List<User> users;

    public void addUserToEmployee(User user){
        if (users == null){
            users = new ArrayList<>();
        }
        users.add(user);
        user.setEmployee(this);
    }
}
