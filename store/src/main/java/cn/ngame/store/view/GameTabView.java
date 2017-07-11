/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import cn.ngame.store.R;

/**
 * 自定义游戏详情窗口中，带动画效果的viewpager Tab指示器控件
 * @author flan
 * @date   2015年11月6日
 */
public class GameTabView extends RelativeLayout implements OnClickListener{
	
	//private Context context;
	private ViewPager viewPager;
	private ImageView img_cursor;
	private TextView tv_tag0,tv_tag1, tv_tag2, tv_tag3;
	
	private int currentTab = 0;
	private int currentTextColor;

	private int imgWidth;
	private int tagWidth;
	private int img_cursor_margin = 80;
	
	public GameTabView(Context context) {
		super(context,null);
	}

	public GameTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//this.context = context;

		currentTextColor = getResources().getColor(R.color.colorPrimary);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.animationTabWidget);
		currentTab = typedArray.getInt(R.styleable.animationTabWidget_current_index, 0);
		typedArray.recycle();
		
		inflate(context, R.layout.layout_game_tabview, this);
		
		img_cursor = (ImageView) this.findViewById(R.id.tag_img_cursor);

		tv_tag0 = (TextView) this.findViewById(R.id.tv_tag0);
		tv_tag1 = (TextView) this.findViewById(R.id.tv_tag1);
		tv_tag2 = (TextView) this.findViewById(R.id.tv_tag2);
		tv_tag3 = (TextView) this.findViewById(R.id.tv_tag3);


		tv_tag0.setOnClickListener(this);
		tv_tag1.setOnClickListener(this);
		tv_tag2.setOnClickListener(this);
		tv_tag3.setOnClickListener(this);


		initDefaultIndex();
	}
	
	/**
	 * 初始化 默认 指示位置
	 */
	private void initDefaultIndex(){
		
		//获取屏幕分辨率 宽度
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int screenW = dm.widthPixels;
		tagWidth = screenW / 4;
		imgWidth = screenW / 4 - img_cursor_margin;
		
		LayoutParams para = (LayoutParams) img_cursor.getLayoutParams();
		para.width = imgWidth;
		img_cursor.setLayoutParams(para);

		if (viewPager != null) {
			viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}

				@Override
				public void onPageSelected(int position) {
					currentTab = position;
					setCurrentTab(currentTab);
				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});
		} else {
			setCurrentTab(currentTab);
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {

			case R.id.tv_tag0:
				currentTab = 0;
				break;
			case R.id.tv_tag1:
				currentTab = 1;
				break;
			case R.id.tv_tag2:
				currentTab = 2;
				break;
			case R.id.tv_tag3:
				currentTab = 3;
				break;

		}
		
		if(viewPager != null){
			viewPager.setCurrentItem(currentTab);
		}
		
		setCurrentTab(currentTab);
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				currentTab = position;
				setCurrentTab(currentTab);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
	
	/**
	 * 设置当前指示器 位置
	 * @param position
	 */
	public void setCurrentTab(int position){

		int normal = getResources().getColor(R.color.font_gray);
		tv_tag0.setTextSize(14);
		tv_tag0.setTextColor(normal);
		tv_tag1.setTextSize(14);
		tv_tag1.setTextColor(normal);
		tv_tag2.setTextSize(14);
		tv_tag2.setTextColor(normal);
		tv_tag3.setTextSize(14);
		tv_tag3.setTextColor(normal);
		//tv_tag4.setTextSize(14);
		//tv_tag4.setTextColor(normal);

		switch (position) {
			case 0:
				tv_tag0.setTextSize(16);
				tv_tag0.setTextColor(currentTextColor);
				break;
			case 1:
				tv_tag1.setTextSize(16);
				tv_tag1.setTextColor(currentTextColor);
				break;
			case 2:
				tv_tag2.setTextSize(16);
				tv_tag2.setTextColor(currentTextColor);
				break;
			case 3:
				tv_tag3.setTextSize(16);
				tv_tag3.setTextColor(currentTextColor);
				break;

		}
		
		Animation animation = null;
		animation = new TranslateAnimation(tagWidth*currentTab+img_cursor_margin/2, tagWidth*position+img_cursor_margin/2, 0, 0);

		animation.setFillAfter(true);//True:图片停在动画结束位置
		animation.setDuration(1000);	//设置动画完成时间
		img_cursor.startAnimation(animation);
		
		currentTab = position;
		
	}
	
}





















