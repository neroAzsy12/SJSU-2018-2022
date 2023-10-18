//
// Created by azsy on 10/17/20.
//

// list, contains, add without deleting works, delete with coalescing works
// but after adding and deleting a bunch of times, it stops running correctly
// couldn't figure out what it was, but at least for a good chunk of the park, the best fit works

#define _XOPEN_SOURCE 500
#define _POSIX_C_SOURCE 200809L // got it from stackoverflow
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#include "bigbag.h"         // header file created by Dr. Reed

struct bigbag_hdr_s *hdr;   // global variable, convenient having the pointer be global

// creates a bigbag file
void create_bigbag_file(int file_descriptor){
    ftruncate(file_descriptor, BIGBAG_SIZE);    // makes the file size be 64K

    hdr->magic = htonl(BIGBAG_MAGIC);                 // htonl() stores BIGBAG_MAGIC in big-endian form as opposed to little-endian
    hdr->first_element = 0;
    hdr->first_free = MIN_ENTRY_SIZE;

    struct bigbag_entry_s *leftover = (struct bigbag_entry_s*) ((char*)hdr + MIN_ENTRY_SIZE);
    leftover->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
    leftover->entry_len = BIGBAG_SIZE - MIN_ENTRY_SIZE - sizeof(struct bigbag_entry_s);

    //printf("create_bigbag_file function works\n");
}

// list_entries() will display all the elements that in the big bag of strings
// it will list entries in order
// the setup of getting bigbag_entry_s is from *entry_addr from bigbag_dump
void list_entries(){
    long bigbag_entry_offset = hdr->first_element; // bigbag_offset will contain where the first_element on the big bag occurs
    char *hdr_ptr = (char*) hdr;                   // hdr_ptr stores the address of hdr, which is needed to iterate through the linked-list

    if(!bigbag_entry_offset){                      // if the offset is 0, then bigbag is empty since no element has been yet
        printf("empty bag\n");
        return;                                    // should return once empty bag is printed
    }
    //printf("%ld\n", big_bag_offset);

    struct bigbag_entry_s *current_entry;
    while(bigbag_entry_offset){
        current_entry = (struct bigbag_entry_s*) (hdr_ptr + bigbag_entry_offset);
        printf("%s\n", current_entry->str);
        bigbag_entry_offset = current_entry->next;
    }
}

// contains function to check if big bag has the target element
long contains_entry(char target_entry[]){
    long bigbag_entry_offset = hdr->first_element;

    //struct bigbag_entry_s *entry_search;
    while(bigbag_entry_offset){
        struct bigbag_entry_s *entry_search = (struct bigbag_entry_s*) ((char*)hdr + bigbag_entry_offset);

        if(strcmp(entry_search->str, target_entry) == 0){
            //printf("%s is in the bag\n", target_element);
            return bigbag_entry_offset;
        }
        bigbag_entry_offset = entry_search->next;
    }
    //printf("%s is not in the bag\n", target_element);
    return 0;
}

// add entry_to_add in the bag in a sorted order by alphabetically
void add_in_sorted_bag(struct bigbag_entry_s *entry_to_add, long best_fit_index, char entry_str[]){
    char *hdr_ptr = (char*)hdr;

    entry_to_add->entry_magic = BIGBAG_USED_ENTRY_MAGIC;
    strcpy(entry_to_add->str, entry_str);
    entry_to_add->next = 0;

    long current_index = hdr->first_element;
    struct bigbag_entry_s *current_entry;

    while(current_index){
        current_entry = (struct bigbag_entry_s*) (hdr_ptr + current_index);
        if(current_index == hdr->first_element && strcmp(current_entry->str, entry_to_add->str) >= 0){
            entry_to_add->next = current_index;
            hdr->first_element = best_fit_index;
            return;
        }else if(current_entry->next){
            struct bigbag_entry_s *next_entry = (struct bigbag_entry_s*) (hdr_ptr + current_entry->next);
            if(strcmp(next_entry->str, entry_to_add->str) > 0){
                entry_to_add->next = current_entry->next;
                current_entry->next = best_fit_index;
                return;
            }
        }else{
            current_entry->next = best_fit_index;
            return;
        }
        current_index = current_entry->next;
    }
}

