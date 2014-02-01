package NinjaBattle;
import jgame.*;
import jgame.platform.*;

public class Player extends Ninja {
	
	private final String[] animationStr={"lstand","rstand","pml","pmr","pdl","pdr","pal","par"};
	private final int FIRE=3;
	private final int BLINK=5;
	private final int WAVE=5;
	private final int MAX_ENERGY=10;
	int energy= 10;
	
	private final int ENERGY_RATE=5000;
	private int SPEED=5;
	private boolean moving=false;
	String blinkRight="sr";
	String blinkLeft="sl";
	long previousTime=0;
	boolean cheat=false;
	public Player(JGEngine game,String name,boolean unique_id,
            double x,double y){
		super(game,name, unique_id, x, y, 1);
		init(animationStr, 10);
		face=Face.LEFT;
		setBBox(40,0,20,70);
	}
	
	
	public void motion(){
		
		if(occupied){
		 
			if(isAnimOver("pal8", "par8")){
				occupied=false;
			}
			if(isAnimOver(	"sl8", "sr8")){
				occupied=false;
				xspeed=0;
			}
			if(isAnimOver("pdl8" ,"pdr8")){
				game.setGameState("End");
			}
			
		}
		else{
			
			if(game.getKey('W')){
				yspeed=-SPEED;
				moving=true;
			}
			if(game.getKey('A')){
				xspeed=-SPEED;
				moving=true;
				face=Face.LEFT;
			}
			if(game.getKey('S')){
				yspeed=SPEED;
				moving=true;

			}
			if(game.getKey('D')){
				xspeed=SPEED;
				moving=true;
				face=Face.RIGHT;
			}
			if(!moving){
				xspeed=0;
				yspeed=0;
			}
			moving=false;
			if(game.getKey('U')){
				attackMelee();
			}
			if(game.getKey('I')){
				if(energy>=FIRE)
					attackFire();
			}
			if(game.getKey('J')){
				if(energy>=WAVE)
					attackWave();
			}
			if(game.getKey('K')){
				if(energy>=BLINK)
					avoid();
			}
			if(game.getKey('T')){
				JGRectangle t=getBBox();
				System.out.println(t);
			}
		}
	}
	
	public void move() {
		
		super.move();
		energy();
		if(cheat){
			hp=10;
			energy=10;
			
		}
		
		
		
	}
	
	private void energy(){
		long currentTime=System.currentTimeMillis();
		if(currentTime>previousTime+ENERGY_RATE && energy<MAX_ENERGY){
			energy++;
			previousTime=currentTime;
		}
	}
	
	private void attackMelee(){
		attack();
		JGRectangle t=getBBox();
		
		if(face==face.LEFT){
			new  Melee(game,t.x-Melee.getWidth(),y,3);
		}
		else{
			new  Melee(game,t.x+t.width,y,3);
		}
	}
	
	private void attackFire() {
		attack();
		energy-=FIRE;
		JGRectangle t=getBBox();
		
		if(face==face.LEFT){
			new  Fire(game,t.x+10,y+25,3,-10);
		}
		else{
			new  Fire(game,t.x+10,y+25,3,10);
		}	
	}

	private void attackWave() {
		attack();
		energy-=WAVE;
		JGRectangle t=getBBox();
		if(face==face.LEFT){
			new  Wave(game,t.x+10,y-40,3,-15);
		}
		else{
			new  Wave(game,t.x+10,y-40,3,15);
		}	
	}
	
	private void avoid(){
		energy-=BLINK;
		occupied=true;
		if(face==Face.LEFT){
			setGraphic(blinkLeft);
			xspeed=-15;
		}
		else{
			setGraphic(blinkRight);
			xspeed=15;
		}
	}
	
	public int getX(){
		
		return (int)x+40;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public void showHealth(){
		super.showHealth();
		
		game.setColor(JGColor.blue);
		for(int i=0; i<energy ; i++){
			game.drawRect(x+5+i*8, y-16+8, 9, 8, true, false);
		}
	}
	boolean isDead(){
		return dead;
	}
	void restart(){
		hp=10;
		energy=10;
		x=100;
		y=600;
		dead=false;
		face=Face.LEFT;
		occupied=false;
	}
	
	void cheat(){
		cheat=!cheat;
		if(SPEED==5)
			SPEED=10;
		else
			SPEED=5;
	}
}
