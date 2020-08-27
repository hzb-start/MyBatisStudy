package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.Student;
import nuc.hzb.mapper.studentMapper;

public class TestMyBatis {

		
	public static void queryStudentCount() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		int count = studentmapper.queryStudentCount();
		System.out.println(count);
		session.close();
	}
	
	public static void queryStudentByStuno() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Student student = studentmapper.queryStudentByStuno(2);
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
	
	public static void queryStuOutByHashMap() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		HashMap<String,  Object> studentMap = studentmapper.queryStuOutByHashMap(5);
		System.out.println(studentMap);
		session.close();
	}
	
	public static void queryStuOutByHashMaps() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		List<HashMap<String,  Object>> studentMapList = studentmapper.queryStuOutByHashMaps(5);
		System.out.println(studentMapList);
		session.close();
	}
	
	public static void main(String args[]) throws IOException {
		queryStudentCount();
		queryStudentByStuno();
		queryAllStudents();
		queryStuOutByHashMap();
		queryStuOutByHashMaps();
	}
}
