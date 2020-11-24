#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <string.h>
#include <ctype.h>
#include <pthread.h>
#include "fs/operations.h"
#include <sys/time.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdlib.h>
#include <strings.h>
#include <sys/uio.h>
#include <unistd.h>
#include <sys/stat.h>

#define MAX_COMMANDS 10
#define MAX_INPUT_SIZE 100
#define MAX_THREADS 100
#define mutex "mutex"
#define read "read"
#define write "write"
#define rwlock "rwlock"
#define nosync "nosync"
#define zero "'\0'"
#define INDIM 30
#define OUTDIM 512
int numberThreads = 0;
char inputCommands[MAX_COMMANDS][MAX_INPUT_SIZE];
char command[MAX_INPUT_SIZE];
int numberCommands = 0;
int headQueue = 0;
int end=0;
pthread_mutex_t lock;
int insert_ptr=0,remove_ptr=0;
pthread_cond_t can_wr,can_apply;


/*int insertCommand(char* data) {
     
    pthread_mutex_lock(&lock);
    
    while(MAX_COMMANDS==numberCommands){
        pthread_cond_wait(&can_wr,&lock);
    }
    
    strcpy(inputCommands[insert_ptr], data);
    puts("data=");
    puts(data);
    insert_ptr++;
    if(insert_ptr==MAX_COMMANDS){
        insert_ptr=0;
    }
    
    numberCommands++;
    pthread_cond_signal(&can_apply);
    pthread_mutex_unlock(&lock);   
    return 1;
}
*/

/*char* removeCommand() {
    pthread_mutex_lock(&lock);
    while((numberCommands==0) && (end==0)) {
                if(pthread_cond_wait(&can_apply,&lock)!=0){
                    printf("PTHREAD CONDITION WAIT ERROR");
                    pthread_mutex_unlock(&lock);
                    exit(EXIT_FAILURE);
                }
            }
    if(numberCommands==0 && end==1){           // PTHREAD UNLOCK ERROR PRINT
            pthread_mutex_unlock(&lock);
            puts("break");
            return NULL;
            }

        strcpy(command,inputCommands[remove_ptr++]);
        if(remove_ptr==MAX_COMMANDS){
            remove_ptr=0;
        }
         numberCommands--;
        pthread_cond_signal(&can_wr);
        pthread_mutex_unlock(&lock);
        return command;
}
*/
void errorParse(){
    
    fprintf(stderr, "Error: command invalid\n");
    exit(EXIT_FAILURE);
}

/*void processInput(char* ficheiro){
    
    char line[MAX_INPUT_SIZE];
    FILE *f = fopen(ficheiro,"r");*/
    /* break loop with ^Z or ^D */
    /*while (fgets(line, sizeof(line)/sizeof(char),f)) {
        char token, type;
        char name[MAX_INPUT_SIZE];
        int numTokens = sscanf(line, "%c %s %c", &token, name, &type);
       */ 
        /* perform minimal validation */
        /*if (numTokens < 1) {
            continue;
        }
        switch (token) {
            case 'c':
                if(numTokens != 3){
                    errorParse();
                }
                if(insertCommand(line))
                    break;
                return;
            
            case 'l':
                if(numTokens != 2)
                    errorParse();
                if(insertCommand(line))
                    break;
                return;
            
            case 'd':
                if(numTokens != 2)
                    errorParse();
                if(insertCommand(line))
                    break;
                return;
            
            case '#':
                break;
            */
            //default: { /* error */
            /*    errorParse();
            }
        }
    }
    end=1;
    
    fclose(f);
}*/


void *applyCommands(void* command){

        int inode_index[INODE_TABLE_SIZE],i=0;
        //const char* command = removeCommand();
            for(int i=0;i<50;i++){
		        inode_index[i]=-1;
	        }
        char token, type;
        char name[MAX_INPUT_SIZE];
        int numTokens = sscanf(command, "%c %s %c", &token, name, &type);
        
        /* erro de invalid command duvida*/
        if (numTokens < 2) {
            fprintf(stderr, "Error: invalid command in Queue\n");
            exit(EXIT_FAILURE);
        }
        
        int searchResult;
        
        switch (token) {
            case 'c':
                switch (type) {
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
            case 'd':
                printf("Delete: %s\n", name);
                delete(name,inode_index);
                break;
            default: { /* error */
                fprintf(stderr, "Error: command to apply\n");
                exit(EXIT_FAILURE);
            }
        }
    return NULL;
}

int main(int argc, char* argv[]) {
    
    /*struct timeval t1;
    struct timeval t2;
    double t3;*/ 
    int sockfd;
    struct sockaddr_un server_addr;
    socklen_t addrlen;
    char *path;

    int i,threads=atoi(argv[1]);
    //FILE *outputfile;
    pthread_t tid[MAX_THREADS];
    

    if (argv[3]){
        fprintf(stderr,"FAILURE: MORE THAN THREE ARGUMENTS\n");
        exit(EXIT_FAILURE);
    }

    if(threads<0){
        fprintf(stderr,"THREAD VALUES ARE NOT VALID\n");
        exit(EXIT_FAILURE);
    }

    if ((sockfd = socket(AF_UNIX, SOCK_DGRAM, 0)) < 0) {
        perror("server: can't open socket");
        exit(EXIT_FAILURE);
    }

    path = argv[2];

    unlink(path);

  addrlen = setSockAddrUn (argv[2], &server_addr);
  if (bind(sockfd, (struct sockaddr *) &server_addr, addrlen) < 0) {
    perror("server: bind error");
    exit(EXIT_FAILURE);
  }


    /* init filesystem */
    
    init_fs();
    
    if(threads>1){
        pthread_cond_init(&can_wr,NULL);
        pthread_cond_init(&can_apply,NULL);
        pthread_mutex_init(&lock,NULL);
    }
    /* process input and print tree */
     addrlen=sizeof(struct sockaddr_un);
    while(1){
    struct sockaddr_un client_addr;
    char in_buffer[INDIM];
    int c;

        c = recvfrom(sockfd, in_buffer, sizeof(in_buffer)-1, 0,(struct sockaddr *)&client_addr, &addrlen);
        if (c <= 0) 
            continue;
        for(i=0;i<threads;i++){
            if(pthread_create(&tid[i],NULL,applyCommands,client_addr.sun_path)!=0){
                fprintf(stderr,"PTHREAD CREATE FAILURE");
                exit(EXIT_FAILURE);
            }
        in_buffer[c]='\0';
        
        printf("Recebeu comandos de %s\n", client_addr.sun_path);
        
        
        for(i=0;i<threads;i++){
            if (pthread_join(tid[i],NULL)!=0){
                exit(EXIT_FAILURE);
            }
        }

    }
    }
    
   
    
    /*gettimeofday(&t2,NULL);
    t3 = (t2.tv_sec-t1.tv_sec)+((t2.tv_usec-t1.tv_usec)/1000000); //alterar nome de t3
    printf("TecnicoFS completed in %.4f seconds\n",t3);
*/
    //outputfile=fopen(argv[2],"w");
    //print_tecnicofs_tree(outputfile);
    
    //fclose(outputfile);
    
    /* release allocated memory */
    destroy_fs();
    
    exit(EXIT_SUCCESS);
}
