#include "tecnicofs-client-api.h"
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdio.h>
#include <sys/types.h>
int tfsCreate(char *filename, char nodeType) {
  return -1;
}

int tfsDelete(char *path) {
  return -1;
}

int tfsMove(char *from, char *to) {
  return -1;
}

int tfsLookup(char *path) {
  return -1;
}

int tfsMount(char * sockPath,struct sockaddr_un client_addr,struct sockaddr_un serv_addr) {
  socklen_t clilen,servlen;
  int sockfd;
  char buffer[1024];
   
  if ((sockfd = socket(AF_UNIX, SOCK_DGRAM, 0) ) < 0) {
    perror("client: can't open socket");
    return -1;
  }
  unlink(argv[1]); 
  clilen = setSockAddrUn (argv[1], &client_addr);
  if (bind(sockfd, (struct sockaddr *) &client_addr, clilen) < 0) {
    perror("client: bind error");
    return -1;
  }  
  servlen = setSockAddrUn(argv[2], &serv_addr);

  if (sendto(sockfd, argv[3], strlen(argv[3])+1, 0, (struct sockaddr *) &serv_addr, servlen) < 0) {
    perror("client: sendto error");
    return -1;
  }
  if (recvfrom(sockfd, buffer, sizeof(buffer), 0, 0, 0) < 0) {
    perror("client: recvfrom error");
    return -1;
  } 
}

int tfsUnmount() {
  return -1;
}