/// w/ best fit algorithm
long add_element(char entry_str[]){
    char *hdr_ptr = (char*) hdr;      // hdr_ptr stores the address of hdr, which is needed to iterate through the linked-list

    /// if the bag is empty, just add the entry since there are no existing strings
    if(!hdr->first_element){
        // first, set up the entry "node" that will be added
        struct bigbag_entry_s *only_entry_add = (struct bigbag_entry_s*) (hdr_ptr + hdr->first_free);
        only_entry_add->entry_magic = BIGBAG_USED_ENTRY_MAGIC;  // set its magic entry to 'da'
        only_entry_add->entry_len = strlen(entry_str) + 1;      // set the entry length to len of entry_str + 1
        strcpy(only_entry_add->str, entry_str);                 // copy the entry_str to only_entry_add->str

        // second, fix hdr so it correctly points to the first element of the bag
        hdr->first_element = hdr->first_free;                                                   // points to the first element of the bag
        hdr->first_free = sizeof(*hdr) + only_entry_add->entry_len + sizeof(*only_entry_add);   // sets up the offset of the free space

        //printf("%d\n", hdr->first_element);
        //printf("%d\n", hdr->first_free);
        //printf("%s\n", entry_to_add->str);

        // third, setup the leftover free space
        struct bigbag_entry_s *free_space = (struct bigbag_entry_s*) (hdr_ptr + hdr->first_free);
        free_space->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;                          // entry_magic is set to f4
        free_space->entry_len = BIGBAG_SIZE - hdr->first_free - sizeof(*free_space);// entry len is whatever is left of the bag
        free_space->next = 0;                                                       // sets the next free space to 0
        //printf("%d\n", free_space->next);

        return hdr->first_free;                                                     // returns where the offset of the free space is at
    }

    /// looping through the free list, find the entry that serves as the best fit, in terms of optimal allocation
    long best_fit_index = 0;                    // should contain where the best fit allocation should take place
    long best_fit_space = 0;                    // should contain how much space is used in the best_fit_index offset
    long free_entry_offset = hdr->first_free;   // the first offset of the free list

    while(free_entry_offset) {
        struct bigbag_entry_s *current_free_entry = (struct bigbag_entry_s*) (hdr_ptr + free_entry_offset);
        if(current_free_entry->entry_len >= strlen(entry_str) + 1){
            if(best_fit_index == 0){
                best_fit_index = free_entry_offset;
                best_fit_space = current_free_entry -> entry_len;
            }else if(best_fit_space > current_free_entry->entry_len){
                best_fit_index = free_entry_offset;
                best_fit_space = current_free_entry->entry_len;
            }
        }
        free_entry_offset = current_free_entry->next;
    }
    //printf("best fit offset: %ld\n", best_fit_index);
    //printf("best fit space length: %ld\n", best_fit_space);

    // should work, when there is no more space or when there is not a free entry in the free list that can be used
    if(!best_fit_index){
        return 0;
    }

    /// gonna try to add to the list
    /// first, go through the free list, and reconnect connections since space in the free space will be allocated
    struct bigbag_entry_s *entry_to_add;
    long current_free_index = hdr->first_free;
    struct bigbag_entry_s *current_free_entry;

    while(current_free_index){
        current_free_entry = (struct bigbag_entry_s*) (hdr_ptr + current_free_index);

        if(current_free_index == hdr->first_free && current_free_index == best_fit_index){
            long available_space = best_fit_space - (strlen(entry_str) + 1);
            if(available_space <= sizeof(struct bigbag_entry_s*) + 1){
                hdr->first_free = current_free_entry->next;

                entry_to_add = (struct bigbag_entry_s*) (hdr_ptr + current_free_index);
                entry_to_add->entry_len = best_fit_space;
            }else{
                long free_offset = current_free_index + sizeof(struct bigbag_entry_s*) + strlen(entry_str) + 1;
                struct bigbag_entry_s *free_entry = (struct bigbag_entry_s*) (hdr_ptr + free_offset);
                free_entry->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
                free_entry->entry_len = available_space - sizeof(*free_entry);
                hdr->first_free = free_offset;
                free_entry->next = current_free_entry->next;

                entry_to_add = (struct bigbag_entry_s*) (hdr_ptr + current_free_index);
                entry_to_add->entry_len = strlen(entry_str) + 1;
            }
            add_in_sorted_bag(entry_to_add, best_fit_index, entry_str);
            return best_fit_index;

        }else {
            if(current_free_entry->next == best_fit_index) {
                long available_space = best_fit_space - (strlen(entry_str) + 1);

                struct bigbag_entry_s *next_free_entry = (struct bigbag_entry_s*) (hdr_ptr + best_fit_index);

                if(available_space <= sizeof(struct bigbag_entry_s*) + 1){
                    current_free_entry->next = next_free_entry->next;

                    entry_to_add = (struct bigbag_entry_s*) (hdr_ptr + current_free_index);
                    entry_to_add->entry_len = best_fit_space;
                }else {
                    long free_offset = best_fit_index + sizeof(struct bigbag_entry_s *) + strlen(entry_str) + 1;

                    struct bigbag_entry_s *free_entry = (struct bigbag_entry_s *) (hdr_ptr + free_offset);
                    free_entry->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
                    free_entry->entry_len = available_space - sizeof(*free_entry);
                    current_free_entry->next = free_offset;
                    free_entry->next = next_free_entry->next;

                    entry_to_add = (struct bigbag_entry_s *) (hdr_ptr + current_free_index);
                    entry_to_add->entry_len = strlen(entry_str) + 1;
                }
                add_in_sorted_bag(entry_to_add, best_fit_index, entry_str);
                return best_fit_index;
            }
        }
        current_free_index = current_free_entry->next;
    }
    /*
    entry_to_add->entry_magic = BIGBAG_USED_ENTRY_MAGIC;
    strcpy(entry_to_add->str, entry_str);


    long current_offset = best_fit_index;
    long current_index = hdr->first_element;
    struct bigbag_entry_s *current_entry;

    do {
        current_entry = (struct bigbag_entry_s*) (hdr_ptr + current_index);
        if(current_index == hdr->first_element && strcmp(current_entry->str, entry_to_add->str) >= 0){
            entry_to_add->next = current_index;
            hdr->first_element = current_offset;
            break;
        }else if(current_entry->next){
            struct bigbag_entry_s *next_entry = (struct bigbag_entry_s*) (hdr_ptr + current_entry->next);
            if(strcmp(next_entry->str, entry_to_add->str) > 0){
                entry_to_add->next = current_entry->next;
                current_entry->next = current_offset;
                break;
            }
        }else{
            current_entry->next = current_offset;
            break;
        }
        current_index = current_entry->next;
    }while(current_index);
    */
    return best_fit_index;
}

