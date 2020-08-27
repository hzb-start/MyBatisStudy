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
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Student student1 = studentmapper.queryStudentByStuno(2);
		session.commit();
		Student student2 = studentmapper.queryStudentByStuno(2);
		System.out.println(student1);
		System.out.println(student2);
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
		
		SqlSession session1 = sessionFactory.openSession();
		studentMapper studentmapper1 = session1.getMapper(studentMapper.class);
		List<Student> students1 = studentmapper1.queryAllStudents();
		System.out.println(students1);
		session1.close();
		
		SqlSession session2 = sessionFactory.openSession();
		studentMapper studentmapper2 = session2.getMapper(studentMapper.class);
		List<Student> students2 = studentmapper2.queryAllStudents();
		System.out.println(students2);
		session2.close();	
	}
		
	public static void main(String args[]) throws IOException {
		queryStudentByStuno();	
		queryAllStudents();
	}
}
