package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.Student;
import nuc.hzb.entity.StudentClass;
import nuc.hzb.mapper.studentMapper;

public class TestMyBatis {
	
	public static void queryWithStudentBusiness() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);	
		List<Student> studentBusinesses = studentmapper.queryWithStudentWithOO(5);
		System.out.println(studentBusinesses);
		session.close();
	}
	
	//查询班级 和班级对应的学生，一对多
	public static void queryClassAndStudents() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml") ;
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);	
		StudentClass studentClass  = studentmapper.queryClassAndStudents(1) ;
		System.out.println(studentClass.getClassId() + "," + studentClass.getClassName());
		List<Student> students = studentClass.getStudents() ;
		for(Student student : students) {
			System.out.println(student.getStuNo() + "," + student.getStuName() + "," + student.getStuAge());
		}
		session.close();
	}
	
	public static void main(String args[]) throws IOException {
		queryWithStudentBusiness();
		queryClassAndStudents();
	}
}
