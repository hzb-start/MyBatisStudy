package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.Student;
import nuc.hzb.mapper.studentMapper;

public class TestMyBatis {

	public static void queryStudentByStuno() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader, "development");
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Student student = studentmapper.queryStudentByStuno(1);
		System.out.println(student);
		session.close();
	}
	
	public static void queryAllStudents() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		List<Student> students = studentmapper.queryAllStudents();
		System.out.println(students);
		session.close();
	}
	
	public static void addStudent() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		Student student = new Student(3, "hhh", 20, "hhhhhh");
	    studentMapper studentmapper = session.getMapper(studentMapper.class);
	    studentmapper.addStudent(student);
		System.out.println("增加成功" );
		session.commit();
		session.close();
	}
	
	public static void deleteStudentByStuno() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		studentmapper.deleteStudentByStuno(3);
		System.out.println("删除成功" );
		session.commit();
		session.close();
	}
	
	public static void updateStudentByStuno() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader, "development");
		SqlSession session = sessionFactory.openSession();
		Student student = new Student();
		student.setStuNo(2);
		student.setStuName("黄朝博");
		student.setStuAge(18);
		student.setGraName("hhh");
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		studentmapper.updateStudentByStuno(student);
		System.out.println("修改成功" );
		session.commit();
		session.close();
	}
	

	public static void main(String args[]) throws IOException {
		queryStudentByStuno();
		queryAllStudents();
		addStudent();
		deleteStudentByStuno();
		updateStudentByStuno();
	}
}
