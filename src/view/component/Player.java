package view.component;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class Player extends JPanel {
	
	private int x;
	private int y;
	private int playerWidth;
	private int playerHeight;
	private Color color;
	
	public Player( int x, int y, int width, int height ) {
		this.x = x;
		this.y = y;
		this.playerWidth = width;
		this.playerHeight = height;
		this.color = Color.BLACK;
	}
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(playerWidth, playerHeight);
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

	public int getPlayerWidth() {
		return playerWidth;
	}

	public void setPlayerWidth(int playerWidth) {
		this.playerWidth = playerWidth;
	}

	public int getPlayerHeight() {
		return playerHeight;
	}

	public void setPlayerHeight(int playerHeight) {
		this.playerHeight = playerHeight;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
