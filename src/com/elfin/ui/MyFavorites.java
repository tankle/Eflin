package com.elfin.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

import weibo4j.Favorite;
import weibo4j.model.Favorites;
import weibo4j.model.FavoritesTag;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;
import weibo4j.util.Log;

import com.elfin.ui.OneStatus.StatusType;

/**
*收藏
*#author Jack_Tan
*/
public class MyFavorites extends WeiboPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4115623666915286133L;

	private static final String ALL_FAVORITES = "全部收藏";
	private Favorite fv;		//用户的收藏列表
	private FavoritesTag favoritesTag;//用于收藏时
	private List<FavoritesTag> favoritesTags;	//评论标签
	private JComboBox comboBox;
	
	public MyFavorites() {
		super();
		
		initComboBox();
		init();
	}

	@Override
	public void run() {
		setLoading(true);
		if(page == 1){
			add(comboBox, gbc);
			validate();
			++gbc.gridy;
		}
		List<Favorites> favors = null;
		try {
			if(favoritesTag == null || 
					ALL_FAVORITES.equals(favoritesTag.getTag())){
				favors = fv.getFavorites(new Paging(page));
			}else {
				favors = fv.getFavoritesByTags(favoritesTag.getId(), new Paging(page));
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		for (Favorites f : favors) {
			f.getStatus().setFavorited(true);
			if(null == f.getStatus().getUser()){
				Log.logInfo("the user is null " + f.toString());
				continue;
			}
			OneStatus oneStatus = new OneStatus(f, StatusType.FAVORITE);
			add(oneStatus, gbc);
			++gbc.gridy;
			getParent().validate();
		}
		++page;
		setLoading(false);
	}
	
	/**
	 * 初始化comobox
	 * 
	 * 首先获取用户的所有标签List
	 * 然后以此来初始化combox
	 */
	private void initComboBox(){
		try {
			if(fv == null){
				fv = new Favorite();
			}
			favoritesTags = fv.getFavoritesTags();
			favoritesTags.add(0, new FavoritesTag(ALL_FAVORITES));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		Vector<String> v  =  new Vector<String>();
		for(FavoritesTag tag : favoritesTags){
			v.add(tag.getTag());
		}
		
		comboBox = new JComboBox(v);
		
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					removeAll();
					int index = comboBox.getSelectedIndex();
					favoritesTag = favoritesTags.get(index);
					page = 1;
					gbc.gridy = 0;
//					setLoading(true);
					addList();
				}
			}
		});
	}
}
