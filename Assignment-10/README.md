# JAVA PINGER

## PING

PING è una utility per la valutazione delle performance della rete utilizzata per verificare la
raggiungibilità di un host su una rete IP e per misurare il round trip time (RTT) per i messaggi spediti
da un host mittente verso un host destinazione.

Lo scopo di questo assignment è quello di implementare un server PING ed un corrispondente client PING
che consenta al client di misurare il suo RTT verso il server.

La funzionalità fornita da questi programmi deve essere simile a quella della utility PING disponibile in
tutti i moderni sistemi operativi. La differenza fondamentale è che si utilizza UDP per la comunicazione
tra client e server, invece del protocollo ICMP (Internet Control Message Protocol).


Inoltre, poichè l'esecuzione dei programmi avverrà su un solo host o sulla rete locale ed in entrambe i
casi sia la latenza che la perdita di pacchetti risultano trascurabili, il server deve introdurre un
ritardo artificiale ed ignorare alcune richieste per simulare la perdita di pacchetti.

## CLIENT

Accetta due argomenti da linea di comando: nome e porta del server. Se uno o più argomenti risultano
scorretti, il client termina dopo aver stampato un messaggio di errore del tipo `ERR -arg x`, dove x è il
numero dell'argomento.

Utilizza una comunicazione UDP per comunicare con il server ed invia 10 messaggi al server, con il
seguente formato.
```
PING sqno timestamp
```
In cui seqno è il numero di sequenza del PING (tra 0-9) ed il timestamp (in millisecondi) indica quando il
messaggio è stato inviato.

Non invia un nuovo PING fino a che non ha ricevuto l'eco del PING precedente, oppure è scaduto un timeout.

Stampa ogni messaggio spedito al server ed il RTT del ping oppure un `*` se la risposta non è stata
ricevuta entro 2 secondi.

Dopo che ha ricevuto la decima risposta (o dopo il suo timeout), il client stampa un riassunto simile a 
quello stampato dal PING UNIX.
```
---- PING Statistics ----
10 packets transmitted, 7 packets received, 30% packet loss
round-trip (ms) min/avg/max = 63/190.29/290
```

Il RTT medio è stampato con 2 cifre dopo la virgola.

## SERVER

Il server è essenzialmente un echo server: rimanda al mittente qualsiasi dato riceve.

Accetta un argomento da linea di comando: la porta \(quella su cui è attivo il server\) e un argomento
opzionale chiamato seed \(valore long utilizzato per la generazione di latenze e perdita di pacchetti\). Se 
uno qualunque degli argomenti è scorretto, stampa un messaggio di errore del tipo `ERR -arg x`, dove x è il
numero dell'argomento.

Dopo aver ricevuto un PING, il server determina se ignorare il pacchetto (simulandone la perdita) o
effettuarne l'eco. La probabilità di perdita di pacchetti di default è del 25%.

Se decide di effettuare l'eco del PING, il server attende un intervallo di tempo casuale per simulare la
latenza di rete.

Stampa l'indirizzo IP e la porta del client, il messaggio di PING e l'azione intrapresa dal server in
seguito alla sua ricezione (PING non inviato, oppure PING ritardato di x ms).

## ESEMPIO ESECUZIONE
```
java PingServer 10002 123
128.82.4.244:44229> PING 0 1360792326564 ACTION: delayed 297 ms
128.82.4.244:44229> PING 1 1360792326863 ACTION: delayed 182 ms
128.82.4.244:44229> PING 2 1360792327046 ACTION: delayed 262 ms
128.82.4.244:44229> PING 3 1360792327309 ACTION: delayed 21 ms
128.82.4.244:44229> PING 4 1360792327331 ACTION: delayed 173 ms
128.82.4.244:44229> PING 5 1360792327505 ACTION: delayed 44 ms
128.82.4.244:44229> PING 6 1360792327550 ACTION: delayed 19 ms
128.82.4.244:44229> PING 7 1360792327570 ACTION: not sent
128.82.4.244:44229> PING 8 1360792328571 ACTION: not sent
128.82.4.244:44229> PING 9 1360792329573 ACTION: delayed 262 ms
```

```
java PingClient localhost 10002
PING 0 1360792326564 RTT: 299 ms
PING 1 1360792326863 RTT: 183 ms
PING 2 1360792327046 RTT: 263 ms
PING 3 1360792327309 RTT: 22 ms
PING 4 1360792327331 RTT: 174 ms
PING 5 1360792327505 RTT: 45 ms
PING 6 1360792327550 RTT: 20 ms
PING 7 1360792327570 RTT: *
PING 8 1360792328571 RTT: *
PING 9 1360792329573 RTT: 263 ms
---- PING Statistics ----
10 packets transmitted, 8 packets received, 20% packet loss
round-trip (ms) min/avg/max = 20/158.62/299
```

Invocazione corretta client/server:
```
java PingClient
Usage: java PingClient hostname port
java PingServer
Usage: java PingServer port [seed]
```

Invocazione non corretta client/server:
```
java PingClient atria three
ERR - arg 2
java PingServer abc
ERR - arg 1
```