package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.StudentBusiness;
import nuc.hzb.mapper.studentMapper;

public class TestMyBatis {

	
	public static void queryWithStudentBusiness() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		studentMapper studentmapper = session.getMapper(studentMapper.class);	
		List<StudentBusiness> studentBusinesses = studentmapper.queryWithStudentBusiness(2);
		System.out.println(studentBusinesses);
		session.close();
	}
	
	
	public static void main(String args[]) throws IOException {
		queryWithStudentBusiness();
	}
}
