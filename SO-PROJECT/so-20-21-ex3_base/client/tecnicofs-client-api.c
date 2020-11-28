#include "tecnicofs-client-api.h"
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdio.h>
#include <sys/types.h>

int setSockAddrUn(char *path, struct sockaddr_un *addr) {

  if (addr == NULL){
    puts("NULL");
    return 0;}

  bzero((char *)addr, sizeof(struct sockaddr_un));
  addr->sun_family = AF_UNIX;
  strcpy(addr->sun_path, path);
  return SUN_LEN(addr);
}

int tfsCreate(int* sockfd,char* command,socklen_t* servlen,struct sockaddr_un* serv_addr ,char nodeType) {
  if (sendto(*sockfd,command,strlen(command)+1, 0, (struct sockaddr *) serv_addr, *servlen) < 0) {
    perror("client: sendto error");
    exit(EXIT_FAILURE);
  } 
  return 0;
}

int tfsPrint(int* sockfd,char* command,socklen_t* servlen,struct sockaddr_un* serv_addr) {
  if (sendto(*sockfd,command,strlen(command)+1, 0, (struct sockaddr *) serv_addr, *servlen) < 0) {
    perror("client: sendto error");
    exit(EXIT_FAILURE);
  } 
  return 0;
}

int tfsDelete(char *path) {
  return -1;
}

int tfsMove(int* sockfd,char* command,socklen_t* servlen,struct sockaddr_un* serv_addr) {
  if (sendto(*sockfd,command,strlen(command)+1, 0, (struct sockaddr *) serv_addr, *servlen) < 0) {
    perror("client: sendto error");
    exit(EXIT_FAILURE);
  } 
  return 0;
}

int tfsLookup(char *path) {
  return -1;
}

int tfsMount(char* serverName,socklen_t* servlen,struct sockaddr_un* serv_addr,int* sockfd) {

  //socklen_t servlen_new;
  socklen_t  clilen;
  struct sockaddr_un client_addr;

  if ((*sockfd = socket(AF_UNIX, SOCK_DGRAM, 0) ) < 0) {
    perror("client: can't open socket");
    exit(EXIT_FAILURE);
  }
  
  clilen = setSockAddrUn ("/tmp/client", &client_addr);
  if (bind(*sockfd, (struct sockaddr *) &client_addr, clilen) < 0) {
    perror("client: bind error");
    exit(EXIT_FAILURE);
  }  

  *servlen = setSockAddrUn(serverName, serv_addr);
  
  return 0;

}
  

int tfsUnmount(int *sockfd) {
    close(*sockfd);
    unlink(CLIENT_ADDR);
  return 0;
}
