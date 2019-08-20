import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class loan implements Serializable {
	
	public static enum LoanState { CURRENT, OVER_DUE, DISCHARGED };
	
	private int id;
	private book book;
	private member member;
	private Date date;
	private LoanState state;

	
	public loan(int loanId, book book, member member, Date dueDate) {
		this.id = loanId;
		this.book = book;
		this.member = member;
		this.date = dueDate;
		this.state = LoanState.CURRENT;
	}

	
	public void checkOverDue() {
		if (state == LoanState.CURRENT &&
			Calendar.getInstance().Date().after(date)) {
			this.state = LoanState.OVER_DUE;			
		}
	}

	
	public boolean overDue() {
		return state == LoanState.OVER_DUE;
	}

	
	public int id() {
		return id;
	}


	public Date getDueDate() {
		return date;
	}
	
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(id).append("\n")
		  .append("  Borrower ").append(member.getId()).append(" : ")
		  .append(member.Get_LastName()).append(", ").append(member.getFirstName()).append("\n")
		  .append("  Book ").append(book.ID()).append(" : " )
		  .append(book.TITLE()).append("\n")
		  .append("  DueDate: ").append(sdf.format(D)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


	public member member() {
		return member;
	}


	public book book() {
		return book;
	}


	public void discharge() {
		state = LoanState.DISCHARGED;		
	}

}
