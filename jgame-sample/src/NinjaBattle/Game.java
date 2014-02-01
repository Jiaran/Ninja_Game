package NinjaBattle;

import jgame.*;
import jgame.platform.*;

import java.util.ArrayList;

import NinjaBattle.Stage.Event;

/**
 * Tutorial example 7: Game states and timers. Defines a simple game state
 * machine, with a title screen, start game, and in-game state. Defines two
 * timers, one ending the StartGame state, and one shooting a bullet from an
 * object.
 */
public class Game extends JGEngine {

	private ArrayList<JGObject> objectList = new ArrayList<JGObject>();

	Player player;
	Stage stage;
	int delay=0;
	String endString;
	char lastKey;
	public static void main(String[] args) {
		new Game(new JGPoint(960, 700));
	}

	/** Application constructor. */
	public Game(JGPoint size) {
		initEngine(size.x, size.y);
	}

	/** Applet constructor. */
	public Game() {
		initEngineApplet();
		
	}

	public void initCanvas() {
		setCanvasSettings(16, 43, 60, 16, null, null, null);
	}

	public void initGame() {
		setFrameRate(35, 2);
		defineMedia("/media.tbl");
		//defineTile();
		setGameState("Splash");
		
		enableAudio() ;
		
	}

	/* Title screen */

	/** Called when the Title state is entered. */
	public void startSplash() {
		// we need to remove all game objects when we go from in-game to the
		// title screen
		removeObjects(null, 0);
		setBGImage("splashscreen");
		
		
	}

	public void paintFrameSplash() {
		drawString("Press Space to Begin", viewWidth() / 2, viewWidth() / 2, 0,
				new JGFont("DEFAULT", JGFont.BOLD, 20), new JGColor(0, 0, 0));
		
	
	}

	public void doFrameSplash() {
		
		if (getKey(' ')) {
			// ensure the key has to be pressed again to register
			clearKey(' ');
			
			removeGameState("splashscreen");
			setGameState("InGame");

		}

	}

	/** Called once when game goes into the InGame state. */
	public void startInGame() {
		
		setPFSize(3*16,43);
		setTiles(0,21,new String[] { "................................................................" });
		setTileSettings(".",1,0);
		if(player==null){
			player = new Player(this, "player", true, 100, 600);
			stage= new Stage(player,this);
		
		}
		Enemy.enemyNum=0;
		stage.inStart();
		

	}

	public void paintFrameInGame() {
		for (int i = 0; i < objectList.size(); i++) {
			JGObject o=objectList.get(i);
			if(!o.isAlive()){
				objectList.remove(i);
			}
			if(o instanceof Ninja)
				((Ninja)o).showHealth();
		}
		

	}

	public void doFrameInGame() {
		stage.inFrame();
		player.motion();
		
		moveObjects(null, 0);
		checkCollision(4, 1);
		checkCollision(3, 2);
		checkCollision(1, 6);
		
		if(getKey(KeyEnter)){
			if(lastKey=='c')
				player.cheat();
				
			
			
		}
		
		lastKey=getLastKeyChar();

	}
	
	/** Called once when game goes into the InGame state. */
	public void startLoading() {
		setBGImage(null);
		
	}

	public void paintFrameLoading() {
		setColor(new JGColor(255,0,0));
		if(stage.stage!=3){
			drawString("Loading", viewWidth() / 2, viewHeight() / 2, 0);
		}
		else{
			drawImage(960*2+350, 250, "boss");
			setPFSize(16*3,43);
			drawString("Boss", viewWidth() / 2, viewHeight() / 2, 0);
		}
			
		

	}

	public void doFrameLoading() {
		
		delay++;
		if(delay>=30){
			setGameState("InGame");
			delay=0;
		}
		

	}
	
	public void startEnd() {
		setBGImage(null);
		setPFSize(16*3,43);
		if(player.isDead()){
			endString="You Die,Press Enter to restart";
		}
		else{
			endString="You Win,Press Enter to leave";
		}
	}

	public void paintFrameEnd() {
		setColor(new JGColor(255, 0, 0));

		drawString(endString, viewWidth() / 2, viewHeight() / 2, 0);
			
		

	}

	public void doFrameEnd() {
	
		if(getKey(KeyEnter)){
			if(player.isDead()){
				removeObjects(null,0);
				objectList.clear();
				player = new Player(this, "player", true, 100, 600);
				stage.setPlayer(player);
				setGameState("InGame");
			}
			else{
				System.exit(0);
			}
		}

	}

	public void addObject(JGObject o) {
		objectList.add(o);
	}
	
	public void addBottle(double x, double y){
		double temp=random(-1,1);
		if(temp>0){
			new RedBottle(x,y);
		}
		else{
			new BlueBottle(x,y);
		}
	}
	public void createEnemy(Event e){

		switch (e.type){
		case "c":
			new CloseEnemy(this, e.x, e.y, player, 5);
			break;
		case "r":
			new RangeEnemy(this, e.x, e.y, player, 5);
			break;
		case "i":
			new IceEnemy(this, e.x, e.y, player, 6);
			break;
		case "i2":
			new IceEnemy2(this, e.x, e.y, player, 6);
			break;
		case "b":

			new Boss(this, e.x, e.y, player, 6);

			break;
		default:
			System.err.print("create enemy error");
		}
	}

}