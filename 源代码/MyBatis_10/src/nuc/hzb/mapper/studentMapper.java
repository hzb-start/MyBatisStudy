package nuc.hzb.mapper;

import java.util.List;

import nuc.hzb.entity.StudentBusiness;



public interface studentMapper {
		
	List<StudentBusiness> queryWithStudentBusiness(int stuno);
}
