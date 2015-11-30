package com.shenbin.myanimation;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * 如果传入的是资源文件的图片，则用imgList的size去判断是否越界。
 * 如果是传入的url图片地址集合，那么就用imgListStr去判断是否越界
 * @author Administrator
 *
 */
public class MoveImageViews extends RelativeLayout {

	// 上下文对象
	private Context context;
	// 显示的ImageView第一个，既imgList的第二个元素的值,控制移动
	private int position = 0;
	// 选中ImageView的值,控制升降
	private int selectPosition = 0;
	// 按钮的高度
	private int btn_up, btn_down;
	// 上一个选中的ImageView
	private ImageView lastSeleteButton;
	// 屏幕中默认的Image的数量
	private int btnCount = 7;
	// Image的占位宽度
	private int imageHoldWidth;
	// Image的显示宽度
	private int imageShowWidth;
	// 位移宽度
	int imageMoveWidth;
	// ImageView的集合
	private List<ImageView> imageList = new ArrayList<>();

	//可以直接传入图片地址，
	private List<String> imgListStr = new ArrayList<String>();
	
	// 直接传入资源文件的图片
	private List<Integer> imgList = new ArrayList<Integer>();

	public MoveImageViews(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// pareAttrs(attrs);
	}

	//用来ImageView设置图片
	public void addImage(String url) {
		imgListStr.add(url);
	}
	
	//设置屏幕中显示的imageView的数量,设置之后就进行初始化。
	public void setCount(int count){
		
		//如果小于3个，默认为7个。
		if(count < 3 ){
			initView();
		}else{
			this.btnCount = count;
			initView();
		}
		
	}
	
	public int getCount(){
		return this.btnCount;
	}
	
	public void setImageList(List<Integer> imgList){
		this.imgList.clear();
		this.imgList.addAll(imgList);
	}

	public void initImage() {
		for (int i = 1; i < imageList.size(); i++) {
			ImageView img = imageList.get(i);
			int posi = (int) img.getTag();

			//判断是否越界
			if (posi >= 0 && posi < imgList.size()) {
				
				img.setImageResource(imgList.get(posi));
				//通过传入的图片地址，用Universal-Image—Loader给ImageView设置图片。
//				MyApplication.loader.displayImage(imgs.get(posi), img,
//						MyApplication.options);
			}
		}
	}
	
	/*** 初始化视图 */
	private void initView() {
		
		int width = getScreenWidth();
		// 各宽度设置
		imageHoldWidth = (int) width / btnCount;
		imageShowWidth = imageHoldWidth - 3;

		// 给高度制定之赋值
		btn_down = DisplayUtil.dip2px(context, 50);
		btn_up = DisplayUtil.dip2px(context, 0);
		// Button顺序添加
		for (int i = 0; i < btnCount; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.ic_launcher);
			RelativeLayout.LayoutParams params1 = new LayoutParams(
					imageShowWidth, LayoutParams.MATCH_PARENT);
			params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			params1.leftMargin = imageHoldWidth * i + 5;
			imageView.setClickable(true);
			imageView.setTag(i);
			imageView.setOnClickListener(new MyButtonClick());
			imageView.setLayoutParams(params1);
			imageList.add(imageView);
			this.addView(imageView);
			down(imageView, 1);// 初始化后立马下移
		}

		// 设置左边屏幕外的ImageView
		RelativeLayout.LayoutParams params2 = new LayoutParams(imageShowWidth,
				LayoutParams.MATCH_PARENT);
		ImageView lastImg = new ImageView(context);
		lastImg.setBackgroundResource(R.drawable.ic_launcher);
		lastImg.setTag(-1);
		lastImg.setClickable(true);
		lastImg.setOnClickListener(new MyButtonClick());
		lastImg.setLayoutParams(params2);
		imageList.add(0, lastImg);
		this.addView(lastImg);
		ObjectAnimator moveLeft = ObjectAnimator.ofFloat(lastImg, "x", 0,
				-imageHoldWidth - 10);
		moveLeft.start();
		down(lastImg, 1);

