package com.example;

public class VectorDotProduct {

	public Double[] a;
	public Double[] b;
	public Double c;

	// zak�adamy, �e po stronie kodu natywnego wyliczony zostanie iloczyn skalarny
	// dw�ch wektor�w
	public native Double multi01(Double[] a, Double[] b);

	// zak�adamy, �e drugi atrybut b�dzie pobrany z obiektu przekazanego do metody
	// natywnej
	public native Double multi02(Double[] a);

	// zak�adamy, �e po stronie natywnej utworzone zostanie okienko na atrybuty,
	// a po ich wczytaniu i przepisaniu do a,b obliczony zostanie wynik.
	// Wynik powinna wylicza� metoda Javy multi04
	// (korzystaj�ca z parametr�w a,b i wpisuj�ca wynik do c).
	public native void multi03();

	public void multi04() {
		double result = 0.0;

		for (int i = 0; i < a.length; i++) {
			result += a[i] * b[i];
		}

		c = result;
	}
}
