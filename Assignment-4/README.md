# COMPRESSIONE DI FILE

Scrivere un programma che dato in input una lista di directory, comprima tutti i file in esse contenuti, con
l'utility `gzip`.
Ipotesi semplificativa: zippare solo i file contenuti nelle directory passate in input senza considerare
ricorsione su eventuali sotto-directory.

Il riferimento ad ogni file individuato viene passato ad un task, che deve essere eseguito in un threadpool.

Individuare nelle API Java la classe di supporto adatta per la compressione.

N.B. L'utilizzo dei threadpool è indicato perché i task presentano un buon mix tra I/O e computazione: I/O heavy (tutti i file devono essere letti e scritti), CPU-intensive (la compressione richiede molta computazione).