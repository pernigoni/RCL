# NIO ECHO SERVER

## SERVER

Scrivere un programma echo server usando la libreria Java NIO, in particolare selector e canali in
modalità non bloccante.

Il server accetta richieste di connessioni dai client, riceve messaggi inviati dai client e li
rispedisce (eventualmente aggiungendo "echoed by server" al messaggio ricevuto).

## CLIENT

Scrivere un programma echo client usando NIO, va bene anche in modalità bloccante.

Il client legge il messaggio da inviare da console, lo invia al server e visualizza quanto ricevuto
dal server.