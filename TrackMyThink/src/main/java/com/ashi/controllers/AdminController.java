package com.ashi.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ashi.bindings.Admin;

import com.ashi.dao.AdminDAO;
import com.ashi.dao.ExamDAO;
import com.ashi.dao.StudentDAO;
import com.ashi.entities.AdminEntity;
import com.ashi.entities.ExamEntity;
import com.ashi.entities.StudentEntity;
import com.ashi.repository.AdminRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminDAO adminDAO; // used for DB operations

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private AdminRepository adminService;

	@Autowired
	private ExamDAO examDAO;

	// login page
	@GetMapping("/login")
	public String adminLoginPage() {
		return "adminlogin";
	}

	// login handler
	@PostMapping("/dashboard")
	public String adminLoginHandler(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {

		// login check
		AdminEntity adminEntity = adminDAO.loginCheck(username, password);
		if (adminEntity == null) {
			model.addAttribute("msg", "invalid creditials!");
			return "adminlogin";
		}
		// Copy entity to DTO class
		Admin adminDTO = new Admin();
		BeanUtils.copyProperties(adminEntity, adminDTO);

		List<ExamEntity> activeNotPublishedExams = examDAO.findByIsActiveTrueAndIsPublishedFalseOrderByExamDateAsc();

		List<ExamEntity> publishedExams = examDAO.findByIsPublishedTrueOrderByExamDateAsc();
		model.addAttribute("publishedExams", publishedExams);
		model.addAttribute("activeExams", activeNotPublishedExams);
		session.setAttribute("admin", adminDTO);
		model.addAttribute("admin", adminDTO);
		return "adminDashboard";
	}

	// dashboard page
	@GetMapping("/dashboard")
	public String adminDashboard(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			model.addAttribute("msg", "Session Expired. Login Again!");
			return "adminlogin";
		}

		List<ExamEntity> activeNotPublishedExams = examDAO.findByIsActiveTrueAndIsPublishedFalseOrderByExamDateAsc();

		List<ExamEntity> publishedExams = examDAO.findByIsPublishedTrueOrderByExamDateAsc();
		model.addAttribute("publishedExams", publishedExams);
		model.addAttribute("admin", admin);
		model.addAttribute("activeExams", activeNotPublishedExams);
		return "adminDashboard";
	}

	// view profile page
	@GetMapping("/viewprofile")
	public String adminViewProfile(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			model.addAttribute("msg", "Session Experied..Login Again !");
			return "adminlogin";
		}
		model.addAttribute("admin", admin);
		return "adminProfile";
	}

	// view students page
	@GetMapping("/viewstudents")
	public String adminViewStudents(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			model.addAttribute("msg", "Session Experied..Login Again !");
			return "adminlogin";
		}
		List<StudentEntity> studentEntities = studentDAO.getAllStudents();
		model.addAttribute("students", studentEntities);
		model.addAttribute("admin", admin);
		return "adminViewStudents";
	}

	// password change page
	@GetMapping("/passwordchange")
	public String adminPasswordChange(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			model.addAttribute("msg", "Session Experied..Login Again !");
			return "adminlogin";
		}
		model.addAttribute("admin", admin);
		return "adminPasswordChange";
	}

	// password change handler

	@PostMapping("/passwordchange")
	public String adminPasswordChangeHandler(Model model, HttpSession session, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		// Get the admin object from session
		Admin admin = (Admin) session.getAttribute("admin");

		if (admin == null) {
			model.addAttribute("errorMsg", "Session expired. Please login again.");
			return "adminlogin"; // Redirect or show login page
		}

		// Check if the old password entered matches the one in session
		if (!admin.getPassword().equals(oldPassword)) {
			model.addAttribute("admin", admin);
			model.addAttribute("errorMsg", "Old password is incorrect.");
			return "adminPasswordChange";
		}

		// Prevent reusing the old password
		if (oldPassword.equals(newPassword)) {
			model.addAttribute("admin", admin);
			model.addAttribute("errorMsg", "New password must be different from the old password.");
			return "adminPasswordChange";
		}

		// Update password in session object
		admin.setPassword(newPassword);
		session.setAttribute("admin", admin);
		model.addAttribute("admin", admin);

		// Copy admin to AdminEntity
		AdminEntity adminEntity = new AdminEntity();
		BeanUtils.copyProperties(admin, adminEntity);

		// Update password in DB
		adminDAO.updateAdmin(adminEntity); // Ensure this method updates the DB

		model.addAttribute("successMsg", "Password updated successfully.");
		return "adminPasswordChange";
	}

	// admin updateProfile page
	@GetMapping("/updateProfile")
	public String adminUpdateProfile(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			model.addAttribute("msg", "Session Experied..Login Again !");
			return "adminlogin";
		}
		model.addAttribute("admin", admin);
		return "adminUpdateProfile";
	}

	// admin updateProfile page handling
	@PostMapping("/updateProfile")
	public String updateAdminProfile(@ModelAttribute("admin") Admin adminDTO,
			@RequestParam("profileImageFile") MultipartFile file, HttpSession session, Model model) {

		Admin sessionAdmin = (Admin) session.getAttribute("admin");

		if (sessionAdmin == null) {
			model.addAttribute("msg", "Session expired. Please login again!");
			return "adminlogin";
		}

		// 1. Fetch existing AdminEntity from DB
		AdminEntity adminEntity = adminDAO.getAdminById(sessionAdmin.getAdminId());

		if (adminEntity == null) {
			model.addAttribute("msg", "Admin not found in database.");
			return "adminlogin";
		}

		// 2. Update only mutable fields
		adminEntity.setName(adminDTO.getName());
		adminEntity.setAddress(adminDTO.getAddress());
		adminEntity.setUpdatedAt(LocalDateTime.now());

		// 3. Handle profile image update if uploaded
		if (file != null && !file.isEmpty()) {
			try {
				adminEntity.setProfileImage(file.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("msg", "Failed to upload image.");
				return "adminProfile";
			}
		}

		// 4. Save updated entity
		AdminEntity updatedEntity = adminDAO.updateAdmin(adminEntity); // Ensure your service does save

		// 5. Convert back to DTO for session update
		Admin updatedDTO = new Admin();
		BeanUtils.copyProperties(updatedEntity, updatedDTO);

		session.setAttribute("admin", updatedDTO);
		model.addAttribute("admin", updatedDTO);
		model.addAttribute("msg", "Profile updated successfully.");
		return "adminProfile";
	}

	@GetMapping("/profile-photo/{id}")
	public ResponseEntity<byte[]> getAdminProfilePhoto(@PathVariable Integer id) {
		Optional<AdminEntity> optionalAdmin = adminService.findById(id);
		// System.out.println("admin"+optionalAdmin.get());
		if (optionalAdmin.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		AdminEntity admin = optionalAdmin.get();
		byte[] image = admin.getProfileImage();

		if (image != null && image.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // or PNG if that's the format
			return new ResponseEntity<>(image, headers, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build(); // or serve default image here if you prefer
		}
	}

	// logout handler
	@GetMapping("/logout")
	public String adminLogoutHandler(HttpSession session, AdminEntity adminEntity, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			// model.addAttribute("msg", "Session Experied..Login Again !");
			return "adminLogout";
		}
		BeanUtils.copyProperties(admin, adminEntity);

		adminEntity.setLastLogin(LocalDateTime.now());
		adminDAO.updateAdmin(adminEntity);
		session.invalidate();
		return "adminLogout";
	}

}
