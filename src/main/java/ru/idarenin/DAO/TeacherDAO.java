package ru.idarenin.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.idarenin.Entity.Student;
import ru.idarenin.Entity.Teacher;
import ru.idarenin.HibernateUtil;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class TeacherDAO {
    private static final TeacherDAO instance = new TeacherDAO();

    private TeacherDAO(){}

    public static TeacherDAO getInstance() {
        return instance;
    }

    public void save(Teacher teacher) {
        try(Session session = HibernateUtil.getSession()) {
            Transaction tx1 = session.beginTransaction();
            session.save(teacher);
            tx1.commit();
        }
    }

    public List<Teacher> getAll() {
        List<Teacher> teachers;
        try(Session session = HibernateUtil.getSession()) {
            teachers = session.createCriteria(Teacher.class).list();
        }
        return teachers;
    }

    public List<Teacher> getByGroup(String groupId) {
        List<Teacher> teachers;
        try(Session session = HibernateUtil.getSession()) {
            teachers = session.createQuery(
                    "from Teacher where groupid = :groupid"
            ).setString("groupid", groupId).list();
        }
        return teachers;
    }

    public List<Teacher> getByLogin(String login, String password) {
        List<Teacher> teachers;
        try(Session session = HibernateUtil.getSession()) {
            Query query = session.createQuery(
                    "select t"
                            +" from Teacher t"
                            + " where t.id in ( "
                            + " select u.idUser"
                            + " from User u"
                            + " where u.login = :login"
                            + " and u.password = :password))"
            )
                    .setString("login", login)
                    .setString("password", password);

            teachers = (List<Teacher>) query.list();
        }
        return teachers;
    }
}
