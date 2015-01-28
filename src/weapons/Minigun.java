package weapons;
import java.util.List;
import javax.imageio.ImageIO;
import entities.Bullet;
import entities.Player;

public class Minigun extends WeaponImpl{
	
	/**
	 * This class rapresent the machinegun, it's a weapon that shoot 3 bullet at same time
	 * 
	 *  @author Giovanni Romio
	 */
	
	private double newX;
	private double newY;
	
	/**
	 * Define the gun properties, could implements more dettails as accuracy and realoadTime
	 */
	
	public Minigun(){
		this.name = "MINIGUN";
		this.damage = 8;
		this.bulletsPerRound = 60;
		this.bullets = 60;
		this.x = 7;
		this.y = 17;
		try{
			sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/minigun.png"));
			HUDsprite =  ImageIO.read(getClass().getResourceAsStream("/sprites/weaponsHUD/minigun.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This metod let the player to shoot adding bullets to the current gameSession.
	 * This gun can shoot up to 3 bullets at the same time.
	 * @param g rapresent the player
	 * @param xMouse Xcoordinate of the Mouse relative to the JFrame
	 * @param yMouse Ycoordinate of the Mouse relative to the JFrame
	 * @param l rapresent the List wich contains all the bullets that are current displayed
	 */
	
	public int shoot(Player g, double xMouse, double yMouse, List<Bullet>l) {
		if(this.bullets>0){
			this.reloading=false;
			/* Add 3 bullets */
			this.bullets-=3;
			/*
			 * To get other 2 bullet trajectory:
			 * translate cursor to origin,
			 * rotation of the selected angle,
			 * translate again in original position.
			 */
			l.add(new Bullet(g,xMouse,yMouse,8));
			newX = g.getXScreen() + (xMouse-g.getXScreen())*Math.cos(0.5) - (yMouse-g.getYScreen())*Math.sin(0.5);
			newY = g.getYScreen() + (xMouse-g.getXScreen())*Math.sin(0.5) + (yMouse-g.getYScreen())*Math.cos(0.5);
			l.add(new Bullet(g,newX,newY,damage));
			newX = g.getXScreen() + (xMouse-g.getXScreen())*Math.cos(-0.5) - (yMouse-g.getYScreen())*Math.sin(-0.5);
			newY = g.getYScreen() + (xMouse-g.getXScreen())*Math.sin(-0.5) + (yMouse-g.getYScreen())*Math.cos(-0.5);
			l.add(new Bullet(g,newX,newY,damage));
		}
		return this.bullets;
	}
}