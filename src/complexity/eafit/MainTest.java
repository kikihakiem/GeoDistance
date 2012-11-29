package complexity.eafit;

import java.util.*;

public class MainTest {
	/*
	 * Generates the list of Elements with random coords and incremental id.
	 */
	public static Element[] populate( int numElements, int numCoords, int maxCoord)
	{
		Element list[]=new Element[numElements];
		float coords[];
		for (int i = 0; i < list.length; i++) {
			coords=new float[numCoords];
			for (int j = 0; j < numCoords; j++) {
				coords[j]=(float) (Math.round(Math.random()*maxCoord));
			}
			Element objElement=new Element(i, coords);
			list[i]=objElement;
		}
		return list;
	}
	
	
	
	public static void main(String args[]){

		int numCoords=3;
		Element list[]=MainTest.populate(10000, numCoords, 1000);
		ElementFinder objElementFinder= new ElementFinder();
		RTreeFinder objRTreeFinder= new RTreeFinder();
		objRTreeFinder.initialize(list, numCoords);
		objElementFinder.initialize(list, numCoords);
		float coords[]={500, 500,500};
		Element objElement= new Element(10,coords);

		
		long b = System.nanoTime();
		List<Integer> resultList=objElementFinder.search(objElement, 50);
		System.out.println("Tiempo: "+ (System.nanoTime()-b));
		
		for (Integer integer : resultList) {
			System.out.print(integer+", ");
		}
		System.out.println("****");
		b = System.nanoTime();
		List<Integer> resultList2=objRTreeFinder.search(objElement,50);
		System.out.println("TiempoRtree: "+ (System.nanoTime()-b));

		for (Integer integer : resultList2) {
			System.out.print(integer+", ");
		}
	}
}
