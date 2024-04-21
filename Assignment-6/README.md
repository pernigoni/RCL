# ANALISI DI UN WEBLOG

Il log file di un web server contiene un insieme di linee, con il seguente formato.
```
150.108.64.57 - - [15/Feb/2001:09:40:58 -0500] "GET / HTTP 1.0" 200 2511
```
* `150.108.64.57` Indica l'host remoto, in genere secondo la dotted quad form.
* `[date]`
* `"HTTP request"`
* `status`
* `bytes sent`
* Eventuale tipo del client.

Scrivere un'applicazione Weblog che prende in input il nome del log file (che sarà fornito) e ne stampa ogni
linea, in cui ogni indirizzo IP è sostituito con l'hostname.

Sviluppare due versioni del programma, la prima single-threaded, la seconda invece utilizza un threadpool,
in cui il task assegnato ad ogni thread riguarda la traduzione di un insieme di linee del file. Confrontare i
tempi delle due versioni.