# SIMULAZIONE UFFICIO POSTALE

Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. Nell'ufficio esiste:
* Un'ampia sala d'attesa in cui ogni persona può entrare liberamente. Quando entra, ogni persona prende il
numero dalla numeratrice e aspetta il proprio turno in questa sala.
* Una seconda sala, meno ampia, posta davanti agli sportelli, in cui si può entrare solo a gruppi di K
persone.

Una persona si mette quindi prima in coda nella prima sala, poi passa nella seconda sala.
Ogni persona impiega un tempo differente per la propria operazione allo sportello. Una volta terminata
l'operazione, la persona esce dall'ufficio.

Scrivere un programma in cui:
* L'ufficio viene modellato come una classe Java, in cui viene attivato un threadpool di dimensione uguale al 
numero degli sportelli.
* La coda delle persone presenti nella sala d'attesa è gestita esplicitamente dal programma.
* La seconda coda (davanti agli sportelli) è quella gestita implicitamente dal threadpool.
* Ogni persona viene modellata come un task che deve essere assegnato ad uno dei thread associati
agli sportelli.
* Si preveda di far entrare tutte le persone nell'ufficio postale all'inizio del programma.

Facoltativo: prevedere il caso di un flusso continuo di clienti e la possibilità
che l'operatore chiuda lo sportello stesso dopo che in un certo intervallo di 
tempo non si presentano clienti al suo sportello.