package com.example.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="subject")
    private RegisteredUser subject;
    @Column(name="valid_from")
    private Date validFrom;
    @Column(name="valid_to")
    private Date validTo;
    @Column(name = "cerfilename")
    private String cerFileName;
}
