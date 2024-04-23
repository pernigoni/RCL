open Unix

let host = "127.0.0.1" (* Indirizzo del server *)
let port = 12345 (* Porta del server *)

let () =
	(* Creazione del socket *)
	let sock = socket PF_INET SOCK_STREAM 0 in

	(* Configurazione dell'indirizzo del server *)
	let server_addr = ADDR_INET (inet_addr_of_string host, port) in

	(* Connessione al server *)
	connect sock server_addr;

	(* Inserimento del messaggio da inviare *)
	print_string "Inserisci un messaggio: ";
	let message = read_line () ^ "\n" in

	(* Invio del messaggio al server *)
	let _ = send sock (Bytes.of_string message) 0 (String.length message) [] in

	(* Ricezione della risposta dal server *)
	let buffer = Bytes.create 1024 in
	let nbytes = recv sock buffer 0 1024 [] in

	(* Stampa della risposta *)
	let response = Bytes.sub_string buffer 0 nbytes in
	Printf.printf "%s" response;

	(* Chiusura del socket *)
	close sock;