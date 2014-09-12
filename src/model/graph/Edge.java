package model.graph;


public class Edge {
	private Vertex source;
	private Vertex destination;
	
	private String id;
	private double weight;
	
	public Edge( String id, Vertex first, Vertex second, double weight ) {
		this.id = id;
		this.source = first;
		this.destination = second;
		this.weight = weight;
	}

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex first) {
		this.source = first;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setDestination(Vertex second) {
		this.destination = second;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return source.toString() + " - " + destination.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
