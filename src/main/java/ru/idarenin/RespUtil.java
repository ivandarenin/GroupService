package ru.idarenin;

import ru.idarenin.Entity.Student;
import ru.idarenin.Entity.Teacher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RespUtil {

    public static GroupType performGroup (List<Student> students, List<Teacher> teachers, String groupId) {
        List<StudentType> studentTypes = new ArrayList<>();
        List<TeacherType> teacherTypes = new ArrayList<>();

        if (students != null && students.size() > 0) {
            for (Student item : students) {
                StudentType studentItem = new StudentType();
                studentItem.setName(item.getName());
                studentItem.setSurname(item.getSurname());

                studentTypes.add(studentItem);
            }
        }
        if (teachers != null && teachers.size() > 0) {
            for (Teacher item : teachers) {
                TeacherType teacherItem = new TeacherType();
                teacherItem.setName(item.getName());
                teacherItem.setSurname(item.getSurname());
                teacherItem.setSalary(BigDecimal.valueOf(item.getSalary()));

                teacherTypes.add(teacherItem);
            }
        }

        GroupType group = new GroupType();
        StudentsType studentsType = new StudentsType();
        TeachersType teachersType = new TeachersType();
        group.setStudents(studentsType);
        group.setTeachers(teachersType);
        group.setId(groupId);

        if (students != null) {
            studentsType.getStudent().addAll(studentTypes);
        }
        if (teachers != null) {
            teachersType.getTeacher().addAll(teacherTypes);
        }

        return group;
    }
}
