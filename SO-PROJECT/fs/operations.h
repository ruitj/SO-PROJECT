#ifndef FS_H
#define FS_H
#include "state.h"
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdlib.h>
#include <strings.h>
#include <sys/uio.h>
#include <unistd.h>
#include <sys/stat.h>
int setSockAddrUn(char *path, struct sockaddr_un *addr);
void init_fs();
void destroy_fs();
int is_dir_empty(DirEntry *dirEntries);
int create(char *name, type nodeType,int inode_index[]);
int delete(char *name,int inode_index[]);
int lookup(char *name,int inode_index[],int i,int is_read);
void print_tecnicofs_tree(FILE *fp);
void unlock_nodes(inode_t* inode_table,int max,int inode_index[]);
int move(char* source,char* destination,int inode_index[]);
#endif /* FS_H */
