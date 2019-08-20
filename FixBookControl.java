public class FixBookControl {
	
	private FixBookUI UI;
	private enum Controlstate { INITIALISED, READY, FIXING };
	private Controlstate state;
	
	private library lib;
	private book currentBook;


	public FixBookControl() {
		this.lib = library.INSTANCE();
		state = Controlstate.INITIALISED;
	}
	
	
	public void setUI(FixBookUI ui) {
		if (!state.equals(Controlstate.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.UI = ui;
		ui.Set_State(FixBookUI.UI_STATE.READY);
		state = Controlstate.READY;		
	}


	public void bookScanned(int bookId) {
		if (!state.equals(Controlstate.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}	
		Cur_Book = LIB.Book(bookId);
		
		if (Cur_Book == null) {
			UI.display("Invalid bookId");
			return;
		}
		if (!Cur_Book.IS_Damaged()) {
			UI.display("Book has not been damaged");
			return;
		}
		UI.display(Cur_Book.toString());
		UI.Set_State(FixBookUI.UI_STATE.FIXING);
		state = Controlstate.FIXING;		
	}


	public void fixBook(boolean MUST_fix) {
		if (!state.equals(Controlstate.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}	
		if (MUST_fix) {
			LIB.Repair_BOOK(Cur_Book);
		}
		Cur_Book = null;
		UI.Set_State(FixBookUI.UI_STATE.READY);
		state = Controlstate.READY;		
	}

	
	public void scanningComplete() {
		if (!state.equals(Controlstate.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}	
		UI.Set_State(FixBookUI.UI_STATE.COMPLETED);		
	}






}
