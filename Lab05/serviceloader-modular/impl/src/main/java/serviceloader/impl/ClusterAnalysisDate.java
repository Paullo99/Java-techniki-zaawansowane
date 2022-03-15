package serviceloader.impl;

import java.util.ArrayList;
import java.util.Comparator;

import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

public class ClusterAnalysisDate implements ClusterAnalysisService, Runnable {

	private boolean grouping;
	private DataSet dataSet, outputDataSet = null;
	private Thread thread= new Thread(this);
	
	@Override
	public void setOptions(String[] options) throws ClusteringException {
		if(options[0].equals("") || options[0].equals(null)) {
			return;
		}else if(options[0].equals("grouping")){
			grouping = true;
		}else {
			throw new ClusteringException("Nie podano poprawnej opcji");
		}
	}

	@Override
	public String getName() {
		return "Grupowanie dat ze wzglêdu na pory roku.";
	}

	@Override
	public void submit(DataSet ds) throws ClusteringException {
		if(thread.isAlive()) {
			throw new ClusteringException("Trwa przetwarzanie innych danych");
		}
		thread= new Thread(this);
		dataSet = ds;
		outputDataSet = null;
		thread.start();
	}

	@Override
	public DataSet retrieve(boolean clear) throws ClusteringException {
		if(outputDataSet == null)
			throw new ClusteringException("Przetwarzanie jeszcze siê nie zakoñczy³o!");
		return outputDataSet;
	}

	@Override
	public void run() {
		String[][] output = dataSet.getData();
		for(String[] vector : output) {
			int tempDate = Integer.parseInt(vector[2].substring(3, 5)+vector[2].substring(0, 2));
			if(tempDate>=321 && tempDate <622)
				vector[1] = "Wiosna";
			else if(tempDate>=622 && tempDate <923)
				vector[1] = "Lato";
			else if(tempDate>=922 && tempDate <1222)
				vector[1] = "Jesieñ";
			else 
				vector[1] = "Zima";
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(grouping)
			output = groupData(output);
		
		outputDataSet = new DataSet();
		outputDataSet.setData(output);
		
		grouping= false;
	}
	
	public String[][] groupData(String[][] data) {
				
		ArrayList<String[]> dataArrayList = new ArrayList<>();
		
		for(String[] vector : data) {
			dataArrayList.add(vector);
		}
		
		dataArrayList.sort(new Comparator<String[]>() {

			@Override
			public int compare(String[] o1, String[] o2) {
				return o1[1].compareTo(o2[1]);
			}
			
		});
		
		String[][] sortedOutput = dataArrayList.toArray(new String[dataArrayList.size()][dataArrayList.get(0).length]);
		
		return sortedOutput;
		
	}

}
