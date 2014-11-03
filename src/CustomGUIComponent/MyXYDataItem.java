package CustomGUIComponent;

import org.jfree.data.xy.XYDataItem;

public class MyXYDataItem extends XYDataItem {

	private static final long serialVersionUID = -3641354962307531846L;
	private String info;
	
	public MyXYDataItem(double x, double y) {
		super(x, y);
	}

	public MyXYDataItem(double x, double y, String info) {
		super(x, y);
		this.setInfo(info);
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
