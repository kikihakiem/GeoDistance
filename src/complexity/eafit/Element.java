package complexity.eafit;

public class Element {
	private int id;
	private float[] coords;
	
	/*
	 * Creates a new Element.
	 */
	public Element(int id, float[] coords)
	{
		this.id=id;
		this.coords=coords;
	}
	
	/*
	 * If the coords are not sent, (0,0,...,0) is setted. 
	 */
	public Element(int id, int numDimensions)
	{
		this.id=id;
		this.coords = new float[numDimensions];
		for (int i = 0; i < coords.length; i++) {
			coords[i]=0;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float[] getCoords() {
		return coords;
	}

	public void setCoords(float[] coords) {
		this.coords = coords;
	}
	public String toString(){
		String msg="id: "+this.id+"\n";
		for (int i = 0; i < this.coords.length; i++) {
			msg+="Dim " + i + " Value: "+ coords[i];
		}
		return msg;
	}
}
