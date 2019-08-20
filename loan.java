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
		if (state == LOAN_STATE.CURRENT &&
			Calendar.INSTANCE().Date().after(D)) {
			this.state = LOAN_STATE.OVER_DUE;			
		}
	}

	
	public boolean OVer_Due() {
		return state == LOAN_STATE.OVER_DUE;
	}

	
	public Integer ID() {
		return ID;
	}


	public Date Get_Due_Date() {
		return D;
	}
	
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(ID).append("\n")
		  .append("  Borrower ").append(M.GeT_ID()).append(" : ")
		  .append(M.Get_LastName()).append(", ").append(M.Get_FirstName()).append("\n")
		  .append("  Book ").append(B.ID()).append(" : " )
		  .append(B.TITLE()).append("\n")
		  .append("  DueDate: ").append(sdf.format(D)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


	public member Member() {
		return M;
	}


	public book Book() {
		return B;
	}


	public void DiScHaRgE() {
		state = LOAN_STATE.DISCHARGED;		
	}

}
