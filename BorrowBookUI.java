import java.util.Scanner;


public class BorrowBookUI {
	
	public static enum UIState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private BorrowBookControl control;
	private Scanner input;
	private UIState state;

	
	public BorrowBookUI(BorrowBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UIState.INITIALISED;
		control.setUI(this);
	}

	
	private String inputValue(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void outputValue(Object object) {
		System.out.println(object);
	}
	
			
	public void setState(UIState STATE) {
		this.state = STATE;
	}

	
	public void run() {
		outputValue("Borrow Book Use Case UI\n");
		
		while (true) {
			switch (state) {			
			case CANCELLED:
				outputValue("Borrowing Cancelled");
				return;
			case READY:
				String MemberString = inputValue("Swipe member card (press <enter> to cancel): ");
				if (MemberString.length() == 0) {
					control.borrowCancel();
					break;
				}
				try {
					int memberID = Integer.valueOf(MemberString).intValue();
					control.swipeMemberCard(memberID);
				}
				catch (NumberFormatException e) {
					outputValue("Invalid Member Id");
				}
				break;	
			case RESTRICTED:
				inputValue("Press <any key> to cancel");
				control.borrowCancel();
				break;
			case SCANNING:
				String BookString = inputValue("Scan Book (<enter> completes): ");
				if (BookString.length() == 0) {
					control.scanComplete();
					break;
				}
				try {
					int bookID = Integer.valueOf(BookString).intValue();
					control.scan(bookID);
					
				} catch (NumberFormatException e) {
					outputValue("Invalid Book Id");
				} 
				break;
			case FINALISING:
				String answer = inputValue("Commit loans? (Y/N): ");
				if (answer.toUpperCase().equals("N")) {
					control.borrowCancel();
					
				} else {
					control.commitLoans();
					inputValue("Press <any key> to complete ");
				}
				break;
			case COMPLETED:
				outputValue("Borrowing Completed");
				return;
			default:
				outputValue("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + state);			
			}
		}		
	}


	public void display(Object object) {
		outputValue(object);		
	}


}
