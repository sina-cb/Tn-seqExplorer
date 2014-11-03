package CustomGUIComponent;

import javax.swing.JLabel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYSeriesCollection;

public class MyChartMouseListener implements ChartMouseListener {

	private JLabel infoLbl;
	
	public MyChartMouseListener() {
		super();
	}
	
	public MyChartMouseListener(JLabel infoLbl) {
		this();
		this.infoLbl = infoLbl;
	}
	
	@Override
	public void chartMouseClicked(ChartMouseEvent e) {
		
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent e) {
		if (e.getEntity().getClass() == XYItemEntity.class){
			XYItemEntity item = (XYItemEntity) e.getEntity();
			XYSeriesCollection dataset = (XYSeriesCollection) item.getDataset();
			infoLbl.setText(((MyXYDataItem)dataset.getSeries(0).getItems().get(item.getItem())).getInfo());
		}
	}

}
