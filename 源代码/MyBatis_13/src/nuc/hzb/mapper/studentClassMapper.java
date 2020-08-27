package nuc.hzb.mapper;

import java.util.List;

import nuc.hzb.entity.StudentClass;

public interface studentClassMapper {
	//查询全部班级
	List<StudentClass> queryClassAndStudents();
}
