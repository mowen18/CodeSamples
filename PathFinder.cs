using System.Collections.Generic;
using UnityEngine;
using System.Collections;


public class PathFinder : MonoBehaviour
{
    private static PathFinder instance;
    private static GameObject[] pathNodes;
    public PathNode startposition;
    public PathNode endPosition;
    List<PathNode> pNodes;
    private Survivor_AI survivor_AI;
    private Survivor_AI[] survivors_ai;
    private static BlackBoard_Player blackBoard_Player;
    private static BlackBoard_Louis blackBoard_louis;
    private static BlackBoard_Zoey blackBoard_zoey;
    private static BlackBoard_Francis blackBoard_francis;
    public BlackBoard_Player bb;
    private void Awake()
    {
        if (instance == null)
        {
            instance = this;
        }
        else
        {
            Destroy(this.gameObject);
        }
    }
  
    void Start()
    {
        pathNodes = GameObject.FindGameObjectsWithTag("PathNode");
      
        survivor_AI = FindObjectOfType<Survivor_AI>();
        pathNodes = GameObject.FindGameObjectsWithTag("PathNode");
        blackBoard_Player = survivor_AI.GetBlackboard();
        blackBoard_Player.pathNodestart = pathNodes;
        blackBoard_Player.startNode = startposition;
        blackBoard_Player.targetNode = endPosition;
        pNodes = FindPath(startposition, endPosition);
        //Debug.Log(pNodes);
        blackBoard_Player.pathNodes = pNodes;
       
    }

    // Update is called once per frame
    void Update()
    {
        List<Loot> loot = GameManager.GetLootList();
        if (pNodes.Count <= 0 && loot.Count > 0)
        {
           
            
            endPosition = FindClosestNodeToTarget(loot[0].transform.position);
            startposition = FindClosestNodeToTarget(survivor_AI.transform.position);
            blackBoard_Player.startNode = startposition;
            blackBoard_Player.targetNode = endPosition;
            pNodes = FindPath(startposition, endPosition);
            Debug.Log(pNodes);
            blackBoard_Player.pathNodes = pNodes;
        }
        

    }

    public static List<PathNode> FindPath(PathNode start, PathNode goal)
    {
        List<PathNode> OpenList = new List<PathNode>();
        HashSet<PathNode> ClosedList = new HashSet<PathNode>();
        OpenList.Add(start);
    
        while (OpenList.Count > 0)
        {
            PathNode currentNode = OpenList[0];
            for (int i = 1; i < OpenList.Count; i++)
            {
                if (OpenList[i].FCost < currentNode.FCost || OpenList[i].FCost == currentNode.FCost && OpenList[i].ihCost < currentNode.ihCost)
                {
                    currentNode = OpenList[i];
                }
            }
            OpenList.Remove(currentNode);
            ClosedList.Add(currentNode);
            if(currentNode == goal)
            {
                List<PathNode> FinalPath = new List<PathNode>();//List to hold the path sequentially 
                PathNode CurrentNode = goal;//Node to store the current node being checked

                while (CurrentNode != start)//While loop to work through each node going through the parents to the beginning of the path
                {
                    FinalPath.Add(CurrentNode);//Add that node to the final path
                    CurrentNode = CurrentNode.ParentNode;//Move onto its parent node
                    
                }
                
                FinalPath.Reverse();//Reverse the path to get the correct order
                return FinalPath; //Set the final path
            }
            foreach(PathNode neighbor in currentNode.neighbors)
            {
                if (ClosedList.Contains(neighbor))
                {
                    continue;
                }
                float moveCost = currentNode.igCost + Vector3.Distance(currentNode.transform.position,neighbor.transform.position);
                if(moveCost < neighbor.igCost || !OpenList.Contains(neighbor))
                {
                    neighbor.igCost = moveCost;
                    neighbor.ihCost = Vector3.Distance(neighbor.transform.position, goal.transform.position);//Set the h cost
                    neighbor.ParentNode = currentNode;
                    if (!OpenList.Contains(neighbor))
                    {
                        OpenList.Add(neighbor);
                    }

                }


            }
        }
        return null;
        
    }
    public static PathNode FindClosestNodeToTarget(Vector3 point)
    {
        GameObject closestNode = null;
        float closestDist = 100f;
        foreach (GameObject node in pathNodes)
        {
            float dist = Vector3.Distance(node.transform.position, point);
            if(dist < closestDist)
            {
                closestDist = dist;
                closestNode = node;
            }
        }
        PathNode pNode = closestNode.GetComponent<PathNode>();
        return pNode;
    }
}
