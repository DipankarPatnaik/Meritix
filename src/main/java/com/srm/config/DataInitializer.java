package com.srm.config;

import com.srm.model.Marks;
import com.srm.model.Student;
import com.srm.model.Teacher;
import com.srm.repository.MarksRepository;
import com.srm.repository.StudentRepository;
import com.srm.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes realistic sample academic data when the database is empty.
 * Ensures the dashboard immediately displays rich metrics, charts,
 * and records upon first launch while preserving full ability to sign up
 * and add new teachers, students, and marks.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Override
    public void run(String... args) {
        if (teacherRepository.count() > 0 || studentRepository.count() > 0) {
            return; // Data already exists
        }

        // 1. Create Faculty / Teachers
        Teacher t1 = teacherRepository.save(new Teacher("Dr. Marcus Vance", "Database Systems & Architecture", "marcus.v", "password123"));
        Teacher t2 = teacherRepository.save(new Teacher("Prof. Alan Turing", "Data Structures & Algorithms", "alan.t", "password123"));
        Teacher t3 = teacherRepository.save(new Teacher("Dr. Sarah Jenkins", "Advanced Applied Mathematics", "sarah.j", "password123"));
        Teacher t4 = teacherRepository.save(new Teacher("Dr. Elena Rostova", "Computer Networks & Security", "elena.r", "password123"));
        Teacher t5 = teacherRepository.save(new Teacher("Dr. Maya Lin", "Artificial Intelligence & ML", "maya.l", "password123"));

        // 2. Create Students
        Student s1 = studentRepository.save(new Student("Alexander Wright", "CS-2026-042", "alex.w", "password123"));
        Student s2 = studentRepository.save(new Student("Emily Chen", "CS-2026-015", "emily.c", "password123"));
        Student s3 = studentRepository.save(new Student("Marcus Johnson", "CS-2026-088", "marcus.j", "password123"));
        Student s4 = studentRepository.save(new Student("Sophia Patel", "CS-2026-104", "sophia.p", "password123"));
        Student s5 = studentRepository.save(new Student("David Kim", "CS-2026-029", "david.k", "password123"));
        Student s6 = studentRepository.save(new Student("Chloe Bennett", "CS-2026-056", "chloe.b", "password123"));

        // 3. Assign Marks
        // Student 1: Alexander Wright (High Achiever)
        marksRepository.save(new Marks(s1, t1, 92));
        marksRepository.save(new Marks(s1, t2, 88));
        marksRepository.save(new Marks(s1, t3, 95));
        marksRepository.save(new Marks(s1, t4, 84));
        marksRepository.save(new Marks(s1, t5, 90));

        // Student 2: Emily Chen (Excellent)
        marksRepository.save(new Marks(s2, t1, 85));
        marksRepository.save(new Marks(s2, t2, 91));
        marksRepository.save(new Marks(s2, t3, 78));
        marksRepository.save(new Marks(s2, t4, 89));
        marksRepository.save(new Marks(s2, t5, 94));

        // Student 3: Marcus Johnson (Average / Improving)
        marksRepository.save(new Marks(s3, t1, 74));
        marksRepository.save(new Marks(s3, t2, 68));
        marksRepository.save(new Marks(s3, t3, 62));
        marksRepository.save(new Marks(s3, t4, 70));
        marksRepository.save(new Marks(s3, t5, 76));

        // Student 4: Sophia Patel (Top Honors)
        marksRepository.save(new Marks(s4, t1, 96));
        marksRepository.save(new Marks(s4, t2, 98));
        marksRepository.save(new Marks(s4, t3, 94));
        marksRepository.save(new Marks(s4, t4, 91));
        marksRepository.save(new Marks(s4, t5, 97));

        // Student 5: David Kim (Needs Support)
        marksRepository.save(new Marks(s5, t1, 58));
        marksRepository.save(new Marks(s5, t2, 45));
        marksRepository.save(new Marks(s5, t3, 52));
        marksRepository.save(new Marks(s5, t4, 60));
        // t5 pending for David Kim

        // Student 6: Chloe Bennett (Partial pending)
        marksRepository.save(new Marks(s6, t1, 82));
        marksRepository.save(new Marks(s6, t2, 79));
        marksRepository.save(new Marks(s6, t3, 88));
        // t4 & t5 pending for Chloe Bennett
    }
}
