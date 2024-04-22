# DUNGEON ADVENTURES

Sviluppare un'applicazione client server in cui il server gestisce le partite giocate
in un semplice gioco, "Dungeon Adventures" basato su una semplice interfaccia testuale.

## INIZIO DEL GIOCO

### GIOCATORE
Ad ogni giocatore viene assegnato, ad inizio del gioco, un livello X di salute e una quantità Y di una
pozione, X e Y generati casualmente.

### MOSTRO
Ogni giocatore combatte con un mostro diverso. Anche al mostro assegnato a un giocatore viene associato, 
all'inizio del gioco, un livello Z di salute generato casualmente.

## ROUND

Il gioco si svolge in round, ad ogni round un giocatore può:
* **Combattere con il mostro -** Il combattimento si conclude decrementando il
livello di salute del mostro e del giocatore. Se LG è il livello di salute attuale del
giocatore e MG quello del mostro, tale livello viene decrementato di un valore
casuale X, con 0≤X≤LG. Analogamente, per il mostro si genera un valore casuale K, con 0≤K≤MG.
* **Bere una parte della pozione -** La salute del giocatore viene incrementata di un valore proporzionale
alla quantità di pozione bevuta, che è un valore generato casualmente.
* **Uscire dal gioco -** In questo caso la partita viene considerata persa per il giocatore.

## CONCLUSIONE COMBATTIMENTO

Il combattimento si conclude quando il giocatore o il mostro o entrambi hanno un valore di salute pari a 0.
Se il giocatore ha vinto o pareggiato, può chiedere di giocare nuovamente, se invece ha perso deve uscire dal
gioco.

## APPLICAZIONE CLIENT/SERVER
Sviluppare un'applicazione client/server che implementi Dungeon Adventures.
* Il server riceve richieste di gioco da parte dei client e gestisce ogni connessione in un diverso thread.
* Ogni thread riceve comandi dal client e li esegue. Nel caso del comando "combattere", simula il 
comportamento del mostro assegnato al client.
* Dopo aver eseguito ogni comando ne comunica al client l'esito.
* Comunica al client l'eventuale terminazione del del gioco, insieme con l'esito.
* Il client si connette con il server.
* Chiede iterativamente all'utente il comando da eseguire e lo invia al server. I
comandi sono i seguenti `1:combatti`, `2:bevi-pozione`, `3:esci-dal-gioco`.
* Attende un messaggio che segnala l'esito del comando.
* Nel caso di gioco concluso vittoriosamente, chiede all'utente se intende continuare a giocare e lo comunica
al server.