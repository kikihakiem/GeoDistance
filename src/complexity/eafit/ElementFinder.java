package complexity.eafit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;



public class ElementFinder implements NDimensionalSearch {
	private Element[] listElements;
	private Node root;
	
	
	@Override
	public void initialize(Element[] elements, int numCoords) {
		this.listElements = elements;
		buildRoot(numCoords);
		insertElements();
	}

	
	private void buildRoot(int numCoords)
	{
		float[] coords=new float[numCoords];
		
		for (int i = 0; i < numCoords; i++) {
			coords[i]= (float)Math.sqrt(Float.MAX_VALUE);
		}
		
		Element initialElement=new Element(-1, coords);
		this.root=new Node(initialElement);
	}

	
	protected Node insertNodes(Node rootNode, Element list[]) {
		rootNode.children=new Node[2];
		/*
		 * Base Cases: 
		 * One child, is assigned as the left child.
		 * Two children, are assigned as left and right children.
		 */
		if(list.length==1){
			rootNode.children[0]=new Node(list[0]);
			rootNode.children[0].parent=rootNode;
			return rootNode;
		}
		if(list.length==2){
			rootNode.children[0]=new Node(list[0]);
			rootNode.children[0].parent=rootNode;
			rootNode.children[1]=new Node(list[1]);
			rootNode.children[1].parent=rootNode;
			return rootNode;
		}
		
		//Select the most distant points in the space.
		Element minAndMax[]=getMostDistantElements(list);
		//Assign the most distant points 
		Node min= new Node(minAndMax[0]);
		min.parent=rootNode;
		Node max= new Node(minAndMax[1]);
		max.parent=rootNode;
		rootNode.children[0]=min;
		rootNode.children[1]=max;

		Vector<Element> minList=new Vector<Element>();
		Vector<Element> maxList=new Vector<Element>();
		for (int i = 0; i < list.length; i++){
			if(minAndMax[0].getId()!=list[i].getId()&&minAndMax[1].getId()!=list[i].getId()){
				float distanceMin= getDistance(minAndMax[0].getCoords(), list[i].getCoords());
				float distanceMax= getDistance(minAndMax[1].getCoords(), list[i].getCoords());
				if(distanceMax>distanceMin)
				{
					minList.add(list[i]);
				}else {
					maxList.add(list[i]);
				}
			}
		}
		if(minList.size()>0)
		min= insertNodes(min, minList.toArray(new Element[minList.size()]));
		if(maxList.size()>0)
		max= insertNodes(max, maxList.toArray(new Element[maxList.size()]));
		
		return rootNode;
	}
	public void insertElements()
	{
	    root = insertNodes(root, listElements);
	}
	

	public Element[] getListElements() {
		return listElements;
	}
	
	private float getDistance(float[] coordsA, float[] coordsB)
	{
		float distance=0f;
		for (int i = 0; i < coordsB.length; i++) {
			if(coordsA.length<3)
			{
				System.out.println("a");
			}
			distance+=(coordsA[i]-coordsB[i])*(coordsA[i]-coordsB[i]);
			
		}
		distance= (float)Math.sqrt( distance);
		return distance;
	}
	
	/*
	 * Finds the two most distant elements in the list.
	 * @return Array with two positions filled with the two most distant elements.
	 */
	private Element[] getMostDistantOld(Element[] list)
	{
		// The origin is the position to find the nearest and the farthest
		float initialPos[]= {0,0,0};
		Element[] minAndMax=new Element[2];
		//min and max distance variables to find the nearest and farthest.
		float maxDistance;
		float minDistance;
		
		//Position of the min and max elements in the listElements array.
		int posMax=0;
		int posMin=0;
		//Default: The first element in the array is the nearest and farthest.
		maxDistance = getDistance(initialPos, list[0].getCoords());
		minDistance = getDistance(initialPos, list[0].getCoords());
		System.out.println("i:"+0+" distance: "+ minDistance);
		for (int i = 1; i < list.length; i++) {
			float distance=getDistance(initialPos,list[i].getCoords());
			System.out.println("i:"+i+" distance: "+ distance);
			if(distance>maxDistance){
				maxDistance=distance;
				posMax=i;
			}
			if(distance<minDistance){
				minDistance=distance;
				posMin=i;
			}
		}
		minAndMax[0]=list[posMin];
		minAndMax[1]=list[posMax];
		return minAndMax;
	}
	
	/*
	 * Finds the two most distant elements in the list. O(n^2)
	 * @return Array with two positions filled with the two most distant elements.
	 */
	public Element[] getMostDistantSlow(Element[] list)
	{
		Element minAndMax[]=new Element[2];
		float maxDistance=getDistance(list[0].getCoords(), list[1].getCoords());
		
		for (int i = 0; i < list.length; i++) {
			for (int j = i; j < list.length; j++) {
				float distance=getDistance(list[i].getCoords(), list[j].getCoords());
				if(maxDistance<distance)
				{
					minAndMax[0]=list[i];
					minAndMax[1]=list[j];
					maxDistance=distance;
				}
			}
		}
		return minAndMax;
	}
	
