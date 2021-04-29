package processing;

public interface Processor { 

	/**
	 * Metoda s³u¿¹ca do zlecania zadañ
	 * 
	 * @param task - tekst podlegaj¹cy przetwarzaniu (reprezentuje zadanie)
	 * @param sl   - s³uchacz, który bêdzie informowany o statusie przetwarzania
	 * @return - wartoœæ logiczn¹ mówi¹c¹ o tym, czy zadanie przyjêto do
	 *         przetwarzania (nie mo¿na zleciæ kolejnych zadañ dopóki bie¿¹ce
	 *         zadanie siê nie zakoñczy³o i nie zosta³ pobrany wynik przetwarzania
	 */
	boolean submitTask(String task, StatusListener sl);

	/**
	 * Metoda s³u¿¹ca do pobierania informacji o algorytmie przetwarzania (np. "Algorytm zliczaj¹cy samog³oski")
	 * 
	 * @return - informacja o algorytmie przetwarzania 
	 */ 
	String getInfo();

	/**
	 * Metoda s³u¿¹ca do pobierania wyniku przetwarzania
	 * 
	 * @return - tekst reprentuj¹cy wynik przetwarzania
	 */
	String getResult();
}
