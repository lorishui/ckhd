 package me.ckhd.opengame.buyflow.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.sys.utils.UserUtils;

/**
 *买量推广工具类
 * @author wizard
 *
 */
public class MediaUtils {
	
	/**
	 * 获取我的推媒体
	 * @return
	 */
	public static List<Media> getMineMediaList(){
		List<Dict> dicts = DictUtils.getDictList("adPush_media");
		List<Media> rets = new ArrayList<Media>();
		
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		
		for(Dict dict : dicts) {
			//权限为空表示不限制，返回所有
			if( mediaPermission.isEmpty() || mediaPermission.contains(dict.getValue()) ) {
				Media media = new Media();
				media.setMediaId(dict.getValue());
				media.setMediaName(dict.getLabel());
				rets.add(media);
			}
		}
		return rets;
	}
	
	
	public static class Media {
		private String mediaId;
		private String mediaName;
		
		public String getMediaId() {
			return mediaId;
		}
		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
		public String getMediaName() {
			return mediaName;
		}
		public void setMediaName(String mediaName) {
			this.mediaName = mediaName;
		}
	}
}
