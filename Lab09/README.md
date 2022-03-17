# Lab09
#### Napisana została aplikacja, która pozwala na komunikację pomiędzy dwoma użytkownikami.
Szczegółowy opis implementacji:
- aplikacja oferuje interfejs graficzny,
- komunikacja odbywa się poprzez gniazda TCP/IP,
- logika związana z komunikacją została zaimplementowana jako osobna biblioteka, wyeksportowana do jara i podpisana cyfrowo (`sbiblioteka.jar`),
- przesyłane komunikaty są szyfrowane kluczem publicznym odbierającego i rozszyfrowywane kluczem prywatnym,
- obsługa menadżera bezpieczeństwa,
- sama aplikacja również została wygenerowana do jara i podpisana cyfrowo.

Interfejs graficzny aplikacji:
