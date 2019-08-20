import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
	
	private static Calendar self;
	private static java.util.Calendar calander;
	
	
	private Calendar() {
		calander = java.util.Calendar.getInstance();
	}
	
	public static Calendar INSTANCE() {
		if (self == null) {
			self = new Calendar();
		}
		return self;
	}
	
	public void incrementDate(int days) {
		calander.add(java.util.Calendar.DATE, days);		
	}
	
	public synchronized void Set_dATE(Date date) {
		try {
			calander.setTime(date);
	        calander.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calander.set(java.util.Calendar.MINUTE, 0);  
	        calander.set(java.util.Calendar.SECOND, 0);  
	        calander.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	public synchronized Date date() {
		try {
	        calander.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calander.set(java.util.Calendar.MINUTE, 0);  
	        calander.set(java.util.Calendar.SECOND, 0);  
	        calander.set(java.util.Calendar.MILLISECOND, 0);
			return calander.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public synchronized Date dueDate(int loanPeriod) {
		Date now = Date();
		calander.add(java.util.Calendar.DATE, loanPeriod);
		Date dueDate = calander.getTime();
		calander.setTime(now);
		return dueDate;
	}
	
	public synchronized long getDaysDifference(Date targetDate) {
		
		long diffMillis = Date().getTime() - targetDate.getTime();
	    long diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
	    return diffDays;
	}

}
