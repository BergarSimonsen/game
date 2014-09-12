package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.TileObserver;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.enumeration.ResourceType;
import model.enumeration.TileState;
import model.graph.Edge;
import model.graph.Graph;
import model.graph.Vertex;
import model.resource.Gold;
import model.resource.Resource;
import view.component.Player;
import view.component.Tile;
import view.component.tileType.Dirt;
import view.component.tileType.Grass;
import view.component.tileType.TileType;
import view.component.tileType.Water;


public class GamePanel extends JPanel {

	private Point viewPort;
	private Point playerPosition;
	private Point playerPositionCenter;
	private ArrayList<Player> players;
	private Player player;
	private Player selectedPlayer;

	private int mapWidth;
	private int mapHeight;

	private Graph graph;
	private Graph view;

	// Tile information
	private int tileAmount;
	private int tileSize;
	private int viewTileAmount;
	
	private boolean startMoving = false;
	
	// for testing
	private Tile tmpSource = null;
	private Tile tmpDestination = null;
	private ArrayList<Vertex> path;
	
	private int heightOffset = 28;

	public GamePanel() {
		this.players = new ArrayList<Player>();
		this.tileSize = 36;
		this.tileAmount = 100;
		this.viewTileAmount = getWidth() / tileSize;
		this.mapWidth = tileAmount * tileSize;
		this.mapHeight = tileAmount * tileSize;

		this.viewPort = new Point( 0, heightOffset );
		this.playerPosition = new Point( tileSize * 20, tileSize * 20 + heightOffset);
		
		this.playerPositionCenter = new Point( playerPosition.x + tileSize / 2, playerPosition.y + tileSize / 2 );

		this.graph = new Graph( tileAmount, tileSize );
		this.view = new Graph( tileAmount, tileSize ); // broytast
		
		System.out.println("view tile amt: " + getWidth() + " " + viewTileAmount);
		
		initTestTiles();
		initGraphEdges();
		
		player = new Player( playerPosition.x, playerPosition.y, tileSize, tileSize);
		players.add( player );
		this.selectedPlayer = null;
		
		initMouseListener();
		initInputMapAndActionMap();
		System.out.println( "const fin: " + getX() + " " + getY());
	}
	
	private void updatePlayerPosition( int deltaX, int deltaY ) {
		playerPosition.x += deltaX;
		playerPosition.y += deltaY;
		playerPositionCenter.x += deltaX;
		playerPositionCenter.y += deltaY;
	}
	
