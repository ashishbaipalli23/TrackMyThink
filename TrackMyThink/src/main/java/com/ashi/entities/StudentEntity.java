package com.ashi.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student")
@Setter
@Getter
@ToString
public class StudentEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", columnDefinition = "INT")
    private Integer studentId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "contact_number",unique = true)
    private String contactNumber;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    private String gender;
    
    @Column(name = "ACTIVE_SW")
    private Boolean isActive = false;
    
    private String address;
    
    @Lob
    @Column(name = "profile_photo",columnDefinition = "LONGBLOB")
    private byte[] profilePhoto; 

    
    @PrePersist
    public void onCreate() {
        registrationDate = LocalDateTime.now();
    }
    
    
    
}
