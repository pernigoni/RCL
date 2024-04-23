import socket

HOST = "localhost"
PORT = 12345

try:
	with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
		s.connect((HOST, PORT))
		s.sendall((input("Inserisci un messaggio: ") + "\n").encode("utf-8")) # invia byte
		print(s.recv(1024).decode("utf-8").replace("\n", "")) # riceve fino a 1024 byte
except Exception as e:
	print("Errore!", e)
	exit(1)