package ApplicationMain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class DateForm {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;

	public DateForm() {

	}

	public DateForm(final Date startDate, final Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "DateForm [startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
