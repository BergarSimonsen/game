package model.graph;

import view.component.Tile;

public class Vertex implements Comparable {
	private int id;
	private Tile tile;
	
	public Vertex( int id, Tile t ) {
		this.id = id;
		this.tile = t;
	}
	
	@Override
	public boolean equals(Object obj) {
		Vertex other = ( Vertex ) obj;
		
		if( obj == null || other == null ) return false;
		if( id == other.getId() ) return true;
//		if( tile.equals( other.getTile() ) ) return true;
		else return false;

	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	@Override
	public String toString() {
		return tile.toString();
	}

	@Override
	public int compareTo(Object arg0) {
		Vertex other = ( Vertex ) arg0;
		if( tile.getId() > other.getTile().getId() ) return 1;
		else if( other.getTile().getId() > this.getTile().getId() ) return -1;
		else return 0;
//		int first = ( tile.getX() / 36 ) * ( tile.getY() / 36 ) + ( tile.getX() + tile.getY() );
//		int second = ( other.getTile().getX() / 36 ) * ( other.getTile().getY() / 36 ) + ( other.getTile().getX() + other.getTile().getY() );
//		if( first > second ) return 1;
//		else if( second > first ) return -1;
//		else {
//			if( tile.getX() / 36 > other.getTile().getX() / 36 ) return 1;
//			else return -1;
//		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