// this is a helper function for delete, this should be able to aid in coalescing continuous available memory
// should 'coalesce' the entry_len and sizeof(entry2) to entry1
void coalesce_bigbag_entries(struct bigbag_entry_s *entry1, struct bigbag_entry_s *entry2){
    entry1->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
    entry1->entry_len += entry2->entry_len + sizeof(*entry2);
    entry1->next = entry2->next;
}

// delete function for deleting strings
long delete(char target[]){
    long bigbag_entry_offset = contains_entry(target);  // first check if the target string is in the bag

    if(!bigbag_entry_offset){                           // checks, if bigbag_entry_offset is 0, then the target string does not exist in the bag
        return 0;                                       // returns 0, if the target string cannot be deleted
    }

    char *hdr_ptr = (char*)hdr;                         // hdr_ptr stores the address of hdr, which is needed to iterate through the linked-list
    struct bigbag_entry_s *entry_removed = (struct bigbag_entry_s*) (hdr_ptr + bigbag_entry_offset);    // "node" that needs to be removed from the 'linked list'
    entry_removed->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;

    // first iterate through the big bag in sorted order to remove the bigbag_entry and reconnect entries as needed
    // if its just the head, just connect hdr->first_element to entry_removed->next
    // if its any other entry, reconnect entries as needed
    if(bigbag_entry_offset == hdr->first_element){  // if the entry offset happens to be the first sorted entry in the bag
        hdr->first_element = entry_removed->next;   // make the first element of hdr point to the next offset 'node' from entry_removed
    }else{
        struct bigbag_entry_s *before_removed_entry = (struct bigbag_entry_s*) (hdr_ptr + hdr->first_element);  // this 'node' is the previous node before the removed_node

        // next, this while will get the previous sorted entry before the removed entry
        while(before_removed_entry->next != bigbag_entry_offset){                                   // while the next in before_removed_entry is not the offset of the target entry
            before_removed_entry = (struct bigbag_entry_s*) (hdr_ptr + before_removed_entry->next); // move to the next entry 'node' and repeat the while loop
        }

        before_removed_entry->next = entry_removed->next;   // connect before_removed_entry->next to entry_removed->next
    }

    /// Next, add the removed entry to the free list, the free list is sorted by offsets
    long current_index = hdr->first_free;
    struct bigbag_entry_s *current_free_entry;

    do{
        current_free_entry = (struct bigbag_entry_s*)(hdr_ptr + current_index);
        if(current_index == hdr->first_free && bigbag_entry_offset < current_index){
            entry_removed->next = current_index;
            hdr->first_free = bigbag_entry_offset;
            break;
        }else if(current_free_entry->next){
            if(bigbag_entry_offset < current_free_entry->next){
                entry_removed->next = current_free_entry->next;
                current_free_entry->next = bigbag_entry_offset;
                break;
            }
        }else{
            current_free_entry->next = bigbag_entry_offset;
            break;
        }

        current_index = current_free_entry->next;
    }while(current_index);

    /// once the entries have been reconnected, and the entry has been added into the free list
    /// go through the free list, and coalesce if the offset of the next free entry matches (current_free + current_free->len + 8)
    long free_entry_offset = hdr->first_free;
    while(free_entry_offset){
        current_free_entry = (struct bigbag_entry_s*) (hdr_ptr + free_entry_offset);

        if(free_entry_offset + current_free_entry->entry_len + sizeof(*current_free_entry) == current_free_entry->next){
            struct bigbag_entry_s *next_entry = (struct bigbag_entry_s*) (hdr_ptr + current_free_entry->next);
            coalesce_bigbag_entries(current_free_entry, next_entry);
        }else{
            free_entry_offset = current_free_entry->next;
        }
    }
    return bigbag_entry_offset;
}

