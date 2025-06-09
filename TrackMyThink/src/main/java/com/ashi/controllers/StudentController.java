package com.ashi.controllers;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ashi.bindings.*;
import com.ashi.dao.ExamDAO;
import com.ashi.dao.QuestionDAO;
import com.ashi.dao.StudentDAO;
import com.ashi.entities.ExamEntity;
import com.ashi.entities.QuestionEntity;
import com.ashi.entities.StudentEntity;
import com.ashi.entities.StudentExamAttemptEntity;
import com.ashi.repository.StudentExamAttemptRepository;
import com.ashi.repository.StudentRepository;
import com.ashi.services.MailSendingService;

import jakarta.servlet.http.HttpSession;

@Controller

public class StudentController {

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private MailSendingService mail;

	@Autowired
	private StudentRepository studentService;

	@Autowired
	private ExamDAO examDAO;
	
	@Autowired
	private  QuestionDAO questionDAO;

	@Autowired
	private StudentExamAttemptRepository studentExamAttemptRepository;
	
	// register page
	@GetMapping("/register")
	public String studentRegister(Model model) { // "@ModelAttribute("student") Student student " in paramaters
		model.addAttribute("student", new Student());
		return "studentRegister";
	}

	// register Handler
	@PostMapping("/register")
	public String registerStudent(Student student, Model model) {

		// this student data is loaded to entity class
		StudentEntity studentEntity = new StudentEntity();

//		studentEntity.setName(student.getName());
//		studentEntity.setEmail(student.getEmail());
//		studentEntity.setPassword(student.getPassword());
//		studentEntity.setContactNumber(student.getContactNumber());

		// Use this method
		student.setIsActive(false);
		BeanUtils.copyProperties(student, studentEntity);

		try {
			studentDAO.saveStudent(studentEntity);
			// Register success mail send to the User
			mail.sendRegisterSuccessMail(studentEntity.getEmail(), studentEntity.getName());

			model.addAttribute("msg", "Registration Successfull Login here");
		} catch (Exception e) {
			model.addAttribute("msg", "Email or Phone Number already exist! Login Here");
		}

		return "studentLogin"; // if login success redirect to studentLogin
	}

	// student login page
	@GetMapping("/login")
	public String studentLoginPage() {
		return "studentLogin";
	}

	// login page handler
	@PostMapping("/student/dashboard")
	public String studentLogin(@RequestParam String username, @RequestParam String password, Model model,
			Student student, HttpSession session) {
       
		StudentEntity studentEntity1 = studentDAO.loginCheck(username, password);

		if (studentEntity1 != null) {
			// load the studentEntity data to student object for binding
			BeanUtils.copyProperties(studentEntity1, student);
			student.setProfilePhoto(studentEntity1.getProfilePhoto());
			session.setAttribute("student", student);
			// Get all attempts by the student
		    List<StudentExamAttemptEntity> attempts = studentExamAttemptRepository
		            .findByStudent_StudentId(student.getStudentId());

		    // Compute stats
		    int examCount = attempts.size();
		    double avgScore = attempts.stream()
		            .mapToDouble(StudentExamAttemptEntity::getScore)
		            .average()
		            .orElse(0.0);

		    //Add to model
		    model.addAttribute("examCount", examCount);
		    model.addAttribute("avgScore", avgScore);
			
		    model.addAttribute("student", student);
		    String formattedScore = String.format("%.1f / 100", avgScore);
		    model.addAttribute("formattedAvgScore", formattedScore);

			return "studentDashboard";// studentDashboard
		} else {
			model.addAttribute("msg", "Invalid Username or Password");
			return "studentLogin";
		}

	}

	@GetMapping("/student/dashboard")
	public String showStudentDashboard(Model model, HttpSession session) {
	    Student student = (Student) session.getAttribute("student");
	    if (student == null) {
	        return "redirect:/student/login"; // Adjust based on your routing
	    }

	    // Get all attempts by the student
	    List<StudentExamAttemptEntity> attempts = studentExamAttemptRepository
	            .findByStudent_StudentId(student.getStudentId());

	    // Compute stats
	    int examCount = attempts.size();
	    double avgScore = attempts.stream()
	            .mapToDouble(StudentExamAttemptEntity::getScore)
	            .average()
	            .orElse(0.0);

	    //Add to model
	    model.addAttribute("examCount", examCount);
	    model.addAttribute("avgScore", avgScore);
	    model.addAttribute("student", student);
	    String formattedScore = String.format("%.1f / 100", avgScore);
	    model.addAttribute("formattedAvgScore", formattedScore);

	    return "studentDashboard"; // Make sure this matches your template file name
	}


	@GetMapping("/student/viewprofile")
	public String studentProfile(HttpSession session, Model model) {
		Student student = (Student) session.getAttribute("student"); // or however you store it
		if (student == null) {
			model.addAttribute("msg", "session expried....Please login Again!");
			return "studentLogin"; // or some fallback
		}
		model.addAttribute("student", student);
		return "studentProfile";
	}

