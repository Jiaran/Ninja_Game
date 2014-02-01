package NinjaBattle;

import jgame.*;
import jgame.platform.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Stage {
	private final String[] stageDiscription={"0 c 500 500" +
			"\n760 c 1000 500 c 1000 400 c 1000 600"+"\n1500 r 1300 500 c 1600 500 "+"\n2000 c 2500 500 c 1500 500 c 1500 600 r 2500 500",
			"0 r 500 500"+
					"\n760 a 1000 500 c 1000 400 c 1000 600"+"\n1500 r 1200 400 r 1700 400 r 1700 600 r 1200 600 "+"\n2000 i 2500 500",
			"0 i2 500 500"+"\n800 c 1400 500 c 1400 600 c 1400 400 r 1400 500"+
					"\n1600 c 2000 400 c 1000 400 c 1000 600 c 2000 600 c 2000 500 c 1000 500"+"\n2200 i2 2600 500 r 1800 400 r 1800 600",
			"0 b 500 500"
	};
	String stage1="0";
	private final int endStage=4;
	Player player;
	Game game;
	ArrayList<Integer> xTrigger= new ArrayList<Integer>();
	int stage=0;
	int currentIndex=0;
	ArrayList < ArrayList<Event> > eventList = new ArrayList < ArrayList<Event> >();
	public Stage(Player player, Game game){
		this.player=player;
		this.game=game;
	}
	
	void inFrame(){
		int currentx= player.getCenterX();
		int xofs;
		if(currentx<=480){
			xofs=480;
		}
		else if(currentx>=960*3-480){
			xofs=960*3-480;
		}
		else
			xofs=currentx;
		game.setViewOffset(
				xofs,350, // the position within the playfield
				true       // true means the given position is center of the view,
				           // false means it is topleft.
			);
		if(currentIndex<xTrigger.size()&& currentx>=xTrigger.get(currentIndex) ){
			ArrayList<Event> temp=eventList.get(currentIndex);
			for(int i=0; i< temp.size(); i++){
				game.createEnemy(temp.get(i));
			}
			currentIndex++;
			
		}
		
		if(Enemy.enemyNum==0&& (currentx>950*3||stage==3) ){
			if(stage==3){
				game.setGameState("End");
			}
			else
				game.setGameState("Loading");
			
			clear(); 
			stage++;
		}
	}
	void inStart(){
		if(stage==endStage){
			
			return;
		}
		clear();
		game.setBGImage("bg"+stage);
		player.setPos(100, 600);
		if(stage==0){
			game.playAudio("background","bgm1",true);
		}
		if(stage==3){
			game.stopAudio("bgm1");
			game.setPFSize(2*16, 43);
			game.playAudio("background","bgm2",true);
			
			try{
				Thread.sleep(3000);
			}
			catch(InterruptedException e){
				
			}
		}
		
		readStage();
		
	}
	
	void readStage(){
		String current=stageDiscription[stage];
		Scanner s=new Scanner(current);
		while(s.hasNext()){
			String str=s.nextLine();
			readLine(str);
			
			
		}
		s.close();
		
	}
	
	void readLine(String Line){
		Scanner s=new Scanner(Line);
		
		xTrigger.add(s.nextInt());
		ArrayList<Event> once=new ArrayList<Event>();
		
		while(s.hasNext()){
			String name=s.next();
			double x= s.nextDouble();
			double y= s.nextDouble();
			once.add( new Event(name,x,y));
			
		}
		eventList.add(once);
	}
	void clear(){
		
		currentIndex=0;
		xTrigger.clear();
		eventList.clear();
	}
	void setPlayer(Player p){
		player=p;
	}
	class Event{
		String type;
		double x;
		double y;
		public Event(String c, double x, double y){
			type=c;
			this.x=x;
			this.y=y;
		}
		
	}
	
}
