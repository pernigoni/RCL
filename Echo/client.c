#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 1024

int main(int argc, char *argv[])
{
	if(argc != 3)
	{
		fprintf(stderr, "Usage: %s <hostname> <port>\n", argv[0]);
		exit(1);
	}

	const char *hostname = argv[1];
	int port = atoi(argv[2]);

	// creo il socket
	int sock = socket(AF_INET, SOCK_STREAM, 0);
	if(sock < 0)
	{
		perror("Errore nella creazione del socket");
		exit(1);
	}

	struct sockaddr_in server_addr;
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(port);

	// converto l'hostname in indirizzo IP
	if(inet_pton(AF_INET, hostname, &server_addr.sin_addr) <= 0)
	{
		perror("Indirizzo IP non valido");
		exit(1);
	}

	// mi connetto al server
	if(connect(sock, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0)
	{
		perror("Errore nella connessione");
		close(sock);
		exit(1);
	}

	char buffer[BUFFER_SIZE];
	printf("Inserisci un messaggio: ");
	fgets(buffer, BUFFER_SIZE, stdin);

	// invio il messaggio al server
	if(write(sock, buffer, strlen(buffer)) < 0)
	{
		perror("Errore nell'invio del messaggio");
		close(sock);
		exit(1);
	}

	// ricevo la risposta dal server
	ssize_t nbytes = read(sock, buffer, BUFFER_SIZE - 1);
	if(nbytes < 0)
	{
		perror("Errore nella lettura della risposta");
		close(sock);
		exit(1);
	}

	// termino la stringa e stampo la risposta
	buffer[nbytes] = '\0';
	printf("%s", buffer);

	close(sock);
	return 0;
}