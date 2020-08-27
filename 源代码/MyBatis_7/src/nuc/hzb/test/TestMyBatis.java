package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.Address;
import nuc.hzb.entity.Student;
import nuc.hzb.mapper.studentMapper;

public class TestMyBatis {

		
	public static void queryStudentByAddress() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Address address = new Address();
		address.setHomeAddress("HH");
		address.setSchoolAddress("%Z%");
		List<Student> students = studentmapper.queryStudentByAddress(address);
		System.out.println(students);
		session.close();
	}
	
	public static void queryStudentByAddress1() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Address address = new Address();
		address.setHomeAddress("HH");
		address.setSchoolAddress("%Z%");
		Student student = new Student();
		student.setAddress(address);
		List<Student> students = studentmapper.queryStudentByAddress1(student);
		System.out.println(students);
		session.close();
	}
	
	public static void queryStudentByStuAgeOrStuNameWithHashMap() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);	
		Map<String, Object> studenMap = new HashMap<String, Object>();
		studenMap.put("stuAge", 18);
		studenMap.put("stuName", "hzb");
		List<Student> students = studentmapper.queryStudentByStuAgeOrStuNameWithHashMap(studenMap);
		System.out.println(students);
		session.close();
	}
	
	public static void queryCountByGradeWithProcedure() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gName", "hzb");
		studentmapper.queryCountByGradeWithProcedure(params);
		int count = (int) params.get("sCount");
		System.out.println(count);
		session.close();
	}
	
	public static void deleteStuByNoWithProcedure() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sno", 1);
		studentmapper.deleteStuByNoWithProcedure(params);
		session.commit();
		session.close();
	}
	
	
	public static void main(String args[]) throws IOException {
		queryStudentByAddress();
		queryStudentByAddress1();
		queryStudentByStuAgeOrStuNameWithHashMap();
		
		queryCountByGradeWithProcedure();
		deleteStuByNoWithProcedure();
	}
}
