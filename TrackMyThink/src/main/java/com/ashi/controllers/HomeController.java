package com.ashi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ashi.services.MailSendingService;

@Controller
public class HomeController {

    @Autowired
    private MailSendingService mailService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/sendmail")
	public String handleContactForm(@RequestParam String cname,
	                                @RequestParam String email,
	                                @RequestParam String msg,
	                                RedirectAttributes redirectAttributes) {

	   
	      	mailService.sendContactMail(email, cname, msg);
	    
	  
	   

	         return "redirect:/";
	}


}
