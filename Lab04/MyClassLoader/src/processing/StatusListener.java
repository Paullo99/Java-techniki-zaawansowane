package processing;

public interface StatusListener {
	/** 
	 * Metoda dostarczaj�ca s�uchaczowi status przetwarzania zadania
	 * s - status przetwarzania zadania
	 */
	void statusChanged(Status s);
}
