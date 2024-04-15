# SIMULAZIONE LABORATORIO DI INFORMATICA

Il laboratorio di Informatica del Polo Marzotto è utilizzato da tre tipi di utenti, 
studenti, tesisti e professori ed ogni utente deve fare una richiesta al tutor per 
accedere al laboratorio. I computer del laboratorio sono numerati da 1 a 20. Le 
richieste di accesso sono diverse a seconda del tipo dell'utente:
* I professori accedono in modo esclusivo a tutto il laboratorio, poiché hanno 
necessità di utilizzare tutti i computer per effettuare prove in rete.
* I tesisti richiedono l'uso esclusivo di un solo computer, identificato dall'indice i, 
poichè su quel computer è istallato un particolare software necessario per lo 
sviluppo della tesi.
* Gli studenti richiedono l'uso esclusivo di un qualsiasi computer.
* I professori hanno priorità su tutti nell'accesso al laboratorio, i tesisti hanno priorità sugli studenti.
* Nessuno però può essere interrotto mentre sta usando un computer.

Scrivere un programma Java che simuli il comportamento degli utenti e del tutor. 
Il programma riceve in ingresso il numero di studenti, tesisti e professori che 
utilizzano il laboratorio ed attiva un thread per ogni utente.

Ogni utente accede K volte al laboratorio, con K generato casualmente. 

Simulare l'intervallo di tempo che intercorre tra un accesso ed il successivo e 
l'intervallo di permanenza in laboratorio mediante il metodo sleep della classe 
Thread. 

Il tutor deve coordinare gli accessi al laboratorio.

Il programma deve terminare quando tutti gli utenti hanno completato i loro 
accessi al laborator Simulare gli utenti con dei thread e incapsulare la logica di 
gestione del laboratorio all'interno di un monitor.
