//
// Created by Azael Zamora on 09/08/20.
//
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>

// used to count the set bits of each char
int count_set_bits(char c){
    int bits_set = 0;
    for(int index = 7; index >= 0; index--){
        //bit wise and with int(c) & (logical shift right at the index bit)
        if( (int)c & (1 << (7 - index)) ){
            bits_set += 1;  // if bit is set, increment bits_set
        }
    }
    return bits_set;        // return the number of bits set for char c
}

int main(int argc, char *argv[]){
    // if there are less than 2 arguments, return exit code 1
    if(argc < 2){
        printf("USAGE: ./pbitcount filename");
        return 1;
    }

    int bit_count = 0;              // will be used when writing to count_pipe[1]
    int total_set_bit_count = 0;    // will contain the total set bits
    pid_t processor_id;             // stores the processor's ID

    /*
     * pipe takes an int array of size 2 as input
     * the array will contain two new file descriptors for the pipeline
     * count_pipe[0] is for reading, count_pipe[1] is for writing
    */
    int count_pipe[2];
    pipe(count_pipe);

    // start @1 since argv[0] is the naem of the c file
    for(int i = 1; i < argc; i++){
        processor_id = fork();  // makes a parent and child processor
        if(processor_id == 0){  // if its a child processor, since the id is 0, follow the steps of bitcount
            FILE *file_input = fopen(argv[i], "r");
            if(file_input == NULL){
                perror(argv[i]);
                bit_count = -1;
                write(count_pipe[1], &bit_count, sizeof(bit_count));
            }else{
                int index = fgetc(file_input);
                while(index != EOF){
                    char a = (char)index;
                    bit_count += count_set_bits(a);
                    index = fgetc(file_input);
                }
                write(count_pipe[1], &bit_count, sizeof(bit_count));    // writes to count_pipe[1]
            }
            exit(0);
        }
    }

    // for parent process
    for(int j = 1; j < argc; j++){
        read(count_pipe[0], &bit_count, sizeof(bit_count));
        if(bit_count == -1){
            return 2;
        }else{
            total_set_bit_count += bit_count;
        }
    }

    printf("%d\n", total_set_bit_count);
    return 0;
}
