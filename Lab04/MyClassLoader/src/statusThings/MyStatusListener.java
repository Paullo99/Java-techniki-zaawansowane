package statusThings;

import processing.Status;
import processing.StatusListener;

public class MyStatusListener implements StatusListener{

	private int progress;
	
	@Override
	public void statusChanged(Status s) {
		progress = s.getProgress();
	}
	
	public int getProgress() {
		return progress;
	}

}
