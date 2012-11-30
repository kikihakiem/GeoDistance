package complexity.eafit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Implements the custom designed search algorithm proposed by <strong>Dr. Juan
 * Guillermo Lalinde</strong>. The purpose of this algorithm is to search close
 * points in a n-dimensional space. This algorithm was inspired on Floyd's
 * algorithm and was given the name Spatial Binary Search (SBS).
 * 
 * The data structure that backs the implementation is a binary tree. Basically
 * what the algorithm does is divide the points in two main clusters according
 * to their distance and store the data in a binary tree. Meaning that the two
 * immediate children (A and B) of the root node are the farthest points among
 * the set of points. Then the children of A are the farthest points that belong
 * to the cluster A, which means that are closer to point A rather than to point
 * B. And the process goes on.
 * 
 * @author Camilo Vieira
 * @author Juan Diego Restrepo
 * 
 */
public class ElementFinder implements NDimensionalSearch {
	private Element[] listElements;
	private Node root;

	@Override
	public void initialize(Element[] elements, int numCoords) {
		this.listElements = elements;
		buildRoot(numCoords);
		insertElements();
	}

	private void buildRoot(int numCoords) {
		float[] coords = new float[numCoords];

		for (int i = 0; i < numCoords; i++) {
			coords[i] = (float) Math.sqrt(Float.MAX_VALUE);
		}

		Element initialElement = new Element(-1, coords);
		this.root = new Node(initialElement);
	}

	/**
	 * Recursive method that builds the binary tree. First the most distant
	 * points are found. Then the clusters around those points are identified.
	 * 
	 * @param rootNode
	 *            Root node of the tree
	 * @param list
	 *            set of elements to be loaded in the binary tree
	 * @return
	 */
	protected Node insertNodes(Node rootNode, Element list[]) {
		rootNode.children = new Node[2];
		/*
		 * Base Cases: One child, is assigned as the left child. Two children,
		 * are assigned as left and right children.
		 */
		if (list.length == 1) {
			rootNode.children[0] = new Node(list[0]);
			rootNode.children[0].parent = rootNode;
			return rootNode;
		}
		if (list.length == 2) {
			rootNode.children[0] = new Node(list[0]);
			rootNode.children[0].parent = rootNode;
			rootNode.children[1] = new Node(list[1]);
			rootNode.children[1].parent = rootNode;
			return rootNode;
		}

		// Select the most distant points in the space.
		Element minAndMax[] = getMostDistantElements(list);
		// Assign the most distant points
		Node min = new Node(minAndMax[0]);
		min.parent = rootNode;
		Node max = new Node(minAndMax[1]);
		max.parent = rootNode;
		rootNode.children[0] = min;
		rootNode.children[1] = max;

		Vector<Element> minList = new Vector<Element>();
		Vector<Element> maxList = new Vector<Element>();
		for (int i = 0; i < list.length; i++) {
			// exclude the parent from the process
			if (minAndMax[0].getId() != list[i].getId()
					&& minAndMax[1].getId() != list[i].getId()) {
				float distanceMin = getDistance(minAndMax[0].getCoords(),
						list[i].getCoords());
				float distanceMax = getDistance(minAndMax[1].getCoords(),
						list[i].getCoords());
				if (distanceMax > distanceMin) {
					minList.add(list[i]);
				} else {
					maxList.add(list[i]);
				}
			}
		}
		if (minList.size() > 0)
			min = insertNodes(min, minList.toArray(new Element[minList.size()]));
		if (maxList.size() > 0)
			max = insertNodes(max, maxList.toArray(new Element[maxList.size()]));

		return rootNode;
	}

	private void insertElements() {
		root = insertNodes(root, listElements);
	}

	/**
	 * @return Set of elements that are loaded in the binary tree.
	 */
	public Element[] getListElements() {
		return listElements;
	}

	/**
	 * Calculates the distance between two n-dimensional points as the square
	 * root of the sum of the squared differences of each corresponding
	 * coordinate of the points.
	 * 
	 * @param coordsA
	 *            Point A
	 * @param coordsB
	 *            Point B
	 * @return distance
	 */
	public static float getDistance(float[] coordsA, float[] coordsB) {
		float distance = 0f;
		for (int i = 0; i < coordsB.length; i++) {
			if (coordsA.length < 3) {
				System.out.println("a");
			}
			distance += (coordsA[i] - coordsB[i]) * (coordsA[i] - coordsB[i]);

		}
		distance = (float) Math.sqrt(distance);
		return distance;
	}

	/**
	 * Finds the two most distant elements in the list. Compares each element
	 * with the rest of the elements being of complexity O(n^2)
	 * 
	 * @return Array with two positions filled with the two most distant
	 *         elements.
	 */
	public static Element[] getMostDistantSlow(Element[] list) {
		Element minAndMax[] = new Element[2];
		float maxDistance = getDistance(list[0].getCoords(),
				list[1].getCoords());

		for (int i = 0; i < list.length; i++) {
			for (int j = i; j < list.length; j++) {
				float distance = getDistance(list[i].getCoords(),
						list[j].getCoords());
				if (maxDistance < distance) {
					minAndMax[0] = list[i];
					minAndMax[1] = list[j];
					maxDistance = distance;
				}
			}
		}
		return minAndMax;
	}

