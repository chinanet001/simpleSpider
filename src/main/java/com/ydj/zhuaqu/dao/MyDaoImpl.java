/** **/
package com.ydj.zhuaqu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import com.ydj.common.JSONPropertyRowMapper;
import com.ydj.common.MultiDataSourceDaoSupport;
import com.ydj.common.kit.Toolbox;
import com.ydj.zhuaqu.ali1688.ProvinceCity;
import com.ydj.zhuaqu.ali1688.ui.Constant;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午09:19:04
 * @version : 1.0
 * @description :
 *
 */
public class MyDaoImpl extends MultiDataSourceDaoSupport implements MyDao {


	@Override
	public int save(int typeOf,String company,String storeURL,String mainProduct,String address,String bussModel,String contact,String tel,String iuCode) {
		
		long now = System.currentTimeMillis();
		
		long updateTime = 0;
		
		if(tel != null && !"".equals(tel)){
			updateTime = now;
		}
		
		int count = this.getJdbcTemplate().queryForObject("SELECT COUNT(id) FROM info_1688 WHERE storeURL=?",new Object[] {storeURL},Integer.class);
		
		if(count > 0){//此前未建立唯一索引  不知道不同分类中会抓到相同的店铺
			return -1;
		}
		
		
		
		
		
		/***********************临时添加修改************************/
		
		String province = "";
		String city = "";
		String mobilePhone = "";
		int isNotMobile = 0 ;
		
		if(Toolbox.isNotEmpty(address)){
			List<String> cityList = new ArrayList<String>();
			
			for(String s : ProvinceCity.getProvinceList()){
				if(address.contains(s)){
					province = s;
					cityList = ProvinceCity.getProvinceCity().get(s);
					break;
				}
			}
			
			for(String s : cityList){
				if(address.contains(s)){
					city = s;
					break;
				}
			}
		}
		
		
		/***********************************************/
		
		
		
		
		return this.getJdbcTemplate()
        .update("INSERT INTO info_1688(typeOf,company,storeURL,mainProduct,address,bussModel,contact,tel,createTime,updateTime,spider,iuCode,province,city,mobilePhone,isNotMobile) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
            new Object[] { typeOf,company,storeURL,mainProduct,address,bussModel,contact,tel,now,updateTime,Constant.currentUser,iuCode,province,city,mobilePhone,isNotMobile});
	
	
	}

	
	
	@Override
	public List<JSONObject> getList()throws Exception {
		 return this.getJdbcTemplate().query("select id,storeURL from info_1688 where updateTime=0 and updateCount<3 ORDER BY RAND() limit 20",new JSONPropertyRowMapper());
	}

	
	@Override
	public List<JSONObject> getListByLimitPage(int currentId)throws Exception {
		 return this.getJdbcTemplate().query("select * from info_1688 where id > ? AND tel<>'' AND province='' AND isNotMobile=0  ORDER BY id  limit 200",new Object[]{currentId},new JSONPropertyRowMapper());
	}
	
	
	@Override
	public int update(int id,String contact, String city,String province,String mobilePhone) {
		
		if(Toolbox.isMobile(mobilePhone)){
			this.getJdbcTemplate()
	        .update("update info_1688 set contact=?,city=?,province=?,mobilePhone=? where id = ?",
	            new Object[] { contact,city,province,mobilePhone,id});
		}else{
			this.getJdbcTemplate()
	        .update("update info_1688 set contact=?,city=?,province=?,isNotMobile=-1 where id = ?",
	            new Object[] { contact,city,province,id});
		}
		
		return 1;
	}


	@Override
	public int update(int id,String contact, String tel) {
		
		long now = System.currentTimeMillis();
		
		long updateTime = 0;
		String mobilePhone = "";
		int isNotMobile = 0 ;
		
		if(Toolbox.isNotEmpty(tel)){
			updateTime = now;
			
			Set<String> tels = Toolbox.cleanTelInfo(tel);
			
			if(tels.isEmpty()){
				mobilePhone = "";
				isNotMobile = -1;
			}
			
			int ii = 0;
//			String otherPhone1st = "";
			for(String s : tels){
				if(ii == 0){
					mobilePhone = s;
				}
//				if(ii == 1){
//					otherPhone1st = s;
//				}
				ii++;
			}
			
		}
		
		
		this.getJdbcTemplate()
        .update("update info_1688 set contact=?,tel=?,updateTime=?,updateCount=updateCount+1,spider=?,mobilePhone=?,isNotMobile=? where id = ?",
            new Object[] { contact,tel,updateTime,Constant.currentUser,mobilePhone,isNotMobile,id});
		
		this.getJdbcTemplate()
        .update("update user_info set updateTime=?,zhuaquCount=zhuaquCount+1 where userName = ?",
            new Object[] { now,Constant.currentUser});
		
		return 1;
	}



