package MotorediGioco;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import SessioniDiGioco.ControllerDiSessione;

@SuppressWarnings("serial")
public class PannelloDiGioco extends JPanel implements Runnable,KeyListener,MouseListener,MouseMotionListener {
	
	/**
	 * Main Thread of the game, implements all the KeyLister and Mouse Listerner
	 * of the Game Panel.
	 * 
	 * @author Giovanni Romio
	 */	
	
	/**Dimensioni*/
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	/**Componenti per stampare gli sprite */
	private Graphics2D g;
	private BufferedImage image;
	/**GameThread*/
	private Thread gameThread;
	private boolean running;	
	private int FPS = 60;
	/**Quanto tempo deve passare tra una draw() e l'altra */
	private long targetTime = 1000/FPS;
	/**Controller di Sessione*/
	private ControllerDiSessione cds;
	private static PannelloDiGioco p;
	private PannelloDiGioco(){
		super();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		/**Per utilizzare il keyListener dobbiamo imporre il requestFocus al nostro JPanel */
		setFocusable(true);
		requestFocus();
		/**Inizializziamo il gameThread */		
		if(gameThread== null){
			gameThread= new Thread(this);
			/**Aggiungiamo i controlli per le periferiche */	
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			gameThread.start();
		}
	}
	/**
	 * 
	 * @return the istance of the current Panel
	 */
	public static PannelloDiGioco getIstanceof(){
		if(PannelloDiGioco.p == null){
			p = new PannelloDiGioco();
		}
		return p;
	}
	/**
	 * Initialize the graphic component
	 */
	private void init(){
		/** Inizializziamo l'immagine di default e il componente g che permettono di stampare
		i vari sprites nel gioco nel nostro schermo */
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g= (Graphics2D) image.getGraphics();
		/** Inizializiamo la variabile per avviare il gameThread*/
		running = true;
		/** Creiamo un'istanza di cds che permette di effettuare lo switch tra le varie sessioni
		di gioco */
		cds = new ControllerDiSessione();
	}	
	/**
	 * Panel update >> cds update >> currentSession update
	 */
	private void update(){
		/** Il gamePanel richiama il metodo update del controllerDiSessione che a sua volta ritrasmette
		tale compito alla Sessione di Gioco interessata */
		cds.update();
	}
	/**
	 * Panel draw >> cds draw >> currentSession draw
	 */
	private void draw(){
		cds.draw(this.g);
	}
	/**
	 * Draw in the Panel the graphic componenet g
	 */
	private void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,WIDTH,HEIGHT, null);
		g2.dispose();
	}
	/**
	 * 
	 * @return the graphic componenet of the Panel
	 */
	public Graphics2D getgraphics(){
		return g;
	}

	/**
	 * Main Thread of the Game, this thread will call the update method for:
	 * player, map, HUD;
	 * This thread will also call draw method for:
	 * player, map, HUD, Zombies, Bullets 
	 */
	public void run() {
		this.init();
		long start;
		long elapsed;
		long wait;
		while(running){	
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			/** Detraggo al tempo di attesa standard la durata dell'ultime 3 operazioni */
			wait = targetTime - elapsed/1000000;
			if(wait < 0) wait =0;
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}			
	}
	
	/** MOUSE */
	public void mousePressed(MouseEvent e) {
		cds.mouseClicked(e.getX(),e.getY());
	}
	
	public void mouseReleased(MouseEvent e) {
		cds.mouseReleased();
	}	

	public void mouseMoved(MouseEvent e) {
		try{
			cds.MouseMovement(e.getX(), e.getY());	
		}catch(NullPointerException n){
			/** Se il mouse non è nella finestra */
			return;
		}
		
	}
	
	public void mouseDragged(MouseEvent e) {
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}	
	
	/** KEYBOARD */
	public void keyPressed(KeyEvent e) {
		try {
			cds.keyPressed(e.getKeyCode());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void keyReleased(KeyEvent e) {
		cds.keyReleased(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {
	}

}
