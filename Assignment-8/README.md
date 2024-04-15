# VALUTAZIONE STRATEGIE I/O BUFFERIZZATO

Lo scopo dell'assignment è dare una valutazione delle prestazioni di diverse strategie di bufferizzazione di
I/O offerte da Java.

Scrivere un programma che copi un file di input in un file di output, utilizzando le seguenti modalità
alternative di bufferizzazione, valutando il tempo impiegato per la copia del file in ognuna delle seguenti
strategie:

* NIO indirect buffer.
* NIO direct buffer.
* NIO `transferTo()`.
* I/O tradizionale con buffered stream.
* I/O tradizionale con file stream e array di byte.