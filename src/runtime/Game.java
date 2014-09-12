package runtime;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.GamePanel;
import view.component.ResourcePanel;

public class Game implements Runnable {
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	private JFrame frame;
	private GamePanel gamePanel;
	private ResourcePanel resourcePanel;
	private boolean running = false;
	private Thread thread;

	public Game() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
		}

		frame = new JFrame("GAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		gamePanel = new GamePanel();
//		frame.add( gamePanel );
		frame.add(gamePanel, BorderLayout.CENTER);
		
		resourcePanel = new ResourcePanel(frame.getWidth(), 36 );
		resourcePanel.setBackground( Color.YELLOW );
		resourcePanel.setVisible(true );
		resourcePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		frame.add( resourcePanel, BorderLayout.NORTH );
		
//		JLabel jl = new JLabel( "Gold: " );
//		frame.add( jl, BorderLayout.NORTH );
//		frame.add( new ResourcePanel(), BorderLayout.NORTH );
		
//		JPanel testPanel = new JPanel();
//		testPanel.add( new JButton( "NORTH" ) );
//		testPanel.setPreferredSize(new Dimension( frame.getWidth(), 36 ) );
//		testPanel.setMaximumSize(new Dimension( frame.getWidth(), 36 ) );
//		testPanel.setMinimumSize(new Dimension( frame.getWidth(), 36 ) );
//		testPanel.setVisible(true);
//		frame.add( testPanel, BorderLayout.NORTH );
		
		frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
//		EventQueue.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//				}
//
//				frame = new JFrame("GAME");
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				frame.setLayout(new BorderLayout());
//				gamePanel = new GamePanel();
//				frame.add(gamePanel);
//				frame.pack();
//				frame.setLocationRelativeTo(null);
//				frame.setVisible(true);
//				running = true;
//				//				run2();
//			}
//		});
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "GAME");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		}
	}

	public void render() {
		BufferStrategy bs = frame.getBufferStrategy();
		if( bs == null ) {
			frame.createBufferStrategy(3);
			render();
			return;
		}

		Graphics g = bs.getDrawGraphics();

		gamePanel.paintComponent(g);
		resourcePanel.revalidate();
		resourcePanel.draw(g);
//		resourcePanel.paintComponents(g);
		
//		System.out.println( resourcePanel.getHeight() );
		
		g.dispose();
		bs.show();
	}

	public void update() {
		if( gamePanel.curResVal > 0 ) {
			int tmp = Integer.valueOf( resourcePanel.getGoldValue().getText() );
			tmp += gamePanel.curResVal;
			gamePanel.curResVal = 0;
			resourcePanel.getGoldValue().setText( String.valueOf( tmp ) ) ;
		}
		
//		System.out.println(" -- " + resourcePanel.getPreferredSize().toString() );
		if( gamePanel.getStartMoving() && gamePanel.getPath() != null ) {
			System.out.println( "game.move" );
			gamePanel.moveAlongPath();
		}
		if( gamePanel.getPath() == null || gamePanel.getPath().size() == 0 ) {
			gamePanel.setStartMoving(false);
		}
	}

	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		long lastFpsTime = 0;
		while( running ) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double)OPTIMAL_TIME);

			lastFpsTime += updateLength;
			if(lastFpsTime >= 1000000000){
				lastFpsTime = 0;
			}

			update();
			render();

			try{
				long lt = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
//				Room.gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
//				System.out.println(Room.gameTime);
//				Thread.sleep(Room.gameTime);
				Thread.sleep( lt * 20 );
			}catch(Exception e){
				
			}
		}
	}
}
