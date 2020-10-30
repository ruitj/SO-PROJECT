#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <string.h>
#include <ctype.h>
#include <pthread.h>
#include "fs/operations.h"
#include <sys/time.h>

#define MAX_COMMANDS 150000
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

void locking(pthread_mutex_t* lock,pthread_rwlock_t* lock_rw,char* strategy,char* Acess_type){
	
	if (strcmp(strategy,mutex)==0){
		pthread_mutex_lock(lock);
        return;
	}

	if((strcmp(Acess_type,read)==0)&&(strcmp(strategy,rwlock)==0)) {
		pthread_rwlock_rdlock(lock_rw);
		return;
	}
	
	if((strcmp(Acess_type,write)==0)&&(strcmp(strategy,rwlock)==0)) {
		pthread_rwlock_wrlock(lock_rw);
		return;
	}
    
    if(strcmp(strategy,nosync)==0){
        return;
    }
    
    else{
        fprintf(stderr, "%s", "LOCKING ERROR\n");
        exit(EXIT_FAILURE);
    }
}

void unlocking(pthread_mutex_t* lock,pthread_rwlock_t* lock_rw,char* strategy){
	
	if (strcmp(strategy,mutex)==0){
		pthread_mutex_unlock(lock);
        return;
	}

	if(strcmp(strategy,rwlock)==0){
		pthread_rwlock_unlock(lock_rw);
		return;
	}
    
    if(strcmp(strategy,nosync)==0){
        return;
    }
	else{
        fprintf(stderr, "%s", "UNLOCKING ERROR\n");
        exit(EXIT_FAILURE);
    }
}

char* removeCommand(pthread_mutex_t lock,char* strategy,char* acess) {
    
    //pthread_mutex_lock(&lock);
    locking(&lock,&lock_rw,strategy,"write");
    
    if(numberCommands > 0){
        numberCommands--;
        headQueue++;
        unlocking(&lock,&lock_rw,strategy);
        return inputCommands[headQueue];  
    }
    
    unlocking(&lock,&lock_rw,strategy);
    
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



void *applyCommands(void* strategy){
    while (numberCommands > 0){
        const char* command = removeCommand(lock,strategy,"write");
        
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
                        create(name, T_FILE);
                        break;
                    case 'd':
                        printf("Create directory: %s\n", name);
                        create(name, T_DIRECTORY);
                        break;
                    default:
                        fprintf(stderr, "Error: invalid node type\n");
                        exit(EXIT_FAILURE);
                    }
                break;
            case 'l': 
                searchResult = lookup(name);
                if (searchResult >= 0){
                    printf("Search: %s found\n", name);
                }
                else
                    printf("Search: %s not found\n", name);
                break;
            case 'd':
                printf("Delete: %s\n", name);
                delete(name);
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
    char strategy[7];
    FILE *outputfile;
    pthread_t tid[MAX_THREADS];
    
    if (argv[5]){
        fprintf(stderr,"FAILURE: MORE THAN THREE ARGUMENTS\n");
        exit(EXIT_FAILURE);
    }
    
    init_fs();
    strcpy(strategy,argv[4]);
    
    /* process input and print tree */
    processInput(argv[1]);
    
    // nosync e nr de threads superior a 1 -> erro
    if(threads<0){
        fprintf(stderr,"THREAD VALUES ARE NOT VALID\n");
        exit(EXIT_FAILURE);
    }
    
    if  ((strcmp(strategy,nosync)==0)&&(threads>1)) {
        fprintf(stderr,"ONLY ONE THREAD ALLOWED WITH NOSYNC STRATEGY\n");
        exit(EXIT_FAILURE);
    }
    
    else if ((strcmp(strategy,mutex)==0)||(strcmp(strategy,rwlock)==0)) {
        
        pthread_mutex_init(&lock, NULL);
        gettimeofday(&t1,NULL);
        
        for(i=0;i<threads;i++){
            if(pthread_create(&tid[i],NULL,applyCommands,strategy)!=0){
                fprintf(stderr,"PTHREAD CREATE FAILURE");
                exit(EXIT_FAILURE);
            }
        }

        for(i=0;i<threads;i++){
            if (pthread_join(tid[i],NULL)!=0){
                exit(EXIT_FAILURE);
            }
        }
    }

    else {
        gettimeofday(&t1,NULL);
        applyCommands(strategy);
    }
    
    gettimeofday(&t2,NULL);
    
    t3 = (t2.tv_sec-t1.tv_sec)+((t2.tv_usec-t1.tv_usec)/1000000);
    printf("TecnicoFS completed in %f seconds\n",t3);
    
    outputfile=fopen(argv[2],"w");
    print_tecnicofs_tree(outputfile);
    
    fclose(outputfile);
    
    /* release allocated memory */
    destroy_fs();
    
    exit(EXIT_SUCCESS);
}
