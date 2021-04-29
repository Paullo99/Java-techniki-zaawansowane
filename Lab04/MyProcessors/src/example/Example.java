package example;

import processing.Processor;
import simpleProcessors.SpaceProcessor;

public class Example {

	public static void main(String[] args) {

		Processor mProcessor = new SpaceProcessor();
		
		mProcessor.submitTask("Aplikacja powinna udostêpniaæ interfejs, na którym bêdzie mo¿na, wielokrotnie, wprowadziæ tekst (dane do przetworzenia), wybraæ klasê do za³adowania (której instancja ten tekst przetworzy),  zleciæ zadanie przetwarzania i monitorowaæ jego przebiec, wyœwietliæ wynik przetwarzania, wy³adowaæ za³adowane klasy.", null);
	}  

}
