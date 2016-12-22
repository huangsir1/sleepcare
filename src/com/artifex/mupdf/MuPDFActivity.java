package com.artifex.mupdf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sleep.utils.Utils;
import com.taiir.sleepcare.home.R;

/**
 * 功能：显示pdf主界面
 */
public class MuPDFActivity extends Activity {

	private enum LinkState {
		DEFAULT, HIGHLIGHT, INHIBIT
	};

	private final int TAP_PAGE_MARGIN = 5;
	public static MuPDFCore core;
	/** 当前文件夹名字 */
	private String mFileName;
	private ReaderView mDocView;
	private View mButtonsView;
	private boolean mButtonsVisible;
	private SeekBar mPageSlider;
	private TextView mPageNumberView;

	private boolean mTopBarIsSearch;

	private AlertDialog.Builder mAlertBuilder;
	private LinkState mLinkState = LinkState.DEFAULT;

	public static String PATH;
	private int writingPageNumble = -1;
	public static String OutPdfFilePath;

	/**
	 * 获取当前的文件路径，解析pdf，并返回core
	 */
	private MuPDFCore openFile(String path) {

		PATH = path;
		int lastSlashPos = path.lastIndexOf("/");
		mFileName = new String(lastSlashPos == -1 ? path
				: path.substring(lastSlashPos + 1));
		try {
			core = new MuPDFCore(path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return core;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 隐去标题栏（要在载入布局前）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View screenView = this.getWindow().getDecorView();// 获得window中的最顶层view
		screenView.setDrawingCacheEnabled(true);// 提高绘图速度
		screenView.buildDrawingCache();
		mAlertBuilder = new AlertDialog.Builder(this);
		if (core == null) {
			core = (MuPDFCore) getLastNonConfigurationInstance();

			if (savedInstanceState != null
					&& savedInstanceState.containsKey("FileName")) {
				mFileName = savedInstanceState.getString("FileName");
			}
		}
		if (core == null) {
			Intent intent = getIntent();
			if (Intent.ACTION_VIEW.equals(intent.getAction())) {
				Uri uri = intent.getData();
				// "content://media/external/file"获取SD卡上的文件夹
				if (uri.toString().startsWith("content://media/external/file")) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { "_data" }, null, null, null);
					if (cursor.moveToFirst()) {
						uri = Uri.parse(cursor.getString(0));
					}
				}
				// 文件名为中文需要解码
				String path = intent.getStringExtra("path");
				// String path = Uri.decode(uri.getEncodedPath());
				Log.d(Utils.TAG,
						"MuPDFActivity.onCreate(), ----------------path = "
								+ path);
				Log.d(Utils.TAG,
						"MuPDFActivity.onCreate(), ----------------uri = "
								+ uri);
				core = openFile(path);
			}
			if (core != null && core.needsPassword()) {
				// requestPassword(savedInstanceState);
				return;
			}
		}
		if (core == null) {
			AlertDialog alert = mAlertBuilder.create();
			alert.setTitle(R.string.open_failed);
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "Dismiss",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alert.show();
			return;
		}

		createUI(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mFileName != null && mDocView != null) {
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}
	}

	@Override
	public void onDestroy() {
		if (core != null)
			core.onDestroy();
		core = null;
		super.onDestroy();
	}

	public void createUI(Bundle savedInstanceState) {
		if (core == null)
			return;

		mDocView = new ReaderView(this) {
			private boolean showButtonsDisabled;

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				if (e.getX() < super.getWidth() / TAP_PAGE_MARGIN) {
					super.moveToPrevious();
				} else if (e.getX() > super.getWidth() * (TAP_PAGE_MARGIN - 1)
						/ TAP_PAGE_MARGIN) {
					super.moveToNext();
				} else if (!showButtonsDisabled) {
					int linkPage = -1;
					if (mLinkState != LinkState.INHIBIT) {
						MuPDFPageView pageView = (MuPDFPageView) mDocView
								.getDisplayedView();
						if (pageView != null) {

						}
					}

					if (linkPage != -1) {
						mDocView.setDisplayedViewIndex(linkPage);
					} else {
						if (!mButtonsVisible) {
							showButtons();
						} else {
							hideButtons();
						}
					}
				}
				return super.onSingleTapUp(e);
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				if (!showButtonsDisabled)
					hideButtons();

				return super.onScroll(e1, e2, distanceX, distanceY);
			}

			@Override
			public boolean onScaleBegin(ScaleGestureDetector d) {
				showButtonsDisabled = true;
				return super.onScaleBegin(d);
			}

			@Override
			public boolean onTouchEvent(MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					showButtonsDisabled = false;
				}
				return super.onTouchEvent(event);
			}

