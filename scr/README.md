Yasser Afifi
ID: 14303154

all the files are in the same directory

compile.sh will compile the server java file.

start.sh will run the server and it REQUIRES one integer argument which represent the port number

compileClient.sh will compile the client java file.

startClient.sh will run the client and it REQUIRES one integer argument which represent the port number


the server is expecting the client to send data as DataInputStream object  and it uses readUTF() to read from it. the server also uses writeUTF() to write back to the client.
