package GUI;

import javax.swing.JProgressBar;

public class ProgressBarUpdater implements Runnable {

	JProgressBar bar = null;
	int size = -1;
	int current = 0;
	
	public ProgressBarUpdater(JProgressBar bar, int size){
		this.size = size;
		this.bar = bar;
	}
	
	@Override
	public void run() {
		
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int valueTemp = (int)((current * 100.0) / size);
			bar.setValue(valueTemp);
			
			if (current == -1){
				break;
			}
		}
		
	}
	
	public void setCurrent(int current){
		this.current = current;
	}

}
