/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tradefigurefx;

/**
 * The <code>Dice</code> interface is the interface used by the types of dice
 * in this package. An implementation of this interface should provide a way 
 * of drawing the dice, rolling the dice, knowing if the rolling has ended and
 * subsequently getting the face numbers of the dice rolled.
 * 
 * @author U. A. David
 */
public interface Dice {

	byte NORMAL_BIAS			= 		0;
	byte HIGH_BIAS                  	= 		1;
	byte LOW_BIAS                           = 		2;
	
	byte INITIAL_STATE 			= 		0;
	byte FINAL_STATE 			= 		1;
	byte IN_BETWEEN_STATE                   = 		2;
	
        /**
	 * This method is used to position the <code>Dice</code> in either default (initial)
	 * state, final state or any state of interest(in-between).
	 * @param n the user defined state.
	 */
	void setState(int n);
	
	/**
	 * Returns the current state of the dice
	 * @return Returns the current state of the dice
	 */
	int getState();
	
	/**
	 * <code>roll</code> simulates the rolling process. The dice is be drawn
         * at regular interval before the end state is reached.
         * The bias is any of Dice.NORMAL_BIAS, Dice.HIGH_BIAS or
	 * Dice.LOW_BIAS.
	 *  The <code> caller</code>'s <Code>exec</code> method is called after the 
         * rolling ends.
	 * @param bias the given bias
         * @param caller the caller that's called after the rolling process
	 * 
	 */
	void roll(int bias,Respond caller);
        
        /**
	 * <code>getFaces</code> simulates the number of dots on the face of the 
         * dice.
	 * 
	 * @return returns the number of dots on the face of this dice at the end state
	 */
	int getFace();
}
