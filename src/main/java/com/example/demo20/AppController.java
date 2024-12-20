package com.example.demo20;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.util.Collections;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Comparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class AppController {

    @Autowired
    private MyAppUserService myAppUserService;

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
    public String tomainblog(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "filter", required = false, defaultValue = "all") String filter,
            Model model
    ) {
        List<Blog> blogList;

        switch (filter) {
            case "name":
                blogList = blogService.searchByName(keyword);
                break;
            case "date":
                blogList = blogService.searchByDate(keyword);
                break;
            case "datename":
                blogList = blogService.searchByDateAndName(keyword);
                break;
            case "text":
                blogList = blogService.searchByText(keyword);
                break;
            case "datetext":
                blogList = blogService.searchByDateAndText(keyword);
                break;
            default:
                blogList = blogService.ListAll(keyword);
                break;
        }

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
    public String saveBlog(@ModelAttribute("blog") Blog blog, MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        blog.setUser(auth.getName());
        blog.setImage(file.getBytes());
        blog.setDate(new Date());

        blogService.save(blog);
        return "redirect:/admin_blog";
    }

    @RequestMapping(value = "/saveblog/{id}", method = RequestMethod.POST)
    public String saveBlog2(@ModelAttribute("goods") Blog blog, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            Blog existing = blogService.get(blog.getId());
            existing.setName(blog.getName());
            existing.setText(blog.getText());
            existing.setDate(blog.getDate());
            existing.setUser(blog.getUser());
        }
        else {
            blog.setImage(file.getBytes());
            blogService.save(blog);
        }
        return "redirect:/";
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


    @GetMapping("/edit1/{id}")
    @ResponseBody
    public ResponseEntity<Goods> callOpen(@PathVariable(name = "id") Long id) {
        Goods goods = goodsService.get(id);
        return ResponseEntity.ok(goods);
    }

    @GetMapping("/new1")
    @ResponseBody
    public ResponseEntity<Goods> callOpen1() {
        Goods goods = new Goods();
        if (goods.getDateofshipment()==null){return ResponseEntity.ok(goods);}
        goodsService.save(goods);
        return ResponseEntity.ok(goods);
    }

    @RequestMapping(value = "/save1", method = RequestMethod.POST)
    public String saveFirmware(@ModelAttribute("goods") Goods goods) {
        goodsService.save(goods);
        return "redirect:/";
    }


    @RequestMapping("toadminpanel")
    public String toadminpanel(Model model){
        List<Myappuser> myappuserList = myAppUserService.findAll();
        model.addAttribute("myappuserList", myappuserList);
        return "adminpanel";
    }

    @PostMapping("/updateRole/{id}")
    public String updateRole(@PathVariable Long id, @RequestParam("role") String role) {
        Myappuser user = myAppUserService.get(id);
        if (user != null) {
            user.setRole(role);
            myAppUserService.save(user); // Update the user in the database
        }
        return "redirect:/toadminpanel"; // Redirect back to the admin panel
    }

    @RequestMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogById(id); // Получение поста по ID
        model.addAttribute("post", blog); // Добавление поста в модель
        return "description"; // Возврат шаблона "description.html"
    }
}