package CustomGUIComponent;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYSeriesCollection;

public class MyChartMouseListener implements ChartMouseListener {

	private ArrayList<String> geneInfo = null;
	private JLabel infoLbl;
	
	public MyChartMouseListener() {
		super();
	}
	
	public MyChartMouseListener(ArrayList<String> geneName, JLabel infoLbl) {
		this();
		this.geneInfo = geneName;
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
