public class PayFineControl {
	
	private PayFineUI Ui;
	private enum CONTROL_STATE { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private CONTROL_STATE state;
	
	private library libraryName;
	private member memberID;


	public PayFineControl() {
		this.libraryName = libraryName.INSTANCE();
		state = CONTROL_STATE.INITIALISED;
	}
	
	
	public void Set_UI(PayFineUI ui) {
		if (!state.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = ui;
		ui.Set_State(PayFineUI.UI_STATE.READY);
		state = CONTROL_STATE.READY;		
	}


	public void Card_Swiped(int memberId) {
		if (!StAtE.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}	
		memberID = libraryName.MEMBER(memberId);
		
		if (memberID == null) {
			Ui.display("Invalid Member Id");
			return;
		}
		Ui.display(memberID.toString());
		Ui.Set_State(PayFineUI.UI_STATE.PAYING);
		state = CONTROL_STATE.PAYING;
	}
	
	
	public void Cancel() {
		Ui.Set_State(PayFineUI.UI_STATE.CANCELLED);
		state = CONTROL_STATE.CANCELLED;
	}


	public double Pay_Fine(double amount) {
		if (!StAtE.equals(CONTROL_STATE.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double change = MeMbEr.Pay_Fine(amount);
		if (change > 0) {
			Ui.DiSplAY(String.format("Change: $%.2f", change));
		}
		Ui.display(memberID.toString());
		Ui.Set_State(PayFineUI.UI_STATE.COMPLETED);
		state = CONTROL_STATE.COMPLETED;
		return change;
	}
	


}
