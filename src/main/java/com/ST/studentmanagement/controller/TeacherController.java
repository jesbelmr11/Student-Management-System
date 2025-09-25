package com.ST.studentmanagement.controller;

import com.ST.studentmanagementsystem.model.*;
import com.ST.studentmanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentNoticeRepository studentNoticeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;  // Add this

    // ====== SIGNUP ======
    @GetMapping("/signup")
    public String showSignupForm() {
        return "teacher_signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute Teacher teacher) {
        teacherRepository.save(teacher);
        return "redirect:/teacher/login";
    }

    // ====== LOGIN ======
    @GetMapping("/login")
    public String showLoginForm() {
        return "teacher_login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        Teacher teacher = teacherRepository.findByEmail(email.trim());

        if (teacher != null && teacher.getPassword().equals(password)) {
            session.setAttribute("loggedTeacher", teacher);
            return "redirect:/teacher/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "teacher_login";
        }
    }

    // ====== DASHBOARD ======
    @GetMapping("/dashboard")
    public String teacherDashboard(Model model) {
        return "teacher_dashboard";
    }

    // ====== VIEW & ADD MARKS ======
    @GetMapping("/marks")
    public String manageMarks(Model model) {
        List<Marks> marksList = new ArrayList<>();
        marksRepository.findAll().forEach(marksList::add);
        model.addAttribute("marksList", marksList);
        return "teacher_marks";
    }

    @PostMapping("/marks")
    public String addMarks(@RequestParam Long Id,
                           @RequestParam String subject,
                           @RequestParam int mark,
                           Model model) {

        Student student = studentRepository.findById(Id).orElse(null);
        if (student == null) {
            model.addAttribute("error", "Student not found for ID: " + Id);
            model.addAttribute("marksList", getAllMarks());
            return "teacher_marks";
        }

        Marks marks = new Marks();
        marks.setStudentId(student.getId());
        marks.setName(student.getName());
        marks.setSubject(subject);
        marks.setMark(mark);
        marksRepository.save(marks);

        model.addAttribute("success",
                "Marks saved for " + student.getName() + " in " + subject);
        model.addAttribute("marksList", getAllMarks());

        return "teacher_marks";
    }

    private List<Marks> getAllMarks() {
        List<Marks> marksList = new ArrayList<>();
        marksRepository.findAll().forEach(marksList::add);
        return marksList;
    }

    // ====== SUBJECTS ======
 // GET method for subjects page
    @GetMapping("/subjects")
    public String manageSubjects(Model model) {
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);
        return "teacher_subjects";
    }

    // POST method to add a new subject
    @PostMapping("/subjects")
    public String addSubject(@RequestParam String subjectName,
                             @RequestParam String teacherName,
                             Model model) {

        // Find teacher by name
        Teacher teacher = teacherRepository.findByNameIgnoreCase(teacherName.trim());

        if (teacher == null) {
            // Show error and stop before trying to access teacher.getName()
            model.addAttribute("error", "Teacher not found with name: " + teacherName);
        } else {
            // Save subject safely
        	Subject subject = new Subject();
        	subject.setName(subjectName);        // ✅ use this
        	subject.setTeacherName(teacher.getName());
        	subject.setTeacherId(teacher.getId());
        	subjectRepository.save(subject);
            model.addAttribute("success", "Subject added successfully!");
        }

        // Always return subjects page with updated list
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);
        return "teacher_subjects";
    }



    // ====== ATTENDANCE ======
 // GET method
    @GetMapping("/attendance")
    public String showAttendanceForm(Model model) {
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        model.addAttribute("students", students);
        return "teacher_attendance";
    }

    // POST method
    @PostMapping("/attendance/submit")
    public String saveAttendance(@RequestParam List<Long> Id,
                                 @RequestParam List<String> statusList,
                                 @RequestParam String date,
                                 Model model) {

        for (int i = 0; i < Id.size(); i++) {
            Attendance attendance = new Attendance();
            attendance.setStudentId(Id.get(i)); // matches entity type Long
            attendance.setStatus(statusList.get(i));
            attendance.setDate(date);
            attendanceRepository.save(attendance);
        }

        return "redirect:/teacher/attendance";
    }



    // ====== VIEW STUDENT NOTICES ======
    @GetMapping("/notices")
    public String viewNotices(Model model) {
        List<StudentNotice> notices = new ArrayList<>();
        studentNoticeRepository.findAll().forEach(notices::add);
        model.addAttribute("notices", notices);
        return "teacher_read_notices";
    }

    // ====== VIEW STUDENT DETAILS ======
    @GetMapping("/students")
    public String viewStudents(Model model) {
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        model.addAttribute("students", students);
        return "teacher_students";
    }
}
