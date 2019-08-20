public class ReturnBookControl {

	private ReturnBookUI Ui;
	private enum CONTROL_STATE { INITIALISED, READY, INSPECTING };
	private CONTROL_STATE state;
	
	private library library;
	private loan current_loan;
	

	public ReturnBookControl() {
		this.library = library.INSTANCE();
		state = CONTROL_STATE.INITIALISED;
	}
	
	
	public void Set_UI(ReturnBookUI ui) {
		if (!state.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = ui;
		ui.Set_State(ReturnBookUI.UI_STATE.READY);
		state = CONTROL_STATE.READY;		
	}


	public void Book_scanned(int Book_ID) {
		if (!state.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		}	
		book CUR_book = library.Book(Book_ID);
		
		if (CUR_book == null) {
			Ui.display("Invalid Book Id");
			return;
		}
		if (!CUR_book.On_loan()) {
			Ui.display("Book has not been borrowed");
			return;
		}		
		current_loan = library.LOAN_BY_BOOK_ID(Book_ID);	
		double Over_Due_Fine = 0.0;
		if (Current_loan.Over_Due()) {
			Over_Due_Fine = library.CalculateOverDueFine(CurrENT_loan);
		}
		Ui.display("Inspecting");
		Ui.display(CUR_book.toString());
		Ui.display(Current_loan.toString());
		
		if (Current_loan.Over_Due()) {
			Ui.display(String.format("\nOverdue fine : $%.2f", Over_Due_Fine));
		}
		Ui.Set_State(ReturnBookUI.UI_STATE.INSPECTING);
		state = CONTROL_STATE.INSPECTING;		
	}


	public void Scanning_Complete() {
		if (!state.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
		}	
		Ui.Set_State(ReturnBookUI.UI_STATE.COMPLETED);		
	}


	public void Discharge_loan(boolean isDamaged) {
		if (!state.equals(CONTROL_STATE.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		}	
		library.Discharge_loan(Current_loan, isDamaged);
		Current_loan = null;
		Ui.Set_State(ReturnBookUI.UI_STATE.READY);
		state = CONTROL_STATE.READY;				
	}


}
