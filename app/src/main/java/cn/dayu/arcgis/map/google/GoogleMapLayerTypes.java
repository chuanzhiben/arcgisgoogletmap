package cn.dayu.arcgis.map.google;

/**
 * Created by chuan on 16/12/13.
 */

public interface GoogleMapLayerTypes {

    /**
     * 谷歌矢量地图服务
     */
    final int GOOGLE_VECTOR_MAP = 1;
    final String VECTOR_TYPE = "m";
    /**
     * 谷歌影像地图服务
     */
    final int GOOGLE_IMAGE_MAP = 2;
    final String IMAGE_TYPE = "y";
    /**
     * 谷歌地形地图服务
     */
    final int GOOGLE_TERRAIN_MAP = 3;
    final String TERRAIN_TYPE = "t";
    /**
     * 谷歌道路等POI地图服务
     */
    final int GOOGLE_ANNOTATION_MAP = 4;
    final String ANNOTATION_TYPE = "h";

}