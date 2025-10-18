package view.timeline;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class TimelineUtils {

	public static double dateToPosition(LocalDate date, LocalDate min, LocalDate max, double availableWidth,
			double offset) {

		long totalDays = ChronoUnit.DAYS.between(min, max);
		if (totalDays == 0)
			totalDays = 1;
		long daysFromStart = ChronoUnit.DAYS.between(min, date);
		return offset + (daysFromStart / (double) totalDays) * availableWidth;
	}

}
