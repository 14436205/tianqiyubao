package cn.zjl.fragment;

import java.util.ArrayList;

import cn.zjl.application.MyApplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class FragmentAdapter  extends  FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentData;

	int currentViewNumber;

	public FragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragmentData,MyApplication myApplication) {
		super(fm);
		this.fragmentData=fragmentData;
		currentViewNumber = myApplication.currentViewNumber;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentData.get(position);
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentData.size();
	}
	//刷新时用的
	public void setCurrentSkinNumber(int position) {
		this.currentViewNumber = position;
	}

}
