
In this java file, it illustrates my attack strucure. I refactor it a little but it still remains 
the big picture. Attack is the base class for any attack, it hits one thing  one time in default. 
AOEAttack extends from Attack but can hit multiple targets. Everything else extends from these two. 
Use polymorphism we can view any form of attack as an Attack class.


Since all the attack form(fire, wave etc) is a kind of Attack, it will havegetDamage function. So 
this design allows me to reduce duplicate codeand let dynamic dispatch to handle everything. As a result, 
 In checkcollision function what we need to do is to specify the correct cid. When hit is called, 
 Ninja class will class getDamage and use the return value to reduce his hp.

Following is what I do in Ninja
class( player, and enemies extends from ninja)


@Override
	public void hit(JGObject o){    
		if(o instanceof Attack){
			int damage= ((Attack)o).getDamage(this)-defense  ;
			damage= damage>0? damage: 0;
			hp-=damage;
		}
	}

 
In this file, I show 4 classes( they should be in separated files) to show how neat it can be.
Fire is an attack creates a fireball and if it hits a faraway target, the damage increase.
Wave is an AOE that can hit lots of targets. Actually, there are also oneTime, Melee, Thunder, Rush, explode,
Spear, arrow, light.(not shown other wise kind of long) They are all attack but using this structure make it very easy to handle everything.
  cid:
  player: 1
  enemy: 2
  player attack: 3
  enemy attack: 4                                
 

package NinjaBattle;
import jgame.*;
import jgame.platform.*;

import java.util.*;


// the Attack class
abstract public class Attack extends JGObject {  
	
	boolean once=false;
	
	private final long LAST_TIME;     // define the existing time of the attack
	private long m_startTime;			  // time when attck is created
	protected int m_damage; 				//damage, only subclass can see
	
	public Attack(JGEngine game,String name,boolean unique_id,		//the constructor can be concise
            double x,double y,										// because name, and unique_id can be fixed
            int collisionid, long lastTime){                       //in case it may have some specific feature,
																	// I kept it this way.
		super(name, unique_id, x, y, collisionid, null);		//JGobject constructor
													
		setEngine(game);
		LAST_TIME=lastTime;
		m_startTime=System.currentTimeMillis();
	}
	
	public int getDamage(JGObject o){
		
		if(once){
			return 0;
		}
		else{
			once=true;           // if not a AOE attack, one attack should just hit one enemy one time 
			remove(); 
			return m_damage;
		}
			
	
	}
	
	public void move(){
		
		long currentTime= System.currentTimeMillis();
		if(currentTime>=(m_startTime+LAST_TIME))
				remove();				//attack expire
	}
	
	/*
	 * @param part of the animation name    //notice that all of my animation is named as lXXXX, rXXXX
	 */
	protected void setAttackGraphic(String str){
		
			if(xspeed>0){
				setGraphic("r"+str);    // set the Animation flying right
			}
			else
				setGraphic("l"+str);	// left
		}
	}
	
}

abstract public class AOEAttack extends Attack{
	Set<JGObject> set= new HashSet<JGObject>();
	public AOEAttack(JGEngine game,String name,boolean unique_id,double x,double y,
            int collisionid, long lastTime){
		super(game,name,name,unique_id, double x, double y, collisionid, lastTime);
	}
	// AOE can hit multiple enemies, but only once for each
	public int getDamage(JGObject o){
		if(!set.contains(o)){
			set.add(o);
			return m_damage;
		}
		return 0;
		
	}
	
}
/* Fire is not AOE so it directly inherits from Attack
 * the difference is Fire's damage increase while the fireball flying
 *Also should implement setAttackGraphics function 
 **/
public class Fire extends Attack{
	private double m_initialX;
	boolean m_increase=false;
	public Fire(JGEngine game,double x,double y,int collisionid,int xspeed){
		 super(game,"Fire",false,x,y,collisionid, 2000);
		 m_damage=1;
		 this.xspeed= xspeed;
		 m_initialX=x;
		 setAttackGraphic("fire");
	}
	public int getDamage(JGObject o){
		m_damage+= (int) (0.01*Math.abs(m_initialX-x));				//more damage when hits a faraway target
		return super.getDamage(o);
			
	}
		
}

public class Wave extends AOEAttack{
	public Wave(JGEngine game,double x,double y,int collisionid,int xspeed){
		 super(game,"Wave",true,x,y,collisionid, 4000);
		 m_damage=2;
		 this.xspeed=xspeed;
		 setAttackGraphic("wave")
		 
	}
}