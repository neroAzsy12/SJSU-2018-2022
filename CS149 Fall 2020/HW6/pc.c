//
// Created by Azael Zamora on 12/1/20.
//
#define _POSIX_C_SOURCE 200112L
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#define BUCKETS (256)

/// linked list structure
typedef struct list_node {
    struct list_node *next; // next node in the list
    char *unique_word;      // the unique word of node
    int count;              // the count of the word
} list_node;

typedef struct linked_list {
    list_node *head_node;   // the starting node of the linked_list
    //int popular_word_count; // the overall popular count of a word in the linked list
} linked_list;

// initialize bucket linked list
void list_init(linked_list *ll) {
    // initialize the head_node, and popular word count
    ll->head_node = NULL;
    //ll->popular_word_count = 0;
}

void list_insert(linked_list *list, char *word){
    list_node *node_entry = malloc(sizeof(list_node));

    node_entry->next = 0;
    node_entry->count = 1;
    node_entry->unique_word = malloc(strlen(word) + 1);
    strcpy(node_entry->unique_word, word);

    node_entry->next = list->head_node;
    list->head_node = node_entry;
}

int list_contains(linked_list *list, char *word){
    int contains = -1;

    list_node *current_node = list->head_node;

    while(current_node != NULL){
        if(strcmp(current_node->unique_word, word) == 0) {
            //current_node->count++;
            contains = current_node->count;
            break;
        }
        current_node = current_node->next;
    }

    return contains;
}

void list_update_word_count(linked_list *list, char *word){
    list_node *current_node = list->head_node;
    while(current_node != NULL){
        if(strcmp(current_node->unique_word, word) == 0){
            current_node->count++;
            break;
        }
        current_node = current_node->next;
    }
}
/// hashtable structure
typedef struct hashtable {
    linked_list *buckets[BUCKETS];
    int most_popular_word_count;
} hashtable;

void hashtable_init(hashtable *ht){
    for(int i = 0; i < BUCKETS; i++){
        ht->buckets[i] = malloc(sizeof(linked_list));
        ht->most_popular_word_count = 0;

        list_init(ht->buckets[i]);
    }
}

size_t hash(const char *str){
    size_t hash = 5381;

    for(int i = 0; str[i] != '\0'; i++){
        hash += hash * 33 + (int)str[i];
    }

    return hash % BUCKETS;
}

void hashtable_insert(hashtable *ht, char *word){
    size_t hashed = hash(word);

    if(ht->most_popular_word_count == 0){
        ht->most_popular_word_count = 1;
    }

    list_insert(ht->buckets[hashed], word);
}

int hashtable_contains(hashtable *ht, char *word){
    size_t hashed = hash(word);
    return list_contains(ht->buckets[hashed], word);
}

void update_existing_word(hashtable *ht, char *word){
    size_t hashed = hash(word);
    list_update_word_count(ht->buckets[hashed], word);
}

void update_popular_count(hashtable *ht, char *word){
    int new_count = hashtable_contains(ht, word);
    if(ht->most_popular_word_count < new_count){
        ht->most_popular_word_count = new_count;
    }
}

void dump_hashtable(hashtable *ht){
    int bucket = 0;
    while(bucket < BUCKETS){
        list_node *current_node = ht->buckets[bucket]->head_node;
        while(current_node != NULL){
            list_node *next_node = current_node;
            current_node = current_node->next;
            free(next_node->unique_word);
            free(next_node);
        }
        free(ht->buckets[bucket]);
        bucket++;
    }
    free(ht);
}
/// Queue structure
typedef struct node {
    struct node *next;
    char *word;
} node;

typedef struct queue {
    node *head;
    node *tail;

    pthread_mutex_t head_lock;
    pthread_mutex_t tail_lock;

} queue;

void queue_init(queue *q){
    q->head = NULL;
    q->tail = NULL;

    pthread_mutex_init(&q->head_lock, NULL);
    pthread_mutex_init(&q->tail_lock, NULL);
}

void queue_enqueue(queue *q, char *word){
    node *new_node = malloc(sizeof(node));

    new_node->next = NULL;
    new_node->word = malloc(strlen(word) + 1);
    strcpy(new_node->word, word);

    pthread_mutex_lock(&q->tail_lock);
    if(q->head == NULL && q->tail == NULL){
        q->head = q->tail = new_node;
    }else{
        q->tail->next = new_node;
        q->tail = new_node;
    }

    pthread_mutex_unlock(&q->tail_lock);
}

