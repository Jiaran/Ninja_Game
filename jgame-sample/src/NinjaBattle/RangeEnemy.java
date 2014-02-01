package NinjaBattle;

import jgame.JGObject;
import jgame.JGRectangle;
import jgame.platform.JGEngine;

public class RangeEnemy extends Enemy {
	private final String[] animationStr={"relstand","rerstand","reml","remr","redl","redr","real","rear"};
	boolean light=false;
	boolean canAttack=true;
	public RangeEnemy(JGEngine game,double x,double y, Player p, int speed){
		super(game, x, y, p, speed);
		init(animationStr, 5);
		face=Face.LEFT;
		setBBox(40,0,20,70);
		wanderPeriod=1000;
		wanderSpeed=2;
	}
	
	public void death(){
		super.death();
		((Game)game).addBottle(getCenterX(),getCenterY());
	}
	public void move(){
		
		super.move();
		if (occupied) {
			if (isAnimOver(attackLeft+"8", attackRight+"8")&&!dead) {
				occupied = false;
			}
			if (wandering&&!dead) {
				if (isWanderOver()) {
					wandering = false;
					occupied = false;
				}
			}
			if (isAnimOver(dieLeft+"8", dieRight+"8")) {
				remove();
			}

		} else {

			attackPattern();
		}
		
		if (checkBGCollision(1, yspeed) != 0) {
			yspeed = 0;
		}
	}
		
	void attackPattern() {
		if (Math.abs(player.getX() - x) >= 500) {
			towardsPlayer();
			xspeed = towardsPlayerSpeedX*5;
			yspeed = towardsPlayerSpeedY*5;
			face();
		} else {
			if (happen(0.4) && canAttack) {

				attack();
			} else {
				wander();
				canAttack = true;
			}
		}
		
	}
	
	
	void attack(){
		canAttack=false;
		int px = player.getX();
		face = px - x > 0 ? Face.RIGHT : Face.LEFT;

		super.attack();
		towardsPlayer();
		JGRectangle t=getBBox();

		new Arrow(game, t.x + 10, y + 5, 4, (int)(towardsPlayerSpeedX * 7),
				(int)(towardsPlayerSpeedY * 7), light);
		
	}
		
	
}


class IceEnemy extends RangeEnemy{
	private final String[] animationStr={"ielstand","ierstand","ieml","iemr","iedl","iedr","ieal","iear"};
	public IceEnemy(JGEngine game,double x,double y, Player p, int speed){
		super(game, x, y, p, speed);
		init(animationStr, 5);
		face=Face.LEFT;
		setBBox(40,0,20,70);
		wanderPeriod=1000;
		wanderSpeed=2;
		light=true;
	}
	
	public void hit(JGObject o){
		if(o instanceof Fire){
			int damage= ((Attack)o).getDamage(this)-defense  ;
			damage= damage>0? damage: 0;
			hp-=damage;
		}
	}
	
	void attack(){
		canAttack=false;
		int px = player.getX();
		face = px - x > 0 ? Face.RIGHT : Face.LEFT;

		super.attack();
		towardsPlayer();
		JGRectangle t=getBBox();
		
		int arrowspeedx= face==Face.LEFT? -10:10;
		new Arrow(game, t.x + 10, y + 5, 4, arrowspeedx,
				0, light);
		
		new Arrow(game, t.x + 10, y + 5, 4, arrowspeedx,
				5, light);
		new Arrow(game, t.x + 10, y + 5, 4, arrowspeedx,
				-5, light);
		
	}
	
	
	
	
}

class IceEnemy2 extends IceEnemy{
	private int interval=500;
	private long startTime=0;
	private int dir=0;
	public IceEnemy2(JGEngine game,double x,double y, Player p, int speed){
		super(game,x,y, p, speed);
	}
	public void move(){
		super.move();
		long current=System.currentTimeMillis();
		if(current>startTime+interval){
			int speedx=0;
			int speedy=0;
			switch (dir){
				case 0: 
					speedx=10; 
					speedy=0;
					break;
				case 1: 
					speedx=5; 
					speedy=-9;
					break;
				case 2: 
					speedx=-5; 
					speedy=-9;
					break;
				case 3: 
					speedx=-10; 
					speedy=0;
					break;
				case 4: 
					speedx=-5; 
					speedy=9;
					break;
				case 5: 
					speedx=5; 
					speedy=9;
					break;
			}
			JGRectangle t=getBBox();
			new Arrow(game, t.x + 10, y + 5, 4, speedx,
					speedy, light);
			
			startTime=current;
			dir++;
			if(dir==6){
				dir=0;
			}
			
		}
	}
	
	void attack(){
		canAttack=false;
		int px = player.getX();
		face = px - x > 0 ? Face.RIGHT : Face.LEFT;

		
		towardsPlayer();
		towardsPlayer();
		JGRectangle t=getBBox();

		new Arrow(game, t.x + 10, y + 5, 4, (int)(towardsPlayerSpeedX * 10),
				(int)(towardsPlayerSpeedY * 10), light);
		
	}
}