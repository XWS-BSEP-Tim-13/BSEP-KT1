package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name="name")
    String name;

    @JsonIgnore
    @Override
    @Column(unique = false,nullable = false)
    public String getAuthority() {
        return name;
    }

}
