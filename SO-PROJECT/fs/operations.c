#include "operations.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "state.h"
#include <pthread.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdlib.h>
#include <strings.h>
#include <sys/uio.h>
#include <unistd.h>
#include <sys/stat.h>
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


int setSockAddrUn(char *path, struct sockaddr_un *addr) {

  if (addr == NULL)
    return 0;

  bzero((char *)addr, sizeof(struct sockaddr_un));
  addr->sun_family = AF_UNIX;
  strcpy(addr->sun_path, path);

  return SUN_LEN(addr);
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
		
        if (entries[i].inumber != FREE_INODE && strcmp(entries[i].name, name) == 0) {   
			return entries[i].inumber;
        }
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
	int parent_inumber, child_inumber;
	char *parent_name,parent_copy[MAX_FILE_NAME],unlock_path[MAX_FILE_NAME],*child_name, name_copy[MAX_FILE_NAME],new_path[MAX_FILE_NAME],*parent_name_parent,*parent_name_child;
	int i=0;                            // passar o vetor como referencia
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




	strcpy(parent_copy, parent_name);  
	split_parent_child_from_path(parent_copy,&parent_name_parent,&parent_name_child);
	
	
	if (strlen(parent_name)==0){
		pthread_rwlock_wrlock(&inode_table[FS_ROOT].lock);
	}
	if (parent_inumber== FAIL) {
		printf("failed to create %s, invalid parent dir %s\n",
		        name, parent_name);
				if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);

					}
				else{
					
					strcpy(new_path,name);
					inode_get(FS_ROOT, &nType, &data);
					char *path = strtok_r(new_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								pthread_rwlock_unlock(&inode_table[i].lock);
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
				
		return FAIL;
	}
	inode_get(parent_inumber, &pType, &pdata);
	if(pType != T_DIRECTORY) {
		printf("failed to create %s, parent %s is not a dir\n",
		        name, parent_name);
				 if(parent_inumber==0){
					 pthread_rwlock_unlock(&inode_table[child_inumber].lock);
					}
				else{
					
					strcpy(unlock_path,name);
					inode_get(FS_ROOT, &nType, &data);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								pthread_rwlock_unlock(&inode_table[i].lock);
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
				}
			return FAIL;
	}
	if (lookup_sub_node(child_name, pdata.dirEntries) != FAIL) {
		printf("failed to create %s, already exists in dir %s\n",
		       child_name, parent_name);
				if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					
					strcpy(unlock_path,name);
					inode_get(FS_ROOT, &nType, &data);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								pthread_rwlock_unlock(&inode_table[i].lock);
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					
					}
				}
		return FAIL;
	}
	/* create node and add entry to folder that contains new node */
	if (child_inumber == FAIL) {

		printf("failed to create %s in  %s, couldn't allocate inode\n",
		        child_name, parent_name);
				if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					
					}
				else{
					
					strcpy(unlock_path,name);
					inode_get(FS_ROOT, &nType, &data);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								pthread_rwlock_unlock(&inode_table[i].lock);
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					
					}
				}
		return FAIL;
	}
	
	if (dir_add_entry(parent_inumber, child_inumber, child_name) == FAIL) {
		printf("could not add entry %s in dir %s\n",
		       child_name, parent_name);
				if(parent_inumber==0){
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					}
				else{
					
					strcpy(unlock_path,name);
					inode_get(FS_ROOT, &nType, &data);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								pthread_rwlock_unlock(&inode_table[i].lock);
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					
					}
				}
				return FAIL;
			

	}
		if(parent_inumber==0){
	
			pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
			
			return SUCCESS;
			}
		else{
			strcpy(unlock_path,name);
					inode_get(FS_ROOT, &nType, &data);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								pthread_rwlock_unlock(&inode_table[i].lock);
								inode_index[i]=-1;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					
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
	char *parent_name,parent_copy[MAX_FILE_NAME],unlock_path[MAX_FILE_NAME],*child_name, name_copy[MAX_FILE_NAME],new_path[MAX_FILE_NAME],*parent_name_parent,*parent_name_child;
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
	
	strcpy(parent_copy, parent_name);  
	split_parent_child_from_path(parent_copy,&parent_name_parent,&parent_name_child);
	strcpy(new_path,name);
	inode_get(parent_inumber, &pType, &pdata);
	child_inumber = lookup_sub_node(child_name, pdata.dirEntries);

	if (parent_inumber == FAIL) {
		printf("failed to delete %s, invalid parent dir %s\n",
		        child_name, parent_name);
					if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
					}
				else{
					strcpy(unlock_path, name);
				
					pthread_rwlock_unlock(&inode_table[parent_inumber].lock);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
					return FAIL;
					}


	inode_get(parent_inumber, &pType, &pdata);

	if(pType != T_DIRECTORY) {
		printf("failed to delete %s, parent %s is not a dir\n",
		        child_name, parent_name);
				if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
					}
				else{
					
					strcpy(unlock_path, name);
					
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
		return FAIL;
	}

	if (child_inumber == FAIL) {
		
		printf("could not delete %s, does not exist in dir %s\n",
		       name, parent_name);
			   if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
					}
				else{
					
					
					strcpy(unlock_path, name);
					
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					
					}
		return FAIL;
	}

	inode_get(child_inumber, &cType, &cdata);

	if (cType == T_DIRECTORY && is_dir_empty(cdata.dirEntries) == FAIL) {
		printf("could not delete %s: is a directory and not empty\n",
		       name);
			   if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);

					}
				else{
					
					strcpy(unlock_path, name);
					
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
		return FAIL;
	}

	/* remove entry from folder that contained deleted node */
	if (dir_reset_entry(parent_inumber, child_inumber) == FAIL) {
		printf("failed to delete %s from dir %s\n",
		       child_name, parent_name);
			   if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);

					}
				else{
					
					strcpy(unlock_path, name);
					
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
		return FAIL;
	}

	if (inode_delete(child_inumber) == FAIL) {
		printf("could not delete inode number %d from dir %s\n",
		       child_inumber, parent_name);
			   if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);
					}
				else{
					
					strcpy(unlock_path, name);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
		return FAIL;
	}

	if(parent_inumber==0){		 
					pthread_rwlock_unlock(&inode_table[FS_ROOT].lock);

					}
				else{
					
					strcpy(unlock_path, name);
					char *path = strtok_r(unlock_path, delim,&saveptr);
					while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
						pthread_rwlock_unlock(&inode_table[current_inumber].lock);
						for(i=0;i<INODE_TABLE_SIZE;i++){
							if (inode_index[i]==current_inumber){								
								inode_index[i]=-1;
								break;
							}
						}
						inode_get(current_inumber, &nType, &data);
						path = strtok_r(NULL, delim,&saveptr);
					}
					}
	return SUCCESS;
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
	char full_path[MAX_FILE_NAME],*parent,*child,newname[MAX_FILE_NAME],newpath[MAX_FILE_NAME];                             // passar o vetor como referencia
	char delim[] = "/",*saveptr,*saveread;
	int unlocked=1;
	strcpy(full_path, name);
	/* start at root node */
	int current_inumber = FS_ROOT,read_number=0;
	/* use for copy */
	type nType;
	union Data data;
	/* get root inode data */
	inode_get(current_inumber, &nType, &data);
	/* search for all sub nodes */
	strcpy(newname,name);
	split_parent_child_from_path(newname,&parent,&child);
	//printf("child=%s\n",child);
	if(strlen(child)==0){
		return FS_ROOT;
	}
	if(strlen(parent)==0){
		current_inumber = lookup_sub_node(child, data.dirEntries);
		return current_inumber;
	}
	char *path = strtok_r(full_path, delim,&saveptr);
	
	while (path != NULL && (current_inumber = lookup_sub_node(path, data.dirEntries)) != FAIL) {
		if(strcmp(path,child)==0){
			if(is_read!=1)
			{pthread_rwlock_wrlock(&inode_table[current_inumber].lock);
			return current_inumber;}
			else
			{
				break;
			}
			
		}
		for(int i=0;i<INODE_TABLE_SIZE;i++){
			if(inode_index[i]==current_inumber){
				unlocked=0;
				break;
			}
			unlocked=2;
		}
		if(unlocked==2)
		{	
			pthread_rwlock_rdlock(&inode_table[current_inumber].lock);
			}
		for(int i=0;i<INODE_TABLE_SIZE;i++){
			if (inode_index[i]==-1){
				inode_index[i]=current_inumber;
			}
		}
		inode_get(current_inumber, &nType, &data);
		path = strtok_r(NULL, delim,&saveptr);
	}

	strcpy(newpath,name);
	inode_get(FS_ROOT, &nType, &data);
	char *read_path = strtok_r(newpath, delim,&saveread);
	
	while (read_path != NULL && (read_number = lookup_sub_node(read_path, data.dirEntries)) != FAIL) {
	
		if((strcmp(read_path,child)==0)&&(is_read==1)) {
			return current_inumber;
		}
		for(int i=0;i<INODE_TABLE_SIZE;i++){
			if(inode_index[i]==read_number){
				pthread_rwlock_unlock(&inode_table[i].lock);
			}
		}
		inode_get(read_number, &nType, &data);
		read_path = strtok_r(NULL, delim,&saveread);
	}
	return FAIL;	
}
int move(char* source,char* destination,int inode_index[]){
	int destination_number,i=0,parent_inumber,child_inumber;
	char name_copy[MAX_FILE_NAME],*parent_name,*child_name;
	destination_number=lookup(destination,inode_index,i,1);
	if (destination_number!=FAIL){
		return FAIL;
		}
	/*search for parent destination inumber*/
	strcpy(name_copy,destination);
	split_parent_child_from_path(name_copy,&parent_name,&child_name);
	parent_inumber = lookup(parent_name,inode_index,i,1);
	
	/*search for existent child inumber*/
	strcpy(name_copy,source);
	split_parent_child_from_path(name_copy,&parent_name,&child_name);
	child_inumber=lookup(source,inode_index,i,1);

	/* add to entry*/
	dir_add_entry(parent_inumber,child_inumber,child_name);

	/* reset entry*/
	strcpy(name_copy,source);
	split_parent_child_from_path(name_copy,&parent_name,&child_name);
	parent_inumber=lookup(parent_name,inode_index,i,1);
	child_inumber=lookup(source,inode_index,i,1);
	dir_reset_entry(parent_inumber,child_inumber);
	return SUCCESS;
}


/*
 * Prints tecnicofs tree.
 * Input:
 *  - fp: pointer to output file
 */

void print_tecnicofs_tree(FILE *fp){
	inode_print_tree(fp, FS_ROOT, "");
}
