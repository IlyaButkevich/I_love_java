package com.example.demo20;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.util.Collections;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Comparator;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
public class AppController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private BlogService blogService;

    @RequestMapping("/")
    public String index(Model model, @Param("keyword") String keyword) {
        List<Goods> goodsList = goodsService.ListAll(keyword);

        model.addAttribute("goodsList", goodsList);
        model.addAttribute("keyword", keyword);
        return "index";
    }


    boolean sorted = true;
    @RequestMapping("sort")
    public  String sortHomePage(Model model, @Param("keyword") String keyword){
        List<Goods> goodsList = goodsService.ListAll(keyword);
        goodsList.sort(Comparator.comparing(Goods::getDateofarrival));

        if (sorted==false){
            goodsList.sort(Comparator.comparing(Goods::getDateofarrival).reversed());
        }

        model.addAttribute("goodsList", goodsList);
        model.addAttribute("keyword", keyword);

        sorted = !sorted;
        return "index";
    }

    @RequestMapping("/new")
    public String newGoods(Model model){
        Goods goods = new Goods();
        model.addAttribute("goods", goods);
        return "new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveGoods(@ModelAttribute("goods") Goods goods){
        goodsService.save(goods);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView editGoods(@PathVariable(name="id") Long id){
        ModelAndView mav = new ModelAndView("edit_good");
        Goods goods = goodsService.get(id);
        mav.addObject("goods", goods);
        return mav;
    }

    @PostMapping("/delete/{id}")
    public String deleteGoods(@PathVariable Long id){
        goodsService.delete(id);
        return "redirect:/";
    }

    @RequestMapping("getgraph")
    public  String showGraph(Model model, @Param("keyword") String keyword){
        List<Goods> goodsList = goodsService.ListAll(keyword);

        Map<Date, Integer> dateMap = new HashMap<>();

        for (Goods goods : goodsList) {
            Date dateGood = goods.getDateofarrival();
            dateMap.put(dateGood, dateMap.getOrDefault(dateGood, 0) + 1);
        }

        List<List<Object>> dateCountMap = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Map.Entry<Date, Integer> entry : dateMap.entrySet()) {
            List<Object> subList = new ArrayList<>();
            subList.add(sdf.format(entry.getKey()));
            subList.add(entry.getValue());
            dateCountMap.add(subList);
        }

        Collections.sort(dateCountMap, new Comparator<List<Object>>() {
            public int compare(List<Object> o1, List<Object> o2) {
                try {
                    Date date1 = sdf.parse((String) o1.get(0));
                    Date date2 = sdf.parse((String) o2.get(0));
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        model.addAttribute("chartData", dateCountMap);

        return "graph";
    }

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public String createUser(@RequestBody Myappuser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        myAppUserRepository.save(user);
    return "login";
    }

    @GetMapping("/req/login")
    public String login(){
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }

    @RequestMapping("goback")
    public String goBack(){
        return "redirect:/";
    }

    @RequestMapping("tologout")
    public String toLogout(){
        return "login";
    }

    @RequestMapping("autoblog")
    public String toBlog(){
        return "blog";
    }

    @RequestMapping("main_blog")
    public String tomainblog(Model model){
        List<Blog> blogList = blogService.ListAll(null);
        model.addAttribute("blogList", blogList);



        return "mainblog";
    }

    @RequestMapping("admin_blog")
    public String toadminblog(Model model){
        List<Blog> blogList = blogService.ListAll(null);
        model.addAttribute("blogList", blogList);

        return "adminblog";
    }

    @RequestMapping(value="/saveblog", method = RequestMethod.POST)
    public String saveBlog(@ModelAttribute("blog") Blog blog){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        blog.setUser(auth.getName());
        blog.setDate(new Date());

        blogService.save(blog);
        return "redirect:/admin_blog";
    }

    @RequestMapping("/addblog")
    public String newBlog(Model model){
        Blog blog = new Blog();
        model.addAttribute("blog", blog);
        return "addblog";
    }

    @PostMapping("/deleteblog/{id}")
    public String deleteblog(@PathVariable Long id){
        blogService.delete(id);
        return "redirect:/admin_blog";
    }

    @RequestMapping("/editblog/{id}")
    public ModelAndView editBlog(@PathVariable(name="id") Long id){
        ModelAndView mav = new ModelAndView("edit_blog");
        Blog blog = blogService.get(id);
        mav.addObject("blog", blog);
        return mav;
    }
}
