package simpleProcessors;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

public class UpperCaseProcessor implements Processor, Runnable {

	private String inputText;
	private StringBuilder sb;
	private StatusListener sl;
	
	@Override
	public boolean submitTask(String task, StatusListener sl) {
		this.sl = sl;
		this.inputText = task;
		sb = new StringBuilder();
		Thread thread = new Thread(this);
		thread.start();
		return true;
	}

	@Override
	public String getInfo() {
		return "UpperCaseProcessor: Zamienia wszystkie litery w tekœcie na du¿e.";
	}

	@Override
	public String getResult() {
		return sb.toString();
	}

	@Override
	public void run() {
		for(int i=0; i<inputText.length(); i++) {
				
			sb.append(Character.toUpperCase(inputText.charAt(i)));

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(i*100/inputText.length() % 5 == 0)
				sl.statusChanged(new Status(inputText, i*100/inputText.length()));
		}
		
		sl.statusChanged(new Status("FINISHED", 100));	
	}

}
