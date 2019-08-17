import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI UI;
	
	private library Library;
	private member Member;
	private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private ControlState state;
	
	private List<book> pending;
	private List<loan> completed;
	private book book;
	
	
	public BorrowBookControl() {
		this.Library = LIBRARY.getInstance();
		state = ControlState.INITIALISED;
	}
	

	public void setUI(BorrowBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)){
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}
		this.UI = ui;
		ui.setState(BorrowBookUI.UI_STATE.READY);
		state = ControlState.READY;		
	}

		
	public void swipeMemberCard(int memberId) {
		if (!state.equals(ControlState.READY)){
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
		}
		Member = Library.getMembers(memberId);
		if (Member == null) {
			UI.Display("Invalid memberId");
			return;
		}
		if (Library.MemberCanBorrow(Member)) {
			pending = new ArrayList<>();
			UI.setState(BorrowBookUI.UI_STATE.SCANNING);
			state = ControlState.SCANNING; 
		}
		else 
		{
			UI.Display("Member cannot borrow at this time");
			UI.setState(BorrowBookUI.UI_STATE.RESTRICTED); 
		}
	}
	
	
	public void scan(int bookId) {
		book = null;
		if (!state.equals(ControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		book = Library.getBook(bookId);
		if (book == null) {
			UI.Display("Invalid bookId");
			return;
		}
		if (!book.isAvailable()) {
			UI.Display("Book cannot be borrowed");
			return;
		}
		pending.add(book);
		for (book B : pending) {
			UI.Display(B.toString());
		}
		if (Library.Loans_Remaining_For_Member(M) - pending.size() == 0) {
			UI.Display("Loan limit reached");
			scanComplete();
		}
	}
	
	
	public void scanComplete() {
		if (pending.size() == 0) {
			borrowCancel();
		}
		else {
			UI.Display("\nFinal Borrowing List");
			for (book B : pending) {
				UI.Display(B.toString());
			}
			completed = new ArrayList<loan>();
			UI.setState(BorrowBookUI.UI_STATE.FINALISING);
			state = ControlState.FINALISING;
		}
	}


	public void commitLoans() {
		if (!state.equals(ControlState.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book B : pending) {
			loan LOAN = Library.IssueLoan(B, Member);
			completed.add(LOAN);			
		}
		UI.Display("Completed Loan Slip");
		for (loan LOAN : completed) {
			UI.Display(LOAN.toString());
		}
		UI.setState(BorrowBookUI.UI_STATE.COMPLETED);
		state = ControlState.COMPLETED;
	}

	
	public void borrowCancel() {
		UI.setState(BorrowBookUI.UI_STATE.CANCELLED);
		state = ControlState.CANCELLED;
	}
	
	
}
