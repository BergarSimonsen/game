package model.graph;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.enumeration.TileState;

import view.component.Tile;


public class Graph {
//	private Map<Integer, Map<Integer, Vertex>> vertices;
	private Vertex[][] vertices;
	private Map<Point, ArrayList<Edge>> edges;
	
	private int tileSize;
	private int tileAmount;
	private int e; // ??
	
	// Used for testing, change later
	private double defaultWeight = 1.0;
	
	public Graph( int tileAmount, int tileSize ) {
//		this.vertices = new HashMap<Integer, Map<Integer, Vertex>>();
		this.vertices = new Vertex[tileAmount][tileAmount];
		this.edges = new HashMap<Point, ArrayList<Edge>>();
		this.tileAmount = tileAmount;
		this.tileSize = tileSize;
	}
	
	public void addEdge( Vertex a, Vertex b ) {
		Point p = new Point( a.getTile().getX(), a.getTile().getY() );
		ArrayList<Edge> tmp = null;
		if( !edges.containsKey(p) || edges.get(p) == null ) {
			tmp = new ArrayList<Edge>();
		} else {
			tmp = edges.get( p );
		}
		tmp.add( new Edge( String.valueOf(a.getId()) + "_" + String.valueOf(b.getId()), a, b, defaultWeight ) );
		
		edges.put( p, tmp );
	}
	
	public void addVertice( Vertex v ) {
		int i = v.getTile().getX() / tileSize;
		int j = v.getTile().getY() / tileSize;
		vertices[i][j] = v;
		
		
//		Map<Integer, Vertex> m = new HashMap<Integer, Vertex>();
//		if( vertices.containsKey(v.getTile().getX()) ) { 
//			m = vertices.get( v.getTile().getX() );
//		}
//		m.put(v.getTile().getY(), v);
//		vertices.put( v.getTile().getX(), m );
//		if( !vertices.containsKey(v.getTile().getX()) ) {
//			Map m = new HashMap<Integer, Vertex>();
//			m.put(v.getTile().getY(), v);
//			vertices.put(v.getTile().getX(), m);
//		} else {
//			if( !vertices.get( v.getTile().getX() ).containsKey(v.getTile().getY()) ) {
//				Map m = new HashMap<Integer, Vertex>();
//				m.put(v.getTile().getY(), v);
//				vertices.get( v.getTile().getX()).put(v.getTile().getY(), v);
//			}
//		}
//		
//		vertices.add( v );
	}
	
	public int V() {
		return tileAmount;
//		return vertices.size() * vertices.size();
//		int size = 0;
//		size += vertices.size();
//		Iterator<Entry<Integer, Map<Integer, Vertex>>> it = vertices.entrySet().iterator();
//		while( it.hasNext() ) {
//			Entry<Integer, Map<Integer, Vertex>> m =  it.next();
//			Iterator<Entry<Integer, Vertex>> mIt = m.getValue().entrySet().iterator();
//			while( mIt.hasNext() ) {
//				size++;
//				mIt.next();
//			}
//		}
//		return size;
	}
	
	public int E() {
		return edges.size();
	}
	
	// OBS!
	public ArrayList<Vertex> vertices() {
		ArrayList<Vertex> retval = new ArrayList<Vertex>();
		
		for( int i = 0; i < tileAmount; i ++ ) {
			for( int j = 0; j < tileAmount; j++ ) {
				retval.add( vertices[i][j] );
			}
		}
		
		
//		Iterator<Entry<Integer, Map<Integer, Vertex>>> it = vertices.entrySet().iterator();
//		while( it.hasNext() ) {
//			Entry<Integer, Map<Integer, Vertex>> m =  it.next();
//			Iterator<Entry<Integer, Vertex>> mIt = m.getValue().entrySet().iterator();
//			while( mIt.hasNext() ) {
//				Map.Entry<Integer, Vertex> pair = ( Map.Entry<Integer, Vertex> ) mIt.next();
//				retval.add( pair.getValue() );
//			}
//		}
		
		return retval;
	}
	
	// OBS!
	public ArrayList<Edge> edges() {
		ArrayList<Edge> retval = new ArrayList<Edge>();
		
		Iterator it = edges.entrySet().iterator();
		while( it.hasNext() ) {
			Map.Entry pair = ( Map.Entry ) it.next();
			ArrayList<Edge> tmp = ( ArrayList<Edge> ) pair.getValue();
			for( Edge e : tmp ) {
				retval.add( e );
			}
		}
		
		return retval;
	}
	
