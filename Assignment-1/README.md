# NON È TUTTO ORO QUEL CHE LUCCICA

Non è tutto oro quel che luccica... ovvero, non sempre il multithreading è conveniente.

Scrivere un'applicazione Java che crea e attiva N thread. Ogni thread esegue esattamente lo stesso task,
ovvero conta il numero di interi minori di 10,000,000 che sono primi. Il numero di thread che devono essere
attivati e mandati in esecuzione viene richiesto all'utente, che lo inserisce tramite la CLI.

Analizzare come varia il tempo di esecuzione dei thread attivati a seconda del loro numero.

Sviluppare quindi un programma in cui si creano N task, tutti eseguono la computazione descritta in 
precedenza e vengono sottomessi a un threadpool di dimensione prefissata.