#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <string.h>
#include <ctype.h>
#include <pthread.h>
#include "fs/operations.h"
#include "fs/state.h"
#include <sys/time.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdlib.h>
#include <sys/uio.h>
#include <unistd.h>
#include <sys/stat.h>
#define SERVER "/tmp/server"
#define MAX_COMMANDS 10
#define MAX_INPUT_SIZE 100
#define MAX_THREADS 100
#define mutex "mutex"
#define read "read"
#define rwlock "rwlock"
#define nosync "nosync"
#define zero "'\0'"
#define INDIM 30
#define OUTDIM 512
FILE* outputfile;
int numberThreads = 0;
char inputCommands[MAX_COMMANDS][MAX_INPUT_SIZE];
char command[MAX_INPUT_SIZE];
int numberCommands = 0;
int headQueue = 0;
int c;
int sockfd;

//pthread_mutex_t trinco;
socklen_t addrlen;
char in_buffer[INDIM];

char* removeCommand() {
    struct sockaddr_un client_addr;
    addrlen=sizeof(struct sockaddr_un);
    c = recvfrom(sockfd, in_buffer, sizeof(in_buffer)-1, 0,(struct sockaddr *)&client_addr, &addrlen);
    in_buffer[c]='\0';
    return in_buffer;
}

void errorParse(){
    
    fprintf(stderr, "Error: command invalid\n");
    exit(EXIT_FAILURE);
}


void *applyCommands(){
        int inode_index[INODE_TABLE_SIZE],i=0;
        const char* command; 
        command= removeCommand();
        if (command){
           
            for(int i=0;i<50;i++){
                inode_index[i]=-1;
                }
        char token, type[MAX_INPUT_SIZE];
        char name[MAX_INPUT_SIZE];
        int numTokens = sscanf(command, "%c %s %s", &token, name, type);
        
        /* erro de invalid command duvida*/
        if (numTokens < 2) {
            fprintf(stderr, "Error: invalid command in Queue\n");
            exit(EXIT_FAILURE);
        }
        
        int searchResult,moveResult;
        
        switch (token) {
            case 'c':
                switch (type[0]) {
                    case 'f':
                        printf("Create file: %s\n", name);
                        create(name, T_FILE,inode_index);
                        break;
                    case 'd':
                        printf("Create directory: %s\n", name);
                        create(name, T_DIRECTORY,inode_index);
                        break;
                    default:
                        fprintf(stderr, "Error: invalid node type\n");
                        exit(EXIT_FAILURE);
                    }
                    break;
            case 'l':
            
                searchResult = lookup(name,inode_index,i,1);
                
                if (searchResult >= 0){
                    printf("Search: %s found\n", name);
                    }
                else
                    printf("Search: %s not found\n", name);
                break;
            case 'm':
                //printf("destination %s\n",type);
                //printf("source %s\n",name);
                moveResult=move(name,type,inode_index);
                if (moveResult == SUCCESS){
                    printf("The file %s moved sucessfully  to %s\n", name,type);
                }
                else
                    printf("O ficheiro %s ja existe \n", type);
                break;
            case 'd':
                printf("Delete: %s\n", name);
                delete(name,inode_index);
                break;
            case 'p':
                {
                outputfile=fopen(name,"w");
                print_tecnicofs_tree(outputfile);
                fclose(outputfile);
                printf("Created file : %s\n", name);
                }
                break;
            default: { /* error */
                fprintf(stderr, "Error: command to apply\n");
                exit(EXIT_FAILURE);
            }
          }
        }
    return NULL;
}

int main(int argc, char* argv[]) {
    
    /*struct timeval t1;
    struct timeval t2;
    double t3;*/ 
    struct sockaddr_un server_addr;

    int i=0,threads=atoi(argv[1]);
    //FILE *outputfile;
    pthread_t tid[MAX_THREADS];
    
    /*
    if (argv[3]){
        fprintf(stderr,"FAILURE: MORE THAN THREE ARGUMENTS\n");
        exit(EXIT_FAILURE);
    }*/

    if(threads<0){
        fprintf(stderr,"THREAD VALUES ARE NOT VALID\n");
        exit(EXIT_FAILURE);
    }

    if ((sockfd = socket(AF_UNIX, SOCK_DGRAM, 0)) < 0) {
        perror("server: can't open socket");
        exit(EXIT_FAILURE);
    }


    unlink(SERVER);

  addrlen = setSockAddrUn (SERVER, &server_addr);
  if (bind(sockfd, (struct sockaddr *) &server_addr, addrlen) < 0) {
    perror("server: bind error");
    exit(EXIT_FAILURE);
  }
    

    /* init filesystem */
    
    init_fs();
    
     
    while(1){
        for(i=0;i<threads;i++){
            if(pthread_create(&tid[i],NULL,applyCommands,NULL)!=0){
                fprintf(stderr,"PTHREAD CREATE FAILURE");
                exit(EXIT_FAILURE);
            }
        }
        
        //printf("Recebeu comandos de %s\n", client_addr.sun_path);
        
        for(i=0;i<threads;i++){
            if (pthread_join(tid[i],NULL)!=0){
                exit(EXIT_FAILURE);
            }
        }
    }
    
   
    
    /*gettimeofday(&t2,NULL);
    t3 = (t2.tv_sec-t1.tv_sec)+((t2.tv_usec-t1.tv_usec)/1000000); //alterar nome de t3
    printf("TecnicoFS completed in %.4f seconds\n",t3);
*/
    
    /* release allocated memory */
    close(sockfd);
    unlink(SERVER);

    destroy_fs();
    
    exit(EXIT_SUCCESS);
}
