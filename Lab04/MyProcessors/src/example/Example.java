package example;

import processing.Processor;
import simpleProcessors.SpaceProcessor;

public class Example {

	public static void main(String[] args) {

		Processor mProcessor = new SpaceProcessor();
		
		mProcessor.submitTask("Aplikacja powinna udost�pnia� interfejs, na kt�rym b�dzie mo�na, wielokrotnie, wprowadzi� tekst (dane do przetworzenia), wybra� klas� do za�adowania (kt�rej instancja ten tekst przetworzy),  zleci� zadanie przetwarzania i monitorowa� jego przebiec, wy�wietli� wynik przetwarzania, wy�adowa� za�adowane klasy.", null);
	}  

}
