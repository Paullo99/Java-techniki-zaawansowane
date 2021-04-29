package processing;

public interface StatusListener {
	/** 
	 * Metoda dostarczaj¹ca s³uchaczowi status przetwarzania zadania
	 * s - status przetwarzania zadania
	 */
	void statusChanged(Status s);
}
