#include "operations.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "state.h"
#include <pthread.h>

extern inode_t inode_table[INODE_TABLE_SIZE];

/* Given a path, fills pointers with strings for the parent path and child
 * file name
 * Input:
 *  - path: the path to split. ATENTION: the function may alter this parameter
 *  - parent: reference to a char*, to store parent path
 *  - child: reference to a char*, to store child file name
 */

pthread_rwlock_t lock;

void split_parent_child_from_path(char * path, char ** parent, char ** child) {

	int n_slashes = 0, last_slash_location = 0;
	int len = strlen(path);
	// deal with trailing slash ( a/x vs a/x/ )
	if (path[len-1] == '/') {
		path[len-1] = '\0';
	}

	for (int i=0; i < len; ++i) {
		if (path[i] == '/' && path[i+1] != '\0') {
			last_slash_location = i;
			n_slashes++;
		}
	}

	if (n_slashes == 0) { // root directory
		*parent = "";
		*child = path;
		//printf("child=%s\n",*child);	
		return;
	}
	
	path[last_slash_location] = '\0';
	*parent = path;
	*child = path + last_slash_location + 1;
	//printf("parent=%s",*parent);
}

/*
 * Initializes tecnicofs and creates root node.
 */

void init_fs() {
	
	inode_table_init();
	
	/* create root inode */
	int root = inode_create(T_DIRECTORY);
	
	if (root != FS_ROOT) {
		printf("failed to create node for tecnicofs root\n");
		exit(EXIT_FAILURE);
	}
}


/*
 * Destroy tecnicofs and inode table.
 */

void destroy_fs() {
	inode_table_destroy();
}


/*
 * Checks if content of directory is not empty.
 * Input:
 *  - entries: entries of directory
 * Returns: SUCCESS or FAIL
 */

int is_dir_empty(DirEntry *dirEntries) {
	if (dirEntries == NULL) {
		return FAIL;
	}
	for (int i = 0; i < MAX_DIR_ENTRIES; i++) {
		if (dirEntries[i].inumber != FREE_INODE) {
			return FAIL;
		}
	}
	return SUCCESS;
}


/*
 * Looks for node in directory entry from name.
 * Input:
 *  - name: path of node
 *  - entries: entries of directory
 * Returns:
 *  - inumber: found node's inumber
 *  - FAIL: if not found
 */

int lookup_sub_node(char *name, DirEntry *entries) {
	if (entries == NULL) {
		return FAIL;
	}
	for (int i = 0; i < MAX_DIR_ENTRIES; i++) {
		//pthread_rwlock_rdlock(&entries[i].trinco);
        if (entries[i].inumber != FREE_INODE && strcmp(entries[i].name, name) == 0) {
            //pthread_rwlock_unlock(&entries[i].trinco);
			return entries[i].inumber;
        }
		//pthread_rwlock_unlock(&entries[i].trinco);
    }
	return FAIL;
}


/*
 * Creates a new node given a path.
 * Input:
 *  - name: path of node
 *  - nodeType: type of node
 * Returns: SUCCESS or FAIL
 */

