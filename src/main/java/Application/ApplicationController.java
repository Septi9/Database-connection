package Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ApplicationController {

    @Autowired
    private LocationDAO dao;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("mainPage.html");
        return "mainPage.html";
    }

    @RequestMapping("/locations")
    public String showLocation(Model model) {
        model.addAttribute("locationList", dao.list());
        return "locations.html";
    }

    @RequestMapping("/insert")
    public String addLocation(Model model) {
        Location location = new Location();
        model.addAttribute("location", location);

        return "addLocation";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@ModelAttribute("location") Location location) {
        dao.insert(location);

        return "redirect:/";
    }

    @RequestMapping("/delete/{location_id}")
    public String delete(@PathVariable(name = "location_id") int location_id) {
        dao.delete(location_id);
        return "redirect:/locations";
    }

    @RequestMapping("/update/{location_id}")
    public ModelAndView updateForm(@PathVariable(name = "location_id") int location_id) {
        ModelAndView modelAndView = new ModelAndView("updateLocation");
        Location location = dao.update(location_id);
        modelAndView.addObject("location", location);
        return modelAndView;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String update(@ModelAttribute("employee") Location location){
        dao.save(location);
        return "redirect:/";
    }

}
