package com.jeecms.bbs.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.util.MapUtil;


@Controller
public class FrameAct {
	@RequiresPermissions("frame:config_main")
	@RequestMapping("/frame/config_main.do")
	public String configMain(ModelMap model) {
		return "frame/config_main";
	}

	@RequiresPermissions("frame:config_left")
	@RequestMapping("/frame/config_left.do")
	public String configLeft(ModelMap model) {
		return "frame/config_left";
	}

	@RequiresPermissions("frame:config_right")
	@RequestMapping("/frame/config_right.do")
	public String configRight(ModelMap model) {
		return "frame/config_right";
	}

	@RequiresPermissions("frame:user_main")
	@RequestMapping("/frame/user_main.do")
	public String userMain(ModelMap model) {
		return "frame/user_main";
	}

	@RequiresPermissions("frame:user_left")
	@RequestMapping("/frame/user_left.do")
	public String userLeft(ModelMap model) {
		return "frame/user_left";
	}

	@RequiresPermissions("frame:user_right")
	@RequestMapping("/frame/user_right.do")
	public String userRight(ModelMap model) {
		return "frame/user_right";
	}

	@RequiresPermissions("frame:category_main")
	@RequestMapping("/frame/category_main.do")
	public String generateMain(ModelMap model) {
		return "frame/category_main";
	}

	@RequiresPermissions("frame:category_left")
	@RequestMapping("/frame/category_left.do")
	public String generateLeft(ModelMap model) {
		return "frame/category_left";
	}

	@RequiresPermissions("frame:category_right")
	@RequestMapping("/frame/category_right.do")
	public String generateRight(ModelMap model) {
		return "frame/category_right";
	}

	@RequiresPermissions("frame:forum_main")
	@RequestMapping("/frame/forum_main.do")
	public String forumMain(ModelMap model) {
		return "frame/forum_main";
	}

	@RequiresPermissions("frame:forum_left")
	@RequestMapping("/frame/forum_left.do")
	public String forumLeft(ModelMap model) {
		return "frame/forum_left";
	}

	@RequiresPermissions("frame:forum_right")
	@RequestMapping("/frame/forum_right.do")
	public String forumRight(ModelMap model) {
		return "frame/forum_right";
	}

	@RequiresPermissions("frame:template_main")
	@RequestMapping("/frame/template_main.do")
	public String templateMain(ModelMap model) {
		return "frame/template_main";
	}

	@RequiresPermissions("frame:resource_main")
	@RequestMapping("/frame/resource_main.do")
	public String resourceMain(ModelMap model) {
		return "frame/resource_main";
	}
	
	@RequiresPermissions("frame:maintain_main")
	@RequestMapping("/frame/maintain_main.do")
	public String maintainMain(ModelMap model){
		return "frame/maintain_main";
	}
	
	@RequiresPermissions("frame:maintain_left")
	@RequestMapping("/frame/maintain_left.do")
	public String maintainLeft(ModelMap model){
		return "frame/maintain_left";
	}
	
	@RequiresPermissions("frame:maintain_right")
	@RequestMapping("/frame/maintain_right.do")
	public String maintainRight(ModelMap model){
		return "frame/maintain_right";
	}
	
	@RequiresPermissions("frame:extend_main")
	@RequestMapping("/frame/extend_main.do")
	public String extendMain(ModelMap model) {
		return "frame/extend_main";
	}
	
	@RequiresPermissions("frame:extend_left")
	@RequestMapping("/frame/extend_left.do")
	public String extendLeft(ModelMap model){
		if(getMenuUrls()==null){
			Map<String,String>menus=getMenus();
			Map<String,String>tops=getTops();
			Map<String,Map<String,String>>menuNames=new HashMap<String,Map<String,String>>();
			Map<String,String>menuUrls=new HashMap<String,String>();
			Map<String,String>menuPerms=new HashMap<String,String>();
			Iterator<String>it=tops.keySet().iterator();
			while(it.hasNext()){
				String priority=it.next();
				Set<String> menuKeySet=menus.keySet();
				List<String> menuKeyList=new ArrayList<String>();
				menuKeyList.addAll(menuKeySet);
				Collections.sort(menuKeyList);
				Map<String,String> menuNameM=new HashMap<String,String>();
				for(String m:menuKeyList){
					if(m.startsWith(priority+".")){
						String str=menus.get(m);
						String[]array=str.split(";");
						menuNameM.put(m,array[0]);
						if(StringUtils.isNotBlank(array[1])&&array[1].contains("http://")){
							menuUrls.put(m,array[1]);
						}else{
							menuUrls.put(m,"../"+array[1]);
						}
						menuPerms.put(m,array[2]);
					}
					menuNames.put(priority,  MapUtil.sortMapByKey(menuNameM));
				}
			}
			setMenuNames(menuNames);
			setMenuUrls(menuUrls);
			setMenuPerms(menuPerms);
			setTops(MapUtil.sortMapByKey(tops));
		}
		model.addAttribute("menuNames", getMenuNames());
		model.addAttribute("menuUrls", getMenuUrls());
		model.addAttribute("menuPerms", getMenuPerms());
		model.addAttribute("tops", getTops());
		return "frame/extend_left";
	}
	
	@RequiresPermissions("frame:extend_right")
	@RequestMapping("/frame/extend_right.do")
	public String extendRight(){
		return "frame/expand_right";
	}
	private Map<String,String> menus;
	private Map<String,String> tops;
	private Map<String,String>menuUrls;
	private Map<String,String>menuPerms;
	private Map<String,Map<String,String>>menuNames;
	public Map<String, String> getMenus() {
		return menus;
	}

	public void setMenus(Map<String, String> menus) {
		this.menus = menus;
	}

	public Map<String, String> getTops() {
		return tops;
	}

	public void setTops(Map<String, String> tops) {
		this.tops = tops;
	}

	public Map<String, String> getMenuUrls() {
		return menuUrls;
	}

	public void setMenuUrls(Map<String, String> menuUrls) {
		this.menuUrls = menuUrls;
	}

	public Map<String, String> getMenuPerms() {
		return menuPerms;
	}

	public void setMenuPerms(Map<String, String> menuPerms) {
		this.menuPerms = menuPerms;
	}

	public Map<String, Map<String, String>> getMenuNames() {
		return menuNames;
	}

	public void setMenuNames(Map<String, Map<String, String>> menuNames) {
		this.menuNames = menuNames;
	}

}