int main(int argc, char **argv){
    // printf("%ld\n", sizeof(struct bigbag_entry_s*));  // size of bigbag_entry_s is 8
    // if there is only 1 argument or more than 3 arguments, return 1
    if(argc <= 1 || argc >= 4){
        printf("USAGE: ./bigbagstring [-t] filename\n");
        return 1;
    }

    // if there are three arguments, but argv[1] != "-t", return 1
    if(argc == 3 && strcmp(argv[1], "-t") != 0){
        printf("USAGE: ./bigbagstring [-t] filename\n");
        return 1;
    }

    int map_format;                 // this will hold whether if mmap will have MAP_PRIVATE or MAP_SHARED

    if(argc == 3){
        map_format = MAP_PRIVATE;   // means that the adding or deleting string will not be saved, -t is an argument
    }else{
        map_format = MAP_SHARED;    // means that adding or deleting strings will be saved
    }

    int fd = open(argv[argc-1], O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
    void *file_base = mmap(0, BIGBAG_SIZE, PROT_READ | PROT_WRITE, map_format, fd, 0);

    hdr = file_base;

    struct stat check_file;
    fstat(fd, &check_file);

    if(check_file.st_size == 0){
        create_bigbag_file(fd);
    }
    //printf("hdr magic: %d\n", hdr->magic);

    //if(hdr->magic == htonl(BIGBAG_MAGIC)){
    //    printf("big bag file\n");
    //}else{
    //    printf("not a bag file\n");
    //}

    if(hdr->magic != htonl(BIGBAG_MAGIC)){
        printf("Not a big ba file\n");
        return 1;
    }

    //printf("11 first free: %d\n", hdr->first_free);
    // since you might run multiple commands, infinite loop is utilized
    while(1){
        char *command = NULL;
        size_t command_len = 0;

        if(getline(&command, &command_len, stdin) == -1){
            exit(3);
        }

        size_t input_string_length = 0;
        for(int i = 0; command[i] != '\n'; i++){
            input_string_length++;
        }

        command[input_string_length] = '\0';
        //printf("%s\n", command);
        if(command[0] == 'a' || command[0] == 'c' || command[0] == 'd'){
            if(input_string_length < 2 || command[1] != ' '){
                printf("%c not used correctly\n", command[0]);
                printf("possible commands:\n");
                printf("a string_to_add\n");
                printf("d string_to_delete\n");
                printf("c string_to_check\n");
                printf("l\n");
                continue;
            }
        }else if(command[0] != 'l'){
            printf("%c not used correctly\n", command[0]);
            printf("possible commands:\n");
            printf("a string_to_add\n");
            printf("d string_to_delete\n");
            printf("c string_to_check\n");
            printf("l\n");
            continue;
        }

        switch(command[0]){
            case 'a':
                if(add_element(command + 2)) {
                    printf("added %s\n", command + 2);
                }else{
                    printf("out of space\n");
                }
                break;
            case 'd':
                if(delete(command + 2)){
                    printf("deleted %s\n", command + 2);
                }else{
                    printf("no %s\n", command + 2);
                }
                break;
            case 'c':
                if(contains_entry(command + 2)){
                    printf("found\n");
                }else{
                    printf("not found\n");
                }
                break;
            case 'l':
                list_entries();
                break;
        }
        free(command);
    }
}