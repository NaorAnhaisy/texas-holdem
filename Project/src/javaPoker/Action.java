package javaPoker;

/**
 * Represents any action exist in the game :
 * could be only by action type (Like Fold, Check, Call And All In) 
 * or also with sum to raise to in a raise type of action.
 * @author Naor Anhaisy
 *
 */
public class Action {
	private ActionType actionType;
	private int sumToRaise;
	
	/**
	 * Makes action for call/check and fold.
	 * @param actionType The action to do.
	 */
	public Action(ActionType actionType) {
		this.actionType = actionType;
		this.sumToRaise = 0;
	}
	
	/**
	 * Makes action for raise.
	 * @param actionType The action to do.
	 * @param sumToRaise Money to raise to.
	 */
	public Action(ActionType actionType, int sumToRaise) {
		this.actionType = actionType;
		this.sumToRaise = sumToRaise;
	}
	
	/**
	 * @return the action type.
	 */
	public ActionType getActionType() {
		return actionType;
	}
	
	/**
	 * @return the sum to raise to.
	 */
	public int getSumToRaise() {
		return sumToRaise;
	}
}
