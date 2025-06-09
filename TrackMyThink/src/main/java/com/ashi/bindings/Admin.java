package com.ashi.bindings;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
//admin DTO
public class Admin {
	
    private Integer adminId;

    
    private String name;

  
    private String email;

    
    private String password;

   
    private byte[] profileImage; // Binary image data

 
    private String role = "ADMIN";

    
    private Boolean isActive = true;

    
   
    private LocalDateTime createdAt;

    
   
    private LocalDateTime updatedAt;

   
    private LocalDateTime lastLogin;
    
    private String address;

	
}
