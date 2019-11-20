
/* Vivarium.java
 * 
 * Mincan Yang - ymcmatt@bu.edu
 * 
 * Vivarium object serves as the vivarium for fish, food and predator
 * when food is eaten or 'F' is pressed, the drawFood Boolean is set to False
 * */

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium implements Displayable, Animate {
	private Tank tank;
	public ArrayList<Component> vivarium = new ArrayList<Component>();
	  public Fish fish;
	  
	  //CREATES PREDATOR
	  public Predator pred;
	  
	  //CREATES FOOD
	  public Food food;
	  public Food food1;
	  public Food food2;
	  
	  //BOOLEAN TO INDICATE FOOD SHOULD BE DRAWN
	  public boolean drawFood;
	  
	  //BOOLEAN TO INDICATE PREY IS EATEN
	  public boolean eaten;

	public Vivarium() {
		tank = new Tank(4.0f, 4.0f, 4.0f);
		Coord fishCoord = new Coord(1,2,1); 
		Coord predCoord = new Coord(2,0,2); 
		Coord foodCoord = new Coord(0,1.5,0); 

		drawFood = false;
		eaten = false; 
	    fish = new Fish("fish", fishCoord,this);
	    pred = new Predator("predator", predCoord, this);
	    food = new Food("food", foodCoord);
	    food1 = new Food("food", foodCoord);
	    food2 = new Food("food", foodCoord);
	    
	}

	public void initialize(GL2 gl) {
	    tank.initialize( gl );
	    
	    fish.init(gl);

	    pred.init(gl);
	    
	}

	public void update(GL2 gl) {
	    tank.update( gl );
	    
	    //FISH IS ONLY UPDATED IF NOT EATEN
	    if (!eaten) {
	    	fish.animationUpdate(gl);
	    }
	    
	    pred.animationUpdate(gl);
	    
	    //DROPS FOOD ONCE DRAWN
	    if(drawFood) {
		    food.init(gl);
	    	food.update(gl);
		    food1.init(gl);
	    	food1.update(gl);
		    food2.init(gl);
	    	food2.update(gl);
	    }
	}

	public void draw(GL2 gl) {
	    tank.draw( gl );
	    
	    //IF THE FISH ISN'T EATEN, IT WILL BE DRAWN
	    if(!eaten) {
	    	fish.draw(gl);
	    }
	    
	    pred.draw(gl);
	    
	    //IF 'F' IS PRESSED, FOOD IS DRAWN
	    if (drawFood) {
	    	food.draw(gl);

	    }
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}

	@Override
	public void animationUpdate(GL2 gl) {
//		for (Component example : vivarium) {
//			if (example instanceof Animate) {
//				((Animate) example).animationUpdate(gl);
//			}
//		}
	}
}
