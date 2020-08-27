package nuc.hzb.mapper;


import java.util.List;

import nuc.hzb.entity.Student;



// 操作MyBatis接口
public interface studentMapper {
	
	Student queryStudentByStuno(int stuno);
	List<Student> queryAllStudents();

}
