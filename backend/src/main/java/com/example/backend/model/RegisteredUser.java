package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUser extends BaseEntity{
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="surname", nullable = false)
    private String surname;
    @Column(name="email", unique = true, nullable = false)
    private String email;
    @Column(name="password", nullable = false)
    private String password;
    @Column(name="salt", nullable = false)
    private String salt;
    @OneToMany(mappedBy = "subject")
    private List<Certificate> certificates;
}
