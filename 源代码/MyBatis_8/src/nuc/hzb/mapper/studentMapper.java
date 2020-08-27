package nuc.hzb.mapper;

import java.util.HashMap;
import java.util.List;

import nuc.hzb.entity.Student;



// 操作MyBatis接口
public interface studentMapper {
	
	int queryStudentCount();
	Student queryStudentByStuno(int stuno);
	List<Student> queryAllStudents();
	HashMap<String, Object> queryStuOutByHashMap(int stuno);	
	List<HashMap<String, Object>> queryStuOutByHashMaps(int stuno);
}
