package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.Grade;
import nuc.hzb.entity.Student;
import nuc.hzb.mapper.studentMapper;

public class TestMyBatis {

	public static void queryStuByNOrAWithSQLTag() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Student student = new Student();
		student.setStuAge(20);
		student.setStuName("hhh");
		Student result = studentmapper.queryStuByNOrAWithSQLTag(student);
		System.out.println(result);
		session.close();
	}
	
	public static void queryStuWithNosInGrade() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Grade grade = new Grade();
		List<Integer> stuNos = new ArrayList<Integer>();
		stuNos.add(2);
		stuNos.add(4);
		grade.setStuNos(stuNos);
		List<Student> students = studentmapper.queryStuWithNosInGrade(grade);
		System.out.println(students);
		session.close();
	}
	
	public static void queryStudentsWithArray() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		int[] stuNos = {2, 5};
		List<Student> students = studentmapper.queryStudentsWithArray(stuNos);
		System.out.println(students);
		session.close();
	}
	
	public static void queryStudentsWithObjectArray() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Student student1 = new Student();
		Student student2 = new Student();
		student1.setStuNo(2);
		student2.setStuNo(5);
		Student[] students = new Student[] {student1, student2};
		List<Student> sList = studentmapper.queryStudentsWithObjectArray(students);
		System.out.println(sList);
		session.close();
	}
	
	
	public static void queryStudentsWithObjectArray1() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);
		Student student1 = new Student();
		Student student2 = new Student();
		student1.setStuNo(2);
		student2.setStuNo(5);
		Student[] students = new Student[] {student1, student2};
		List<Student> sList = studentmapper.queryStudentsWithObjectArray(students);
		System.out.println(sList);
		session.close();
	}
	
	
	public static void main(String args[]) throws IOException {
		
		queryStuByNOrAWithSQLTag();
		
		queryStuWithNosInGrade();
		
		queryStudentsWithArray();
		
		queryStudentsWithObjectArray();
		
		queryStudentsWithObjectArray1();
	}
}
