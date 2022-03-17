# Lab10
- Przygotowano wielowydaniową wersję aplikacji (działa dla Javy 8, 9, 10 oraz 11)
- Stworzono instalator aplikacji za pomocą narzędzia Inno Setup,
- Stworzono instalator przy użyciu JPackage.

#### Komendy pomocne w rozwiązaniu zadania:
- Kompilacja kodu źródłowego dla Javy 8
```
D:\Java\jdk-11.0.10\bin\javac.exe --release 8 -d bin\classes java\com\chat\app\*.java java\com\chat\library\*.java
```

- Kompilacja kodu źródłowego dla Javy 11
```
D:\Java\jdk-11.0.10\bin\javac.exe --release 11 -d bin\classes-11 java11\com\chat\app\*.java java11\com\chat\library\*.java
```

- Tworzenie wielowydaniowego jara:
```
D:\Java\jdk-11.0.10\bin\jar.exe --create --file chat-app-multi-release.jar --main-class com.chat.app.Window -C .\bin\classes . --release 9 -C .\bin\classes-9 . --release 10 -C .\bin\classes-10 . --release 11 -C .\bin\classes-11 .
```

- Uruchamianie wielowydaniowego jara za pomocą róznych JDK:
```
D:\Java\jdk1.8.0_281\bin\java -jar .\chat-app-multi-release.jar
```

```
D:\Java\jdk-11.0.10\bin\java -jar .\chat-app-multi-release.jar
```

Instalator Inno Setup:

Instalator JPackage:
