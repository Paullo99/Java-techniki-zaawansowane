# Lab11
#### Napisana została aplikacja wykorzystująca JNI, która umożliwia obliczanie iloczynu skalarnego.
Szczegółowy opis implementacji:
- po stronie kodu natywnego zaimplementowano 4 zadeklarowane przez prowadzącego metody:
```java
// po stronie kodu natywnego wyliczany jest iloczyn skalarny dwóch wektorów
public native Double multi01(Double[] a, Double[] b);

// drugi atrybut pobierany jest z obiektu przekazanego do metody natywnej 
public native Double multi02(Double[] a); 

// Po stronie natywnej utworzone zostaje okienko na atrybut.
// Po ich wczytaniu i przepisaniu do a oraz b obliczony zostaje wynik. 
// Wynik wylicza metoda Javy multi04().
public native void multi03();

public void multi04(){
//... mnożenie a i b, wpisanie wyniku do zmiennej c
 }
```
- metoda `multi03()` wykorzystuje prosty JDialog, z którego pobiera potrzebne do przetworzenia wektory,
- prosty interfejs graficzny: <br>
![lab11_1](https://user-images.githubusercontent.com/49610728/158809831-4edae4b4-9eb0-43ec-8ba8-ea057e1c325d.png)
