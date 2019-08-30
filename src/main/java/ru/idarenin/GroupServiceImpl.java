package ru.idarenin;

import ru.idarenin.DAO.StudentDAO;
import ru.idarenin.DAO.TeacherDAO;
import ru.idarenin.Entity.Student;
import ru.idarenin.Entity.Teacher;

import javax.jws.WebService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@WebService(endpointInterface = "ru.idarenin.GroupService", serviceName = "GroupService")
public class GroupServiceImpl implements GroupService, ServletContextListener {

	private final String admLogin = "admin";
	private final String admPassword = "password";

	@Override
	public String save(GroupType parameters) {
		List<StudentType> students = parameters.getStudents().getStudent();
		List<TeacherType> teachers = parameters.getTeachers().getTeacher();
		String groupId = parameters.getId();

		StudentDAO studentDAO = StudentDAO.getInstance();
		TeacherDAO teacherDAO = TeacherDAO.getInstance();

		if (students != null && students.size() > 0) {
			for (StudentType item : students) {
				int id = ThreadLocalRandom.current().nextInt(1, 100000);

				Student student = new Student();
				student.setGroupid(groupId);
				student.setId(id);
				student.setName(item.getName());
				student.setSurname(item.getSurname());

				studentDAO.save(student);
			}
		}

		if (teachers != null && teachers.size() > 0) {
			for (TeacherType item : teachers) {
				int id = ThreadLocalRandom.current().nextInt(1, 100000);

				Teacher teacher = new Teacher();
				teacher.setGroupid(groupId);
				teacher.setId(id);
				teacher.setName(item.getName());
				teacher.setSurname(item.getSurname());
				teacher.setSalary(item.getSalary().doubleValue());

				teacherDAO.save(teacher);
			}
		}


		return "OK";
	}

	@Override
	public GroupsType get(UserInfoType parameters) {
		String login = parameters.getLogin();
		String password = parameters.getPassword();

		GroupsType resp = new GroupsType();

		StudentDAO studentDAO = StudentDAO.getInstance();
		TeacherDAO teacherDAO = TeacherDAO.getInstance();

		List<Student> students;
		List<Teacher> teachers ;

		if (admLogin.equals(login) && admPassword.equals(password)) {
			students = studentDAO.getAll();
			teachers = teacherDAO.getAll();

			Map<String, List<Student>> studentMap = new HashMap<>();
			Map<String, List<Teacher>> teacherMap = new HashMap<>();

			if (students != null && students.size() > 0) {
				for (Student item : students) {
					List<Student> tempList = studentMap.get(item.getGroupid());

					if (tempList == null) {
						tempList = new ArrayList<>();
						tempList.add(item);
						studentMap.put(item.getGroupid(), tempList);
					} else {
						tempList.add(item);
					}
				}
			}

			if (teachers != null && teachers.size() > 0) {
				for (Teacher item : teachers) {
					List<Teacher> tempList = teacherMap.get(item.getGroupid());

					if (tempList == null) {
						tempList = new ArrayList<>();
						tempList.add(item);
						teacherMap.put(item.getGroupid(), tempList);
					} else {
						tempList.add(item);
					}
				}
			}

			for (String groupId : studentMap.keySet()) {
				students = studentMap.get(groupId);
				teachers = teacherMap.get(groupId);

				GroupType group = RespUtil.performGroup(students, teachers, groupId);

				resp.getGroup().add(group);
			}
		} else {
			String groupId = null;
			students = studentDAO.getByLogin(login, password);
			if (students != null && students.size() > 0) {
				groupId = students.get(0).getGroupid();
			} else {
				teachers = teacherDAO.getByLogin(login, password);
				if (teachers != null && teachers.size() > 0) {
					groupId = teachers.get(0).getGroupid();
				}
			}

			if (groupId != null) {
				students = studentDAO.getByGroup(groupId);
				teachers = teacherDAO.getByGroup(groupId);

				GroupType group = RespUtil.performGroup(students, teachers, groupId);

				resp.getGroup().add(group);
			}
		}

		return resp;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//здесь заполнение справочников
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}