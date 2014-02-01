package NinjaBattle;

import jgame.*;
import jgame.platform.*;

public class Ninja extends JGObject {
	
	JGEngine game;
	int hp;
	int maxhp;
	String lstand;
	String rstand;
	String moveLeft;
	String moveRight;
	String dieLeft;
	String dieRight;
	String attackLeft;
	String attackRight;
	boolean dead=false;
	Move move;
	Face face;
	boolean occupied;
	int defense=0;
	
	
	public Ninja(JGEngine game,String name,boolean unique_id,
            double x,double y,
            int collisionid){
		
		super(name, unique_id, x, y, collisionid, null);
		this.game=game;
		setEngine(game);
	}
     
	void init(String[] animations, int maxhp){
		if(animations.length!=8)
			System.out.println("Number of Animations is incorrect ");
		lstand= animations[0];
		rstand=animations[1];
		moveLeft= animations[2];
		moveRight= animations[3];
		dieLeft= animations[4];
		dieRight= animations[5];
		attackLeft=animations[6];
		attackRight=animations[7];
		this.maxhp=maxhp;
		hp=maxhp;
		
		move=Move.LEFT;
		occupied=false;
		((Game)game).addObject(this);
	}
	
	public void move(){
		if(hp<=0&&!dead)
			death();
		if (occupied) {

		} else {
			if (xspeed == 0 && yspeed == 0) {

				if (face == Face.LEFT)
					setGraphic(lstand);
				else
					setGraphic(rstand);
			} else if (face == Face.LEFT)
				setGraphic(moveLeft);
			else
				setGraphic(moveRight);
			
			
			if (checkBGCollision(1, yspeed) != 0) {
				yspeed = 0;
			}
			if (checkBGCollision(1, xspeed) != 0) {
				if(xspeed>0){
					xspeed=-1;
				}
				else
					xspeed=1;
			}
			

		}
	}
	
	public void showHealth(){
		int num= hp*5/maxhp+1;
		if(num>5)
			num=5;
		if(hp==0)
			num=0;
		game.setColor(JGColor.green);
		for(int i=0; i<num ; i++){
			game.drawRect(x+5+i*16, y-16, 17, 8, true, false);
		}
	}
	
	void attack(){
		occupied=true;
		xspeed=0;
		yspeed=0;
		if(face==Face.LEFT){
			setGraphic(attackLeft);
		}
		else{
			setGraphic(attackRight);
		}
	}
	
	void death(){
		
		occupied=true;
		
		xspeed=0;
		yspeed=0;
		colid=0;
		if(dead)
			return;
		if(face==Face.LEFT){
			setGraphic(dieLeft);
			new Blood(game, x, y, 0,false);
		}
		else{
			setGraphic(dieRight);
			new Blood(game, x, y, 0,true);
		}
		dead=true;
		
	}
	
	boolean isAnimOver(String s1, String s2){
		String str=this.getImageName();
		if(str==null)
			return false;
		return str.equals(s1)|| str.equals(s2);
	}
	
	@Override
	public void hit(JGObject o){
		if(o instanceof Attack){
			int damage= ((Attack)o).getDamage(this)-defense  ;
			damage= damage>0? damage: 0;
			hp-=damage;
		}
	}
	
	
	
	int getCenterX(){
		JGRectangle t= getBBox();
		int cx= t.x+t.width/2;
		return cx;
	}
	
	int getCenterY(){
		JGRectangle t= getBBox();
		int cy= t.y+t.height/2;
		return cy;
		
	}
}

enum Move{ LEFT, RIGHT,UP,DOWN, ATTACK1, ATTACK2, ATTACK3, AVOID };
enum Face{LEFT,RIGHT};