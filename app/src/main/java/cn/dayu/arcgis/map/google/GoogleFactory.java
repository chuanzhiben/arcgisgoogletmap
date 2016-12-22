package cn.dayu.arcgis.map.google;

import com.esri.core.geometry.Point;

/**
 * Created by chuan on 16/12/13.
 */
public class GoogleFactory {

    public static final int SRID_2000 = 4490;
    public static final int SRID_MERCATOR = 102100;
    public static final int SRID_WGS84 = 4326;
    public static String http = "http://";
    public static String GoogleType = ".google.cn/vt/lyrs=";
    public static String GoogleX = "&hl=zh-CN&gl=CN&src=app&x=";
    public static String GoogleY = "&y=";
    public static String GoogleZ = "&z=";
    public static String GoogleG = "&s=Ga";
    public static String[] subDomains = new String[]{"mt1", "mt2", "mt3"};
    public static int minLevel = 0;
    public static int maxLevel = 19;
    public static Point origin = new Point(-20037508.342787, 20037508.342787);
    public static int dpi = 96;
    public static int tileWidth = 256;
    public static int tileHeight = 256;
    public static double[] scales = new double[]{591657527.591555,
            295828763.79577702, 147914381.89788899, 73957190.948944002,
            36978595.474472001, 18489297.737236001, 9244648.8686180003,
            4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
            288895.277144, 144447.638572, 72223.819286, 36111.909643,
            18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
            1128.4971760000001};
    public static double[] resolutions = new double[]{156543.03392800014,
            78271.516963999937, 39135.758482000092, 19567.879240999919,
            9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
            1222.9924525624949, 611.49622628138, 305.748113140558,
            152.874056570411, 76.4370282850732, 38.2185141425366,
            19.1092570712683, 9.55462853563415, 4.7773142679493699,
            2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
            0.29858214164761665};

    public static String getGoogleMapType(int layerType) {
        String ltype = "";
        switch (layerType) {
            case GoogleMapLayerTypes.GOOGLE_VECTOR_MAP:
                ltype = GoogleMapLayerTypes.VECTOR_TYPE;
                break;
            case GoogleMapLayerTypes.GOOGLE_IMAGE_MAP:
                ltype = GoogleMapLayerTypes.IMAGE_TYPE;
                break;
            case GoogleMapLayerTypes.GOOGLE_TERRAIN_MAP:
                ltype = GoogleMapLayerTypes.TERRAIN_TYPE;
                break;
            case GoogleMapLayerTypes.GOOGLE_ANNOTATION_MAP:
                ltype = GoogleMapLayerTypes.ANNOTATION_TYPE;
                break;
        }
        return ltype;
    }}
