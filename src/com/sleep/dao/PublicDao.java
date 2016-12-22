package com.sleep.dao;

import java.util.List;

import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.Hospitals;
import com.sleep.local.classs.UserHistory;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;

public interface PublicDao {
	// 增加
	public boolean addUserLogin(UserNumber unumber);

	public boolean addUser(UserManager umanager);

	public boolean addDiabetesHy(DiabetesHy diabeteshy);

	public boolean addEpworth(Epworth epworth);

	public boolean addHospitals(Hospitals hospitals);

	// 修改
	public boolean modifyusermanager(UserManager umanager);

	public boolean modifyusernumber(UserNumber unumber);

	public boolean modifyDiabetesHy(DiabetesHy diabeteshy);

	public boolean modifyepworth(Epworth epworth);

	public boolean modifyhospitals(Hospitals hospitals);

	// 删除
	public boolean removeUser(int id);

	// 查集合
	public List<UserManager> findAllUsers();

	public List<DiabetesHy> findAllDiabetesHy();

	public List<Epworth> findAllEpworth();

	public List<Hospitals> findAllHospitals();

	// id查询
	public UserManager findAllUserById(int id);

	public DiabetesHy findAllDiabetesHyById(int userid);

	public Epworth findAllEpworthById(int userid);

	public Hospitals findAllHospitalsById(int hid);
	//查询tel和email
	public UserNumber findTelorEmail();
	// name查询
	public UserManager findAllUserByName(String name);

	/**
	 * 上传判定
	 */
	// 用户资料
	public List<UserManager> findAllWebUploadUser();

	// 疾病史
	public List<DiabetesHy> findAllWebUploadeDiabetesHy();

	// ESS
	public List<Epworth> findAllWebUploadeEpworth();

	/**
	 * 历史数据
	 */
	// 增加
	public boolean addHistorys(UserHistory history);

	// 修改
	public boolean modifyHistorys(UserHistory history);

	// 删除
	public boolean removeHistory(int id);
	//按历史id和用户id删除
	public boolean deleteSpecificHistory(int hid,int userid);

	// 删除最小的
	public void deletes(int userid);

	// 上传文件
	public List<UserHistory> findAllUnuploadFile();

	// 历史上传(报告)
	public List<UserHistory> findAllWebUploadUserHistory();

	// 查询未上传报告或数据文件的历史记录
	public List<UserHistory> findUnUploadUserHistory();
	//根据用户ID 查询未上传报告或数据文件的历史记录
	public List<UserHistory> findUnUploadUserHistoryByUserId(int userid);
	
	// 查询集合
	public List<UserHistory> findAllHistorys();

	// 按用户id 查询最大历史id
	public UserHistory findMaxHistoryIdByUserId(int userid);

	// 按用户id 查询最小历史id
	public UserHistory findMinHistoryIdByUserId(int userid);

	// 查询历史集合
	public List<UserHistory> findAllHistorysById(int hid);

	// 通过用户id查历史集合
	public List<UserHistory> findAllHistoryByUserId(int userid);

	/**
	 * 新增(字段)方法-附
	 */
	//按条件查询
	public UserHistory findGivenHistory(int userid,int hid);
	//清空表
	public void truncateUserLogin();
	public void truncateUserManager();
	public void truncateDiabetesHy();
	public void truncateEpworth();
	public void truncateHospitals();
	public void truncateUserHistory();
}
