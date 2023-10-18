//
// Created by Azael Zamora on 08/26/20.
//
#include <stdio.h>

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
    // if there are more than or less than 2 arguments, return exit code 1
    if(argc != 2){
        printf("USAGE: ./bitcount filename\n");
        return 1;
    }

    // opens the file that is provided by argv[1] in readable mode
    FILE *file_input = fopen(argv[1], "r");

    // if file is not accessible or does not exist, return exit code 2
    if(file_input == NULL) {
        perror(argv[1]);
        return 2;
    }else {
        int i = fgetc(file_input);                  // gets the first char from file_input
        int total_set_bits = 0;                     // the total number of set bits in file_input
        while (i != EOF) {                          // while it has not reached end of file
            char a = (char) i;                      // cast i into its char value
            total_set_bits += count_set_bits(a);    // use count_set_bits func to increment total number of set bits
            i = fgetc(file_input);                  // get the next char from the file
        }

        // prints the total number of set bits in the file
        printf("%d\n", total_set_bits);
        return 0;   // exit code is 0, meaning it was successful
    }
}
