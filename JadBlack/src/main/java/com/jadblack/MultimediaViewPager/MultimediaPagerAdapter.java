
package com.jadblack.MultimediaViewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jadblack.MultimediaViewPager.MultimediaItemManager.MultimediaItem;

public class MultimediaPagerAdapter extends FragmentStatePagerAdapter{
	
	public MultimediaItemManager mManager;

	public MultimediaPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void setItemManager(MultimediaItemManager manager){
		mManager = manager;
	}
		
	public CharSequence getPageTitle(int position) {
		//MultimediaItem item = mManager.get(position);		
		return (position + 1) + "";		
	}
	
	public String getItemTitle(int position){
		MultimediaItem item = mManager.get(position);
		return item.title;
	}
	
	public Fragment getItem(int position) {
		MultimediaItem item = mManager.get(position);
		MultimediaBaseFragment f;
		
		switch(item.type){
		case MultimediaItem.Type.PICTURE:
			f = new MultimediaImageFragment();
			break;
			
		case MultimediaItem.Type.YOUTUBE:
			f = new MultimediaYoutubeFragment();
			break;
			
		case MultimediaItem.Type.PDF:
		case MultimediaItem.Type.WORD:
		case MultimediaItem.Type.EXCEL:
			f = new MultimediaFileFragment();
			break;
			
		default:
			//Should never happen
			f = new MultimediaBaseFragment();
			break;
		}
		
		f.setItem(item);
		return f;
    }

    public int getCount() {
        return mManager.size();
    }
}
