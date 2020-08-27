package nuc.hzb.mapper;

import java.util.List;

import nuc.hzb.entity.Student;
import nuc.hzb.entity.StudentClass;




public interface studentMapper {
		
	List<Student> queryWithStudentWithOO(int stuno);
	
	StudentClass queryClassAndStudents(int classid);
}
