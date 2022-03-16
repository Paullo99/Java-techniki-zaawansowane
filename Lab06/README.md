# Lab06
#### Napisana została aplikacja, która implementuje rozproszony system imitujący działanie sieci reklamowych.
Szczegółowy opis implementacji:
- osobny interfejs graficzny dla Managera, Klienta i Tablicy,
- działanie aplikacji oparte jest o Java RMI,
- bezpieczeństwo zapewnione dzięki SecurityManagerowi,
- <b> (Menadżer) Manager </b>  - odpowiedzialny za przyjmowanie od klientów zamówień, wyświetlanie haseł reklamowych oraz przesyłanie tych haseł na tablice reklamowe,
- <b> Client (Klient) </b> - odpowiedzialny za zgłaszanie menadżerowi zamówień lub ich wycofywanie,
- <b> Billboard (Tablica) </b> - odpowiedzialna za wyświetlanie haseł, dowiązująca się do menadżera, który może zatrzymać i uruchomić wyświetlanie haseł.

Interfejs graficzny aplikacji:

