package NinjaBattle;

import jgame.JGColor;
import jgame.JGRectangle;
import jgame.platform.JGEngine;

public class Boss extends Enemy {
	private String[] animationStr={"bosslstand","bossrstand","bossml","bossmr",
			"bossdl","bossdr","bossal","bossar"};
	private String row="bossrs";
	private String close="bossth";
	private String normal="bossa";
	private String blink="bossb";
	private String ultra="bossu";
	boolean isIntervalOver=true;
	boolean delayStart=false;
	boolean isRow=false;
	boolean isClose=false;
	boolean isNormal=false;
	boolean isBlink=false;
	boolean isUltra=false;
	boolean inMove=false;
	boolean canUltra=true;
	long intervalStart=0;
	boolean canThunder=false;
	boolean riot=false;
	private int[] intervalTime={ 500,2000,1000,0,9500 };
	private int bossMaxHP=20;
	int bwidth=20;
	int bheight=70;
	public Boss(JGEngine game,double x,double y, Player p, int speed){
		super(game, x, y, p, speed);
		init(animationStr, bossMaxHP);
		face=Face.LEFT;
		setBBox(70,20,bwidth,bheight);
		
	}
	
	public void move(){
		super.move();
		if(hp<5){
			riot=true;
			
			for(int i=0;i<4;i++){
				intervalTime[i]*=0.7;
			}
		}
		if(delayStart){
			delayStart();
		}
		System.out.println(xspeed);
		if (occupied) {
			interval();

		} else {
			if(happen(0.2))
				wander();
			else
				attackPattern();
		}
	}
	
	
	void attackPattern(){
		occupied=true;
		xspeed=0;
		yspeed=0;
		if(riot&&canUltra){
			ultra();
			return;
		}
		if(happen(0.2)){
			blink();
			return;
		}
		if(canThunder){
			close();
			return;
		}
		if(happen(0.3)){
			row();
		}
		normal();
		//else
			//normal();
		
	}
	
	void interval(){
		
		if (wandering) {
			if (isWanderOver()) {
				wandering = false;
				occupied = false;
			}
		}
		if (isAnimOver(row+"l12", row+"r12")) {
			whenAnimOver(intervalTime[0]);
		}
		if (isAnimOver(close+"l12", close+"r12")) {
			whenAnimOver(intervalTime[1]);
		}
		if (isAnimOver(normal+"l8", normal+"r8")) {
			whenAnimOver(intervalTime[2]);
		}
		if (isAnimOver(blink+"l4", blink+"r4")) {
			whenAnimOver(intervalTime[3]);
		}
		if (isAnimOver(ultra+"l3", ultra+"r3")) {
			whenAnimOver(intervalTime[4]);
		}
		if (isAnimOver(dieLeft+"8", dieRight+"8")) {
			remove();
		}
	}
	
	boolean isIntervalOver(long time){
		long current= System.currentTimeMillis();
		return current>intervalStart+time;
		
	}
	void whenAnimOver(long time){
		if (inMove) {
			inMove = false;
			intervalStart = System.currentTimeMillis();
			
			xspeed=0;
			yspeed=0;
			stopAnim();
		} else if (isIntervalOver(time)) {
			occupied = false;
			startAnim();
		} else
			return;
		
		
		
	}
	
	void normal(){
		if(inMove)
			return;
		inMove=true;
		isNormal=true;
		setGraphic(face, normal);
		int px = player.getX();
		JGRectangle t=getBBox();
		face = px - t.x > 0 ? Face.RIGHT : Face.LEFT;

		super.attack();
		
		
		if(riot){
			new Arrow(game, t.x + 10, y + 45, 4, 7,7, true);
			new Arrow(game, t.x + 10, y + 45, 4, 7,-7, true);
			new Arrow(game, t.x + 10, y + 45, 4, -7,7, true);
			new Arrow(game, t.x + 10, y + 45, 4, -7,-7, true);
		}
		else{
			towardsPlayer();
			new Arrow(game, t.x + 10, y + 45, 4, (int)(towardsPlayerSpeedX * 5),
				(int)(towardsPlayerSpeedY * 5), true);
		}
	}
	void close(){
		if(inMove)
			return;
		inMove=true;
		game.playAudio("v");
		canThunder=false;
		face= getCenterX()>player.getCenterX()? Face.LEFT:Face.RIGHT;
		delayStart=true;
		isClose=true;
		setGraphic(face, close);
	}
	void row(){
		if(inMove)
			return;
		game.playAudio("v");
		inMove=true;
		delayStart=true;
		isRow=true;
		face= getCenterX()>player.getCenterX()? Face.LEFT:Face.RIGHT;
		setGraphic(face, row);
	}
	void blink(){
		if(inMove)
			return;
		canThunder=true;
		inMove=true;
		delayStart=true;
		isBlink=true;
		face= getCenterX()>player.getCenterX()? Face.LEFT:Face.RIGHT;
		setGraphic( face, blink);
	}
	void ultra(){
		if(inMove)
			return;
		inMove=true;
		delayStart=true;
		isUltra=true;
		canUltra=false;
		setGraphic(face, ultra);
		
	}
	
	void delayStart(){
		if(isBlink){
			if(isAnimOver(blink+"l3",blink+"r3")){
				clearState();
				face=player.face;
				y=player.getY()-80;
				if (face == Face.LEFT)
					x = player.getX() - 100;
				
				else
					x = player.getX()-20 ;
				game.playAudio("ra");
			}
		}

		if (isUltra) {
			 
		        gameThread.start();
		        clearState();

		}
		if (isRow) {
			if(isAnimOver(row+"l5",row+"r5")){
				clearState();
				towardsPlayer();
				xspeed=towardsPlayerSpeedX*10;
				yspeed=towardsPlayerSpeedY*10;
				new Row(game, x, y, 4, this);
			}

		}
		if (isClose) {
			if(isAnimOver(close+"l3",close+"r3")){
				clearState();				
				new Thunder(game, x-20, y-35+40, 4);
			}
		}
	}
	
	void setGraphic(Face face, String str){
		if(face==Face.LEFT){
			setGraphic(str+"l");
		}
		else{
			setGraphic(str+"r");

		}
		
	}
	
	void clearState(){
		delayStart=false;
		isRow=false;
		isClose=false;
		isNormal=false;
		isBlink=false;
		isUltra=false;
	}
	
	public void showHealth(){
		int num= hp*10/maxhp+1;
		if(num>10)
			num=10;
		if(hp==0)
			num=0;
		game.setColor(JGColor.green);
		for(int i=0; i<num ; i++){
			game.drawRect(getCenterX()-20+8*i, getCenterY(), 9, 8, true, false);
		}
	}
	
	Thread gameThread = new Thread() {
		 
        @Override
        public void run(){
        	int cx;
        	int cy;
        	int tx;
        	int ty;
        	game.playAudio("alarm");
            for(int i=0; i<25; i++){
            	
            	cx=player.getCenterX();
            	cy=player.getCenterY();
            	tx=(int)game.random(cx-150, cx+150);
            	ty=(int)game.random(cy-150, cy+150);
            	new Target(game, tx, ty);
            	if(i%5==0){
            		game.playAudio("exp");
            	}
            	if(happen(0.5)){
            		new Target(game, getCenterX(), getCenterY());
            	}
            	try{
            		Thread.sleep(600-i*15);
            	}
            	catch(InterruptedException e){break;}
            }
        }
    };
    public void death(){
    	super.death();
    	gameThread.interrupt();
    }
    public void destroy(){
    	
    	super.destroy();
    }
}
