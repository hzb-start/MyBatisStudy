package nuc.hzb.test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import nuc.hzb.entity.Student;

public class TestMyBatis {

	public static void queryStudentByStuno() throws IOException {
		// SqlSession相当于Connection，SqlSession操作MyBatis
		// 将普通的硬盘存储的文件读到Reader对象中
		Reader reader = Resources.getResourceAsReader("conf.xml");
		// reader产生一个SqlSession对象，用来操作数据库
		// 这里的设计模式使用了工厂模式
		// 通过build的第二个参数指定数据库环境，一般不这样处理
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader, "development");
		SqlSession session = sessionFactory.openSession();
		// 1就是输入参数
		String statement = "nuc.hzb.entity.studentMapper.queryStudentByStuno";
		Student student = session.selectOne(statement, 1); 
		System.out.println(student);
		session.close();
	}
	
	public static void queryAllStudents() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		String statement = "nuc.hzb.entity.studentMapper.queryAllStudents";
		List<Student> students = session.selectList(statement);
		System.out.println(students);
		session.close();
	}
	
	public static void addStudent() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		String statement = "nuc.hzb.entity.studentMapper.addStudent";
		Student student = new Student(3, "hhh", 20, "hhhhhh");
		int count = session.insert(statement, student);
		// statement：指定执行的sql，student：sql中需要的参数（？？？？）
		System.out.println("增加了" + count + "个学生" );
		session.commit(); // 提交事务
		session.close();
	}
	
	public static void deleteStudentByStuno() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		String statement = "nuc.hzb.entity.studentMapper.deleteStudentByStuno";
		int count = session.delete(statement, 3);
		System.out.println("删除了" + count + "个学生" );
		session.commit(); // 提交事务
		session.close();
	}
	
	public static void updateStudentByStuno() throws IOException {
		Reader reader = Resources.getResourceAsReader("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader, "development");
		SqlSession session = sessionFactory.openSession();
		String statement = "nuc.hzb.entity.studentMapper.updateStudentByStuno";
		// 修改的参数
		Student student = new Student();
		// 修改哪个人，where stuno = 2；
		student.setStuNo(2);
		student.setStuName("黄朝博");
		student.setStuAge(18);
		student.setGraName("hhh");
		int count = session.update(statement, student);
		System.out.println("修改了" + count + "个学生" );
		session.commit(); // 提交事务
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
