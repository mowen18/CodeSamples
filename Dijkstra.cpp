#include <iostream>
#include <fstream>
#include<limits.h>
#include <string>
#define MAX_LEN 25

using namespace std;

void dijkstraAlgo(int**, int, int);

int getMinDistance(int[], int, bool[]);

int main()

{
    int V;
    string srcV;
    string destV;
    int weight;
    string vStringMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ifstream file;

    //****ENTER FULL PATH FOR TEST FILE HERE*****
    file.open("C:\\Users\\mjowe\\CLionProjects\\Dijkstra\\Case1.txt");

    if (!file.is_open())
    {
        std::cout << "Couldn't open test file, open main and provide full path";
    }
    else
    {
        file >> V;

        int **edgeWeightMat = new int*[V];

        for (int i = 0; i < V; i++)

            edgeWeightMat[i] = new int[V];
        
        for (int i = 0; i < V; i++)

            for (int j = 0; j < V; j++)

                edgeWeightMat[i][j] = 0;

        while (!file.eof())
        {
            file >> srcV;

            file >> destV;

            file >> weight;

            int row = vStringMap.find(srcV);

            int col = vStringMap.find(destV);

            edgeWeightMat[row][col] = weight;
        }
        file.close();
        dijkstraAlgo(edgeWeightMat, V, 0); //0--->A
    }
    return 0;
}

int getMinDistance(int distance[], int V, bool haveVisited[])

{
    int min_dis = INT_MAX;
    int min_index;

    for (int i = 0; i < V; i++)
    {
        if (distance[i] <= min_dis && haveVisited[i] == false )
        {
            min_index = i;
            min_dis = distance[i];
        }
    }
    return min_index;
}

void dijkstraAlgo(int **g, int V, int src)

{
    string *lastV = new string[MAX_LEN];

    for (int i = 0; i < V; i++)
    {
        lastV[i] = "nil";

    }
    string vStringMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    int distance[MAX_LEN];
    bool haveVisited[MAX_LEN];
    for (int i = 0; i < V; i++)

    {
        distance[i] = INT_MAX;
        haveVisited[i] = false;
    }

    distance[src] = 0;

    for (int num = 0; num < V - 1; num++)

    {
        int u = getMinDistance(distance, V, haveVisited);
        haveVisited[u] = true;

        for (int vertex = 0; vertex < V; vertex++)

        {
            if (!haveVisited[vertex] && g[u][vertex] && distance[u] != INT_MAX
                && distance[u] + g[u][vertex] < distance[vertex])

            {
                lastV[vertex] = vStringMap.at(u);
                distance[vertex] = distance[u] + g[u][vertex];

            }
        }
    }
    std::cout << distance[1] << endl;

    string curV = "B";
    string sp = "B";

    while (lastV[vStringMap.find(curV)] != "nil")
    {
        sp = lastV[vStringMap.find(curV)] + " " + sp;
        curV = lastV[vStringMap.find(curV)];
    }
    std::cout << sp;

}
