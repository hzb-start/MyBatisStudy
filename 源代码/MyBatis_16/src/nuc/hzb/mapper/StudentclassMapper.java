package nuc.hzb.mapper;

import java.util.List;
import nuc.hzb.entity.Studentclass;
import nuc.hzb.entity.StudentclassExample;
import org.apache.ibatis.annotations.Param;

public interface StudentclassMapper {
    long countByExample(StudentclassExample example);

    int deleteByExample(StudentclassExample example);

    int deleteByPrimaryKey(Short classid);

    int insert(Studentclass record);

    int insertSelective(Studentclass record);

    List<Studentclass> selectByExample(StudentclassExample example);

    Studentclass selectByPrimaryKey(Short classid);

    int updateByExampleSelective(@Param("record") Studentclass record, @Param("example") StudentclassExample example);

    int updateByExample(@Param("record") Studentclass record, @Param("example") StudentclassExample example);

    int updateByPrimaryKeySelective(Studentclass record);

    int updateByPrimaryKey(Studentclass record);
}