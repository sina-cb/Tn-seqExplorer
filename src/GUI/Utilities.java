package GUI;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Utilities {

	private static JFrame frame;
	private static JTabbedPane panel;
	private static int newFrameHeight;
	private static int newPanelHeigth;
	private static int step;
	private static int interval;

	public static void shrinkFrame(JFrame frame, JTabbedPane panel, int newFrameHeight, int newPanelHeigth, int step, int interval){

		Utilities.frame = frame;
		Utilities.panel = panel;
		Utilities.newFrameHeight = newFrameHeight;
		Utilities.newPanelHeigth = newPanelHeigth;
		Utilities.step = step;
		Utilities.interval = interval;

		new Thread(new Runnable()
		{
			public void run()
			{
				Dimension frameDim = Utilities.frame.getSize();
				Dimension panelDim = Utilities.panel.getSize();

				int step = (int) ((frameDim.getHeight() - Utilities.newFrameHeight) / Utilities.step);

				while (frameDim.getHeight() >= Utilities.newFrameHeight)
				{
					try
					{
						Utilities.frame.setSize((int)(frameDim.getWidth()), (int)(frameDim.getHeight() - step));
						Utilities.panel.setSize((int)(panelDim.getWidth()), (int)(panelDim.getHeight() - step));
						Thread.sleep(Utilities.interval);
					}
					catch(InterruptedException ex) 
					{

					}
					frameDim = Utilities.frame.getSize();
					panelDim = Utilities.panel.getSize();
				}
			}
		}).start();
		
		Rectangle rec = panel.getBounds();
		panel.setBounds((int)rec.getX(), (int)rec.getY(), (int)rec.getWidth(), newPanelHeigth);
	}
	
	public static void expandFrame(JFrame frame, JTabbedPane panel, int newFrameHeight, int newPanelHeigth, int step, int interval){

		Utilities.frame = frame;
		Utilities.panel = panel;
		Utilities.newFrameHeight = newFrameHeight;
		Utilities.newPanelHeigth = newPanelHeigth;
		Utilities.step = step;
		Utilities.interval = interval;

		new Thread(new Runnable()
		{
			public void run()
			{
				Dimension frameDim = Utilities.frame.getSize();
				Dimension panelDim = Utilities.panel.getSize();

				int step = (int) ((Utilities.newFrameHeight - frameDim.getHeight()) / Utilities.step);

				while (frameDim.getHeight() <= Utilities.newFrameHeight)
				{
					try
					{
						Utilities.frame.setSize((int)(frameDim.getWidth()), (int)(frameDim.getHeight() + step));
						Utilities.panel.setSize((int)(panelDim.getWidth()), (int)(panelDim.getHeight() + step));
						Thread.sleep(Utilities.interval);
					}
					catch(InterruptedException ex) 
					{

					}
					frameDim = Utilities.frame.getSize();
					panelDim = Utilities.panel.getSize();
				}
			}
		}).start();
	}
	
}