	/**
	 * Finds the two most distant elements in the list. The algorithm is an
	 * optimization and consists of finding the closest point (A) to the origin,
	 * then finding the farthest point (B) to it. And then, find if there exists
	 * a point that is farther to B than A, and in such a case keep looking for
	 * distant points until the farthest are found.
	 * 
	 * @return Array with two positions filled with the two most distant
	 *         elements.
	 */
	public static Element[] getMostDistantElements(Element[] list) {
		// The origin is the position to find the nearest and the farthest
		float initialPos[] = new float[list[0].getCoords().length];
		for (int i = 0; i < initialPos.length; i++) {
			initialPos[i] = 0;
		}
		Element minAndMax[];
		float minDistance;

		// Position of the min elements in the listElements array.
		int posMin = 0;
		// Default: The first element in the array is the nearest
		minDistance = getDistance(initialPos, list[0].getCoords());

		// start loop from position 1 since we already have got element 0
		for (int i = 1; i < list.length; i++) {
			float distance = getDistance(initialPos, list[i].getCoords());
			if (distance < minDistance) {
				minDistance = distance;
				posMin = i;
			}
		}
		float maxDistance = 0;
		int maxPosition = 0;
		// Find the farthest with the element found.
		for (int i = 0; i < list.length; i++) {
			float distance = getDistance(list[posMin].getCoords(),
					list[i].getCoords());
			if (maxDistance < distance) {
				maxDistance = distance;
				maxPosition = i;
			}
		}
		minAndMax = getMostDistant(list[posMin], list[maxPosition], list);
		return minAndMax;
	}

	private static Element[] getMostDistant(Element min, Element max, Element[] list) {
		float maxDistance = getDistance(min.getCoords(), max.getCoords());
		int maxPosition = -1;
		Element minAndMax[] = new Element[2];
		for (int i = 0; i < list.length; i++) {
			float distance = getDistance(max.getCoords(), list[i].getCoords());
			if (maxDistance < distance) {
				maxDistance = distance;
				maxPosition = i;
			}
		}
		if (maxPosition != -1) {
			minAndMax = getMostDistant(max, list[maxPosition], list);
		} else {
			minAndMax[0] = min;
			minAndMax[1] = max;
		}
		return minAndMax;
	}

	/**
	 * @author Camilo Vieira
	 * @author Juan Diego Restrepo
	 * 
	 */
	private class Node {
		Element objElement;
		Node[] children;

		Node parent;

		private Node(Element element) {
			this.objElement = element;
			children = new Node[2];
		}

		@Override
		public String toString() {
			String msg = objElement.getId() + " - ";
			if (children[0] != null)
				msg += children[0].toString();
			if (children[1] != null)
				msg += children[1].toString();
			msg += "\n";
			return msg;
		}

		public int hasChildren() {
			if (children[0] != null && children[1] != null) {
				return 2;
			} else if (children[0] != null) {
				return 0;
			} else if (children[1] != null) {
				return 1;
			}
			return -1;
		}
	}

	@Override
	public List<Integer> search(Element objElement, float tolerance) {
		List<Element> list = searchNodes(this.root, objElement, tolerance);
		List<Integer> returnList = new ArrayList<Integer>();
		for (Element element : list) {
			returnList.add(element.getId());
		}
		return returnList;
	}

	/**
	 * Recursive method that performs a search of a point in a binary tree
	 * containing spatial data. The algorithm creates a circular boundary that
	 * works as a tolerance for finding near points.
	 * 
	 * @param rootNode
	 * @param pElement
	 * @param tolerance
	 * @return
	 */
	private List<Element> searchNodes(Node rootNode, Element pElement,
			float tolerance) {
		List<Element> list = new ArrayList<Element>();
		float distanceLeft = 0;
		float distanceRight = 0;
		int hasChildren = rootNode.hasChildren();

		// hasChildren property is a specific representation that allows us to
		// know whether left, right, none or all children are set.
		// 0 means left, 1 means right, 2 means both, -1 means none.
		// left
		if (hasChildren == 0) {
			distanceLeft = getDistance(pElement.getCoords(),
					rootNode.children[0].objElement.getCoords());
			if (distanceLeft <= tolerance) {
				list.add(rootNode.children[0].objElement);
			}
			list.addAll(searchNodes(rootNode.children[0], pElement, tolerance));

		}
		// right
		if (hasChildren == 1) {
			distanceRight = getDistance(pElement.getCoords(),
					rootNode.children[1].objElement.getCoords());
			if (distanceRight <= tolerance) {
				list.add(rootNode.children[1].objElement);
			}
			list.addAll(searchNodes(rootNode.children[1], pElement, tolerance));
		}
		// both
		if (hasChildren == 2) {
			distanceLeft = getDistance(pElement.getCoords(),
					rootNode.children[0].objElement.getCoords());
			distanceRight = getDistance(pElement.getCoords(),
					rootNode.children[1].objElement.getCoords());
			if (distanceRight <= tolerance && distanceLeft <= tolerance) {
				list.add(rootNode.children[0].objElement);
				list.add(rootNode.children[1].objElement);
				list.addAll(searchNodes(rootNode.children[0], pElement,
						tolerance));
				list.addAll(searchNodes(rootNode.children[1], pElement,
						tolerance));
			} else if (distanceRight <= tolerance) {
				list.add(rootNode.children[1].objElement);
			} else if (distanceLeft <= tolerance) {
				list.add(rootNode.children[0].objElement);
			}
			if (distanceRight > tolerance || distanceLeft > tolerance) {
				if (distanceLeft - tolerance > distanceRight - tolerance) {
					list.addAll(searchNodes(rootNode.children[1], pElement,
							tolerance));

				} else if (distanceLeft - tolerance < distanceRight - tolerance) {
					list.addAll(searchNodes(rootNode.children[0], pElement,
							tolerance));
				} else {
					list.addAll(searchNodes(rootNode.children[0], pElement,
							tolerance));
					list.addAll(searchNodes(rootNode.children[1], pElement,
							tolerance));
				}
			}

		}
		return list;
	}

}
