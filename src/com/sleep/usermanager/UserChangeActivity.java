package com.sleep.usermanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loopj.android.application.MyApplication;
import com.sleep.activity.MainActivity;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.utils.CheckNotUploadFiles;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.NetworkProber;
import com.taiir.sleepcare.home.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.Status;

public class UserChangeActivity extends Activity {
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private TextView addBtn;

	private ListView mlistView;
	private UserManager umItem;

	private PublicDao pd;
	public static UserChangeActivity instance = null;

	private SimpleAdapter adapter;
	private List<Map<String, Object>> username;
	private ArrayList<UserManager> all;
	private AlertDialog alertDialog;

	@SuppressLint("CommitPrefEdits")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_change);
		ExitActivity.getInstance().addActivity(this);
		instance = this;
		mSharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mlistView = (ListView) findViewById(R.id.name_report_list_change);
		addBtn = (TextView) findViewById(R.id.add_btn_change);
		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
				if (isConnected || isConnected3g) {
					pd = new PublicDaoImpl(UserChangeActivity.this);
					final UserNumber un = pd.findTelorEmail();
					if (un != null && un.getToken() != null) {
						new Thread() {
							@Override
							public void run() {
								try {
									Status testToken = new WebAPI().testToken(un.getToken());
									if (!Status.isSuccess(testToken)) {
										mHandler.sendEmptyMessage(2);
									} else {
										mHandler.sendEmptyMessage(4);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}.start();
					} else {
						mHandler.sendEmptyMessage(4);
					}
				} else {
					Toast.makeText(UserChangeActivity.this,
							UserChangeActivity.this.getString(R.string.modify_failed_please_open_the_network),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		/**
		 * mlistview监听 (列表项item)
		 */
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// String name = (String)
				// username.get(position).get("username");
				int siid = (Integer) username.get(position).get("usid");
				pd = new PublicDaoImpl(UserChangeActivity.this);
				// UserManager um = sd.findAllUserByName(name);
				umItem = pd.findAllUserById(siid);
				mEditor.putBoolean("deleteself", false).commit();
				final UserNumber un = pd.findTelorEmail();
				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
				if (isConnected || isConnected3g) {

					if (un != null && un.getToken() != null) {
						new Thread() {
							@Override
							public void run() {
								try {
									Status testToken = new WebAPI().testToken(un.getToken());
									if (!Status.isSuccess(testToken)) {
										mHandler.sendEmptyMessage(2);
									} else {
										mHandler.sendEmptyMessage(3);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}.start();
					} else {
						mHandler.sendEmptyMessage(3);
					}
				} else {
					Toast.makeText(UserChangeActivity.this,
							UserChangeActivity.this.getString(R.string.modify_failed_please_open_the_network),
							Toast.LENGTH_SHORT).show();
				}

				// boolean isConnected =
				// NetworkProber.isWifi(MyApplication.getContext());
				// boolean isConnected3g =
				// NetworkProber.is3G(MyApplication.getContext());
				//
				// if (isConnected || isConnected3g) {
				// new Thread() {
				// public void run() {
				// new UploadUserThread(UserChangeActivity.this).run();
				// new
				// UploadESSAndDisEaseHistoryDto(UserChangeActivity.this).run();
				// };
				// }.start();
				// }
			}
		});
		// 添加长按点击事件
		mlistView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final UserManager um = all.get(position);
				final int pos = position;
				// AlertDialog dialog = new AlertDialog.Builder(
				// UserChangeActivity.this)
				// .setTitle(R.string.delete_user_title)
				// .setMessage(R.string.delete_user_really)
				// .setPositiveButton("确定",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// // 删除一条记录
				// pd.removeUser(um.getId());
				// all.remove(um);
				// username.remove(pos);
				// adapter.notifyDataSetChanged();
				// Toast.makeText(UserChangeActivity.this,
				// "该用户删除成功！", Toast.LENGTH_SHORT)
				// .show();
				// dialog.dismiss();
				// }
				// })
				// .setNegativeButton(R.string.delete_user_cancle,
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// dialog.cancel();
				// }
				// }).create();
				// dialog.show();
				alertDialog = new AlertDialog.Builder(UserChangeActivity.this).create();
				alertDialog.show();
				Window window = alertDialog.getWindow();
				window.setContentView(R.layout.dialog_delete_user);
				TextView message1_delete = (TextView) window.findViewById(R.id.message1_delete);
				message1_delete.setText(UserChangeActivity.this.getString(R.string.delete_user_sure));
				Button b1 = (Button) window.findViewById(R.id.positive_delete);
				Button b2 = (Button) window.findViewById(R.id.close_delete);
				b1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
						boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
						final UserManager um1 = pd.findAllUserById(um.getId());
						if (isConnected || isConnected3g) {
							new Thread() {
								public void run() {
									pd = new PublicDaoImpl(UserChangeActivity.this);
									final UserNumber un = pd.findTelorEmail();
									if (un != null && un.getToken() != null) {
										try {
											Status testToken = new WebAPI().testToken(un.getToken());
											if (!Status.isSuccess(testToken)) {
												mHandler.sendEmptyMessage(2);
											} else {
												Status status = new WebAPI().deleteUser(un.getToken(), um1.getUuid());
												if (Status.isSuccess(status)) {
													// 删除一条记录
													pd.removeUser(um.getId());
													all.remove(um);
													username.remove(pos);
													mHandler.sendEmptyMessage(0);
												} else {
													mHandler.sendEmptyMessage(5);
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										// 删除一条记录
										pd.removeUser(um.getId());
										all.remove(um);
										username.remove(pos);
										mHandler.sendEmptyMessage(0);
										Log.d("TAG", "UserChangeActivity-mlistView-b1-token =" + un.getToken());
									}
								};
							}.start();
						} else {
							mHandler.sendEmptyMessage(1);
							Toast.makeText(UserChangeActivity.this,
									UserChangeActivity.this.getString(R.string.modify_failed_please_open_the_network),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				b2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.cancel();
					}
				});
				return true;// 返回true，可以解决点击和长按的手势冲突
			}
		});
		// 登录状态
		// getInfo();
		// 查看用户列表
		getUsermanager();
		// adapter.notifyDataSetChanged();

		// 2016.7.25
//		CheckNotUploadFiles.getInstance().ExecutionThread(getApplicationContext(),
//				new PublicDaoImpl(getApplicationContext()).findUnUploadUserHistory());
	}

	// 登录状态及引导页
	// private void getInfo() {
	// mEditor.putInt("firstInstalregister", 1);
	// mEditor.commit();
	// }

	// 显示用户列表
	private void getUsermanager() {
		pd = new PublicDaoImpl(UserChangeActivity.this);
		all = (ArrayList<UserManager>) pd.findAllUsers();
		username = new ArrayList<Map<String, Object>>();
		for (UserManager userManager : all) {
			Map<String, Object> users = new HashMap<String, Object>();
			users.put("username", userManager.getUsername());
			users.put("img", R.drawable.sadduer003);// add-listview-imageview遍历
			users.put("usid", userManager.getId());
			username.add(users);
		}
		adapter = new SimpleAdapter(UserChangeActivity.this, username,
				// "img" 得到imageview
				R.layout.userchange_adapter, new String[] { "img", "username" },
				// R.id.image_list_view 获取imageview的id
				new int[] { R.id.image_list_view, R.id.user_name_ap });
		mlistView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - exitTime > 2000) {
			Toast.makeText(this, UserChangeActivity.this.getString(R.string.then_click_one_exit_procedure),
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			ExitActivity.getInstance().exit();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				if (alertDialog != null && alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				Toast.makeText(UserChangeActivity.this,
						UserChangeActivity.this.getString(R.string.the_user_deleted_successfully), Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				if (alertDialog != null && alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				break;
			case 2:
				if (UserRegisterActivity.instance != null) {
					UserRegisterActivity.instance.finish();
				}
				Intent intent = new Intent(UserChangeActivity.this, UserRegisterActivity.class);
				mEditor.putInt("removeregister", 0);
				mEditor.commit();
				startActivity(intent);
				finish();
				break;
			case 3:
				Intent intent1 = new Intent(UserChangeActivity.this, MainActivity.class);
				intent1.putExtra("cname", umItem.getUsername());
				intent1.putExtra("cid", umItem.getId());
				startActivity(intent1);
				UserChangeActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			case 4:
				pd = new PublicDaoImpl(UserChangeActivity.this);
				List<UserManager> um = pd.findAllUsers();
				if (um.size() >= 3) {
					Toast.makeText(UserChangeActivity.this,
							UserChangeActivity.this.getString(R.string.the_number_of_users_is_full), Toast.LENGTH_SHORT)
							.show();
				} else {
					Intent intent2 = new Intent(UserChangeActivity.this, UserRootActivity.class);
					mEditor.putInt("ucai", 1);
					mEditor.putInt("swuai", 2);
					mEditor.commit();
					startActivity(intent2);
					UserChangeActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
				break;
			case 5:
				Toast.makeText(UserChangeActivity.this, UserChangeActivity.this.getString(R.string.delete_user_fail),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
}
