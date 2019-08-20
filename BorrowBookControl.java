import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI UI;
	

	private Library library;
	private Member member;
	private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private ControlState state;

	
	private List<book> pending;
	private List<loan> completed;
	private Book book;
	
	
	public BorrowBookControl() {
		this.library = LIBRARY.getInstance();
		state = ControlState.INITIALISED;
	}
	

	public void setUI(BorrowBookUI UI) {
		if (!state.equals(ControlState.INITIALISED)){
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}
		this.UI = UI;
		UI.setState(BorrowBookUI.UIState.READY);
		state = ControlState.READY;		
	}

		
	public void swipeMemberCard(int memberId) {
		if (!state.equals(ControlState.READY)){
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");

		}
		member = library.getMembers(memberId);
		if (member == null) {
			UI.display("Invalid memberId");

			return;
		}
		if (library.MemberCanBorrow(Member)) {
			pending = new ArrayList<>();
			UI.setState(BorrowBookUI.UIState.SCANNING);
			state = ControlState.SCANNING; 
		}
		else 
		{
			UI.display("Member cannot borrow at this time");
			UI.setState(BorrowBookUI.UIState.RESTRICTED); 
		}
	}
	
	
	public void scan(int bookId) {
		book = null;
		if (!state.equals(ControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}	
		book = library.getBook(bookId);
		if (book == null) {
			UI.display("Invalid bookId");
			return;
		}
		if (!book.isAvailable()) {
			UI.display("Book cannot be borrowed");
			return;
		}
		pending.add(book);
		for (book bookItem : pending) {
			UI.display(bookItem.toString());
		}
		if (library.loansRemainingForMember(M) - pending.size() == 0) {
			UI.display("Loan limit reached");
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
		for (book bookItem : pending) {
			loan LOAN = library.IssueLoan(bookItem, member);
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