	@GetMapping("/student/updateProfile")
	public String updateProfile(HttpSession session, Model model) {
		Student student = (Student) session.getAttribute("student");
		if (student == null) {
			model.addAttribute("msg", "session expried....Please login Again!");
			return "studentLogin"; // or some fallback
		}
		model.addAttribute("student", student);
		return "studentUpdateProfile";
	}

	@PostMapping("/student/updateProfile")
	public String updateStudentProfile(@ModelAttribute("student") Student student,
			@RequestParam("photo") MultipartFile photo, HttpSession session, Model model) {

		Student sessionStudent = (Student) session.getAttribute("student");

		if (sessionStudent == null) {
			model.addAttribute("msg", "Session expired. Please login again!");
			return "studentLogin";
		}

		StudentEntity existingStudent = studentDAO.getStudentById(sessionStudent.getStudentId());
		if (existingStudent == null) {
			model.addAttribute("msg", "Student not found!");
			return "studentLogin";
		}

		existingStudent.setAddress(student.getAddress());
		existingStudent.setContactNumber(student.getContactNumber());
		existingStudent.setGender(student.getGender());
		existingStudent.setIsActive(true);

		try {
			if (!photo.isEmpty()) {
				existingStudent.setProfilePhoto(photo.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "Photo upload failed!");
			return "updateProfile";
		}

		studentDAO.updateProfile(existingStudent);

		// : Convert entity to DTO before storing in session
		Student updatedStudent = new Student();
		BeanUtils.copyProperties(existingStudent, updatedStudent);
		updatedStudent.setProfilePhoto(existingStudent.getProfilePhoto());

		session.setAttribute("student", updatedStudent);
		model.addAttribute("student", updatedStudent);
		model.addAttribute("msg", "Profile updated successfully!");

		return "studentProfile";
	}

	@GetMapping("/student/profile-photo/{id}")
	public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Integer id) {
		Optional<StudentEntity> optionalStudent = studentService.findById(id);

		if (optionalStudent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		StudentEntity student = optionalStudent.get();
		byte[] image = student.getProfilePhoto();

		if (image != null && image.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // or detect from stored format
			return new ResponseEntity<>(image, headers, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build(); // or serve a default image
		}
	}

	// change password logic

	@GetMapping("/student/passwordchange")
	public String passwordChange(Model model, HttpSession session) {
		// get the student object from the session
		Student student = (Student) session.getAttribute("student");
		if (student == null) {
			model.addAttribute("msg", "session expried....Please login Again!");
			return "studentLogin"; // or some fallback
		}
		model.addAttribute("student", student);

		return "studentPasswordChange";
	}

	// Password change handler
	@PostMapping("/student/passwordchange")
	public String passwordChangeHandler(Model model, HttpSession session, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		// Get the student object from session
		Student student = (Student) session.getAttribute("student");

		if (student == null) {
			model.addAttribute("errorMsg", "Session expired. Please login again.");
			return "studentLogin"; // Redirect or show login page
		}

		// Check if the old password entered matches the one in session
		if (!student.getPassword().equals(oldPassword)) {
			model.addAttribute("student", student);
			model.addAttribute("errorMsg", "Old password is incorrect.");
			return "studentPasswordChange";
		}

		// Prevent reusing the old password
		if (oldPassword.equals(newPassword)) {
			model.addAttribute("student", student);
			model.addAttribute("errorMsg", "New password must be different from the old password.");
			return "studentPasswordChange";
		}

		// Update password in the database
		student.setPassword(newPassword);

		// update the student in the session object
		session.setAttribute("student", student);
		// add to model object
		model.addAttribute("student", student);

		// copy student to student Entitie
		StudentEntity studentEntity = new StudentEntity();

		BeanUtils.copyProperties(student, studentEntity);
		System.out.println("student entitie :" + studentEntity);

		// repo.save() method used for the password update also
		studentDAO.updateProfile(studentEntity); // Ensure this method updates the DB

		model.addAttribute("successMsg", "Password updated successfully.");
		return "studentPasswordChange";

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "logout";

	}

	/* ===========EXAM WRITING LOGICS ==================== */
	
	
	//attemptExam Page
	
	@GetMapping("/student/upcommingexams")
	public String viewUpcomingExams(HttpSession session, Model model) {
	    Student student = (Student) session.getAttribute("student");
	    if (student == null) {
	        model.addAttribute("msg", "Session expired. Please login again.");
	        return "studentLogin";
	    }

	    LocalDate today = LocalDate.now();
	    List<ExamEntity> exams = examDAO.findByIsActiveTrueAndIsPublishedTrueAndExamDateGreaterThanEqualOrderByExamDateAsc(today);

	    // Fetch attempts of this student
	    List<StudentExamAttemptEntity> attempts = studentExamAttemptRepository.findByStudent_StudentId(student.getStudentId());
	    Set<Integer> attemptedExamIds = attempts.stream().map(a -> a.getExam().getExamId()).collect(Collectors.toSet());

	    
	    // Get current time
	    LocalTime now = LocalTime.now();

	    model.addAttribute("exams", exams);
	    model.addAttribute("attemptedExamIds", attemptedExamIds);
	    model.addAttribute("currentTime", now);  
	    model.addAttribute("student", student);

	    return "studentUpcommingExams";
	}
	
	
	 @GetMapping("/student/examPage/{id}")
	    public String showExamPage(@PathVariable("id") Integer examId,
	                               HttpSession session,
	                                Model model) {

		 Student student = (Student) session.getAttribute("student");
		    if (student == null) {
		        model.addAttribute("msg", "Session expired. Please login again.");
		        return "studentLogin";
		    }
	        // Fetch exam
	        ExamEntity exam = examDAO.getExamById(examId);
	        if (exam == null) {
	            model.addAttribute("error", "Exam not found");
	            return "error";
	        }

	        // Validate exam time
	        LocalTime now = LocalTime.now();
	        if (now.isBefore(exam.getStartTime()) || now.isAfter(exam.getEndTime())) {
	            model.addAttribute("error", "You can only access the exam during the scheduled time.");
	            return "error";
	        }

	        // Check if student already attempted
	        Boolean isAttempted = studentExamAttemptRepository.existsByStudent_StudentIdAndExam_ExamId(student.getStudentId(), examId);
	        if (isAttempted) {
	            model.addAttribute("error", "You have already attempted this exam.");
	            return "error";
	        }

	        // Load questions for the exam
	        List<QuestionEntity> questions = questionDAO.getQuestionsByExamId(examId);

	        model.addAttribute("exam", exam);
	        model.addAttribute("questions", questions);
	        model.addAttribute("student", student);

	        return "studentWriteExam";  
	    }

	
	//after submiting the exam 
	 @PostMapping("/student/submitExam")
	 public String submitExam(@RequestParam Integer examId,
	                          @RequestParam Map<String, String> params,
	                          HttpSession session,
	                          Model model) {

	     // Step 1: Validate session
	     Student student = (Student) session.getAttribute("student");
	     if (student == null) {
	         model.addAttribute("msg", "Session expired. Please login again.");
	         return "studentLogin";
	     }

	     // Step 2: Fetch exam and questions
	     ExamEntity exam = examDAO.getExamById(examId);
	     List<QuestionEntity> questions = questionDAO.getQuestionsByExamId(examId);

	     if (questions == null || questions.isEmpty()) {
	         model.addAttribute("msg", "No questions found for this exam.");
	         return "errorPage";
	     }

	     // Step 3: Extract submitted answers
	     Map<Integer, String> answers = new HashMap<>(); // index -> selected option (e.g., "A")
	     for (String key : params.keySet()) {
	         if (key.startsWith("answers[")) {
	             try {
	                 int index = Integer.parseInt(key.substring(8, key.length() - 1));
	                 answers.put(index, params.get(key));
	             } catch (NumberFormatException e) {
	                 // Skip malformed keys
	             }
	         }
	     }

	     // Step 4: Evaluate score
	     int correctCount = 0;
	     for (int i = 0; i < questions.size(); i++) {
	         QuestionEntity question = questions.get(i);
	         String studentAnswer = answers.get(i);  // Will be "A", "B", "C", or "D"
	         String correctAnswer = question.getCorrectOption(); // Must also be "A", "B", "C", "D"

	         if (studentAnswer != null && studentAnswer.equalsIgnoreCase(correctAnswer)) {
	             correctCount++;
	         }
	     }

	     double score = ((double) correctCount / questions.size()) * 100.0;

	     // Step 5: Save attempt
	     StudentExamAttemptEntity attempt = new StudentExamAttemptEntity();

	     // Convert session DTO -> Entity
	     StudentEntity studentEntity = new StudentEntity();
	     BeanUtils.copyProperties(student, studentEntity);

	     attempt.setStudent(studentEntity);
	     attempt.setExam(exam);
	     attempt.setScore(score);

	     studentExamAttemptRepository.save(attempt);

	     // Step 6: Return result page
	     model.addAttribute("score", score);
	     model.addAttribute("correct", correctCount);
	     model.addAttribute("total", questions.size());
	     model.addAttribute("exam", exam);

	     return "studentExamResult"; // Ensure this template exists
	 }

	   //Exam History Page
	 
	    @GetMapping("/student/examHistory")
	    public String showExamHistory(HttpSession session, Model model) {
	        Student student = (Student) session.getAttribute("student");
	        if (student == null) {
	            model.addAttribute("msg", "Session expired. Please login again.");
	            return "studentLogin";
	        }
	        List<StudentExamAttemptEntity> attempts = studentExamAttemptRepository.findByStudent_StudentId(student.getStudentId());
	        model.addAttribute("attempts", attempts);
	        model.addAttribute("student", student);
	        return "studentExamHistory";
	    }
	
	 
	 
	 
	 

}
















