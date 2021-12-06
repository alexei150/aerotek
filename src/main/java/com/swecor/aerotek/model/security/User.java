package com.swecor.aerotek.model.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends EntityParent{

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
