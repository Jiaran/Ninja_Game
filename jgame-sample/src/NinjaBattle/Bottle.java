package NinjaBattle;
import jgame.*;
import jgame.platform.*;


abstract public class Bottle extends JGObject {
	Bottle(double x, double y){
		super("bottle", false, x, y, 6, null);
	}
	
	abstract public void hit(JGObject o);
}

class RedBottle extends Bottle{
	RedBottle(double x, double y){
		super(x,y);
		setImage("heal");
	}
	public void hit(JGObject o){
		if(o instanceof Player){
			((Player) o).hp+=3;
			remove();
		}
		
	}
}

class BlueBottle extends Bottle{
	BlueBottle(double x, double y){
		super(x,y);
		setImage("mana");
	}
	public void hit(JGObject o){
		if(o instanceof Player){
			((Player) o).energy+=5;
			remove();
		}
	}
}
