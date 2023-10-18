// Created by Azael on 9/27/20.
#include <stdio.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>

/*
 * phase 1: fork, execvp the command, wait for the child to finish
 * phase 2: you look at the exit code of wait, and loop if need to, repeat phase 1
 * phase 3: figure out how to connect output to parent processor,
 */

pid_t child_id; // global variable for the child ID when using fork
int wstatus;    // status for the child processor
int fd;         // file descriptor

// function for the alarm handler
void alarm_handler(){
    kill(child_id, SIGKILL);    // kills the child
}

int main(int argc, char *argv[]){
    if(argc == 1) {
        printf("USAGE: ./unflake max_tries max_timeout test_command args...\n");
        printf("max_tries - must be greater than or equal to 1\n");
        printf("max_timeout - number of seconds greater than or equal to 1\n");
        return 1;
    }

    int attempts = atoi(argv[1]);   // converts argv[1] into an int
    int time_out = atoi(argv[2]);   // converts argv[2] into an int
    if(attempts < 1 || time_out < 1){
        printf("USAGE: ./unflake max_tries max_timeout test_command args...\n");
        printf("max_tries - must be greater than or equal to 1\n");
        printf("max_timeout - number of seconds greater than or equal to 1\n");
        return 1;
    }

    int runs = 1;   // the number of runs that will happen

    while(runs <= attempts){
        signal(SIGALRM, alarm_handler); // set up the single function
        alarm(time_out);                // sets up the alarm

        char filename[16];  // filename is char array used to name test_output.runs
        sprintf(filename, "test_output.%d", runs);  // used to formate filename
        fd = open(filename ,O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR); // get the file descriptor of the output file

        child_id = fork();  // first fork to have a parent and child processor
        if(child_id == 0){
            dup2(fd, 1);
            dup2(fd, 2);
            execvp(argv[3], argv+3);
            dprintf(fd,"could not exec %s\n", argv[3]);   // adds this to the file should it be unable to exec argv[3]
            exit(errno);
        }
        wait(&wstatus); // waits for the child process to finish first

        if (WIFEXITED(wstatus)) {   // if child was exited with exit code
            int exit_status = WEXITSTATUS(wstatus);         // gets the exit code
            dprintf(fd,"exit code %d\n", exit_status);  // prints to fd

            if (exit_status == 0 || runs == attempts) {     // if exit code is 0, or runs has reached its limit
                printf("%d runs\n", runs);           // prints out the number of runs it took
                FILE *file_fd = fopen(filename, "r");// opens the filename as readable
                int c = fgetc(file_fd);
                while(c != EOF){
                    printf("%c", c);                 // prints out the contents of test_output.runs
                    c = fgetc(file_fd);
                }
                fclose(file_fd);
                return exit_status;
            }
        } else if (WIFSIGNALED(wstatus)) {  // if child was killed by signal
            int signal_status = WTERMSIG(wstatus);  // gets the signal that was used
            dprintf(fd, "killed with signal %d\n", signal_status);  // the signal to the fd

            if(runs == attempts){
                printf("%d runs\n", runs);
                FILE *file_fd = fopen(filename, "r");
                int c = fgetc(file_fd);
                while(c != EOF){
                    printf("%c", c);
                    c = fgetc(file_fd);
                }
                fclose(file_fd);
                return 255;
            }
        }
        runs++; // increment runs
    }
}
