package nuc.hzb.entity;

public class Student {
	private int stuNo;
	private String stuName;
	private int stuAge;
	
	private StudentCard studentCard;
	
	public Student() {}
	
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

	public StudentCard getStudentCard() {
		return studentCard;
	}

	public void setStudentCard(StudentCard studentCard) {
		this.studentCard = studentCard;
	}

	@Override
	public String toString() {
		return "Student [stuNo=" + stuNo + ", stuName=" + stuName + ", stuAge=" + stuAge + ", studentCard="
				+ studentCard + "]";
	}

}