	public List<Vertex> adjacentTo( Vertex v ) {
		ArrayList<Vertex> retval = new ArrayList<Vertex>();
		ArrayList<Vertex> l = new ArrayList<Vertex>();
		
		Tile srcTile = v.getTile();
		int srcX = srcTile.getX() / tileSize;
		int srcY = srcTile.getY() / tileSize;
		
		if( srcTile.getState() != TileState.OPEN ) return retval;
		else {
			if( srcX >= 0 && srcX < tileAmount && srcY >= 0 && srcY < tileAmount) {
				if( srcX - 1 >= 0 && srcY - 1 >= 0 ) l.add( vertices[srcX - 1][srcY - 1] );
				if( srcY - 1 >= 0 ) l.add( vertices[srcX][srcY - 1] );
				if( srcX + 1 < tileAmount && srcY - 1 >= 0 ) l.add( vertices[srcX + 1][srcY - 1] );
				if( srcX - 1 >= 0 ) l.add( vertices[srcX - 1][srcY] );
				if( srcX + 1 < tileAmount ) l.add( vertices[srcX + 1][srcY] );
				if( srcX - 1 >= 0 && srcY + 1 < tileAmount ) l.add( vertices[srcX - 1][srcY + 1] );
				if( srcY + 1 < tileAmount ) l.add( vertices[srcX][srcY + 1] );
				if( srcX + 1 < tileAmount && srcY + 1 < tileAmount ) l.add( vertices[srcX + 1][srcY + 1] );
			}
			
			for( Vertex vx : l ) {
				if( vx.getTile().getState() == TileState.OPEN ) retval.add( vx );
			}
		}
		
//		ArrayList<Edge> tmp = edges.get(new Point( v.getTile().getX(), v.getTile().getY() ) );
//		
//		if( tmp != null ) {
//			for( Edge e : tmp ) {
//				retval.add( e.getSecond() );
//			}
//		}

//		for( Edge e : edges ) {
//			if( e.getFirst().equals( v ) && e.getSecond() != null ) {
//				if( retval.size() <= 8 ) retval.add( e.getSecond() );
//				else return retval;
//			}
//		}

		return retval;
	}
	
	public int degree( Vertex v ) {
		ArrayList<Edge> tmp = edges.get( new Point( v.getTile().getX(), v.getTile().getY() ) );
		if( tmp != null ) return tmp.size();
		else return 0;
	}
	
	public boolean hasVertex( Vertex v ) {
		int i = v.getTile().getX() / tileSize;
		int j = v.getTile().getY() / tileSize;
		
		Vertex retval  = vertices[i][j];
		
		if( retval != null ) return true;
		else return false;
		
//		if( vertices.containsKey( v.getTile().getX() ) ) {
//			if( vertices.get( v.getTile().getX() ).containsKey( v.getTile().getY() ) ) {
//				return true;
//			}
//		}
//		return false;
	}
	
	public boolean hasEdge( Vertex v1, Vertex v2 ) {
		if( edges.containsKey( new Point( v1.getTile().getX(), v1.getTile().getY() ) ) ) {
			ArrayList<Edge> tmp = edges.get( new Point( v1.getTile().getX(), v1.getTile().getY() ) );
			if( tmp != null ) {
				for( Edge e : tmp ) {
					if( e.getDestination().equals(v2) ) return true;
				}
			}
		}
		return false;
	}
	
	public List<Vertex> getPath( Vertex source, Vertex destination, List<Vertex> path ) {
		// Needs fixing, stack overflow at hasVertex
		if( path == null ) path = new ArrayList<Vertex>();
		path.add( source );
		
		if( hasVertex( source ) && hasVertex( destination ) ) {
			if( source.equals( destination ) ) {
				path.add( destination );
				return path;
			} else {
				ArrayList<Vertex> neighbours = (ArrayList<Vertex>) adjacentTo( source );
				Vertex closest = null;
				Vertex oldClosest = null;
				for( int i = 0; i < neighbours.size(); i++ ) {
					Vertex v = neighbours.get( i );
					if( v.equals( destination ) ) {
						path.add( v );
						return path;
					}
					if( closest == null ) closest = v;
					else if( v.equals( source ) ) {
						continue;
					} else {
						if ( v.getTile().getCenter().distance( destination.getTile().getCenter() ) < closest.getTile().getCenter().distance(destination.getTile().getCenter()) ) {
							oldClosest = closest;
							closest = v;
						}
					}
				}
//				if( !oldClosest.equals( closest ) ) getPath(closest, destination, path);
				// Stack overflow, infinite loop ?
				if( oldClosest == null || !oldClosest.equals( closest ) ) getPath(closest, destination, path);
				else return null;
			}
		} else {
			path.remove( path.size() - 1 );
			return null;
		}
		
		return path;
	}
	
	public void testPrint() {
//		Iterator it = vertices.entrySet().iterator();
//		while( it.hasNext() ) {
//			HashMap<Integer, Vertex> m = ( HashMap<Integer, Vertex> ) it.next();
//			Iterator mIt = m.entrySet().iterator();
//			while( mIt.hasNext() ) {
//				Map.Entry<Integer, Vertex> pair = ( Map.Entry<Integer, Vertex> ) it.next();
//				System.out.println(pair.getKey() + " " + pair.getValue().toString());
//			}
//		}
	}

	public void printVertices() {
		if( vertices() != null ) {
			for( Vertex v : vertices() ) {
				System.out.println(v.toString());
			}
		}
	}
	
	public void printEdges() {
		if( edges != null ) {
			for( Edge e : edges() ) {
				System.out.println(e.toString());
			}
		}
	}


	public void setVertices(Vertex[][] vertices) {
		this.vertices = vertices;
	}
	

	public void setEdges(Map<Point, ArrayList<Edge>> edges) {
		this.edges = edges;
	}

	public Vertex[][] getVertices() {
		return vertices;
	}

	public Map<Point, ArrayList<Edge>> getEdges() {
		return edges;
	}
	
}
