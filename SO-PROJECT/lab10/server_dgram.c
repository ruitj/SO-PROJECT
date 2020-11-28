#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdlib.h>
#include <strings.h>
#include <sys/uio.h>
#include <unistd.h>
#include <sys/stat.h>

int setSockAddrUn(char *path, struct sockaddr_un *addr) {

  if (addr == NULL)
    return 0;

  bzero((char *)addr, sizeof(struct sockaddr_un));
  addr->sun_family = AF_UNIX;
  strcpy(addr->sun_path, path);
  puts(addr->sun_path);
  return SUN_LEN(addr);
}

#define INDIM 30
#define OUTDIM 512

int main(int argc, char **argv) {
  int sockfd;
  struct sockaddr_un server_addr;
  socklen_t addrlen;
  char *path;

  if (argc < 2)
    exit(EXIT_FAILURE);

  if ((sockfd = socket(AF_UNIX, SOCK_DGRAM, 0)) < 0) {
    perror("server: can't open socket");
    exit(EXIT_FAILURE);
  }

  path = argv[1];

  unlink(path);

  addrlen = setSockAddrUn (path, &server_addr);
  if (bind(sockfd, (struct sockaddr *) &server_addr, addrlen) < 0) {
    perror("server: bind error");
    exit(EXIT_FAILURE);
  } // binding created socket to the address of the server
  
  while (1) {
    struct sockaddr_un client_addr;
    char in_buffer[INDIM], out_buffer[OUTDIM];
    int c;

    addrlen=sizeof(struct sockaddr_un);
    c = recvfrom(sockfd, in_buffer, sizeof(in_buffer)-1, 0,
		 (struct sockaddr *)&client_addr, &addrlen);
    if (c <= 0) continue;
    //Preventivo, caso o cliente nao tenha terminado a mensagem em '\0', 
    in_buffer[c]='\0';
    
    printf("Recebeu mensagem de %s\n", client_addr.sun_path);

    c = sprintf(out_buffer, "Ola' %s, que tal vai isso?", in_buffer);
    
    sendto(sockfd, out_buffer, c+1, 0, (struct sockaddr *)&client_addr, addrlen);

  }

  //Fechar e apagar o nome do socket, apesar deste programa 
  //nunca chegar a este ponto
  close(sockfd);
  unlink(argv[1]);
  exit(EXIT_SUCCESS);

}
		
