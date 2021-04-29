package processing;

public interface Processor { 

	/**
	 * Metoda s�u��ca do zlecania zada�
	 * 
	 * @param task - tekst podlegaj�cy przetwarzaniu (reprezentuje zadanie)
	 * @param sl   - s�uchacz, kt�ry b�dzie informowany o statusie przetwarzania
	 * @return - warto�� logiczn� m�wi�c� o tym, czy zadanie przyj�to do
	 *         przetwarzania (nie mo�na zleci� kolejnych zada� dop�ki bie��ce
	 *         zadanie si� nie zako�czy�o i nie zosta� pobrany wynik przetwarzania
	 */
	boolean submitTask(String task, StatusListener sl);

	/**
	 * Metoda s�u��ca do pobierania informacji o algorytmie przetwarzania (np. "Algorytm zliczaj�cy samog�oski")
	 * 
	 * @return - informacja o algorytmie przetwarzania 
	 */ 
	String getInfo();

	/**
	 * Metoda s�u��ca do pobierania wyniku przetwarzania
	 * 
	 * @return - tekst reprentuj�cy wynik przetwarzania
	 */
	String getResult();
}
