package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import nuc.hzb.entity.Person;

public class TestMyBatis {
	
	public static void main(String[] args) throws IOException {
		// 加载MyBatis配置文件（为了访问数据库）
		Reader reader = Resources.getResourceAsReader("conf.xml");
		// SqlSessionFactory
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		// openSession()相当于之前的connection
		SqlSession session = sessionFactory.openSession();
		String statement = "nuc.hzb.entity.personMapper.queryPersonById";
		Person person = session.selectOne(statement, 1);
		System.out.println(person);
		session.close();
	}

}
