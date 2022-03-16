package bilboards;

import java.io.Serializable;
import java.time.Duration; // available since JDK 1.8

/**
 * Klasa reprezentuj¹ca zamówienie wyœwietlania og³oszenia o zadanej treœci
 * przez zadany czas ze wskazaniem na namiastke klienta, przez któr¹ mo¿na
 * przes³aæ informacje o numerze zamówienia w przypadku jego przyjêcia
 * 
 * @author tkubik
 *
 */
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String advertText;
	public Duration displayPeriod;
	public IClient client;
}