package complexity.eafit;

import java.util.List;

/**
 * Represents the general contract offered by a service that operates on a group
 * of spatial data and performs search operations on that data.
 * 
 * @author Camilo Vieira
 * @author Juan Diego Restrepo
 * 
 */
public interface NDimensionalSearch {

	/**
	 * Initializes the data structure that backs up the implementing service.
	 * 
	 * @param elements
	 *            Group of points that will be loaded initially in the data
	 *            structure.
	 * @param numCoords
	 *            Number of dimensions of the data. Should match with the
	 *            dimensions held in <code>coords</code> property of every
	 *            element contained in <code>elements</code> parameter.
	 */
	void initialize(Element[] elements, int numCoords);

	/**
	 * @param objElement
	 * @param tolerance
	 * @return
	 */
	List<Integer> search(Element objElement, float tolerance);

}
