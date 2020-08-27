package nuc.hzb.entity;

public class Student {
	private int stuNo;
	private String stuName;
	private int stuAge;
	private String graName; // gradeName
	private boolean stuSex; 
	
	public Student() {}

	public Student(int stuNo, String stuName, int stuAge, String graName, boolean stuSex) {
		super();
		this.stuNo = stuNo;
		this.stuName = stuName;
		this.stuAge = stuAge;
		this.graName = graName;
		this.stuSex = stuSex;
	}

	public int getStuNo() {
		return stuNo;
	}

	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public int getStuAge() {
		return stuAge;
	}

	public void setStuAge(int stuAge) {
		this.stuAge = stuAge;
	}

	public String getGraName() {
		return graName;
	}

	public void setGraName(String graName) {
		this.graName = graName;
	}

	public boolean isStuSex() {
		return stuSex;
	}

	public void setStuSex(boolean stuSex) {
		this.stuSex = stuSex;
	}

	@Override
	public String toString() {
		return "Student [stuNo=" + stuNo + ", stuName=" + stuName + ", stuAge=" + stuAge + ", graName=" + graName
				+ ", stuSex=" + stuSex + "]";
	}
	
}
