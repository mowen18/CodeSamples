/*-------------------------------------------------------------------------*
 *---									---*
 *---		assignment4.c						---*
 *---									---*
 *---	    This program splits a linked list into 3 roughly equal-	---*
 *---	lengthed linked lists.  The linked lists are printed, and then	---*
 *---	free()d.							---*
 *---									---*
 *---	----	----	----	----	----	----	----	----	---*
 *---									---*
 *---	Version 1a	2020 November 3	      Prof. Joseph Phillips	---*
 *---									---*
 *-------------------------------------------------------------------------*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

//  PURPOSE:
const int LINE_LEN = 256;

#define STOP_CMD "quit"

const int STOP_CMD_LEN = sizeof(STOP_CMD) - 1;

//  PURPOSE:  To hold a node in a linked list of integer pairs.
struct Node
{
    int integer_;
    struct Node *nextPtr_;
};

//  PURPOSE:  To create and return a linked list of integer pairs.
struct Node *makeList()
{
    struct Node *list = NULL;
    struct Node *end = NULL;
    int integer;
    char line[LINE_LEN];

    while (1)
    {
        printf("Enter integer (or %s) to quit: ", STOP_CMD);
        fgets(line, LINE_LEN, stdin);

        if (strncmp(line, STOP_CMD, STOP_CMD_LEN) == 0)
            break;

        integer = strtol(line, NULL, 10);

        struct Node *next = (struct Node *)malloc(sizeof(struct Node));

        if (list == NULL)
        {
            list = next;
        }
        else
        {
            end->nextPtr_ = next;
        }

        end = next;
        next->integer_ = integer;
        next->nextPtr_ = NULL;
    }

    return (list);
}

void append(struct Node **beginHandle,
            struct Node **endHandle,
            struct Node *nodePtr)
{
    struct Node *next = (struct Node *)malloc(sizeof(struct Node));
    
    if(*beginHandle == NULL)
    {
        *beginHandle = next;
        
    }
    else
    {
         //nodePtr->nextPtr_ = *endHandle;
        (*endHandle)->nextPtr_ = next;
    
    }
    *endHandle = next;
    next->integer_=nodePtr->integer_;
    next->nextPtr_=NULL;

    
}

void split(struct Node *source,
           struct Node **sublist0Handle,
           struct Node **sublist1Handle,
           struct Node **sublist2Handle)
{
    struct Node *end0 = NULL;
    struct Node *end1 = NULL;
    struct Node *end2 = NULL;
    struct Node *next;

    *sublist0Handle = NULL;
    *sublist1Handle = NULL;
    *sublist2Handle = NULL;

    while (source != NULL)
    {
        next = source->nextPtr_;
        append(sublist0Handle,&end0,source);
        if(next==NULL)
            break;
        source = next;
        next = source->nextPtr_;
        append(sublist1Handle,&end1,source);
        if(next==NULL)
            break;
        source = next;
        
        next = source->nextPtr_;
        append(sublist2Handle,&end2,source);
        if(next==NULL)
            break;
        source = next;
        
    }
}

//  PURPOSE:  To print the 'textCPtr_', and the 'integer_' values found in
//	'list'.  No return value.
void print(const char *textCPtr,
           const struct Node *list)
{
    const struct Node *run;

    printf("%s\n",textCPtr);
    for (run = list; run != NULL; run = run->nextPtr_)
    {
        printf("%d\n", run->integer_);
    }
}

//  PURPOSE:  To do nothing if 'list' is NULL.  Otherwise to 'free()' both
//	'nextPtr_' for 'list', and all of 'nextPtr_' successors.
//	No return value.
void release(struct Node *list)
{
    if(list==NULL)
        return;
    
    release(list->nextPtr_);
    free(list);

}

//  PURPOSE:  To create, print, and 'free()' a linked list of integers.
//	Returns 'EXIT_SUCCESS' to OS.
int main(int argc,
         char *argv[])
{
    struct Node *list;
    struct Node *list0;
    struct Node *list1;
    struct Node *list2;

    list = makeList();
    print("Original:", list);
    split(list, &list0, &list1, &list2);

    print("List0:", list0);
    print("List1:", list1);
    print("List2:", list2);

    release(list0);
    release(list1);
    release(list2);

    return (EXIT_SUCCESS);
}