	/*
	 * Finds the two most distant elements in the list.
	 * @return Array with two positions filled with the two most distant elements.
	 */
	public Element[] getMostDistantElements(Element[] list)
	{
		// The origin is the position to find the nearest and the farthest
		float initialPos[]=new float[list[0].getCoords().length];
		for (int i = 0; i < initialPos.length; i++) {
			initialPos[i]=0;
		}
		Element minAndMax[];
		float minDistance;
		
		//Position of the min elements in the listElements array.
		int posMin=0;
		//Default: The first element in the array is the nearest
		minDistance = getDistance(initialPos, list[0].getCoords());
//		System.out.println("i:"+0+" distance: "+ minDistance);
		for (int i = 1; i < list.length; i++) {
			float distance=getDistance(initialPos,list[i].getCoords());
//			System.out.println("i:"+i+" distance: "+ distance);
			if(distance<minDistance){
				minDistance=distance;
				posMin=i;
			}
		}
		float maxDistance=0;
		int maxPosition=0;
		//Find the farthest with the element found.
		for (int i = 0; i < list.length; i++) {
			float distance=getDistance(list[posMin].getCoords(), list[i].getCoords());
			if(maxDistance<distance){
				maxDistance=distance;
				maxPosition=i;
			}
		}
		minAndMax=getMostDistant(list[posMin], list[maxPosition],list);
		return minAndMax;
	}

	private Element[] getMostDistant(Element min, Element max, Element[] list)
	{
		float maxDistance=getDistance(min.getCoords(), max.getCoords());
		int maxPosition=-1;
		Element minAndMax[]=new Element[2];
		for (int i = 0; i < list.length; i++) {
			float distance= getDistance(max.getCoords(), list[i].getCoords());
			if(maxDistance<distance){
				maxDistance=distance;
				maxPosition=i;
			}
		}
		if(maxPosition!=-1)
		{
			minAndMax=getMostDistant(max, list[maxPosition],list);
		}else{
			minAndMax[0]=min;
			minAndMax[1]=max;
		}
		return minAndMax;
	}


	
	private class Node
	  {
	    Element objElement;
	    Node[] children;

	    Node parent;

	    private Node(Element element)
	    {
	      this.objElement = element;
	      children = new Node[2];
	    }
	    @Override
	    public String toString() {
	    	String msg=objElement.getId()+" - ";
	    	if(children[0]!=null)
	    		msg+=children[0].toString();
	    	if(children[1]!=null)
	    		msg+=children[1].toString();
	    	msg+="\n";
	    	return msg;
	    }
	    
	    public int hasChildren()
	    {
	    	if(children[0]!=null&&children[1]!=null)
	    	{
	    		return 2;
	    	}else if(children[0]!=null)
	    	{
	    		return 0;
	    	}else if(children[1]!=null)
	    	{
	    		return 1;
	    	}	    	
	    	return -1;
	    }
	  }
	
	@Override
	public List<Integer> search(Element objElement, float tolerance)
	{
		List<Element> list=searchNodes(this.root,objElement,tolerance);
		List<Integer> returnList= new ArrayList<Integer>();
		for (Element element : list) {
			returnList.add(element.getId());
		}
		return returnList;
	}
	
	private List<Element> searchNodes(Node rootNode, Element pElement, float tolerance)
	{
		List<Element> list=new ArrayList<Element>();
		float distanceLeft=0;
		float distanceRight=0;
		int hasChildren= rootNode.hasChildren();
		if(hasChildren==0){
			distanceLeft=getDistance(pElement.getCoords(), rootNode.children[0].objElement.getCoords());
			if(distanceLeft<=tolerance)
			{
				list.add(rootNode.children[0].objElement);
			}
			list.addAll(searchNodes(rootNode.children[0],pElement, tolerance));
			
		}
		if(hasChildren==1){
			distanceRight=getDistance(pElement.getCoords(), rootNode.children[1].objElement.getCoords());
			if(distanceRight<=tolerance)
			{
				list.add(rootNode.children[1].objElement);
			}
			list.addAll(searchNodes(rootNode.children[1],pElement, tolerance));
		}
		if(hasChildren==2)
		{
			distanceLeft=getDistance(pElement.getCoords(), rootNode.children[0].objElement.getCoords());
			distanceRight=getDistance(pElement.getCoords(), rootNode.children[1].objElement.getCoords());
			if(distanceRight<=tolerance&&distanceLeft<=tolerance)
			{
				list.add(rootNode.children[0].objElement);
				list.add(rootNode.children[1].objElement);
				list.addAll(searchNodes(rootNode.children[0],pElement, tolerance));
				list.addAll(searchNodes(rootNode.children[1],pElement, tolerance));
			}else if(distanceRight<=tolerance)
			{
				list.add(rootNode.children[1].objElement);
			}else if(distanceLeft<=tolerance)
			{
				list.add(rootNode.children[0].objElement);
			}
			if(distanceRight>tolerance||distanceLeft>tolerance)
			{
				if(distanceLeft-tolerance>distanceRight-tolerance)
				{
					list.addAll(searchNodes(rootNode.children[1],pElement, tolerance));
					
				}else if(distanceLeft-tolerance<distanceRight-tolerance)
				{
					list.addAll(searchNodes(rootNode.children[0],pElement, tolerance));
				}else
				{
					list.addAll(searchNodes(rootNode.children[0],pElement, tolerance));
					list.addAll(searchNodes(rootNode.children[1],pElement, tolerance));
				}
			}

		}
		return list;
	}


	
	
}



