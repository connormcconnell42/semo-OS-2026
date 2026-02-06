#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <string.h>

#define SHARED_MEMORY_NAME "/collatz_shm"
#define MAX_SEQUENCE_LENGTH 4096

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <starting number>\n", argv[0]);
        return 1;
    }
    int start_num = atoi(argv[1]);
    if (start_num <= 0) {
        fprintf(stderr, "Starting number must be a positive integer.\n");
        return 1;
    }

    int shm_fd;
    void *ptr;
    pid_t pid;

    shm_fd = shm_open(SHARED_MEMORY_NAME, O_CREAT | O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("shm_open");
        return 1;
    }
    if (ftruncate(shm_fd, MAX_SEQUENCE_LENGTH) == -1) {
        perror("ftruncate");
        shm_unlink(SHARED_MEMORY_NAME); 
        return 1;
    }
    ptr = mmap(0, MAX_SEQUENCE_LENGTH, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    if (ptr == MAP_FAILED) {
        perror("mmap");
        shm_unlink(SHARED_MEMORY_NAME);
        return 1;
    }
    pid = fork();

    if (pid < 0) {
        perror("fork");
        munmap(ptr, MAX_SEQUENCE_LENGTH);
        shm_unlink(SHARED_MEMORY_NAME);
        return 1;
    } else if (pid == 0) {
        char buffer[MAX_SEQUENCE_LENGTH];
        int num = start_num;
        int offset = 0;
        offset += sprintf(buffer + offset, "%d", num);
        while (num != 1) {
            if (num % 2 == 0) {
                num = num / 2;
            } else {
                num = 3 * num + 1;
            }
            offset += sprintf(buffer + offset, ", %d", num);
        }
        strcpy((char*)ptr, buffer);
        exit(0);
    } else {
        wait(NULL);
        printf("Collatz Sequence (Starting at %d): %s\n", start_num, (char*)ptr);
        if (munmap(ptr, MAX_SEQUENCE_LENGTH) == -1) {
            perror("munmap");
        }
        if (shm_unlink(SHARED_MEMORY_NAME) == -1) {
            perror("shm_unlink");
            return 1;
        }
    }
    return 0;
}
