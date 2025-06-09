package com.ashi.bindings;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class Student {

	private Integer studentId;

    private String name;

   
    private String email;

    
    private String password;

   
    private String contactNumber;

    private LocalDateTime registrationDate;
   
    private String gender;
    
    private Boolean isActive = false;
    
    private byte[] profilePhoto; 
    
    private String address;
    
}