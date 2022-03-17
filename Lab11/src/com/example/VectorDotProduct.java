package com.example;

public class VectorDotProduct {

	public Double[] a;
	public Double[] b;
	public Double c;

	// zak³adamy, ¿e po stronie kodu natywnego wyliczony zostanie iloczyn skalarny
	// dwóch wektorów
	public native Double multi01(Double[] a, Double[] b);

	// zak³adamy, ¿e drugi atrybut bêdzie pobrany z obiektu przekazanego do metody
	// natywnej
	public native Double multi02(Double[] a);

	// zak³adamy, ¿e po stronie natywnej utworzone zostanie okienko na atrybuty,
	// a po ich wczytaniu i przepisaniu do a,b obliczony zostanie wynik.
	// Wynik powinna wyliczaæ metoda Javy multi04
	// (korzystaj¹ca z parametrów a,b i wpisuj¹ca wynik do c).
	public native void multi03();

	public void multi04() {
		double result = 0.0;

		for (int i = 0; i < a.length; i++) {
			result += a[i] * b[i];
		}

		c = result;
	}
}
