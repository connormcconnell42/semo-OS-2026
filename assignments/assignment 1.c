#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <sys/wait.h>

#define BUFFER_SIZE 100

int main() {
    int pipe_fd[2];
    pid_t pid;
    char input_str[BUFFER_SIZE];
    if (pipe(pipe_fd) == -1) {
        perror("Pipe failed");
        return 1;
    }
    pid = fork();
    if (pid < 0) {
        perror("Fork failed");
        return 1;
    }

    if (pid > 0) {
        close(pipe_fd[0]);
        printf("Parent: Enter a string: ");
        fgets(input_str, BUFFER_SIZE, stdin);
        input_str[strcspn(input_str, "\n")] = 0;
        write(pipe_fd[1], input_str, strlen(input_str) + 1);
        close(pipe_fd[1]);
        wait(NULL);
    } else {
        close(pipe_fd[1]);
        char received_str[BUFFER_SIZE];
        read(pipe_fd[0], received_str, BUFFER_SIZE);
        close(pipe_fd[0]);

        for (int i = 0; received_str[i] != '\0'; i++) {
            if (isupper(received_str[i])) {
                received_str[i] = tolower(received_str[i]);
            } else if (islower(received_str[i])) {
                received_str[i] = toupper(received_str[i]);
            }
        }
        printf("Child: Modified string: %s\n", received_str);
        exit(0);
    }

    return 0;
}