int create(char *name,type nodeType,int inode_index[]){

	int parent_inumber, child_inumber,parent_child;
	char *parent_name,parent_copy[MAX_FILE_NAME],*child_name, name_copy[MAX_FILE_NAME],new_path[MAX_FILE_NAME],*parent_name_parent,*parent_name_child;
	int i=0,unlocked=1;                            // passar o vetor como referencia
	char delim[] = "/",*saveptr;
	/* use for copy */
	type pType;
	union Data pdata;
	int current_inumber = FS_ROOT;

	/* use for copy */
	type nType;
	union Data data;

	
	strcpy(name_copy, name);             
	split_parent_child_from_path(name_copy, &parent_name, &child_name);
	parent_inumber = lookup(parent_name,inode_index,i,0);
	child_inumber = inode_create(nodeType);
	
	pthread_rwlock_trywrlock(&inode_table[child_inumber].lock);
	
	if(strlen(parent_name)>0){
	strcpy(parent_copy, parent_name);  
	split_parent_child_from_path(parent_copy,&parent_name_parent,&parent_name_child);
	strcpy(new_path,name);
	//puts(parent_name_child);
	inode_get(current_inumber, &nType, &data);

	//pthread_rwlock_rdlock(&inode_table[child_inumber].lock);
	//pthread_rwlock_rdlock(&inode_table[parent_inumber].lock);
	char *path = strtok_r(new_path,delim,&saveptr);
	while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
		if(strcmp(path,parent_name_child)==0){
			for(int j=0;j<INODE_TABLE_SIZE;j++){
				if(inode_index[j]==current_inumber){
					unlocked=0;
					break;
				}
			}
		if(unlocked==1){
			puts("here");	
			pthread_rwlock_trywrlock(&inode_table[current_inumber].lock);
			inode_index[++i]=current_inumber;
			continue;
			}
		}
	pthread_rwlock_rdlock(&inode_table[current_inumber].lock);
	inode_index[++i]=current_inumber;
	inode_get(current_inumber, &nType, &data);
	path = strtok_r(NULL, delim,&saveptr);
	}
	parent_child = lookup(parent_name_child,inode_index,i,0);	
	
	if (parent_inumber == FAIL) {
		printf("failed to create %s, invalid parent dir %s\n",
		        name, parent_name);
				if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
					pthread_rwlock_unlock(&inode_table[child_inumber].lock);
					}
				else{
					
					unlock_nodes(inode_table,i,inode_index);
					}
				
		return FAIL;
	}
	inode_get(parent_inumber, &pType, &pdata);
	if(pType != T_DIRECTORY) {
		printf("failed to create %s, parent %s is not a dir\n",
		        name, parent_name);
				 if(parent_inumber==0){
					 pthread_rwlock_unlock(&inode_table[child_inumber].lock);
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					
					unlock_nodes(inode_table,i,inode_index);
					}

			return FAIL;
		}
	}
	if(strlen(parent_name)==0){
		//pthread_rwlock_trywrlock(&inode_table[FS_ROOT].lock);
		//pthread_rwlock_wrlock(&inode_table[FS_ROOT].lock);
		pthread_rwlock_rdlock(&inode_table[child_inumber].lock);
		
	}

	if (lookup_sub_node(child_name, pdata.dirEntries) != FAIL) {
		printf("failed to create %s, already exists in dir %s\n",
		       child_name, parent_name);
				if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					pthread_rwlock_unlock(&inode_table[child_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
			
		return FAIL;
	}
	/* create node and add entry to folder that contains new node */
	if (child_inumber == FAIL) {
		printf("failed to create %s in  %s, couldn't allocate inode\n",
		        child_name, parent_name);
				if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					pthread_rwlock_unlock(&inode_table[child_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
				
		return FAIL;
	}
	if (dir_add_entry(parent_inumber, child_inumber, child_name) == FAIL) {
		printf("could not add entry %s in dir %s\n",
		       child_name, parent_name);
				if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					pthread_rwlock_unlock(&inode_table[child_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
				return FAIL;
	}	
		if(parent_inumber==0){
			//puts("finally");
			pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
			pthread_rwlock_unlock(&inode_table[child_inumber].lock);
			return SUCCESS;
			}
		else{
			unlock_nodes(inode_table,i,inode_index);
			return SUCCESS;
			}

}
               
/*
 * Deletes a node given a path.
 * Input:
 *  - name: path of node
 * Returns: SUCCESS or FAIL
 */

int delete(char *name,int inode_index []){

	int parent_inumber, child_inumber;
	char *parent_name, *child_name, name_copy[MAX_FILE_NAME],new_path[MAX_FILE_NAME],*parent_name_parent,*parent_name_child;
	int i=0;
	char delim[] = "/",*saveptr;
	/* use for copy */
	type pType, cType;
	union Data pdata, cdata;
	/* use for copy */
	type nType;
	union Data data;
	int current_inumber = FS_ROOT;
	strcpy(name_copy, name);
	
	split_parent_child_from_path(name_copy, &parent_name, &child_name);
	
	parent_inumber = lookup(parent_name,inode_index,i,0);
	
	if (parent_inumber == FAIL) {
		printf("failed to delete %s, invalid parent dir %s\n",
		        child_name, parent_name);
					unlock_nodes(inode_table,i,inode_index);
					
					}
		return FAIL;

	strcpy(new_path,parent_name);
	if(strlen(parent_name)==0) {
		pthread_rwlock_trywrlock(&inode_table[FS_ROOT].lock);
	}  
	else
	{	
		split_parent_child_from_path(new_path,&parent_name_parent,&parent_name_child);
		strcpy(new_path,parent_name);
		inode_get(current_inumber, &nType, &data);
		char *path = strtok_r(new_path,delim,&saveptr);
		
		while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
		path = strtok_r(NULL, delim,&saveptr);
		pthread_rwlock_rdlock(&inode_table[current_inumber].lock);
		inode_get(current_inumber, &nType, &data);
			if(strcmp(new_path,parent_name_child)==0){
				pthread_rwlock_trywrlock(&inode_table[current_inumber].lock);
				}
		inode_index[++i]=current_inumber;
			}
		}
	
	inode_get(parent_inumber, &pType, &pdata);

	if(pType != T_DIRECTORY) {
		printf("failed to delete %s, parent %s is not a dir\n",
		        child_name, parent_name);
		return FAIL;
	}

	child_inumber = lookup_sub_node(child_name, pdata.dirEntries);

	if (child_inumber == FAIL) {
		printf("could not delete %s, does not exist in dir %s\n",
		       name, parent_name);
			   if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
		return FAIL;
	}

	inode_get(child_inumber, &cType, &cdata);

	if (cType == T_DIRECTORY && is_dir_empty(cdata.dirEntries) == FAIL) {
		printf("could not delete %s: is a directory and not empty\n",
		       name);
			   if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
		return FAIL;
	}

	/* remove entry from folder that contained deleted node */
	if (dir_reset_entry(parent_inumber, child_inumber) == FAIL) {
		printf("failed to delete %s from dir %s\n",
		       child_name, parent_name);
			   if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
		return FAIL;
	}

	if (inode_delete(child_inumber) == FAIL) {
		printf("could not delete inode number %d from dir %s\n",
		       child_inumber, parent_name);
			   if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
		return FAIL;
	}

	if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					unlock_nodes(inode_table,i,inode_index);
					
					}
	return SUCCESS;
}
void unlock_nodes(inode_t* inode_table,int max,int inode_index[]){
	int j,k;
	for(j=max;j<0;j--){
		k=inode_index[j];
		pthread_rwlock_unlock(&inode_table[k].lock);
		inode_index[j]=-3;
	}
}


/*
 * Lookup for a given path.
 * Input:
 *  - name: path of node
 * Returns:
 *  inumber: identifier of the i-node, if found
 *     FAIL: otherwise
 */

int lookup(char *name,int inode_index[],int i,int is_read) {   // alterar a variavel i       
	char full_path[MAX_FILE_NAME];                             // passar o vetor como referencia
	char delim[] = "/",*saveptr;
	int k,j,is_active=0,unlocked=1;
	strcpy(full_path, name);
	/* start at root node */
	int current_inumber = FS_ROOT,helper=0;

	/* use for copy */
	type nType;
	union Data data;
	/* get root inode data */
	inode_get(current_inumber, &nType, &data);
	//printf("number=%d\n",current_inumber);
	/* search for all sub nodes */
	//pthread_rwlock_rdlock(&inode_table[current_inumber].lock);
	if (strlen(name)==0){
		return FS_ROOT;
	}
	char *path = strtok_r(full_path, delim,&saveptr);
	while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
		
		//printf("path=%s\n",path);
		is_active=1;
			for(int j=0;j<INODE_TABLE_SIZE;j++){
				if(inode_index[j]==current_inumber){
					unlocked=0;
					break;
				}
			}
			if(unlocked==1){
				//printf("path=%s\n",path);
				pthread_rwlock_rdlock(&inode_table[current_inumber].lock);
				inode_index[++i]=current_inumber;
				helper++;
			}
		inode_get(current_inumber, &nType, &data);
		path = strtok_r(NULL, delim,&saveptr);
	}
	
		while((helper-->0)&&(is_active==1)&&(is_read==1)) {
		k=i--;
		j=inode_index[k];	
		pthread_rwlock_unlock(&inode_table[j].lock);
		inode_index[k]=-1;		
		}
	return current_inumber;	
}

/*
 * Prints tecnicofs tree.
 * Input:
 *  - fp: pointer to output file
 */

void print_tecnicofs_tree(FILE *fp){
	inode_print_tree(fp, FS_ROOT, "");
}