		// 设置右边屏幕外的ImageView
		ImageView nextImg = new ImageView(context);
		nextImg.setClickable(true);
		nextImg.setTag(this.btnCount);
		nextImg.setBackgroundResource(R.drawable.ic_launcher);
		nextImg.setOnClickListener(new MyButtonClick());
		nextImg.setLayoutParams(params2);
		imageList.add(nextImg);
		this.addView(nextImg);
		ObjectAnimator moveRight = ObjectAnimator.ofFloat(nextImg, "x",
				width, this.btnCount * imageHoldWidth + 10);
		moveRight.start();
		down(nextImg, 1);
		// 开启默认第一个被选中的动画
		up(imageList.get(1));
		lastSeleteButton = imageList.get(1);
	}

	/*** 上一个移动 */
	public void last() {
		if (selectPosition == 0) {
			Toast.makeText(context, "已经是第一个了", Toast.LENGTH_SHORT).show();
			return;
		}
		if (selectPosition <= 3) {
			selectPosition--;
			jump();
			return;
		}
		position--;
		imageList.get(imageList.size() - 1).setTag((position - 1));

		int posi = (int) imageList.get(imageList.size() - 1).getTag();
		if (posi >= 0 && posi < imgList.size()) {
			
			imageList.get(imageList.size() - 1).setImageResource(imgList.get(position - 1));
			
			//通过传入的图片地址，用Universal-Image—Loader给ImageView设置图片。
//			MyApplication.loader.displayImage(imgs.get(position - 1),
//					imageList.get(imageList.size() - 1), MyApplication.options);
		}

		moveLast();
		jump();

	}

	/*** 下一个移动 */
	public void next() {
		selectPosition++;
		if (selectPosition > 3) {
			position++;
			// 这里要减去添加的两个缓冲Button,否则会漏掉数据
			imageList.get(0).setTag(position + imageList.size() - 2);

			int posi = (int) imageList.get(0).getTag();
			if (posi >= 0 && posi < imgList.size()) {
				
				imageList.get(0).setImageResource(imgList.get(posi));
				//通过传入的图片地址，用Universal-Image—Loader给ImageView设置图片。
//				MyApplication.loader.displayImage(imgs.get(posi),
//						imageList.get(0), MyApplication.options);
			}
			moveNext();
		}
		jump();
	}

	/*** 具体移动特效 -- 上一个 */
	private void moveLast() {
		selectPosition--;
		ObjectAnimator moveLeft = ObjectAnimator.ofFloat(
				imageList.get(imageList.size() - 1), "x", 2 * (-imageHoldWidth),
				-imageHoldWidth - 5);
		moveLeft.setDuration(1000);
		moveLeft.start();
		for (int i = 0; i < imageList.size() - 1; i++) {
			ObjectAnimator move = ObjectAnimator.ofFloat(imageList.get(i), "x",
					(i - 1) * imageHoldWidth, i * imageHoldWidth + 5);
			move.setDuration(1000);
			move.start();
		}
		imageList.add(0, imageList.get(imageList.size() - 1));
		imageList.remove(imageList.size() - 1);
	}

	/*** 具体移动特效 -- 下一个 */
	private void moveNext() {
		ObjectAnimator moveRight = ObjectAnimator.ofFloat(imageList.get(0), "x",
				((imageList.size() - 1) * imageHoldWidth), (imageList.size() - 2)
						* imageHoldWidth + 5);
		moveRight.setDuration(1000);
		moveRight.start();
		for (int i = 1; i < imageList.size(); i++) {
			ObjectAnimator move = ObjectAnimator.ofFloat(imageList.get(i), "x",
					(i - 1) * imageHoldWidth, (i - 2) * imageHoldWidth + 3);
			move.setDuration(1000);
			move.start();
		}

		imageList.add(imageList.get(0));
		imageList.remove(0);
	}

	/*** 上下动作方法 */
	private void jump() {
		ImageView currentButton = null;
		for (ImageView btn : imageList) {
			if (selectPosition == (int) btn.getTag()) {
				currentButton = btn;
			}
		}
		up(currentButton);
		down(lastSeleteButton, 1000);
		lastSeleteButton = currentButton;
	}

	// 进行移动操作 -- up
	private void up(ImageView currentButton) {
		ObjectAnimator up = ObjectAnimator.ofFloat(currentButton, "y",
				btn_down, btn_up);
		up.setDuration(1000);
		up.start();
	}

	// 进行移动操作 -- down
	private void down(ImageView lastSeleteButton2, int time) {
		ObjectAnimator down = ObjectAnimator.ofFloat(lastSeleteButton2, "y",
				btn_up, btn_down);
		down.setDuration(time);
		down.start();
	}

	/*** 获取屏幕宽度的方法 */
	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		// float density = dm.density;// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
		// int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
		// float xdpi = dm.xdpi;
		// float ydpi = dm.ydpi;
		int screenWidth = dm.widthPixels;
//		int screenHeight = dm.heightPixels;
		return screenWidth;
	}

	/*** 点击事件 */
	private class MyButtonClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			int position = (int) v.getTag();
			Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
		}
	}
}
