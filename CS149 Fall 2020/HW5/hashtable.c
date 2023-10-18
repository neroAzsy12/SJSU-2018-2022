//
// Created by azsy on 11/18/20.
//
#define _POSIX_C_SOURCE 200112L
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define BUCKETS (256)       // number of buckets for the hashtable

// node structure for the linked list
struct list_node {
    char *unique_word;      // unique word
    struct list_node *next; // the next list_node
};

// linked list structure for a hashtable bucket
struct linked_list {
    struct list_node *head_node;    // the head node of the linked list
    int total_unique;               // number of unique words in the linked list
    pthread_mutex_t lock;           // for bucket locking / unlocking
};

// initialize the bucket linked list
struct linked_list* list_initialize() {
    // initializing a new bucket
    struct linked_list *new_list_bucket = malloc(sizeof(struct linked_list));

    // initialize the head_node, total_unique and the lock
    new_list_bucket->head_node = NULL;                          // make the head node be NULL
    new_list_bucket->total_unique = 0;                          // total unique is 0
    pthread_mutex_init(&(new_list_bucket->lock), NULL);// initialize the pthread mutex

    return new_list_bucket;                                     // returns the new bucket linked list
}

// creates the hash entry node for a linked list
struct list_node* create_hashentry(char *word){
    struct list_node *node_entry = malloc(sizeof(struct list_node));     // malloc space for the entry node
    node_entry->unique_word = malloc(strlen(word) + 1);     // allocate memory for the unique word
    strcpy(node_entry->unique_word, word);                               // copies word into unique word
    //node_entry->unique_word = strdup(word);
    node_entry->next = NULL;                                             // sets the next node to NULL
    return node_entry;                                                   // returns the new entry node
}

// initialize global list lock
pthread_mutex_t list_lock = PTHREAD_MUTEX_INITIALIZER;

// initializing the hashtable
struct linked_list *hashtable[BUCKETS] = {0};

// Hash Function using djb2 by Dan Bernstein, used to get the bucket index for the hashtable
size_t hash(const char *str){
    size_t hash = 5381;

    for(int i = 0; str[i] != '\0'; i++){
        hash += hash * 33 + (int)str[i];
    }

    return hash % BUCKETS;
}

int contains_word(char *word){
    size_t bucket_index = hash(word);

    if(hashtable[bucket_index] == NULL){
        return -1;
    }

    // lock the hash bucket linked list
    pthread_mutex_lock(&(hashtable[bucket_index]->lock));

    // get the head_node from the bucket linked list
    struct list_node *current_node = hashtable[bucket_index]->head_node;

    // iterate through the nodes in the linked list to see if the list contains word
    while(current_node){
        if(strcmp(current_node->unique_word, word) == 0){
            // unlock the hash bucket linked list before returning
            pthread_mutex_unlock(&(hashtable[bucket_index]->lock));
            return 0;
        }
        current_node = current_node->next;
    }

    // unlock the hash bucket linked list
    pthread_mutex_unlock(&(hashtable[bucket_index]->lock));
    return -1;
}

// inserting a new entry node to the bucket linked list
int insert_word(char *word) {
    size_t bucket_index = hash(word);                   // gets the hash value of the word, bucket index
    //struct list_node *entry_node = create_hashentry(word);

    if(hashtable[bucket_index] == NULL) {
        pthread_mutex_lock(&(list_lock));

        struct linked_list *list = list_initialize();
        hashtable[bucket_index] = list;

        pthread_mutex_unlock(&(list_lock));
    }

    //if(find_word(word) == 1){
    //    return 1;
    //}

    pthread_mutex_lock(&(hashtable[bucket_index]->lock));

    struct list_node *entry_node = create_hashentry(word);
    entry_node->next = hashtable[bucket_index]->head_node;
    hashtable[bucket_index]->head_node = entry_node;
    hashtable[bucket_index]->total_unique++;

    pthread_mutex_unlock(&hashtable[bucket_index]->lock);
    return 0;
}

int dump_hashtable(){
    pthread_mutex_lock(&list_lock);
    int i = 0;
    while(i < BUCKETS){
        if(hashtable[i] == NULL){
            i++;
            continue;
        }else{
            struct list_node *current_node = hashtable[i]->head_node;
            while(current_node != NULL){
                struct list_node *next_node = current_node;
                current_node = current_node->next;
                free(next_node->unique_word);
                free(next_node);
            }

            free(hashtable[i]);
            i++;
        }
    }
    pthread_mutex_unlock(&list_lock);
    return 0;
}

static volatile int unique_counter = 0;
pthread_mutex_t lock_thread = PTHREAD_MUTEX_INITIALIZER;

void *file_thread(void *file_name){
    FILE *file_pointer;
    file_pointer = fopen((char*)file_name, "r");

    if(file_pointer == NULL){
        perror(file_name);
        return (void*)2;
    }else{
        //printf("works\n");
        while(1){
            pthread_mutex_lock(&lock_thread);
            char *ptr;
            int vars = fscanf(file_pointer, "%ms", &ptr);
            if(vars != 1){
                pthread_mutex_unlock(&lock_thread);
                break;
            }
            //printf("%s\n", ptr);
            if(contains_word(ptr) != 0) {
                insert_word(ptr);
                unique_counter++;
            }
            //    unique_counter++;
            //}
            free(ptr);
            pthread_mutex_unlock(&lock_thread);
        }
    }
    fclose(file_pointer);
    //free(file_pointer);
    return 0;
}

int main(int argc, char *argv[]){
    pthread_t threads[argc-1];
    //printf("argc: %d\n", argc);

    //memset(hashtable, 0, sizeof(hashtable));
    // pthread_mutex_init(&lock, NULL);
    for(int i = 1; i < argc; i++){
        pthread_create(&threads[i-1], NULL, file_thread, argv[i]);
    }

    for(int j = 0; j < argc-1; j++){
        pthread_join(threads[j], NULL);
    }

    //printf("end\n");
    //int count = 0;
    //for(int i = 0; i < BUCKETS; i++){
    //    if(hashtable[i] != NULL){
    //        count += hashtable[i]->total_unique;
    //    }
    //}
    //printf("%d\n", count);
    printf("%d\n", unique_counter);
    dump_hashtable();
    return 0;
}