package com.sleep.updatedata.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.loopj.android.application.MyApplication;
import com.sleep.activity.MainActivity;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.runningman.CustomDialog_new;
import com.sleep.usermanager.UserRegisterActivity;
import com.sleep.usermanager.UserRootActivity;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.NetworkProber;
import com.taiir.sleepcare.home.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.Status;

public class SwitchUserActivity extends FragmentActivity {
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private TextView addBtn;

	private ListView mlistView;
	private UserManager umItem;

	private PublicDao pd;

	private SimpleAdapter adapter;
	private List<Map<String, Object>> username;
	private ArrayList<UserManager> all;
	private ImageView backiv;
	private AlertDialog alertDialog;
	public static SwitchUserActivity instance = null;

	// wanghanqing
	private boolean wait;
	private CustomDialog_new dialog_wait;
	private Timer t = new Timer();

	private int masuid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_user);
		ExitActivity.getInstance().addActivity(this);
		instance = this;
		mSharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		Intent intent = getIntent();
		masuid = intent.getIntExtra("suid", -1);
		wait = getIntent().getBooleanExtra("wait", false);
		if (wait) {
			showDialog_reconnection();
		}
		backiv = (ImageView) findViewById(R.id.imageView1_switch);
		backiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			}
		});
		if (mSharedPreferences.getBoolean("deleteself", true)) {
			backiv.setAlpha(0);
			backiv.setClickable(false);
		}
		mlistView = (ListView) findViewById(R.id.name_report_list_switch);
		addBtn = (TextView) findViewById(R.id.add_btn_switch);
		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
				if (isConnected || isConnected3g) {
					pd = new PublicDaoImpl(SwitchUserActivity.this);
					final UserNumber un = pd.findTelorEmail();
					if (un != null && un.getToken() != null) {
						new Thread() {
							@Override
							public void run() {
								try {
									Status testToken = new WebAPI().testToken(un.getToken());
									if (!Status.isSuccess(testToken)) {
										mHandler.sendEmptyMessage(3);
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
					Toast.makeText(SwitchUserActivity.this,
							SwitchUserActivity.this.getString(R.string.modify_failed_please_open_the_network),
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
				pd = new PublicDaoImpl(SwitchUserActivity.this);
				// UserManager um = sd.findAllUserByName(name);
				umItem = pd.findAllUserById(siid);
				mEditor.putBoolean("deleteself", false).commit();
				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
				if (isConnected || isConnected3g) {
					final UserNumber un = pd.findTelorEmail();
					if (un != null && un.getToken() != null) {
						new Thread() {
							@Override
							public void run() {
								try {
									Status testToken = new WebAPI().testToken(un.getToken());
									if (!Status.isSuccess(testToken)) {
										mHandler.sendEmptyMessage(3);
									} else {
										mHandler.sendEmptyMessage(5);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}.start();
					} else {
						mHandler.sendEmptyMessage(5);
					}
				} else {
					Toast.makeText(SwitchUserActivity.this,
							SwitchUserActivity.this.getString(R.string.modify_failed_please_open_the_network),
							Toast.LENGTH_SHORT).show();
				}

				// boolean isConnected =
				// NetworkProber.isWifi(MyApplication.getContext());
				// boolean isConnected3g=
				// NetworkProber.is3G(MyApplication.getContext());
				// if (isConnected||isConnected3g){
				// new Thread() {
				// public void run() {
				// new UploadUserThread(SwitchUserActivity.this).run();
				// new
				// UploadESSAndDisEaseHistoryDto(SwitchUserActivity.this).run();
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
				// SwitchUserActivity.this)
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
				// if (masuid==um.getId()) {
				// backiv.setAlpha(0);
				// backiv.setClickable(false);
				// mEditor.putBoolean("deleteself", true).commit();
				// }
				// Toast.makeText(SwitchUserActivity.this,
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
				alertDialog = new AlertDialog.Builder(SwitchUserActivity.this).create();
				alertDialog.show();
				Window window = alertDialog.getWindow();
				window.setContentView(R.layout.dialog_delete_user);
				TextView message1_delete = (TextView) window.findViewById(R.id.message1_delete);
				message1_delete.setText(SwitchUserActivity.this.getString(R.string.delete_user_sure));
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
									pd = new PublicDaoImpl(SwitchUserActivity.this);
									final UserNumber un = pd.findTelorEmail();
									if (un != null && un.getToken() != null) {
										Status testToken;
										try {
											testToken = new WebAPI().testToken(un.getToken());
											if (!Status.isSuccess(testToken)) {
												mHandler.sendEmptyMessage(3);
											} else {
												Status status = new WebAPI().deleteUser(un.getToken(), um1.getUuid());
												if (Status.isSuccess(status)) {
													pd.removeUser(um.getId());
													all.remove(um);
													username.remove(pos);
													if (masuid == um.getId()) {
														mHandler.sendEmptyMessage(2);
													}
													mHandler.sendEmptyMessage(0);
												} else {
													mHandler.sendEmptyMessage(6);
												}
											}
										} catch (IOException e) {
											e.printStackTrace();
										}
									} else {
										pd.removeUser(um.getId());
										all.remove(um);
										username.remove(pos);
										if (masuid == um.getId()) {
											mHandler.sendEmptyMessage(2);
										}
										mHandler.sendEmptyMessage(0);
										Log.d("TAG", "SwitchUserActivity-mlistView-b1-token =" + un.getToken());
									}
								};
							}.start();
						} else {
							mHandler.sendEmptyMessage(1);
							Toast.makeText(SwitchUserActivity.this,
									SwitchUserActivity.this.getString(R.string.modify_failed_please_open_the_network),
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
		getUsermanager();
	}

	// 显示用户列表
	private void getUsermanager() {
		pd = new PublicDaoImpl(SwitchUserActivity.this);
		all = (ArrayList<UserManager>) pd.findAllUsers();
		username = new ArrayList<Map<String, Object>>();
		for (UserManager userManager : all) {
			Map<String, Object> users = new HashMap<String, Object>();
			users.put("username", userManager.getUsername());
			users.put("img", R.drawable.sadduer003);// add-listview-imageview遍历
			users.put("usid", userManager.getId());
			username.add(users);
		}
		adapter = new SimpleAdapter(SwitchUserActivity.this, username,
				// "img" 得到imageview
				R.layout.userchange_adapter, new String[] { "img", "username" },
				// R.id.image_list_view 获取imageview的id
				new int[] { R.id.image_list_view, R.id.user_name_ap });
		mlistView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	// sleepreport

	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		pd = new PublicDaoImpl(SwitchUserActivity.this);
		List<UserManager> um = pd.findAllUsers();
		if (um.size() != 0 && !mSharedPreferences.getBoolean("deleteself", true)) {
			finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
		} else {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(this, SwitchUserActivity.this.getString(R.string.then_click_one_exit_procedure),
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				mEditor.putBoolean("deleteself", false).commit();
				finish();
				ExitActivity.getInstance().exit();
			}
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
				Toast.makeText(SwitchUserActivity.this,
						SwitchUserActivity.this.getString(R.string.the_user_deleted_successfully), Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				if (alertDialog != null && alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				break;
			case 2:
				backiv.setAlpha(0);
				backiv.setClickable(false);
				mEditor.putBoolean("deleteself", true).commit();
				break;
			case 3:
				if (UserRegisterActivity.instance != null) {
					UserRegisterActivity.instance.finish();
				}
				Intent intent = new Intent(SwitchUserActivity.this, UserRegisterActivity.class);
				mEditor.putInt("removeregister", 0);
				mEditor.commit();
				startActivity(intent);
				finish();
				break;
			case 4:
				pd = new PublicDaoImpl(SwitchUserActivity.this);
				List<UserManager> um = pd.findAllUsers();
				if (um.size() >= 3) {
					Toast.makeText(SwitchUserActivity.this,
							SwitchUserActivity.this.getString(R.string.the_number_of_users_is_full), Toast.LENGTH_SHORT)
							.show();
				} else {
					Intent intent1 = new Intent(SwitchUserActivity.this, UserRootActivity.class);
					mEditor.putInt("swuai", 1);
					mEditor.putInt("ucai", 2);
					mEditor.commit();
					intent1.putExtra("suid", masuid);
					startActivity(intent1);
					SwitchUserActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
				break;
			case 5:
				if (MainActivity.instance != null) {
					MainActivity.instance.finish();
				}
				Intent intent2 = new Intent(SwitchUserActivity.this, MainActivity.class);
				intent2.putExtra("cname", umItem.getUsername());
				intent2.putExtra("cid", umItem.getId());
				startActivity(intent2);
				SwitchUserActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			case 6:
				Toast.makeText(SwitchUserActivity.this, SwitchUserActivity.this.getString(R.string.delete_user_fail),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	// wanghanqing
	/**
	 * 显示重新搜索蓝牙对话框
	 * 
	 * @param v
	 */
	public void showDialog_reconnection() {
		if (dialog_wait == null) {
			dialog_wait = new CustomDialog_new(SwitchUserActivity.this,
					SwitchUserActivity.this
							.getString(R.string.waiting_for_the_bluetooth_connection_to_close_please_later),
					false, null, null, false, null, null);
		}
		if (dialog_wait != null) {
			t.schedule(new TimerTask() {
				public void run() {
					if (dialog_wait != null)
						dialog_wait.dismiss(); // when the task active then
												// close the dialog_searching
				}
			}, 5000); // after 5 second (or 5000 miliseconds), the task will be
						// active.
			dialog_wait.show();
			// mEditor.putBoolean("wait", false).commit();
		}
	}
}
