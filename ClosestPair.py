import math
import sys

def findDistance(point1,point2):
    return math.sqrt((point1[0] - point2[0]) ** 2 + (point1[1] - point2[1]) ** 2)
def bruteForce(point):
    min_dis = findDistance(point[0], point[1])
    pair1 = point[0]
    pair2 = point[1]
    point_length = len(point)
    if point_length == 2:
        return pair1,pair2,min_dis
    for i in range(0, point_length,1):
        for j in range(i + 1, point_length,1):
            if i == 0 and j == 1:
                continue
            d = findDistance(point[i],point[j])
            if d < min_dis:
                min_dis = d
                pair1, pair2 = point[i], point[j]
    return pair1,pair2, min_dis

def closestPair(pairLeft,pairRight):
    pairLeft_len = len(pairLeft)
    if pairLeft_len <= 3:
        return bruteForce(pairLeft)
    mid = pairLeft_len // 2
    leftP1 = pairLeft[:mid]
    leftP2 = pairLeft[mid:]
    midpoint = pairLeft[mid][0]
    firstY = []
    secondY = []
    for i in pairRight:
        if i[0] <= midpoint:
            firstY.append(i)
        else:
            secondY.append(i)
    (pair1, q1, min_dist1) = closestPair(leftP1, firstY)
    (pair2, q2, min_dist2) = closestPair(leftP2, secondY)

    if min_dist1 <= min_dist2:
        dist = min_dist1
        minD = (pair1, q1)
    else:
        dist = min_dist2
        minD = (pair2,q2)
    (pair32,pair31,min_dist3) = SplitPair(pairLeft,pairRight,dist,minD)
    if dist <= min_dist3:
        return minD[0], minD[1], dist
    else:
        return pair32, pair31, min_dist3

def SplitPair(left, right, L, min_pair):
    ln_x = len(left)
    mid = left[ln_x//2][0]
    sortedY = [i for i in right if mid - L <= i[0] <= mid + L]
    smallestDis = L
    ln_y = len(sortedY)
    for i in range(ln_y -1):
        for j in range(i+1, min(i+7,ln_y)):
            p,q = sortedY[i], sortedY[j]
            splitDist = findDistance(p,q)
            if splitDist < smallestDis:
                min_pair = p,q
                smallestDis = splitDist
    return min_pair[0],min_pair[1], smallestDis

points = []
#read test file in from command line
with open(sys.argv[1],"r") as f:
    data = f.readlines()
    for line in data:
        points.append(tuple(map(int, line.split())))
    xSort = sorted(points, key=lambda x: x[0])
    ySort = sorted(points, key=lambda x: x[1])
    xy1, xy2, minimumD = closestPair(xSort, ySort)
    if(xy1[0] > xy2[0]):
        print("Pair:", xy2, xy1)
    else:
        print("Pair:", xy1, xy2)
    print("Minimum distance:",minimumD)

