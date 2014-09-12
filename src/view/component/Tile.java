package view.component;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.enumeration.TileState;
import model.resource.Obtainable;
import view.component.tileType.TileType;


public class Tile {
	private BufferedImage background;
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
//	private boolean blocked;
	private Point centerPoint;
	private int id;
	private TileType type;
	private TileState state;
	
	private Obtainable obtainable;
	
	public Tile(int x, int y, int width, int height, TileType type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.centerPoint = new Point( x + ( width / 2 ), y + ( height / 2 ) );
		this.id = (x / width) * 10 + (y / width);
		this.type = type;
		
//		if( ( x + y ) % 72 == 0 ) {
//			this.color = Color.RED;
//		}
//		else {
//			this.color = Color.BLUE;
//		}
		
//		try {
//			this.background = ImageIO.read(getClass().getResource("/grass.jpg"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public Point getCenter() {
		return centerPoint;
	}
	
	public void setCenter( Point p ) {
		this.centerPoint = p;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public BufferedImage getBackground() {
		return background;
	}

	public void setBackground(BufferedImage background) {
		this.background = background;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		Color retval = type.getColor();
		if( state != TileState.OPEN  ) {
			retval = Color.RED;
		}
		return retval;
	}

	public void setColor(Color color) {
		this.color = color;
	}

//	public void setBlocked(boolean blocked) {
//		if( blocked ) this.color = Color.red;
//		else this.color = Color.BLUE;
//		this.blocked = blocked;
//	}
	
	@Override
	public String toString() {
		String retval = "-- Printing tile -- \n";
		retval += "id: " + id + "\n";
		retval += "x: " + x + "\n";
		retval += "y: " + y + "\n";
		retval += "isBlocked: " + state + "\n";
		retval += "center: " + getCenter() + "\n";
		retval += "------------";
		
		return retval;
	}

	@Override
	public boolean equals(Object obj) {
		Tile other = ( Tile ) obj;
		if( obj == null || other == null ) return false;
		else if( this.x == other.x &&
				this.y == other.y &&
				this.width == other.width &&
				this.height == other.height &&
				this.color == other.color &&
				this.getCenter() == other.getCenter() ) {
			return true;
		} else {
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public TileState getState() {
		return state;
	}

	public void setState(TileState state) {
		this.state = state;
	}

	public Obtainable getObtainable() {
		return obtainable;
	}

	public void setObtainable(Obtainable obtainable) {
		this.obtainable = obtainable;
	}
}
