package nuc.hzb.mapper;


import java.util.List;

import nuc.hzb.entity.Grade;
import nuc.hzb.entity.Student;



// 操作MyBatis接口
public interface studentMapper {
		
	Student queryStuByNOrAWithSQLTag(Student student);
	
	List<Student> queryStuWithNosInGrade(Grade grade);
	
	List<Student> queryStudentsWithArray(int[] stuNos);
	
	List<Student> queryStudentsWithObjectArray(Student[] students);
	
	List<Student> queryStudentsWithObjectArray1(Student[] students);
}
