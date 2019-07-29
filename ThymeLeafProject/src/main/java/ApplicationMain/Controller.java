package ApplicationMain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {

	@Value("${error.message}")
	private String errorMessage;

	@Value("${error.startDateMessage}")
	private String startDateMessage;

	@Value("${error.endDateMessage}")
	private String endDateMessage;

	@InitBinder
	public void initDateBinder(final WebDataBinder dataBinder, final Locale locale) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(value = { "", "/", "/dateForm" }, method = RequestMethod.GET)
	public ModelAndView getDateForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("dateForm", new DateForm());
		mav.setViewName("dateForm");
		return mav;
	}

	@RequestMapping(value = { "/dateForm" }, method = RequestMethod.POST)
	public ModelAndView postDateForm(@ModelAttribute("dateForm") DateForm dateForm, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Date startDate = dateForm.getStartDate();
		Date endDate = dateForm.getEndDate();

		if (startDate != null && endDate != null && startDate.before(endDate)) {
			long diff = endDate.getTime() - startDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			model.addAttribute("daysDiff", diffDays);
			modelAndView.setViewName("result");
			modelAndView.addObject("dateForm", dateForm);
			return modelAndView;
		}
		if (startDate == null) {
			model.addAttribute("errorMessage", startDateMessage);
		} else if (endDate == null) {
			model.addAttribute("errorMessage", endDateMessage);
		} else {
			model.addAttribute("errorMessage", errorMessage);
		}
		modelAndView.setViewName("dateForm");
		modelAndView.addObject("dateForm", dateForm);
		return modelAndView;
	}
}
