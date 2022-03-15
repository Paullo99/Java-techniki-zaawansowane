package serviceloader.client;

import java.awt.EventQueue;

public class Client {

	public static void main(String[] args) {

		System.out.println("Wersja modularna");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
    }
}
