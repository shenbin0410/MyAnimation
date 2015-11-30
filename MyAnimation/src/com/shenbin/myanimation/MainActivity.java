package com.shenbin.myanimation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	
	//自定义的移动的图片集合View
	private MoveImageViews miv = null;
	
	//给Imageveiw设置图片。
	private List<Integer> imgList = new ArrayList<Integer>();
	
	//还可以自己传入图片的url地址集合。
	//在MoveImageViews里面给图片设置图片的时候，给他用Universal-Image—Loader加载图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		initView();
		
	}

	private void initView() {
		//注意：miv的高度在布局里面最好能给一个确定的高度。
		miv = (MoveImageViews) this.findViewById(R.id.mymIVs);
		for (int i = 0; i < 10; i++) {
			imgList.add(R.drawable.ic_launcher);
		}
		//设置图片
		miv.setImageList(imgList);
		//设置屏幕中显示的数量,如果小于3个，则默认为7个。
		//数量最好设置为奇数个，这样效果会比较好。
		miv.setCount(5);
	}
	
	public void next(View v){
		//下一个，向左移动
		miv.next();
	}
	
	public void last(View v){
		//上一个，向右移动
		miv.last();
	}

	
}