	private void initInputMapAndActionMap() {
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "goRight");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "goLeft");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "goUp");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "goDown");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "moveAlongPAth");

		am.put("goRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movePlayer(tileSize, 0);
			}
		});
		am.put("goLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movePlayer(-tileSize, 0);
			}
		});

		am.put("goUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movePlayer(0, -tileSize);
			}
		});
		am.put("goDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movePlayer(0, tileSize);
			}
		});
		am.put("moveAlongPAth", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				moveAlongPath();
				startMoving = true;
			}
		});
	}

	private void initTestTiles() {
		int id = 0;
		Random r = new Random();
		for( int i = 0; i < tileAmount; i++ ) {
			for( int j = 0; j < tileAmount; j++ ) {
				int x = r.nextInt();
				TileType tt = null;
				if( x % 3 == 0 ) tt = new Water();
				else if( x % 5 == 0 ) tt = new Grass();
				else tt = new Dirt();
				Tile t = new Tile( ((i*tileSize) + getX() ), ((j*tileSize) + getY() ), tileSize, tileSize, tt );
//				Tile t = new Tile( (i*tileSize), (j*tileSize), tileSize, tileSize, tt );
				if( x % 37 == 0 || ( i == 0 || j == 0 ) || ( i == tileAmount - 1 || j == tileAmount - 1 ) ) {
					t.setState(TileState.PERMANENT_BLOCK);
					t.setColor(Color.RED);
				} else {
					t.setState(TileState.OPEN);
					t.setColor( Color.blue );
				}
				if( x % 17 == 0 ) t.setObtainable( new Gold( 100 ) );
				t.setId(id);
				graph.addVertice( new Vertex( t.getId(), t ) );
				view.addVertice( new Vertex( t.getId(), t ) );
				id++;
			}
		}
		String s = "";
	}
	
	private Tile getSpecificTile( int x, int y ) {
		Vertex[][] tiles = graph.getVertices();
		Tile t = tiles[x][y].getTile();
		return t;
	}
	
	private Tile getTileContainingPoint( Point p ) {
		// not 100% accurate
		int x = ( p.x - ( p.x % tileSize ) ) / tileSize;
		int y = ( p.y - ( p.y / tileSize ) ) / tileSize;
		return getSpecificTile(x, y);
	}
	
	private void addEdge( Tile t, Tile t2 ) {
		if( t != null && ( t2 != null && t2.getState() == TileState.OPEN ) ) {
			graph.addEdge(new Vertex( t.getId(), t ), new Vertex( t2.getId(), t2 ) );
		}
	}
	
	private void initGraphEdges() {
		Vertex[][] tiles = graph.getVertices();
		for( int i = 0; i < tileAmount; i++ ) {
			for( int j = 0; j < tileAmount; j++ ) {
				Tile t = tiles[i][j].getTile();
				if( t.getState() == TileState.OPEN && ( i != 0 || i != tileAmount - 1 || j != 0 || j != tileAmount - 1 ) ) {
					int x = t.getX() / tileSize;
					int y = t.getY() / tileSize;
					Tile tmp = null;
					
					// top left corner neighbor
					tmp = getSpecificTile( x - 1, y - 1 );
					addEdge( t, tmp );
						
					// top neighbor
					tmp = getSpecificTile( x, y - 1 );
					addEdge( t, tmp );
					
					// top right corner neighbor
					tmp = getSpecificTile( x + 1, y - 1 );
					addEdge( t, tmp );
					
					// left neighbor
					tmp = getSpecificTile( x - 1, y );
					addEdge( t, tmp );
					
					// right neighbor
					tmp = getSpecificTile( x + 1, y );
					addEdge( t, tmp );
					
					// bottom left neighbor
					tmp = getSpecificTile( x - 1, y + 1 );
					addEdge( t, tmp );
					
					// bottom neighbor
					tmp = getSpecificTile( x, y + 1 );
					addEdge( t, tmp );
					
					// bottom right neighbor
					tmp = getSpecificTile( x + 1, y + 1 );
					addEdge( t, tmp );
				}
			}
		}
	}

	/**
	 * Update the coordinates of the viewport.
	 * @param x new x coordinate
	 * @param y new y coordinate
	 * @param relativeX if true, add x to existing ( e.g += x ) else replace
	 * @param relativeY if true, add y to existing ( e.g += y ) else replace
	 */
	private void updateViewPortCoordinates( int x, int y, boolean relativeX, boolean relativeY ) {
		if( relativeX ) {
			viewPort.x += x;
		} else {
			viewPort.x = x;
		}
		
		if( relativeY ) {
			viewPort.y += y + heightOffset;
		} else {
			viewPort.y = y;
		}
		invalidate();
		repaint();
	}
	
	// DELETE
	public int curResVal = 0;
	
	protected void movePlayer(int xDelta, int yDelta) {
		// Needs optimization
		Tile currentTile = null;
		Tile nextTile = null;
		
		// Get the current tile
		currentTile = getSpecificTile(playerPosition.x / tileSize, playerPosition.y / tileSize);

		// Check if it is possible to move to next tile.
		if( currentTile != null ) {
			int newX = currentTile.getX();
			int newY = currentTile.getY() + heightOffset;
			// only works if move is constant ( e.g tilesize )
			nextTile = getSpecificTile((newX + xDelta) / tileSize, (newY + yDelta) / tileSize);
		}
		
		if( nextTile != null && nextTile.getState() != TileState.OPEN ) {
			System.out.println(" can't move" );
			return;
		}
		
		// Get resource
		if( nextTile != null && nextTile.getState() == TileState.OPEN ) {
			if( nextTile.getObtainable() != null ) {
				Resource res = ( Resource ) nextTile.getObtainable();
				if( res.getType() == ResourceType.GOLD ) {
					curResVal = res.getValue();
					nextTile.setObtainable( null );
				}
			}
		}
		
//		if( currentTile == null ) System.out.println( "curTile == null" );
//		else System.out.println( "curTile found" );
//		if( nextTile == null ) System.out.println( "nextTile == null" );
//		else System.out.println( "nextTile found" );
		
		// Change following to keep player center
		updatePlayerPosition(xDelta, yDelta);
		
		Point view = fromWorld(playerPosition);
		if (view.x > getWidth() - ( tileSize * 4 )) {
			updateViewPortCoordinates(xDelta, 0, true, true);
			if (viewPort.x + getWidth() > mapWidth) {
				updateViewPortCoordinates(mapWidth - getWidth(), 0, false, true);
				updatePlayerPosition(mapWidth - (player.getWidth() / 2) - 1, 0);
			}
			invalidate();
		} else if (view.x < tileSize * 4) {
			updateViewPortCoordinates(xDelta, 0, true, true);
			if (viewPort.x < 0) {
				updateViewPortCoordinates(0, 0, false, true);
				updatePlayerPosition(player.getWidth() / 2, 0);
			}
			invalidate();
		}
		if (view.y > getHeight() - ( tileSize * 4 )) {
			updateViewPortCoordinates(0, yDelta, true, true);
			if (viewPort.y + getHeight() > mapHeight) {
				updateViewPortCoordinates(0, mapHeight - getHeight(), true, false);
				updatePlayerPosition(0, mapHeight - (player.getHeight() / 2) - 1);
			}
			invalidate();
		} else if (view.y < tileSize * 4 ) {
			updateViewPortCoordinates(0, yDelta, true, true);
			if (viewPort.y < 0) {
				updateViewPortCoordinates(0, 0, true, false);
				updatePlayerPosition(0, player.getHeight() / 2);
			}
			invalidate();
		}
		repaint();
	}

	@Override
	public void invalidate() {
		view = null;
		super.invalidate();
	}

	private void updatePathToView() {
		for( Vertex v : path ) {
			v.getTile().setCenter( fromWorld( v.getTile().getCenter() ) );
		}
	}
	
	public Graph getView() {
		if (view == null && getWidth() > 0 && getHeight() > 0) {
			view = new Graph( tileAmount, tileSize);
			
			// Needs edit, currently drawing all tiles
//			view.setVertices( getVisibleTiles( viewPort.x, viewPort.y, getWidth(), getHeight()) );
			view.setVertices(graph.getVertices());
			if( path != null ) updatePathToView();
		}

		return view;
	}

	private Vertex[][] getVisibleTiles( int x, int y, int width, int height ) {
		// Needs edit, crashes on viewport change
		Vertex[][] retval = new Vertex[tileAmount][tileAmount];
		int x1 = x;
		int x2 = x + ( width / tileSize );
		int y1 = y;
		int y2 = y + ( height / tileSize );
		Point p = fromWorld(new Point( x1, y1 ));
		
		Vertex[][] tmpTiles = graph.getVertices();
		
		int g = 0;
		int h = 0;
		System.out.println( " start: " +  x1 + " " + y1 + " " + x2 + " " + y2);
//		if( x1 < x2 && x2 < tileAmount && y1 < y2 && y2 < tileAmount ) {
			for( int i = x1; i < x2; i++ ) {
				for( int j = y1; j < y2; j++ ) {
//					System.out.println( i + " " + j + " " + g + " " + h);
					if( g < tileAmount && h < tileAmount ) {
						retval[g][h] = tmpTiles[i][j];
						h++;
					}
				}
				if( g < tileAmount ) {
					h = 0;
					g++;
				}
				else break;
			}
//		}
		return retval;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	private void drawTileBorder( Graphics2D g2d, Point p, Tile t ) {
		g2d.setColor(Color.BLACK);
		g2d.drawRect(p.x, p.y, t.getWidth(), t.getHeight());
	}
	
	private void drawTileBackground( Graphics2D g2d, Point p, Tile t ) {
		if( t.getState() == TileState.OPEN || t.getState() == TileState.NAUTICAL_OPEN ) {
			g2d.drawImage( t.getType().getBackgroundImage(), p.x, p.y, null );
		} else {
			g2d.setColor(t.getColor());
			g2d.fillRect(p.x, p.y, t.getWidth(), t.getHeight());
		}
	}
	
	private void drawTileObtainable( Graphics2D g2d, Point p, Tile t ) {
		if( t.getObtainable() != null ) {
			int oX = p.x + ( tileSize / 2 ) - 5;
			int oY = p.y + ( tileSize / 2 ) - 5;
			g2d.setColor(Color.CYAN);
			g2d.fillOval(oX, oY, 5, 5);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
		if (graph != null && graph.V() > 0) { // edit

			Graph v = getView();
			
			Vertex[][] tArr = v.getVertices();
			for( int i = 0; i < tArr.length; i++ ) {
				for( int j = 0; j < tArr[i].length; j++ ) {
					if( tArr[i][j] != null ) {
						Tile t = tArr[i][j].getTile();
						
						// Translate coordinates to correspond to viewport
						Point p = new Point( t.getX(), t.getY() );
						p = fromWorld(p);
						p.y += heightOffset;
						
						drawTileBackground(g2d, p, t);
						drawTileBorder(g2d, p, t);
						drawTileObtainable(g2d, p, t);
					}
				}
			}

			// TODO: Change to paint heroes
			Point real = fromWorld(playerPosition);
			int x = real.x - (player.getWidth() / 2);
			int y = real.y - (player.getHeight()/ 2);
			g2d.setColor(player.getColor());
			g2d.fillRect(x, y, player.getPlayerWidth(), player.getPlayerHeight());
			
			// paintGraph
//			drawGraphEdges(g2d);
			
			drawPath(g2d, path);
			
		}
		g2d.dispose();
	}
	
	private void drawPath( Graphics2D g2d, ArrayList<Vertex> path ) {
		if( path != null && path.size() > 0 ) {
			g2d.setColor( Color.ORANGE );
			for( int i = 0; i < path.size(); i++ ) {
				if( i > 0 ) {
					Vertex first = path.get( i - 1 );
					Vertex second = path.get( i );
					g2d.drawLine(first.getTile().getCenter().x, first.getTile().getCenter().y + heightOffset, second.getTile().getCenter().x, second.getTile().getCenter().y + heightOffset);
				}
			}
		}
	}
	
	private void drawGraphEdges( Graphics2D g2d ) {
		if( graph != null && graph.edges() != null && graph.edges().size() > 0 ) {
			g2d.setColor( Color.CYAN );
			for( Edge e : graph.edges() ) {
				Vertex v1 = e.getSource();
				Vertex v2 = e.getDestination();
				g2d.drawLine(v1.getTile().getCenter().x, 
						v1.getTile().getCenter().y, 
						v2.getTile().getCenter().x, 
						v2.getTile().getCenter().y);
			}
		}
	}

	protected Point fromWorld(Point wp) {

		Point p = new Point();

		p.x = wp.x - viewPort.x;
		p.y = wp.y - viewPort.y + heightOffset;
		
		return p;

	}

	private void initMouseListener() {
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				System.out.println( "mp: " + arg0.getPoint().toString());
				getPath(arg0.getPoint());
//				getSelectedItem( arg0.getPoint() );
//				toggleTileBlocked(arg0.getPoint());
//				for( Tile t : view ) {
//					Point po = new Point( t.getX(), t.getY() );
//					Rectangle r = new Rectangle( po.x, po.y, t.getWidth(), t.getHeight() );
//					if( r.contains(arg0.getPoint())) System.out.println("found, " + t.toString() );
//				}
//				initGraphEdges();
//				invalidate();
//				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void getSelectedItem(Point p) {
		boolean found = false;
		// Check if player is selected
//		for( Player pl : players ) {
//			Point pp = fromWorld(new Point( pl.getX(), pl.getY() ));
//			Rectangle r = new Rectangle( pp.x, pp.y, pl.getPlayerWidth(), pl.getPlayerHeight() );
//			if( r.contains( p ) ) {
//				player.setColor( Color.WHITE );
//				selectedPlayer = player;
//				found = true;
//				break;
//			}
//		}
		
		if( !found ) {
			toggleTileBlocked( p );
		}
		
		if( found ) {
			invalidate();
			repaint();
		}
	}
	
	private void getPath( Point p ) {
		boolean found = false;
		
		Point plPoint = fromWorld( playerPosition );

		tmpSource = getSpecificTile(playerPosition.x / tileSize, playerPosition.y / tileSize);
//		tmpSource = getSpecificTile( plPoint.x, plPoint.y );
		
		Point pnt = fromWorld(p);
		Tile t = getTileContainingPoint(pnt);
		if( t != null ) System.out.println(t.toString());
		if( t != null && t.getState() == TileState.OPEN ) {
			System.out.println("found destination");
			tmpDestination = t;

			if( tmpSource != null && tmpDestination != null ) {
				path = null;
				path = (ArrayList<Vertex>) graph.getPath(new Vertex( tmpSource.getId(), tmpSource ), new Vertex( tmpDestination.getId(), tmpDestination ), null );

				if( path != null && path.size() > 1 ) {
					for( int g = 0; g < path.size(); g++ ) {
						Vertex vt = path.get( g );
						for( int h = 0; h < view.V(); h++ ) {
							Tile t2 = view.vertices().get( h ).getTile();
							if( vt.getTile().getCenter().equals(t2.getCenter())) {
								break;
							}
						}
					}

					//					if( path != null && path.size() > 1 ){
					//						invalidate();
					//						repaint();
					//					}
				}
				else {
					System.out.println("no path found");
				}
			}
		}
		
//		if( tmpDestination == null ) System.out.println("dest is null");
//		System.out.println("starting dijkstra");
//		DijkstraAlgorithm da = new DijkstraAlgorithm(graph);
//		da.execute(new Vertex(tmpSource.getId(), tmpSource));
//		LinkedList<Vertex> newPath = da.getPath(new Vertex(tmpDestination.getId(), tmpDestination));
//		if( newPath != null ) System.out.println("dijkstra finish, size: " + newPath.size());
//		else System.out.println("dijkstra finished, path is null");
			
		if( path != null && path.size() > 0 ) {
			invalidate();
			repaint();
		}
	}
	
	private void toggleTileBlocked(Point p) {
//		boolean found = false;
//		for( int i = 0; i < tiles.length; i++ ) {
//			for( int j = 0; j < tiles.length; j++ ) {
//				Tile t = tiles[i][j];
//				Point tmpPoint = new Point( t.getX(), t.getY() );
//				Point pnt = fromWorld(tmpPoint);
//				Rectangle r = new Rectangle( pnt.x, pnt.y, t.getWidth(), t.getHeight() );
////				if( r.contains(p) ) {
////					System.out.println("found tile");
//////					t.setBlocked(!t.isBlocked() );
//				if( r.contains(p) && !t.isBlocked() ) {
//					if( tmpSource == null ) tmpSource = t;
//					else tmpDestination = t;
//					if( tmpSource != null && tmpDestination != null ) {
//						System.out.println( "source: " + tmpSource.getCenter().toString());
//						System.out.println( "destination " + tmpDestination.getCenter().toString());
//						path = null;
//						path = (ArrayList<Vertex>) graph.getPath(new Vertex( tmpSource ), new Vertex( tmpDestination ), null );
//						
//						if( path != null && path.size() > 1 ) {
//							System.out.println("graph found, size: " + path.size());
//							for( int g = 0; g < path.size(); g++ ) {
//								Vertex vt = path.get( g );
//								for( int h = 0; h < view.size(); h++ ) {
//									Tile t2 = view.get( h );
//									if( vt.getPoint().equals(t2.getCenter())) {
//										System.out.println(t2.toString());
//										break;
//									}
//								}
//							}
//							
//							if( path != null && path.size() > 1 ){
//								invalidate();
//								repaint();
//							}
//						}
//						else {
//							System.out.println("no path found");
//						}
////					found = true;
////					t.setBlocked(!t.isBlocked());
////					break;
//					}
//				}
//			}
//		}
//		
//		if( found ) {
//			System.out.println(("Found"));
//			invalidate();
//			repaint();
//		}
	}

	public void moveAlongPath() {
		if( path != null && path.size() > 0 ) {
			for( int i = 0; i < path.size(); i++ ) {
				Vertex v = path.get( i );
				
				// Not moving precisely
				System.out.println( "XXXXXX " + v.getTile().getCenter().toString() );
				System.out.println( "PPPPPP " + playerPositionCenter.toString());
				
				Point pp = playerPosition;
				Point tp = new Point( v.getTile().getX(), v.getTile().getY() );
				int deltaX = pp.x - tp.x;
				int deltaY = pp.y - tp.y;
				
				playerPositionCenter = v.getTile().getCenter();
				playerPositionCenter.y += heightOffset;
				playerPosition.x = playerPositionCenter.x - ( tileSize / 2 );
				playerPosition.y = playerPositionCenter.y - ( tileSize / 2 );
				
//				updatePlayerPosition(deltaX, deltaY);
//				while( !playerPositionCenter.equals(v.getTile().getCenter()) ) {
//					if( playerPositionCenter.x < v.getTile().getCenter().x &&
//							playerPositionCenter.y < v.getTile().getCenter().y) {
//						movePlayer(tileSize, tileSize);
//						continue;
//					}
//					if( playerPositionCenter.x < v.getTile().getCenter().x &&
//							playerPositionCenter.y > v.getTile().getCenter().y) {
//						movePlayer(tileSize, -tileSize);
//						continue;
//					}
//					if( playerPositionCenter.x > v.getTile().getCenter().x &&
//							playerPositionCenter.y < v.getTile().getCenter().y) {
//						movePlayer(-tileSize, tileSize);
//						continue;
//					}
//					if( playerPositionCenter.x > v.getTile().getCenter().x &&
//							playerPositionCenter.y > v.getTile().getCenter().y) {
//						movePlayer(-tileSize, -tileSize);
//						continue;
//					}
//					if( playerPositionCenter.x < v.getTile().getCenter().x ) {
//						movePlayer(tileSize, 0);
//						continue;
//					}
//					if( playerPositionCenter.y < v.getTile().getCenter().y ) {
//						movePlayer(0, tileSize);
//						continue;
//					}
//					if( playerPositionCenter.x > v.getTile().getCenter().x ) {
//						movePlayer(-tileSize, 0);
//						continue;
//					}
//					if( playerPositionCenter.y > v.getTile().getCenter().y ) {
//						movePlayer(0, -tileSize);
//						continue;
//					}
//				}
				try {
					Thread.sleep( 1 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		path = new ArrayList<Vertex>();
	}

	public boolean getStartMoving() {
		return startMoving;
	}

	public void setStartMoving(boolean startMoving) {
		this.startMoving = startMoving;
	}

	public ArrayList<Vertex> getPath() {
		return path;
	}

	public void setPath(ArrayList<Vertex> path) {
		this.path = path;
	}
}
