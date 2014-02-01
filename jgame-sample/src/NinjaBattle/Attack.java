package NinjaBattle;
import jgame.*;
import jgame.platform.*;
import java.util.*;

/* cid: 
 * player: 1
 * enemy: 2
 * player attack: 3
 * enemy attack: 4
 */
public class Attack extends JGObject {
	
	JGEngine game;
	boolean once=false;
	boolean aoe=false;
	
	private final long LAST_TIME;
	private long startTime;
	int damage;
	Set<JGObject> set= new HashSet<JGObject>();
	public Attack(JGEngine game,String name,boolean unique_id,
            double x,double y,
            int collisionid, long lastTime){
		
		super(name, unique_id, x, y, collisionid, null);
		this.game=game;
		setEngine(game);
		LAST_TIME=lastTime;
		startTime=System.currentTimeMillis();
	}
	
	public int getDamage(JGObject o){
		once=true;
		if(!aoe)
			return damage;
		else if( !set.contains(o)){			
			set.add(o);
			return damage;
		}
		else
			return 0;
			
	}
	
	public void move(){
		if(once && !aoe)
			remove();
		long currentTime= System.currentTimeMillis();
		if(currentTime>=(startTime+LAST_TIME))
				remove();
	}
	
	
}

class Melee extends Attack{
	static int meleeWidth=70;
	static int meleeHeight=50;
	static int lastTime=500;
	public Melee(JGEngine game,double x,double y,int collisionid){
		 super(game,"Melee",true,x,y,collisionid, lastTime);
		 damage=1;
		 setBBox(0, 0, meleeWidth, meleeHeight);
		 game.playAudio("ca");
//		 JGRectangle t=getBBox();
//			System.out.println(t);
	}
	
	static int getWidth(){
		return meleeWidth;
	}
}

class Thunder extends Melee{
	static{
		meleeWidth=200;
		meleeHeight=200;
		lastTime=1000;
	}
	public Thunder(JGEngine game,double x,double y,int collisionid){
		 super(game,x,y,collisionid);
		 damage=5;
		 game.playAudio("close");
	}
}

class Row extends Melee{
	static{
		meleeWidth=100;
		meleeHeight=100;
		lastTime=1000;
	}
	Ninja source;
	public Row(JGEngine game,double x,double y,int collisionid,Ninja source){
		 super(game,x,y,collisionid);
		 damage=3;
		 this.source=source;
		 game.playAudio("row");
	}
	public void move(){
		super.move();
		int offset = source.face==Face.LEFT? -20:20;
		x=source.getCenterX()-meleeWidth/2+offset;
		y=source.getCenterY()+meleeHeight/2;
	}
}

class Arrow extends Attack{
	
	
	public Arrow(JGEngine game,double x,double y,int collisionid,int xspeed, int yspeed, boolean light){
		 super(game,"Arrow",true,x,y,collisionid, 2000);
		if (light){
			damage = 2;
			setGraphic("light");
			game.playAudio("wave");
		}
		else{
			damage = 1;
			setGraphic("arrow");
			game.playAudio("ra");
		}
		 this.xspeed=xspeed;
		 this.yspeed=yspeed;
		 
		 
	}
	
	
}

class Fire extends Attack{
	private double orignx;
	boolean increase=false;
	public Fire(JGEngine game,double x,double y,int collisionid,int xspeed){
		 super(game,"Fire",true,x,y,collisionid, 2000);
		 damage=1;
		 this.xspeed=xspeed;
		 if(xspeed>0){
			 setGraphic("rfire");
		 }
		 else{
			 setGraphic("lfire");
		 }
		 orignx=x;
	}
	public int getDamage(JGObject o){
		System.out.println(colid+""+o.colid);
		
		damage+= (int) (0.01*Math.abs(orignx-x));
		return super.getDamage(o);
			
	}
	
	 
}

class Wave extends Attack{
	public Wave(JGEngine game,double x,double y,int collisionid,int xspeed){
		 super(game,"Fire",true,x,y,collisionid, 4000);
		 game.playAudio("v");
		 aoe=true;
		 damage=2;
		 this.xspeed=xspeed;
		 if(xspeed>0){
			 setGraphic("waver");
		 }
		 else{
			 setGraphic("wavel");
		 }
		 game.playAudio("wave");
		 
	}
}



abstract class OneTime extends Attack{
	String endImg;
	public OneTime(JGEngine game,double x,double y,int collisionid, String endImg, String Anim){
		 super(game,"OneTime",true,x,y,collisionid, 5000);
		 this.endImg=endImg;
		 aoe=true;
		 setGraphic(Anim);
	}
	
	public void move(){
		super.move();
		String str=this.getImageName();
		if(str.equals(endImg)){
			remove();
		}
	}
	
}
class Target extends OneTime{
	public Target(JGEngine game,double x,double y){
		super(game,x,y,0, "target5", "target");
		damage=0;
		double c=game.random(0, 1);
		//if(c<0.2)
			//game.playAudio("exp");
	}
	public void move(){
		
		String str=this.getImageName();
		if(str.equals(endImg)){
			double c=game.random(0, 1);
			if(c<0.1){
				((Game)game).addBottle(x,y);
			}
			new Explode(game,x,y-75,4);
			remove();
		}
	}
}

class Explode extends OneTime{
	public Explode(JGEngine game,double x,double y,int collisionid){
		super(game,x,y,collisionid, "explode18", "explode");
		damage=5;
		setBBox(0,75,70,75 );
	}
}

class Spear extends OneTime{
	public Spear(JGEngine game,double x,double y,int collisionid){
		super(game,x,y,collisionid, "spear8", "spear");
		damage=3;
	}
}

class Blood extends OneTime{
	public Blood(JGEngine game,double x,double y,int collisionid, boolean left){
		super(game,x,y,0, "", null);
		if(left){
			
			endImg="bl8";
			setGraphic("bl");
			JGRectangle t= getBBox();
			this.x=x-20;
			this.y=y-70;
			
		}
		else{
			
			endImg="br8";
			setGraphic("br");
			JGRectangle t= getBBox();
			this.x=x+40;
			this.y=y-70;
			
		}
		damage=0;
		game.playAudio("die");
	}
}
