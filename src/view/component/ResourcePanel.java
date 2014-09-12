package view.component;

import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResourcePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private int panelWidth;
	private int panelHeight;
	
	private JLabel goldLabel;
	private JLabel goldValue;
	
	private JLabel woodLabel;
	private JLabel woodValue;
	
	public ResourcePanel( int width, int height ) {
		this.panelWidth = width;
		this.panelHeight = height;
//		setLayout(new FlowLayout());
//		setBackground( Color.ORANGE );
		initPanels();
	}
	
	public void draw( Graphics g ) {
		paintComponent( g );
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent(g);
		paintComponents(g);
	}
	
	public ResourcePanel() {
		initPanels();
	}
	
	private void initPanels() {
		this.goldLabel = new JLabel( "Gold: " );
		this.goldValue = new JLabel( String.valueOf( 55 ) );
		
		this.woodLabel = new JLabel( "Wood: " ) ;
		this.woodValue = new JLabel( "100" );
		
		add( goldLabel );
		add( goldValue );
		
		add( woodLabel );
		add( woodValue );
	}

	public JLabel getGoldValue() {
		return goldValue;
	}

	public void setGoldValue(JLabel goldValue) {
		this.goldValue = goldValue;
	}

	public JLabel getWoodValue() {
		return woodValue;
	}

	public void setWoodValue(JLabel woodValue) {
		this.woodValue = woodValue;
	}
}
