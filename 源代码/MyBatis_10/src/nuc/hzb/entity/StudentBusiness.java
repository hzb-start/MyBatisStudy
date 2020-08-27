package nuc.hzb.entity;

public class StudentBusiness extends Student {
	
	private int cardid;
	private String cardinfo;
	public int getCardid() {
		return cardid;
	}
	public void setCardid(int cardid) {
		this.cardid = cardid;
	}
	public String getCardinfo() {
		return cardinfo;
	}
	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}
	
	@Override
	public String toString() {
		return super.toString() + "StudentBusiness [cardid=" + cardid + ", cardinfo=" + cardinfo + "]";
	}
	
	
}
