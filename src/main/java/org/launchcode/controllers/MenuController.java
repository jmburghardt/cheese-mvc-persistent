package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());
        return("menu/index");
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("menu", new Menu());

        model.addAttribute("title", "Menus");
        return ("menu/add");
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu menu,
                                       Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

//Am i getting the Id correct?????
    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId){
        model.addAttribute("menu", menuDao.findOne(menuId));
        model.addAttribute("title", menuDao.findOne(menuId).getName());
        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){
        AddMenuItemsForm newItem = new AddMenuItemsForm(menuDao.findOne(menuId), cheeseDao.findAll());
        model.addAttribute("form", newItem);
        model.addAttribute("title", "Add item to menu: " + menuDao.findOne(menuId).getName());
        model.addAttribute("cheeses", cheeseDao.findAll());
        return"menu/add-item";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.POST)
    public String processAddMenuForm(@PathVariable int menuId,
                                     @ModelAttribute @Valid AddMenuItemsForm addMenuItemsForm,
                                     @RequestParam int cheeseId,
                                     @RequestParam int menuId1,
                                     Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu: " + menuDao.findOne(menuId1).getName());
            return "menu/add";
        }
        Cheese newCheese = cheeseDao.findOne(cheeseId);
        Menu newMenu = menuDao.findOne(menuId1);
        menuDao.save(newMenu);
        return "redirect:" + newMenu.getId();
    }
}