			@Override
			protected void onChildSetup(int i, View v) {

				((PageView) v)
						.setLinkHighlighting(mLinkState == LinkState.HIGHLIGHT);
			}

			@Override
			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				mPageNumberView.setText(String.format("%d/%d", i + 1,
						core.countPages()));
				mPageSlider.setMax(core.countPages() - 1);
				mPageSlider.setProgress(i);
			}

			@Override
			protected void onSettle(View v) {

				((PageView) v).addHq();
			}

			@Override
			protected void onUnsettle(View v) {

				((PageView) v).removeHq();
			}
		};

		mDocView.setAdapter(new MuPDFPageAdapter(this, core));

		// Make the buttons overlay, and store all its
		// controls in variables
		makeButtonsView();

		// Activate the seekbar
		mPageSlider
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						mDocView.setDisplayedViewIndex(seekBar.getProgress());
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						updatePageNumView(progress);
					}
				});

		// Reenstate last state if it was recorded
		if (writingPageNumble == -1) {
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));
		} else {
			mDocView.setDisplayedViewIndex(writingPageNumble);
		}

		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(mDocView);
		layout.addView(mButtonsView);
		layout.setBackgroundResource(R.drawable.tiled_background);
		setContentView(layout);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode >= 0)
			mDocView.setDisplayedViewIndex(resultCode);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		MuPDFCore mycore = core;
		core = null;
		return mycore;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mFileName != null && mDocView != null) {
			outState.putString("FileName", mFileName);
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}

		if (!mButtonsVisible)
			outState.putBoolean("ButtonsHidden", true);

		if (mTopBarIsSearch)
			outState.putBoolean("SearchMode", true);
	}

	/**
	 * 显示按钮
	 */
	public void showButtons() {
		if (core == null)
			return;

		if (!mButtonsVisible) {
			mButtonsVisible = true;
			// Update page number text and slider
			int index = mDocView.getDisplayedViewIndex();
			updatePageNumView(index);
			mPageSlider.setMax(core.countPages() - 1);
			mPageSlider.setProgress(index);
			if (mTopBarIsSearch) {

			}

			Animation anim = new TranslateAnimation(0, 0,
					mPageSlider.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					mPageSlider.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mPageNumberView.setVisibility(View.VISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
		}
	}

	/**
	 * 隐藏按钮
	 */
	public void hideButtons() {
		if (mButtonsVisible) {
			mButtonsVisible = false;

			Animation anim = new TranslateAnimation(0, 0, 0,
					mPageSlider.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					mPageNumberView.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mPageSlider.setVisibility(View.INVISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
		}
	}

	/**
	 * 更新页面信息
	 */
	public void updatePageNumView(int index) {
		if (core == null)
			return;
		mPageNumberView.setText(String.format("%d/%d", index + 1,
				core.countPages()));
	}

	/**
	 * 控件按钮的绑定和点击事件的处理
	 */
	public void makeButtonsView() {
		mButtonsView = getLayoutInflater()
				.inflate(R.layout.read_pdf_main, null);

		mPageSlider = (SeekBar) mButtonsView.findViewById(R.id.pageSlider);
		mPageNumberView = (TextView) mButtonsView.findViewById(R.id.pageNumber);
		mPageNumberView.setVisibility(View.INVISIBLE);
		mPageSlider.setVisibility(View.INVISIBLE);

	}
}
