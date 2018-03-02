package com.jeecms.bbs.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsWebservice;


public class BbsWebservice extends BaseBbsWebservice {
	private static final long serialVersionUID = 1L;
	public static final String SERVICE_TYPE_ADD_USER = "addUser";
	public static final String SERVICE_TYPE_UPDATE_USER = "updateUser";
	public static final String SERVICE_TYPE_DELETE_USER = "deleteUser";
	
	public void addToParams(String name, String defaultValue) {
		List<BbsWebserviceParam> list = getParams();
		if (list == null) {
			list = new ArrayList<BbsWebserviceParam>();
			setParams(list);
		}
		BbsWebserviceParam param = new BbsWebserviceParam();
		param.setParamName(name);
		param.setDefaultValue(defaultValue);
		list.add(param);
	}
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getAddress())) {
			json.put("address",getAddress());
		}else{
			json.put("address", "");
		}
		if (StringUtils.isNotBlank(getTargetNamespace())) {
			json.put("targetNamespace", getTargetNamespace());
		}else{
			json.put("targetNamespace", "");
		}
		if (StringUtils.isNotBlank(getSuccessResult())) {
			json.put("successResult", getSuccessResult());
		}else{
			json.put("successResult", "");
		}
		if (StringUtils.isNotBlank(getType())) {
			json.put("type", getType());
		}else{
			json.put("type", "");
		}
		if (StringUtils.isNotBlank(getOperate())) {
			json.put("operate", getOperate());
		}else{
			json.put("operate", "");
		}
		if (getParams()!=null) {
			List<BbsWebserviceParam> params = getParams();
			String paramNames = "";
			String defaultValues="";
			for (int i = 0; i < params.size(); i++) {
				if (StringUtils.isNotBlank(params.get(i).getParamName())) {
					paramNames +=params.get(i).getParamName()+",";
				}
				if (StringUtils.isNotBlank(params.get(i).getDefaultValue())) {
					defaultValues+=params.get(i).getDefaultValue()+",";
				}
			}
			if (paramNames.length()>0) {
				paramNames=paramNames.substring(0, paramNames.length()-1);
			}
			if (defaultValues.length()>0) {
				defaultValues=defaultValues.substring(0,defaultValues.length()-1);
			}
			json.put("paramNames", paramNames);
			json.put("defaultValues", defaultValues);
		}else{
			json.put("paramNames", "");
			json.put("defaultValues", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsWebservice () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsWebservice (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsWebservice (
		java.lang.Integer id,
		java.lang.String address) {

		super (
			id,
			address);
	}

/*[CONSTRUCTOR MARKER END]*/


}