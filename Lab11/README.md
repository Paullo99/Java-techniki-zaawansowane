# Lab11
#### Napisana została aplikacja wykorzystująca JNI, która umożliwia obliczanie iloczynu skalarnego.
Szczegółowy opis implementacji:
- po stronie kodu natywnego zaimplementowano 3 zadeklarowane przez prowadzącego metody:
```[Java]
// po stronie kodu natywnego wyliczany jest iloczyn skalarny dwóch wektorów
public native Double multi01(Dobuble[] a, Double[] b);

// drugi atrybut pobierany jest z obiektu przekazanego do metody natywnej 
public native Double multi02(Dobuble[] a); 

// Po stronie natywnej utworzone zostaje okienko na atrybut.
// Po ich wczytaniu i przepisaniu do a oraz b obliczony zostaje wynik. 
// Wynik powinna wyliczać metoda Javy multi04().

public native void multi03();

public void multi04(){
//... mnożenie a i b, wpisanie wyniku do zmiennej c
 }
```
- metoda `multi03()` wykorzystuje prosty JDialog, z którego pobiera potrzebne do przetworzenia wektory,
- prosty interfejs graficzny:
