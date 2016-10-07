package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.adapter.FirstClassAdapter;
import com.hbut.internship.adapter.PositionListViewAdapter;
import com.hbut.internship.adapter.SecondClassAdapter;
import com.hbut.internship.util.FiterUtil;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.ScreenUtils;
import com.internship.model.FirstClassItem;
import com.internship.model.Position;
import com.internship.model.SecondClassItem;

public class FindInternshipSearchActivity extends BaseActivity implements
		OnClickListener {

	// 筛选
	/** 左侧一级分类的数据 */
	private List<FirstClassItem> firstList;
	/** 右侧二级分类的数据 */
	private List<SecondClassItem> secondList;
	/** 使用PopupWindow显示一级分类和二级分类 */
	private PopupWindow popupWindow;
	/** 左侧和右侧两个ListView */
	private ListView leftLV, rightLV;
	// 弹出PopupWindow时背景变暗
	private View darkView;
	// 弹出PopupWindow时，背景变暗的动画
	private Animation animIn, animOut;

	private List<Position> mList = new ArrayList<Position>();
	private List<Position> positionList;
	private PositionListViewAdapter positionListViewAdapter;

	private String[] fitername = { "企业", "职位" };

	private TextView fiternumber;
	private Button search, fiter;
	private ImageButton back;
	private Spinner spinner;
	private EditText searchEdittext;// 输入框搜索内容
	private ListView mListView;

	private String getFitername;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			positionList = (List<Position>) msg.obj;
			positionListViewAdapter = new PositionListViewAdapter(
					FindInternshipSearchActivity.this, R.layout.position_item,
					positionList);
			mListView.setAdapter(positionListViewAdapter);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findinternshipsearch);

		findView();
		FiterUtil.initData(firstList);
		initPopup();

	}

	private void findView() {
		darkView = findViewById(R.id.main_darkview);
		animIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
		animOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);
		back = (ImageButton) findViewById(R.id.imbt_findinternship_back);
		back.setOnClickListener(this);

		search = (Button) findViewById(R.id.bt_findinternship_search);
		search.setOnClickListener(this);

		searchEdittext = (EditText) findViewById(R.id.et_searchinternship);

		spinner = (Spinner) findViewById(R.id.spinner_fitername);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				FindInternshipSearchActivity.this, R.layout.fitername_item,
				R.id.tv_fitername, fitername);
		spinner.setAdapter(arrayAdapter);

		mListView = (ListView) findViewById(R.id.lv_jobfilter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FindInternshipSearchActivity.this,
						PositionInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("position", positionList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// 筛选按钮
		fiter = (Button) findViewById(R.id.bt_searchposition_fiter);
		fiter.setOnClickListener(this);
		// 筛选后的数量统计
		fiternumber = (TextView) findViewById(R.id.tv_searchresultnumber);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_findinternship_back:
			finish();
			break;
		case R.id.bt_findinternship_search:// 查找实习
			switch (spinner.getSelectedItem().toString()) {
			case "企业":
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							getFitername = searchEdittext.getText().toString();
							mList = Internet.FindIntershipWithEn(getFitername);
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.obj = mList;
									handler.sendMessage(msg);
								}
							}).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				break;
			case "职位":
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							getFitername = searchEdittext.getText().toString();
							mList = Internet.FindIntershipWithPo(getFitername);
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.obj = mList;
									handler.sendMessage(msg);
								}
							}).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				break;
			default:
				break;
			}
			break;

		case R.id.bt_searchposition_fiter:// 筛选按钮
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else {
				popupWindow
						.showAsDropDown(findViewById(R.id.bt_searchposition_fiter));
				popupWindow.setAnimationStyle(-1);
				// 背景变暗
				darkView.startAnimation(animIn);
				darkView.setVisibility(View.VISIBLE);
			}

			break;
		default:
			break;
		}
	}

	private void initPopup() {
		popupWindow = new PopupWindow(this);
		View view = LayoutInflater.from(this).inflate(R.layout.popup_layout,
				null);
		leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
		rightLV = (ListView) view.findViewById(R.id.pop_listview_right);

		popupWindow.setContentView(view);
		popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setFocusable(true);

		popupWindow.setHeight(ScreenUtils.getScreenH(this) * 2 / 3);
		popupWindow.setWidth(ScreenUtils.getScreenW(this));

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				darkView.startAnimation(animOut);
				darkView.setVisibility(View.GONE);

				leftLV.setSelection(0);
				rightLV.setSelection(0);
			}
		});

		// 为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
		// 加载一级分类
		final FirstClassAdapter firstAdapter = new FirstClassAdapter(this,
				firstList);
		leftLV.setAdapter(firstAdapter);

		// 加载左侧第一行对应右侧二级分类
		secondList = new ArrayList<SecondClassItem>();
		secondList.addAll(firstList.get(0).getSecondList());
		final SecondClassAdapter secondAdapter = new SecondClassAdapter(this,
				secondList);
		rightLV.setAdapter(secondAdapter);

		// 左侧ListView点击事件
		leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 二级数据
				List<SecondClassItem> list2 = firstList.get(position)
						.getSecondList();
				// 如果没有二级类，则直接跳转
				if (list2 == null || list2.size() == 0) {
					popupWindow.dismiss();

					int firstId = firstList.get(position).getId();
					String selectedName = firstList.get(position).getName();
					handleResult(firstId, -1, selectedName);
					return;
				}

				FirstClassAdapter adapter = (FirstClassAdapter) (parent
						.getAdapter());
				// 如果上次点击的就是这一个item，则不进行任何操作
				if (adapter.getSelectedPosition() == position) {
					return;
				}

				// 根据左侧一级分类选中情况，更新背景色
				adapter.setSelectedPosition(position);
				adapter.notifyDataSetChanged();

				// 显示右侧二级分类
				updateSecondListView(list2, secondAdapter);
			}
		});

		// 右侧ListView点击事件
		rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 关闭popupWindow，显示用户选择的分类
				popupWindow.dismiss();

				int firstPosition = firstAdapter.getSelectedPosition();
				int firstId = firstList.get(firstPosition).getId();
				int secondId = firstList.get(firstPosition).getSecondList()
						.get(position).getId();
				String selectedName = firstList.get(firstPosition)
						.getSecondList().get(position).getName();
				handleResult(firstId, secondId, selectedName);
			}
		});
	}

	// 顶部第一个标签的点击事件
	private void tab1OnClick() {

	}

	// 刷新右侧ListView
	private void updateSecondListView(List<SecondClassItem> list2,
			SecondClassAdapter secondAdapter) {
		secondList.clear();
		secondList.addAll(list2);
		secondAdapter.notifyDataSetChanged();
	}

	// 处理点击结果
	private void handleResult(int firstId, int secondId, String selectedName) {

	}
}