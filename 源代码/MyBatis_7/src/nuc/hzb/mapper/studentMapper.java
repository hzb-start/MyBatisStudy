package nuc.hzb.mapper;

import java.util.List;
import java.util.Map;

import nuc.hzb.entity.Address;
import nuc.hzb.entity.Student;



// 操作MyBatis接口
public interface studentMapper {
	
	/**
	 * 1.方法名和mapper.xml文件中标签的id值相同
	 * 2.方法的输入参数和mapper.xml文件中标签的parameterType类型一致
	 * 3.方法的返回值和mapper.xml文件中标签的resultType类型一致
	 */
	
	List<Student> queryStudentByAddress(Address address);
	List<Student> queryStudentByAddress1(Student student);
	List<Student> queryStudentByStuAgeOrStuNameWithHashMap(Map<String, Object> map);
	
	
	void queryCountByGradeWithProcedure(Map<String, Object> params);
	void deleteStuByNoWithProcedure(Map<String, Object> sno);
	
}
