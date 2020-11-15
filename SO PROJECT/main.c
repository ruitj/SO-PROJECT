#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <string.h>
#include <ctype.h>
#include <pthread.h>
#include "fs/operations.h"
#include <sys/time.h>

#define MAX_COMMANDS 10
#define MAX_INPUT_SIZE 100
#define MAX_THREADS 100
#define mutex "mutex"
#define read "read"
#define write "write"
#define rwlock "rwlock"
#define nosync "nosync"

int numberThreads = 0;
char inputCommands[MAX_COMMANDS][MAX_INPUT_SIZE];
int numberCommands = 0;
int headQueue = 0;

pthread_mutex_t lock;
pthread_rwlock_t lock_rw;

int insertCommand(char* data) {    
    if(numberCommands != MAX_COMMANDS) {
        strcpy(inputCommands[numberCommands++], data);
        return 1;
    }

    return 0;
}

char* removeCommand() {
    if(numberCommands > 0){
        numberCommands--;
        return inputCommands[headQueue++];  
    }
    return NULL;
}

void errorParse(){
    
    fprintf(stderr, "Error: command invalid\n");
    exit(EXIT_FAILURE);
}

void processInput(char* ficheiro){
    
    char line[MAX_INPUT_SIZE];
    FILE *f = fopen(ficheiro,"r");
    
    /* break loop with ^Z or ^D */
    while (fgets(line, sizeof(line)/sizeof(char),f)) {
        char token, type;
        char name[MAX_INPUT_SIZE];
        int numTokens = sscanf(line, "%c %s %c", &token, name, &type);
        
        /* perform minimal validation */
        if (numTokens < 1) {
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
            
            default: { /* error */
                errorParse();
            }
        }
    }
    
    fclose(f);
}



void *applyCommands(){
    int inode_index[INODE_TABLE_SIZE]={-1},i=0;
    while (numberCommands > 0){
        const char* command = removeCommand();
        //printf("%s",command);
        
        if (command == NULL){
            continue;
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
                /*for(i=0;i<INODE_TABLE_SIZE;i++){
                    inode_index[i]=0;
                }*/
                
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
    }
    
    return NULL;
}

int main(int argc, char* argv[]) {
    
    struct timeval t1;
    struct timeval t2;
    double t3; 
    
    /* init filesystem */
    int i,threads=atoi(argv[3]);
    FILE *outputfile;
    pthread_t tid[MAX_THREADS];
    
    if (argv[4]){
        fprintf(stderr,"FAILURE: MORE THAN THREE ARGUMENTS\n");
        exit(EXIT_FAILURE);
    }
    
    init_fs();
    
    /* process input and print tree */
    processInput(argv[1]);

    if(threads<0){
        fprintf(stderr,"THREAD VALUES ARE NOT VALID\n");
        exit(EXIT_FAILURE);
    }
    gettimeofday(&t1,NULL);
        
        for(i=0;i<threads;i++){
            if(pthread_create(&tid[i],NULL,applyCommands,NULL)!=0){
                fprintf(stderr,"PTHREAD CREATE FAILURE");
                exit(EXIT_FAILURE);
            }
        }

        for(i=0;i<threads;i++){
            if (pthread_join(tid[i],NULL)!=0){
                exit(EXIT_FAILURE);
            }
        }
    gettimeofday(&t2,NULL);
    t3 = (t2.tv_sec-t1.tv_sec)+((t2.tv_usec-t1.tv_usec)/1000000); //alterar nome de t3
    printf("TecnicoFS completed in %.4f seconds\n",t3);
    // destroy mutex
    outputfile=fopen(argv[2],"w");
    print_tecnicofs_tree(outputfile);
    
    fclose(outputfile);
    
    /* release allocated memory */
    destroy_fs();
    
    exit(EXIT_SUCCESS);
}