char *queue_dequeue(queue *q){
    pthread_mutex_lock(&q->head_lock);

    if(q->head == NULL){
        pthread_mutex_unlock(&q->head_lock);
        return NULL;
    }

    node *current_head = q->head;
    char *word = current_head->word;

    if(q->head == q->tail){
        q->head = q->tail = NULL;
    }else{
        q->head = q->head->next;
    }

    free(current_head);
    pthread_mutex_unlock(&q->head_lock);

    return word;
}

/// structure for consumer
struct consumer_thread_params {
    queue *q;
    hashtable *ht;

} consumer_thread_params;

struct consumer_thread_params *params[4];
hashtable *hashtables[4] = {0};
queue *queues[4] = {0};
// queues[0] stores d, h, l, p, t, x
// queues[1] stores a, e, i, m, q, u, y
// queues[2] stores b, f, j, n, r, v, z
// queues[3] stores c, g, k, o, s, w
static volatile int most_popular_count = 0;
pthread_mutex_t lock_thread = PTHREAD_MUTEX_INITIALIZER;

void *produce_words(void *filename){
    FILE *fp;
    fp = fopen((char*)filename, "r");

    if(fp == NULL){
        perror(filename);
        return (void*)2;
    }else{
        while(1){
            pthread_mutex_lock(&lock_thread);

            char *ptr;
            int var = fscanf(fp, "%ms", &ptr);

            if(var != 1){
                pthread_mutex_unlock(&lock_thread);
                break;
            }

            int queue_index = ptr[0] & 3;

            //printf("producing the word %s on queue %d\n", ptr, queue_index);
            queue_enqueue(queues[queue_index], ptr);

            free(ptr);
            pthread_mutex_unlock(&lock_thread);
        }
    }
    fclose(fp);
    return 0;
}

void *consume_words(void *arg){
    struct consumer_thread_params *ctp = (struct consumer_thread_params*)arg;

    while(1){
        pthread_mutex_lock(&lock_thread);

        //printf("run the consumer\n");

        if(ctp->q->head == NULL){
            //printf("consumer finished\n");
            if(most_popular_count < ctp->ht->most_popular_word_count){
                most_popular_count = ctp->ht->most_popular_word_count;
            }
            pthread_mutex_unlock(&lock_thread);
            break;
        }

        char *word = queue_dequeue(ctp->q);
        //printf("consumed %s\n", word);

        if(hashtable_contains(ctp->ht, word) != -1){
            update_existing_word(ctp->ht, word);
            update_popular_count(ctp->ht, word);
        }else{
            hashtable_insert(ctp->ht, word);
        }

        free(word);
        pthread_mutex_unlock(&lock_thread);
    }
    //printf("returned\n");
    //free(ctp->q);
    return 0;
}

int main(int argc, char *argv[]){
    for(int q = 0; q < 4; q++){
        queues[q] = malloc(sizeof(queue));
        queue_init(queues[q]);
    }

    for(int h = 0; h < 4; h++){
        hashtables[h] = malloc(sizeof(hashtable));
        hashtable_init(hashtables[h]);
    }

    for(int c = 0; c < 4; c++){
        params[c] = malloc(sizeof(struct consumer_thread_params));
        params[c]->q = queues[c];
        params[c]->ht = hashtables[c];
    }

    pthread_t producer_threads[argc - 1];
    for(int i = 1; i < argc; i++){
        pthread_create(&producer_threads[i - 1], NULL, produce_words, argv[i]);
    }

    for(int p = 0; p < argc - 1; p++){
        pthread_join(producer_threads[p], NULL);
    }
    //printf("producers have ran\n");

    pthread_t consumer_threads[4];

    //printf("consumer\n");

    for(int j = 0; j < 4; j++){
        pthread_create(&consumer_threads[j], NULL, consume_words, params[j]);
    }

    //printf("consumer is created\n");

    for(int c = 0; c < 4; c++){
        pthread_join(consumer_threads[c], NULL);
    }

    //printf("most popular word count %d\n", most_popular_count);

    if(most_popular_count != 0){
        for(int ht = 0; ht < 4; ht++){
            hashtable *tmp = hashtables[ht];
            for(int i = 0; i < BUCKETS; i++){
                list_node *curr = tmp->buckets[i]->head_node;
                while(curr != NULL){
                    if(curr->count == most_popular_count){
                        printf("%s %d\n", curr->unique_word, most_popular_count);
                    }
                    curr = curr->next;
                }
            }
        }
    }

    for(int d = 0; d < 4; d++){
        dump_hashtable(hashtables[d]);
        free(queues[d]);
        free(params[d]);
    }
}