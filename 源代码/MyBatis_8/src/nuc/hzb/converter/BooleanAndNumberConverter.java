package nuc.hzb.converter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;


public class BooleanAndNumberConverter extends BaseTypeHandler<Boolean> {

	// set方法是java（Boolean）到db（Number）
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub
		if (parameter) {
			// 如果是true，将该变量的值变成1
			ps.setInt(i, 1);
		} else {
			// 如果是false，将该变量的值变成0
			ps.setInt(i, 0);
		}
		
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		int sexNum = rs.getInt(columnName);
		/*
		 * if (sexNum == 1) { return true; } else { return false; }
		 */
		return sexNum == 1?true:false;
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		int sexNum = rs.getInt(columnIndex);
		return sexNum == 1?true:false;
	}

	@Override
	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		int sexNum = cs.getInt(columnIndex);
		return sexNum == 1?true:false;
	}

}
