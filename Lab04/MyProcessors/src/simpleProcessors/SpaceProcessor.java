package simpleProcessors;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

public class SpaceProcessor implements Processor, Runnable{

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
		return "SpaceProcessor: usuwa wszystkie spacje w podanym tekœcie.";
	}

	@Override
	public String getResult() {
		return sb.toString();
	}

	@Override
	public void run() {
	
		for(int i=0; i<inputText.length(); i++) {
			if(inputText.charAt(i) == ' ') 
				sb.append("");
			else 
				sb.append(inputText.charAt(i));

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(i*100/inputText.length() % 5 == 0)
				sl.statusChanged(new Status(inputText, i*100/inputText.length()));
		}

		sl.statusChanged(new Status("FINISHED", 100));
	}

}