	@Override
	public int getAllCount() {
		return this.getJdbcTemplate().queryForObject("SELECT COUNT(id) FROM info_1688 WHERE tel<>''",Integer.class);
	}



	@Override
	public List<JSONObject> getUserInfoList() throws Exception {
		return this.getJdbcTemplate().query("select * from user_info",new JSONPropertyRowMapper());
	}



	@Override
	public int updateUserConfig(String userAgent, String cookie) {
		
		String savePath = Constant.savePath;
		
		if(savePath.contains("/")){
			savePath = savePath.substring(0, savePath.indexOf("/"));
		}
		
		this.getJdbcTemplate().update("update user_info set userAgent=?,cookie=?,savePath=? where userName = ?",new Object[] { userAgent,cookie,savePath,Constant.currentUser});
		return 0;
	}



	@Override
	public int saveStore(String keyword, String beginURL, String iuCode,String iuName) {
		long now = System.currentTimeMillis();
		this.getJdbcTemplate()
        .update("INSERT INTO store_1688(beginURL,keyword,iuCode,iuName,createTime,creater) VALUES(?,?,?,?,?,?)",
            new Object[] { beginURL,keyword,iuCode,iuName,now,Constant.currentUser});
		return 0;
	}



	@Override
	public JSONObject getStore(String beginURL) {
		try {
			return this.getJdbcTemplate().queryForObject("select * from store_1688 where beginURL=?",new Object[] {beginURL}, new JSONPropertyRowMapper() );
		} catch (Exception e) {
		}
		return null;
	}



	@Override
	public int updateStore(String beginURL, int spiderCount) {
		return this.getJdbcTemplate().update("update store_1688 set spiderCount=?,spiderEndTime=? where beginURL=?",new Object[] {spiderCount,System.currentTimeMillis(),beginURL});
	}



	@Override
	public List<JSONObject> getUserSpiderInfoReport() throws Exception {
		return this.getJdbcTemplate().query("SELECT spider  AS 'spider',COUNT(1) AS 'sum',MAX(updateTime) AS 'updateTime' FROM info_1688 WHERE  tel<>'' GROUP BY spider ORDER BY COUNT(1) DESC ",new JSONPropertyRowMapper());
	}



	@Override
	public List<JSONObject> getCategorySpiderReport() throws Exception {
		return this.getJdbcTemplate().query("SELECT (SELECT keyword FROM store_1688 WHERE id= info.typeOf) AS 'category',COUNT(1) AS 'sum',MAX(updateTime) AS 'updateTime' FROM info_1688 info WHERE  tel<>'' GROUP BY typeof ORDER BY COUNT(1) DESC ",new JSONPropertyRowMapper());
	}



	@Override
	public List<JSONObject> getReport() throws Exception {
		return this.getJdbcTemplate().query("SELECT COUNT(1) AS '总信息数',COUNT(IF(tel<>'',TRUE,NULL)) AS '总有效数',COUNT(IF(tel='',(if(updateCount>=3,true,null)),NULL)) AS '3次都未抓取成功数' FROM info_1688 ",new JSONPropertyRowMapper());
	}



	@Override
	public List<JSONObject> getMyCategoryReport() throws Exception {
		return this.getJdbcTemplate().query("SELECT iuCode  AS '行业名称',COUNT(1) AS '总数',COUNT(IF(tel<>'',TRUE,NULL)) AS '有效数',COUNT(IF(tel='',(IF(updateCount>=3,TRUE,NULL)),NULL)) AS '3次都未抓取成功数',MAX(updateTime) AS '最后抓取时间' FROM info_1688 GROUP BY iuCode ORDER BY COUNT(1) DESC ",new JSONPropertyRowMapper());
	}



	@Override
	public int updateZero() {
		return this.getJdbcTemplate().update("UPDATE info_1688 SET updateCount=0,updateTime=0 WHERE tel='' AND updateCount>=3 AND isNotMobile=0");
	}



	@Override
	public List<JSONObject> getHuiCongDataListByLimitPage(int currentId)
			throws Exception {
		 return this.getJdbcTemplate().query("select * from huicongshop where id > ? ORDER BY id  limit 500",new Object[]{currentId},new JSONPropertyRowMapper());
	}



	@Override
	public int insertHuiCongData(String chineseName, String company,
			String companyAddress, String province, String city,
			String mobilePhone, String otherPhone1st, String otherPhone2nd,
			String otherPhone3rd, String personIUCode) throws Exception {
		this.getJdbcTemplate()
        .update("INSERT INTO user_data_temp(typeOf,chineseName, company, companyAddress, province, city, mobilePhone, otherPhone1st, otherPhone2nd, otherPhone3rd, personIUCode) VALUES(?,?,?,?,?,?,?,?,?,?,?)",
            new Object[] {4,chineseName, company, companyAddress, province, city, mobilePhone, otherPhone1st, otherPhone2nd, otherPhone3rd, personIUCode});
		return 0;
	}
	
	
	
	
}
