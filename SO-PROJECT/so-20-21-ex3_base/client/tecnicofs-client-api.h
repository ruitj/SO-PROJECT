#ifndef API_H
#define API_H
#define CLIENT_ADDR "/tmp/client"
#include "tecnicofs-api-constants.h"
#include <sys/socket.h>
#include <sys/un.h>
#include <stdio.h>
#include <sys/types.h>
int tfsCreate(int* sockfd,char* command,socklen_t* servlen,struct sockaddr_un* serv_addr, char nodeType);
int tfsDelete(char *path);
int tfsLookup(char *path);
int tfsMove(int* sockfd,char* command,socklen_t* servlen,struct sockaddr_un* serv_addr);
int tfsMount(char* serverName,socklen_t* servlen,struct sockaddr_un* serv_addr,int* sockfd);
int tfsUnmount(int* sockfd);
int tfsPrint(int* sockfd,char* command,socklen_t* servlen,struct sockaddr_un* serv_addr);
#endif /* CLIENT_H */
