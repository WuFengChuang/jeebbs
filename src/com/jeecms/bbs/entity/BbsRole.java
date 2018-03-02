package com.jeecms.bbs.entity;

import java.util.Collection;
import java.util.Set;

import com.jeecms.bbs.entity.base.BaseBbsRole;


public class BbsRole extends BaseBbsRole {
	private static final long serialVersionUID = 1L;

	public static Integer[] fetchIds(Collection<BbsRole> roles) {
		if (roles == null) {
			return null;
		}
		Integer[] ids = new Integer[roles.size()];
		int i = 0;
		for (BbsRole r : roles) {
			ids[i++] = r.getId();
		}
		return ids;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsRole () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsRole (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsRole (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer priority,
		java.lang.Boolean m_super) {

		super (
			id,
			name,
			priority,
			m_super);
	}
	public void delFromUsers(BbsUser user) {
		if (user == null) {
			return;
		}
		Set<BbsUser> set = getUsers();
		if (set == null) {
			return;
		}
		set.remove(user);
	}

	/* [CONSTRUCTOR MARKER END] */

}