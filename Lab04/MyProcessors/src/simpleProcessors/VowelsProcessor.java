package simpleProcessors;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

public class VowelsProcessor implements Processor, Runnable{

	private String inputText;
	private String output;
	private StatusListener sl;
	
	@Override
	public boolean submitTask(String task, StatusListener sl) {
		this.sl = sl;
		this.inputText = task;
		Thread thread = new Thread(this);
		thread.start();
		return true;
	}

	@Override
	public String getInfo() {
		return "VowelsProcessor: oblicza liczbê samog³osek w podanym tekœcie.";
	}

	@Override
	public String getResult() {
		return output;
	}

	@Override
	public void run() {
		
		char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y', '¹', 'ê'};
		int counter = 0 ;
		
		String inputTextLowerCase = inputText.toLowerCase();
		
		for(int i=0; i<inputText.length(); i++) {
			
			for(int j=0; j<vowels.length; j++)
				if(inputTextLowerCase.charAt(i) == vowels[j])
					counter++;
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(i*100/inputText.length() % 5 == 0)
				sl.statusChanged(new Status(inputText, i*100/inputText.length()));
		}
		output = "Liczba samog³osek: "+String.valueOf(counter);
		sl.statusChanged(new Status("FINISHED", 100));	
	}
}
