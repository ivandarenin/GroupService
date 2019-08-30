package ru.idarenin.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.idarenin.Entity.Student;
import ru.idarenin.HibernateUtil;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class StudentDAO {

    private static final StudentDAO instance = new StudentDAO();

    private StudentDAO(){}

    public static StudentDAO getInstance() {
        return instance;
    }

    public void save(Student student) {
        try(Session session = HibernateUtil.getSession()) {
            Transaction tx1 = session.beginTransaction();
            session.save(student);
            tx1.commit();
        }
    }

    public List<Student> getAll() {
        List<Student> students;
        try(Session session = HibernateUtil.getSession()) {
            students = session.createCriteria(Student.class).list();
        }
        return students;
    }

    public List<Student> getByGroup(String groupId) {
        List<Student> students;
        try(Session session = HibernateUtil.getSession()) {
            students = session.createQuery(
                    "from Student where groupid = :groupid"
            ).setString("groupid", groupId).list();
        }
        return students;
    }

    public List<Student> getByLogin(String login, String password) {
        List<Student> students;
        try(Session session = HibernateUtil.getSession()) {
            Query query = session.createQuery(
                    "select s"
                            + " from Student s"
                            + " where s.id in ("
                            + " select u.idUser"
                            + " from User u"
                            + " where u.login = :login"
                            + " and u.password = :password)"
            )
                    .setString("login", login)
                    .setString("password", password);
            students = (List<Student>) query.list();
        }
        return students;
    }
}
