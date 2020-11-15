#ifndef FS_H
#define FS_H
#include "state.h"

void init_fs();
void destroy_fs();
int is_dir_empty(DirEntry *dirEntries);
int create(char *name, type nodeType,int inode_index[]);
int delete(char *name,int inode_index[]);
int lookup(char *name,int inode_index[],int i,int is_read);
void print_tecnicofs_tree(FILE *fp);
void unlock_nodes(inode_t* inode_table,int max,int inode_index[]);
#endif /* FS_H */
