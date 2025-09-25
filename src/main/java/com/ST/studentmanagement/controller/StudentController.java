package com.ST.studentmanagement.controller;

import com.ST.studentmanagementsystem.model.*;
import com.ST.studentmanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private AbsentDetailsRepository absentDetailsRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentNoticeRepository studentNoticeRepository;

    // ====== SIGNUP ======
    @GetMapping("/signup")
    public String showSignupForm() {
        return "student_signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute Student student) {
        studentRepository.save(student);
        return "redirect:/student/login";
    }

    // ====== LOGIN ======
    @GetMapping("/login")
    public String showLoginForm() {
        return "student_login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Student student = studentRepository.findByEmail(email);
        if (student != null && student.getPassword().equals(password)) {
            session.setAttribute("loggedStudent", student); // store logged-in student
            return "redirect:/student/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "student_login";
        }
    }

    // ====== DASHBOARD ======
    @GetMapping("/dashboard")
    public String studentDashboard() {
        return "student_dashboard";
    }

    // ====== ATTENDANCE ======
    @GetMapping("/attendance")
    public String viewAttendance(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) return "redirect:/student/login";

        List<Attendance> attendanceList = StreamSupport.stream(attendanceRepository.findAll().spliterator(), false)
                .filter(a -> a.getStudentId().equals(student.getId()))
                .collect(Collectors.toList());
        model.addAttribute("attendanceList", attendanceList);
        return "student_attendance";
    }

    // ====== ABSENT DETAILS ======
    @GetMapping("/absent")
    public String viewAbsentDetails(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) return "redirect:/student/login";

        List<AbsentDetails> absentList = StreamSupport.stream(absentDetailsRepository.findAll().spliterator(), false)
                .filter(a -> a.getStudentId().equals(student.getId()))
                .collect(Collectors.toList());
        model.addAttribute("absentList", absentList);
        return "student_absent";
    }

    // ====== MARKS ======
    
    @GetMapping("/marks")
    public String viewMarks(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) return "redirect:/student/login";

        List<Marks> marksList = marksRepository.findByStudentId(student.getId());
        model.addAttribute("marksList", marksList);
        return "student_marks";
    }

    // ====== SUBJECTS ======
    
    @GetMapping("/subjects")
    public String viewSubjects(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) return "redirect:/student/login";

        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);
        return "student_subjects";
    }

    // ====== SEND NOTICE ======
    @GetMapping("/notice")
    public String showNoticeForm(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) return "redirect:/student/login";

        model.addAttribute("studentId", student.getId());
        return "student_notice";
    }

    @PostMapping("/notice")
    public String sendNotice(HttpSession session,
                             @RequestParam String message) {
        Student student = (Student) session.getAttribute("loggedStudent");
        if (student == null) return "redirect:/student/login";

        StudentNotice notice = new StudentNotice();
        notice.setStudentId(String.valueOf(student.getId())); // stored as String in your entity
        notice.setMessage(message);
        studentNoticeRepository.save(notice);

        return "redirect:/student/dashboard";
    }
}
