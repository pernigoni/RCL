javac Server.java -d bin

gcc client.c -o bin/client_c

#ocamlfind opt -linkpkg -package unix -o bin/client_ml client.ml
ocamlc unix.cma -o bin/client_ml client.ml
mv client.cmi client.cmo bin/