***Tema Studiului Individual : Drone Repair Center, Reparații și închirieri drone.***

Acestă aplicație folosește baza de date MySQL, pentru a rula cu succes este nevoie să rulați query-ul [drone_repair.sql](drone_repair.sql) din acest proiect pentru a crea baza de date, iar la necesitate, să configurați stringul de conectare aflat în Database.java

**Notă.**
Proiectul a fost realizat în 10 ore.

***Funcționalități:***

1. Pe scurt, aplicația folosește baza de date pentru ca clienții să poată să-și înregistreze propriile drone în sistemul dat pentru a le putea repara ulterior sau ca să închirieze o dronă.

2. Pentru administratori, aceștia pot accepta reparația unei drone și să specifice prețul reparației, iar daca clientul este satisfăcut de preț acesta poate indica în aplicație ca meșterul să înceapă lucrul, daca nu, reparațîa este anulată. Deasemenea administratorii au access la informația despre toate dronele din baza de date și pot modifica statusul unei drone.

3. Odată ce o chirie se finisează, automat se schimbă statusul dronei și chiria ia sfârșit.

4. Parolele în baza de date nu sunt criptate, însă la încărcarea acestora în aplicație se face criptare de tip 'one way encrypt', în care nu se pot decripta, iar dacă apare o eroare la criptarea parolelor,aplicația se închide instant pentru a preveni scurgerea de date confidențiale.

5. și altele ...


***[Linkul Video pentru demonstrare](https://youtu.be/gAkrwpgposU)***



