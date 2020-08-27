package nuc.hzb.mapper;

import java.util.List;

import nuc.hzb.entity.Student;



// 操作MyBatis接口
public interface studentMapper {
	
	/**
	 * 1.方法名和mapper.xml文件中标签的id值相同
	 * 2.方法的输入参数和mapper.xml文件中标签的parameterType类型一致
	 * 3.方法的返回值和mapper.xml文件中标签的resultType类型一致
	 */
	Student queryStudentByStuno(int stuno);
	List<Student> queryAllStudents();
	void addStudent(Student student);
	void deleteStudentByStuno(int stuno);
	void updateStudentByStuno(Student student);
	
	
	Student queryStudentByStuname(String stuname);
	List<Student> queryStudentOrderByColumn(String column);
	List<Student> queryStudentByStuAgeOrStuName(Student student);
	
	
}
