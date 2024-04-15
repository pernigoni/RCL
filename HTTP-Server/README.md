# HTTP-BASED FILE TRANSFER

Scrivere un programma Java che implementi un server HTTP che gestisca richieste di trasferimento di file di
diverso tipo (e.g. txt, jpg, mp4, pdf) provenienti da un browser web.

Il server sta in ascolto su una porta nota al client (non nelle prime 1024) e gestisce richieste HTTP di tipo
`GET` alla Request URL `localhost:port/filename`. Le connessioni possono essere non persistenti. Usare le
classi Socket e ServerSocket per sviluppare il programma server. Utilizzare un qualsiasi browser per inviare
le richieste al server.