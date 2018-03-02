package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;

import com.jeecms.bbs.entity.base.BaseForumExt;

public class BbsForumExt extends BaseForumExt {
	private static final long serialVersionUID = 1L;
	
	public void init() {
		blankToNull();
	}
	
	public void blankToNull() {
		if (StringUtils.isBlank(getTplForum())) {
			setTplForum(null);
		}
		if (StringUtils.isBlank(getTplTopic())) {
			setTplTopic(null);
		}
		if (StringUtils.isBlank(getTplMobileForum())) {
			setTplMobileForum(null);
		}
		if (StringUtils.isBlank(getTplMobileTopic())) {
			setTplMobileTopic(null);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsForumExt() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsForumExt(java.lang.Integer id) {
		super(id);
	}


	/* [CONSTRUCTOR MARKER END] */

}