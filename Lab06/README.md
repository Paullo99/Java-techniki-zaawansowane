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
- Okienka pozwalające na parametryzację odpowiednich portów: <br>
![lab06_1](https://user-images.githubusercontent.com/49610728/158701138-29b42a35-0018-4bd5-8254-fced58209cf8.png)
![lab06_2](https://user-images.githubusercontent.com/49610728/158701140-18dbaab6-f6c7-472f-ae5d-4847251a7830.png)
![lab06_3](https://user-images.githubusercontent.com/49610728/158701142-eb805459-98a9-45f6-aa76-9376365b3cd4.png)

- Działanie systemu: <br>
![lab06_4](https://user-images.githubusercontent.com/49610728/158701136-2fadf2e5-ae8f-4f21-8cea-5b7da8becbbe.png)
