package com.ashi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ashi.bindings.Admin;
import com.ashi.bindings.ExamDTO;
import com.ashi.bindings.QuestionDTO;
import com.ashi.dao.ExamDAO;
import com.ashi.dao.QuestionDAO;
import com.ashi.entities.AdminEntity;
import com.ashi.entities.ExamEntity;
import com.ashi.entities.QuestionEntity;
import com.ashi.entities.StudentExamAttemptEntity;
import com.ashi.repository.StudentExamAttemptRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ExamController {
	

	@Autowired
	private ExamDAO examDAO;
	
	@Autowired
	private QuestionDAO questionDAO;
	
	@Autowired
	private StudentExamAttemptRepository studentExamAttemptRepository;
	
	
	// manage exam page
		@GetMapping("/manageexams")
		public String adminManageExam(HttpSession session, Model model) {
			Admin admin = (Admin) session.getAttribute("admin");
			if (admin == null) {
				model.addAttribute("msg", "Session Experied..Login Again !");
				return "adminlogin";
			}
			
			//get the avilable exams from DB
			List<ExamEntity> exams = examDAO.findByIsActiveFalseOrderByExamDateAsc();
			
			if(!exams.isEmpty()) {
				model.addAttribute("exams", exams);
			}
			else {
				model.addAttribute("exams", "no exams available");
			}
		
			
			model.addAttribute("admin", admin);
			return "adminManageExam";
		}
	
		

		// create new exam page
		@GetMapping("/createnewexam")
		public String adminCreateExam(HttpSession session, Model model) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        model.addAttribute("msg", "Session Expired..Login Again !");
		        return "adminlogin";
		    }

		    // Only add a new ExamDTO if one isn't already present (e.g., from flash attributes)
		    if (!model.containsAttribute("examDTO")) {
		        model.addAttribute("examDTO", new ExamDTO());
		    }

		    model.addAttribute("admin", admin);
		    return "adminCreateExam";
		}
		
		
		// create new exam handler
		
		@PostMapping("/create-exam")
		public String adminCreateExamHandler(HttpSession session, RedirectAttributes redirectAttributes,
		                                     @ModelAttribute("examDTO") ExamDTO examDTO) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        redirectAttributes.addFlashAttribute("msg", "Session Expired..Login Again !");
		        return "redirect:/admin/adminlogin";
		    }

		    
		   
		    //copy adminDTO to AdminEntity
		    AdminEntity adminEntity = new AdminEntity();
		    BeanUtils.copyProperties(admin, adminEntity);
		    
		    //copy examDTO to entity
		    ExamEntity examEntity = new ExamEntity();
		   
		    BeanUtils.copyProperties(examDTO, examEntity);  
		    examEntity.setAdmin(adminEntity);
		    
		    System.out.println("exam Entity  :"+examEntity);
		    
		    examDAO.saveExam(examEntity);
		    
		    redirectAttributes.addFlashAttribute("examSaved", true);
		    redirectAttributes.addFlashAttribute("admin", admin);
		    redirectAttributes.addFlashAttribute("examDTO", examDTO);

		    return "redirect:/admin/createnewexam"; // REDIRECT, not direct view return
		}

		@GetMapping("/edit-exam/{id}")
		public String showEditExamForm(@PathVariable Integer id, HttpSession session, Model model) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        model.addAttribute("msg", "Session Expired..Login Again !");
		        return "adminlogin";
		    }

		    ExamEntity exam = examDAO.getExamById(id);
		    if (exam == null) {
		        model.addAttribute("msg", "Exam not found!");
		        return "redirect:/admin/manageexams";
		    }

		    // Convert Entity to DTO (optional)
		    ExamDTO dto = new ExamDTO();
		    BeanUtils.copyProperties(exam, dto);
		    dto.setAdmin(admin);
		    dto.setExamDate(exam.getExamDate());     
		    dto.setStartTime(exam.getStartTime());       
		    dto.setEndTime(exam.getEndTime());          
        
		   

		    // Add the DTO to the model

		    model.addAttribute("examDTO", dto);
		    model.addAttribute("admin", admin);

		    return "adminEditExam"; // Thymeleaf page
		}

		
		@PostMapping("/update-exam")
		public String updateExam(@ModelAttribute("examDTO") ExamDTO dto, HttpSession session, RedirectAttributes redirectAttrs) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        redirectAttrs.addFlashAttribute("msg", "Session Expired..Login Again !");
		        return "redirect:/adminlogin";
		    }

		    ExamEntity exam = examDAO.getExamById(dto.getExamId());
		    if (exam == null) {
		        redirectAttrs.addFlashAttribute("msg", "Exam not found!");
		        return "redirect:/admin/manageexams";
		    }

		    // Update fields
		    BeanUtils.copyProperties(dto, exam);
		    examDAO.saveExam(exam);
		    redirectAttrs.addFlashAttribute("msg", "Exam updated successfully!");

		    return "redirect:/admin/manageexams";
		}

		//add questions page 
		@GetMapping("/exam/{id}/add-questions")
		public String showAddQuestionPage(@PathVariable("id") Integer examId, Model model, HttpSession session) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        model.addAttribute("msg", "Session Expired.. Login Again!");
		        return "adminlogin";
		    }

		    ExamEntity exam = examDAO.getExamById(examId);
		    if (exam == null) {
		        model.addAttribute("msg", "Invalid Exam ID");
		        return "redirect:/admin/manageexams";
		    }

		    // Count existing questions
		    int existingQuestionCount = questionDAO.countByExamId(examId);
		    int questionsRemaining = exam.getTotalQuestions() - existingQuestionCount;

		    // Prepare empty QuestionDTO
		    QuestionDTO questionDTO = new QuestionDTO();
		    
		    // Set exam details in DTO
		    ExamDTO examDTO = new ExamDTO();
		    BeanUtils.copyProperties(exam, examDTO);
		    questionDTO.setExam(examDTO);

		    model.addAttribute("admin", admin);
		    model.addAttribute("exam", exam);
		    model.addAttribute("question", questionDTO);
		    model.addAttribute("existingCount", existingQuestionCount);
		    model.addAttribute("remainingCount", questionsRemaining);

		    return "adminAddQuestion"; 
		}


		
		//save question
		@PostMapping("/exam/{id}/save-question")
		public String saveQuestion(
		        @PathVariable("id") Integer examId,
		        @ModelAttribute("question") QuestionDTO questionDTO,
		        RedirectAttributes redirectAttributes,
		        HttpSession session,
		        Model model) {

		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        model.addAttribute("msg", "Session Expired.. Please log in again.");
		        return "adminlogin";
		    }

		    ExamEntity examEntity = examDAO.getExamById(examId);
		    if (examEntity == null) {
		        redirectAttributes.addFlashAttribute("error", "Invalid Exam ID!");
		        return "redirect:/admin/manageexams";
		    }

		    //Convert DTO to Entity manually
		    QuestionEntity questionEntity = new QuestionEntity();
		    questionEntity.setQuestionText(questionDTO.getQuestionText());
		    questionEntity.setOptionA(questionDTO.getOptionA());
		    questionEntity.setOptionB(questionDTO.getOptionB());
		    questionEntity.setOptionC(questionDTO.getOptionC());
		    questionEntity.setOptionD(questionDTO.getOptionD());
		    questionEntity.setCorrectOption(questionDTO.getCorrectOption());

		    //Associate ExamEntity with QuestionEntity
		    questionEntity.setExam(examEntity);

		    // Save to DB
		    questionDAO.saveQuestion(questionEntity);

		    redirectAttributes.addFlashAttribute("msg", "Question added successfully!");
		    return "redirect:/admin/exam/" + examId + "/add-questions";
		}

		
		//delete exam 
		@GetMapping("/delete-exam/{id}")
		public String deleteExam(@PathVariable("id") Integer examId,
		                         RedirectAttributes redirectAttributes,
		                         HttpSession session,
		                         Model model) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        model.addAttribute("msg", "Session expired. Please login again.");
		        return "adminlogin";
		    }

		    try {
		        examDAO.deleteExam(examId);  
		        redirectAttributes.addFlashAttribute("msg", "Exam deleted successfully!");
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("error", "Failed to delete exam. It may have associated questions.");
		    }

		    return "redirect:/admin/manageexams";
		}

		
		//preview the questions
		@GetMapping("/exam/{id}/preview")
		public String previewQuestions(@PathVariable("id") Integer examId, Model model, HttpSession session) {
		    Admin admin = (Admin) session.getAttribute("admin");
		    if (admin == null) {
		        model.addAttribute("msg", "Session expired. Please login again.");
		        return "adminlogin";
		    }

		    // Fetch ExamEntity
		    ExamEntity examEntity = examDAO.getExamById(examId);
		    // Fetch related questions
		    List<QuestionEntity> questionEntities = questionDAO.getQuestionsByExamId(examId);

		    // Convert ExamEntity → ExamDTO
		    ExamDTO examDTO = new ExamDTO();
		    BeanUtils.copyProperties(examEntity, examDTO);

		    // Convert QuestionEntity list → QuestionDTO list
		    List<QuestionDTO> questionDTOs = new ArrayList<>();
		    for (QuestionEntity question : questionEntities) {
		        QuestionDTO dto = new QuestionDTO();
		        BeanUtils.copyProperties(question, dto);

		        // Link ExamDTO to QuestionDTO
		        dto.setExam(examDTO);

		        questionDTOs.add(dto);
		    }

		    // Pass data to view
		    model.addAttribute("admin", admin);
		    model.addAttribute("exam", examDTO);          // Use DTO
		    model.addAttribute("questions", questionDTOs); // Use DTO list

		    return "adminPreviewQuestions";
		}

      
		//active the exam logic
		@PostMapping("/exam/{id}/activate")
		public String activateExam(@PathVariable("id") Integer examId, RedirectAttributes redirectAttributes) {
		    ExamEntity exam = examDAO.getExamById(examId);
		    if (exam != null) {
		        exam.setIsActive(true);
		        examDAO.saveExam(exam);  // Ensure this method exists
		        redirectAttributes.addFlashAttribute("msg", "✅ Exam marked as active!");
		    }
		    return "redirect:/admin/manageexams";
		}

		
		//publish the exam
		 @GetMapping("/exam/{id}/publish")
		    public String publishExam(@PathVariable("id") Integer examId, RedirectAttributes redirectAttributes) {
		        ExamEntity exam = examDAO.getExamById(examId);

		        if (exam == null) {
		            redirectAttributes.addFlashAttribute("error", "Exam not found.");
		            return "redirect:/admin/exams";
		        }

		        if (Boolean.TRUE.equals(exam.getIsPublished())) {
		            redirectAttributes.addFlashAttribute("info", "Exam is already published.");
		            return "redirect:/admin/exams";
		        }

		        exam.setIsPublished(true);
		        // Optional: activate exam on publish
		        exam.setIsActive(true);
		        examDAO.saveExam(exam);

		       
		        return "redirect:/admin/dashboard"; // change as per your list page URL
		    }

		
		//get the results of the published exams
		 @GetMapping("/exam/{id}/results")
		 public String viewExamResults(@PathVariable("id") Integer examId, Model model,HttpSession session) {
			 
			 Admin admin = (Admin) session.getAttribute("admin");
			    if (admin == null) {
			        model.addAttribute("msg", "Session expired. Please login again.");
			        return "adminlogin";
			    }
			 
		     // Fetch exam details
		     ExamEntity exam = examDAO.getExamById(examId);
		     if (exam == null) {
		         model.addAttribute("error", "Exam not found.");
		         return "adminError"; // or redirect to a safer fallback
		     }

		     // Fetch all attempts for this exam
		     List<StudentExamAttemptEntity> attempts = studentExamAttemptRepository.findByExam_ExamId(examId);

		     // Pass data to the view
		     model.addAttribute("exam", exam);
		     model.addAttribute("attempts", attempts);
		     model.addAttribute("admin",admin);
		     
            
		     return "adminExamResults"; // This will be your Thymeleaf template
		 }

	
}
