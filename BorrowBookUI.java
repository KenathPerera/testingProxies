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
				String MEM_STR = inputValue("Swipe member card (press <enter> to cancel): ");
				if (MEM_STR.length() == 0) {
					control.borrowCancel();
					break;
				}
				try {
					int Member_ID = Integer.valueOf(MEM_STR).intValue();
					control.swipeMemberCard(Member_ID);
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
				String Book_Str = inputValue("Scan Book (<enter> completes): ");
				if (Book_Str.length() == 0) {
					control.scanComplete();
					break;
				}
				try {
					int BiD = Integer.valueOf(Book_Str).intValue();
					control.scan(BiD);
					
				} catch (NumberFormatException e) {
					outputValue("Invalid Book Id");
				} 
				break;
			case FINALISING:
				String Ans = inputValue("Commit loans? (Y/N): ");
				if (Ans.toUpperCase().equals("N")) {
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
