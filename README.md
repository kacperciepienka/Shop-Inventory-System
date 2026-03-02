# JDBC Manager

Prosty system konsolowy do zarządzania asortymentem sklepu, stworzony w celu nauki integracji Javy z bazami danych.

## Funkcje
* **Wyszukiwanie:** Możliwość szukania produktów po nazwie (z ignorowaniem wielkości liter).
* **Zarządzanie stanem:** System sprzedaży aktualizujący bazę danych w czasie rzeczywistym.
* **Obsługa wyjątków:** Własna obsługa błędów przy braku wystarczającej ilości towaru na magazynie.

## Technologie
* Java (JDBC)
* H2 Database
* Maven

## Jak uruchomić
1. Sklonuj repozytorium.
2. Zaimportuj projekt jako projekt Maven w IntelliJ IDEA.
3. Uruchom klasę `Main`.
