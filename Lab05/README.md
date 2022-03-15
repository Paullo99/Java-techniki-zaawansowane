# Lab05
#### Napisana została aplikacja, która umożliwia przeprowadzić analizę skupień na danych tabelarycznych.
Szczegółowy opis implementacji:
- aplikacja oferuje interfejs graficzny,
- zaimplementowany został własny ładowacz klas implementujących interfejs processing.Processor (narzucony przez prowadzącego),
- zostały zaimplementowane 3 klasy przetwarzające tekst - UpperCaseProcessor, VowelsProcessor oraz SpaceProcessor,
- wybieranie klas odbywa się poprzez wybranie z listy nazwy klasy wraz z jej opisem getInfo(),
- w celu możliwości monitorowania bieżącego statusu przetwarzania zaimplementowano wątki powodujące opóźnienia,
- klasy po załadowaniu na listę i pobraniu informacji z getInfo() są wyładowywane, ponadto po wykonaniu każdego taska również następuje ich wyładowanie.

