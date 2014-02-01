package NinjaBattle;

import jgame.JGRectangle;
import jgame.platform.JGEngine;

public class CloseEnemy extends Enemy {
	private final String[] animationStr={"clstand","crstand","cml","cmr","cdl","cdr","cal","car"};
	
	CloseEnemy(JGEngine game,double x,double y, Player p, int speed){
		super(game, x, y, p, speed);
		init(animationStr, 5);
		face=Face.LEFT;
		setBBox(40,0,20,70);
	}
	
	public void move(){
		super.move();
		System.out.println(x+","+xspeed);
		if (occupied) {
			if (isAnimOver("cal8", "car8")&&!dead) {
				occupied = false;
			}
			if(wandering&&!dead){
				if(isWanderOver()){
					wandering=false;
					occupied=false;
				}
			}
			if(isAnimOver("cdl8" ,"cdr8")){
				remove();
			}

		} else {
			if (isPlayerInRange()) {
				if (happen(0.4))
					attack();
				else{
					wander();
					System.out.println("wander");
				}
			} else {
				towardsPlayer();
				System.out.println("to player");
				System.out.println(player.x+","+player.y);
				xspeed = towardsPlayerSpeedX*speed;
				yspeed = towardsPlayerSpeedY*speed;
				face();
			}
		}
		if (checkBGCollision(1, yspeed) != 0) {
			yspeed = 0;
		}
		
	}
	
	boolean isPlayerInRange(){
		int px=player.getCenterX();
		int py=player.getCenterY();
		JGRectangle t=getBBox();
		if(Math.abs(px-getCenterX())<=3&&Math.abs(py-getCenterY())<=3){
			return true;
		}
		if(face == face.LEFT){
			return (px>=t.x-50&&px<=t.x && y>=t.y-40 && y<=t.y+t.height+40);
				
		}
		else{
			return (px<=(t.x+t.width+50)&&px>=(t.x+t.width) &&y>=t.y-40 && y<=t.y+t.height+40);
		}
		
	}
	
	void attack(){
		super.attack();
		JGRectangle t=getBBox();
		
		if(face==face.LEFT){
			new  Melee(game,t.x-Melee.getWidth(),y,4);
		}
		else{
			new  Melee(game,t.x+t.width,y,4);
		}
	}
}
