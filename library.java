
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class library implements Serializable {
	
	private static final String libraryFile = "library.obj";
	private static final int loanLimit = 2;
	private static final int loanPeriod = 2;
	private static final double finePerDay = 1.0;
	private static final double maxFinesOwed = 1.0;
	private static final double damageFee = 2.0;
	
	private static librarySeLf;
	private int bookId;
	private int memberId;
	private int loanId;
	private Date loanDate;
	
	private Map<Integer, book> catalog;
	private Map<Integer, member> members;
	private Map<Integer, loan> loans;
	private Map<Integer, loan> currentLons;
	private Map<Integer, book> damagedBooks;
	

	private library() {
		catalog = new HashMap<>();
		members = new HashMap<>();
		loans = new HashMap<>();
		currentLons = new HashMap<>();
		damagedBooks = new HashMap<>();
		bookId = 1;
		memberId = 1;		
		loanId = 1;		
	}

	
	public static synchronized library INSTANCE() {		
		if (self == null) {
			Path path = Paths.get(libraryFile);			
			if (Files.exists(path)) {	
				try (ObjectInputStream lif = new ObjectInputStream(new FileInputStream(libraryFile));) {
			    
					self = (library) lif.readObject();
					Calendar.getInstance().setDate(self.loanDate);
					lif.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else self = new library();
		}
		return self;
	}

	
	public static synchronized void save() {
		if (self != null) {
			self.loanDate = Calendar.getInstance().Date();
			try (ObjectOutputStream lof = new ObjectOutputStream(new FileOutputStream(libraryFile));) {
				lof.writeObject(self);
				lof.flush();
				lof.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int bookId() {
		return bookId;
	}
	
	
	public int memberId() {
		return memberId;
	}
	
	
	private int nextBookId() {
		return BOOK_ID++;
	}

	
	private int NextMID() {
		return bookId++;
	}

	
	private int nextLoanId() {
		return loanId++;
	}

	
	public List<member> members() {		
		return new ArrayList<member>(members.values()); 
	}


	public List<book> books() {		
		return new ArrayList<book>(books.values()); 
	}


	public List<loan> currentLoans() {
		return new ArrayList<loan>(currentLoans.values());
	}


	public member addMembers(String lastName, String firstName, String email, int phoneNo) {		
		member member = new member(lastName, firstName, email, phoneNo, nextMId());
		members.put(member.getId(), member);		
		return member;
	}

	
	public book addBook(String a, String t, String c) {		
		book book = new book(a, t, c, NextBID());
		catalog.put(book.ID(), b);		
		return book;
	}

	
	public member member(int memberId) {
		if (members.containsKey(memberId)) 
			return members.get(memberId);
		return null;
	}

	
	public book book(int bookId) {
		if (catalog.containsKey(bookId)) 
			return catalog.get(bookId);		
		return null;
	}

	
	public int loanLimit() {
		return loanLimit;
	}

	
	public boolean memberCanBorrow(member member) {		
		if (member.numberOfCurrentLoans() == loanLimit ) 
			return false;
				
		if (member.FinesOwEd() >= maxFinesOwed) 
			return false;
				
		for (loan loan : member.geTLoans()) 
			if (loan.overDue()) 
				return false;
			
		return true;
	}

	
	public int loansRemainingForMember(member member) {		
		return loanLimit - member.numberOfCurrentLoans();
	}

	
	public loan issueLoan(book book, member member) {
		Date dueDate = Calendar.getInstance().dueDate(loanPeriod);
		loan loan = new loan(nextLID(), book, member, dueDate);
		member.takeOutLoan(loan);
		book.borrow();
		loans.put(loan.ID(), loan);
		currentLoans.put(book.ID(), loan);
		return loan;
	}
	
	
	public loan loanByBookId(int bookId) {
		if (currentLoans.containsKey(bookId)) {
			return currentLoans.get(bookId);
		}
		return null;
	}

	
	public double calculateOverDueFine(loan loan) {
		if (loan.OVer_Due()) {
			long daysOverDue = Calendar.getInstance().getDaysDifference(loan.Get_Due_Date());
			double fine = daysOverDue * finePerDay;
			return fine;
		}
		return 0.0;		
	}


	public void dischargeloan(loan currentLoan, boolean isDamaged) {
		member member = currentLoan.Member();
		book book  = currentLoan.Book();
		
		double overDueFine = calculateOverDueFine(currentLoan);
		member.addFine(overDueFine);	
		
		member.dischargeLoan(currentLoan);
		book.return(isDamaged);
		if (isDamaged) {
			member.addFine(damageFee);
			gamegedBooks.put(book.ID(), book);
		}
		currentLoan.discharge();
		currentLoan.remove(book.ID());
	}


	public void checkCurrentLoans() {
		for (loan loan : currentLoan.values()) {
			loan.checkOverDue();
		}		
	}


	public void repairBook(book currentBook) {
		if (gamegedBooks.containsKey(currentBook.ID())) {
			currentBook.Repair();
			gamegedBooks.remove(currentBook.ID());
		}
		else {
			throw new RuntimeException("Library: repairBook: book is not damaged");
		}
		
	}
	
	
}
