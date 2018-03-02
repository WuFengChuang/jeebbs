package com.jeecms.plug.live.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.jeecms.common.hibernate4.HibernateTree;
import com.jeecms.common.hibernate4.PriorityInterface;
import com.jeecms.plug.live.entity.base.BaseBbsLiveChapter;



public class BbsLiveChapter extends BaseBbsLiveChapter implements HibernateTree<Integer>,
PriorityInterface{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 获得列表用于下拉选择。
	 * 
	 * @return
	 */
	public List<BbsLiveChapter> getListForSelect() {
		return getBeanListForSelect(null);
	}

	public List<BbsLiveChapter> getBeanListForSelect(BbsLiveChapter exclude) {
		List<BbsLiveChapter> list = new ArrayList<BbsLiveChapter>((getRgt() - getLft()) / 2);
		addChildToList(list, this,exclude);
		return list;
	}
	
	/**
	 * 获得列表用于下拉选择。条件：有内容的章节。
	 * 
	 * @param topList
	 *            顶级章节
	 * @return
	 */
	public static List<BbsLiveChapter> getListForSelect(List<BbsLiveChapter> topList) {
		return getListForSelect(topList, null);
	}

	public static List<BbsLiveChapter> getListForSelect(List<BbsLiveChapter> topList,
			 BbsLiveChapter exclude) {
		List<BbsLiveChapter> list = new ArrayList<BbsLiveChapter>();
		for (BbsLiveChapter c : topList) {
			addChildToList(list, c,exclude);
		}
		return list;
	}

	/**
	 * 递归将子章节加入列表。
	 * 
	 * @param list
	 *            章节容器
	 * @param chapter
	 *            待添加的chapter章节，且递归添加子章节
	 */
	private static void addChildToList(List<BbsLiveChapter> list, BbsLiveChapter chapter,
			 BbsLiveChapter exclude) {
		list.add(chapter);
		Set<BbsLiveChapter> child = chapter.getChild();
		for (BbsLiveChapter c : child) {
			addChildToList(list, c,  exclude);
		}
	}
	
	/**
	 * 每个用户各自维护独立的树结构
	 * 
	 * @see HibernateTree#getTreeCondition()
	 */
	public String getTreeCondition() {
		return "bean.user.id=" + getUser().getId();
	}

	/**
	 * @see HibernateTree#getParentId()
	 */
	public Integer getParentId() {
		BbsLiveChapter parent = getParent();
		if (parent != null) {
			return parent.getId();
		} else {
			return null;
		}
	}
	
	/**
	 * 获得节点列表。从父节点到自身。
	 * 
	 * @return
	 */
	public List<BbsLiveChapter> getNodeList() {
		LinkedList<BbsLiveChapter> list = new LinkedList<BbsLiveChapter>();
		BbsLiveChapter node = this;
		while (node != null) {
			list.addFirst(node);
			node = node.getParent();
		}
		return list;
	}

	/**
	 * 获得节点列表ID。从父节点到自身。
	 * 
	 * @return
	 */
	public Integer[] getNodeIds() {
		List<BbsLiveChapter> channels = getNodeList();
		Integer[] ids = new Integer[channels.size()];
		int i = 0;
		for (BbsLiveChapter c : channels) {
			ids[i++] = c.getId();
		}
		return ids;
	}

	/**
	 * 获得深度
	 * 
	 * @return 第一层为0，第二层为1，以此类推。
	 */
	public int getDeep() {
		int deep = 0;
		BbsLiveChapter parent = getParent();
		while (parent != null) {
			deep++;
			parent = parent.getParent();
		}
		return deep;
	}
	
	public BbsLiveChapter getTopBbsLiveChapter() {
		BbsLiveChapter parent = getParent();
		while (parent != null) {
			if(parent.getParent()!=null){
				parent = parent.getParent();
			}else{
				break;
			}
		}
		return parent;
	}

	/**
	 * @see HibernateTree#getLftName()
	 */
	public String getLftName() {
		return DEF_LEFT_NAME;
	}

	/**
	 * @see HibernateTree#getParentName()
	 */
	public String getParentName() {
		return DEF_PARENT_NAME;
	}

	/**
	 * @see HibernateTree#getRgtName()
	 */
	public String getRgtName() {
		return DEF_RIGHT_NAME;
	}

	public static Integer[] fetchIds(Collection<BbsLiveChapter> channels) {
		if (channels == null) {
			return null;
		}
		Integer[] ids = new Integer[channels.size()];
		int i = 0;
		for (BbsLiveChapter c : channels) {
			ids[i++] = c.getId();
		}
		return ids;
	}
	
	public void init() {
		if (getPriority() == null) {
			setPriority(10);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveChapter () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveChapter (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveChapter (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		java.lang.String name,
		java.lang.String path,
		java.lang.Integer lft,
		java.lang.Integer rgt,
		java.lang.Integer priority) {

		super (
			id,
			user,
			name,
			path,
			lft,
			rgt,
			priority);
	}

/*[CONSTRUCTOR MARKER END]*/


}