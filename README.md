GeoDistance
===========


Implements a custom designed search algorithm proposed by <strong>Dr. Juan
Guillermo Lalinde</strong>. The purpose of this algorithm is to search close
points in a n-dimensional space. This algorithm was inspired on Floyd's
algorithm and was given the name Spatial Binary Search (SBS).

The data structure that backs the implementation is a binary tree. Basically
what the algorithm does is divide the points in two main clusters according
to their distance and store the data in a binary tree. Meaning that the two
immediate children (A and B) of the root node are the farthest points among
the set of points. Then the children of A are the farthest points that belong
to the cluster A, which means that are closer to point A rather than to point
B. And the process goes on.

Besides implementing SBS we also perform a comparison with a search algorithm 
on an RTree.

Additional to this we also developed a slow version of a farthest point finder algorithm.
The not so fancy method “getMostDistantSlow” performs an all-to-all search O(n2). 

Authors:

Camilo Vieira                                                                                                             
Juan Diego Restrepo

Additional Credits:
RTree source code from open project: http://newbrightidea.com/2011/01/24/rtree-implementation-in-java/
