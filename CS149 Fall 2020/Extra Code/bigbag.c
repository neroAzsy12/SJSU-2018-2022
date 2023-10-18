//
// Created by azsy on 10/22/20.
//
// sizeof(struct bigbag_entry_s) = 8

//#define _XOPEN_SOURCE 500
//#define _POSIX_C_SOURCE 200809L // got it from stackoverflow
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#include "bigbag.h"         // header file created by Dr. Reed

struct bigbag_hdr_s *hdr;

void create_bigbag_file(int file_descriptor){
    ftruncate(file_descriptor, BIGBAG_SIZE);

    hdr->magic = htonl(BIGBAG_MAGIC);
    hdr->first_element = 0;
    hdr->first_free = MIN_ENTRY_SIZE;

    //printf("first free: %d\n", hdr->first_free);
    //printf("%ld\n", sizeof(*hdr));
    //printf("offset: %s\n", hdr_ptr + sizeof(*hdr));

    struct bigbag_entry_s *leftover = (struct bigbag_entry_s*) ((char*)hdr + MIN_ENTRY_SIZE);
    leftover->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
    leftover->entry_len = BIGBAG_SIZE - MIN_ENTRY_SIZE - sizeof(struct bigbag_entry_s);

    printf("create_bigbag_file function works\n");
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
    long bigbag_entry_offset = sizeof(*hdr);

    struct bigbag_entry_s *entry_search;
    while(bigbag_entry_offset < hdr->first_free){
        entry_search = (struct bigbag_entry_s*) ((char*)hdr + bigbag_entry_offset);
        if(strcmp(entry_search->str, target_entry) == 0){
            //printf("%s is in the bag\n", target_element);
            return bigbag_entry_offset;
        }
        bigbag_entry_offset += sizeof(*entry_search) + entry_search->entry_len;
    }
    //printf("%s is not in the bag\n", target_element);
    return 0;
}

long add_element(char entry_str[]){
    char *hdr_ptr = (char*) hdr;

    struct bigbag_entry_s *entry_to_add = (struct bigbag_entry_s*) (hdr_ptr + hdr->first_free);
    entry_to_add->entry_magic = BIGBAG_USED_ENTRY_MAGIC;
    entry_to_add->entry_len = strlen(entry_str) + 1;
    strcpy(entry_to_add->str, entry_str);

    if(!hdr->first_element){
        hdr->first_element = hdr->first_free;
        hdr->first_free = sizeof(*hdr) + entry_to_add->entry_len + sizeof(*entry_to_add);

        //printf("%d\n", hdr->first_element);
        //printf("%d\n", hdr->first_free);
        //printf("%s\n", entry_to_add->str);

        struct bigbag_entry_s *free_space = (struct bigbag_entry_s*) (hdr_ptr + hdr->first_free);
        free_space->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
        free_space->entry_len = BIGBAG_SIZE - hdr->first_free - sizeof(*free_space);

        return hdr->first_free;
    }
    long current_offset = hdr->first_free;
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

    hdr->first_free += entry_to_add->entry_len + sizeof(*entry_to_add);
    //printf("%d\n", hdr->first_element);
    //printf("%d\n", hdr->first_free);
    //printf("%s\n", entry_to_add->str);
    struct bigbag_entry_s *free_space = (struct bigbag_entry_s*) (hdr_ptr + hdr->first_free);
    free_space->entry_magic = BIGBAG_FREE_ENTRY_MAGIC;
    free_space->entry_len = BIGBAG_SIZE - hdr->first_free - sizeof(*free_space);

    return 0;
}

int main(int argc, char **argv){
    //printf("%ld\n", sizeof(struct bigbag_entry_s*));
    if((argc <= 1 || argc >= 4) || (argc == 3 && (argv[1][0] != '-' || argv[1][1] != 't'))){
        printf("USAGE: ./bigbagstring [-t] filename\n");
        return 1;
    }

    int map_format; // this will hold whether if mmap will have MAP_PRIVATE or MAP_SHARED

    if(argc == 3){
        map_format = MAP_PRIVATE;   // means that the adding or deleting string will not be saved
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

    if(hdr->magic == htonl(BIGBAG_MAGIC)){
        printf("big bag file\n");
    }else{
        printf("not a bag file\n");
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
            // can't use malloc
            //input_string = malloc(input_string_length);
            //for(int c = 2; c < input_string_length; c++){
            //    input_string[c - 2] = command[c];
            //}
            //input_string[input_string_length] = '\0';

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