package nuc.hzb.mapper;

import java.util.List;

import nuc.hzb.entity.Student;




public interface studentMapper {
		
	List<Student> queryWithStudentWithOO(int stuno);
}
