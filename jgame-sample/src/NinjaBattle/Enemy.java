package NinjaBattle;

import jgame.platform.JGEngine;
import static java.lang.Math.*;

abstract public class Enemy extends Ninja {
	static int enemyNum=0;
	Player player;  // needs player location to attack
	boolean inMove;
	boolean wandering;
	int speed;
	int wanderSpeed=1;
	double towardsPlayerSpeedX;
	double towardsPlayerSpeedY;
	long wanderStartTime=0;
	long wanderPeriod=500;
	
	public Enemy(JGEngine game,double x,double y, Player p, int speed){
		super(game,"enemy",true,x,y,2);
		player=p;
	    this.speed=speed;     
	    enemyNum++;
	}
	
	boolean happen(double prob){
		double temp=game.random(0, 1);
		inMove=true;
		return temp<prob;
	}
	
	void towardsPlayer(){
		double px=player.getCenterX();
		double py=player.getCenterY();
		double cx=getCenterX();
		double cy=getCenterY();
		double dis=  sqrt (pow(px-cx,2.0)+pow(py-cy,2.0));
		towardsPlayerSpeedX=  (px-cx)/dis;
		towardsPlayerSpeedY=  (py-cy)/dis;
	}
	
	
	void wander(){
		occupied=true;
		wandering=true;
		wanderStartTime=System.currentTimeMillis();
		xspeed=game.random(-1,1,1)*wanderSpeed;
		yspeed=game.random(-1,1,1)*wanderSpeed;
		face();
				
	}
	
	boolean isWanderOver(){
		long current = System.currentTimeMillis();
		return current>wanderStartTime+wanderPeriod;
			
		
	}
	//called when set new speed
	void face(){
		if (xspeed < 0) {
			setGraphic(moveLeft);
			face = Face.LEFT;
		} else if (xspeed > 0) {
			face = Face.RIGHT;
			setGraphic(moveRight);
		}
	}
	
	void death(){
		if(dead)
			return;
		if(happen(0.15)){
			((Game)game).addBottle( getCenterX(), getCenterY());
		}
		super.death();
		enemyNum--;
	}
	
	
}
