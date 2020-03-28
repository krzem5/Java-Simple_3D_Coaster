package com.krzem.simple_3d_coaster - Copy;



import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.Math;



public class Constants{
	public static final int DISPLAY_ID=0;
	public static final GraphicsDevice SCREEN=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[DISPLAY_ID];
	public static final Rectangle WINDOW_SIZE=SCREEN.getDefaultConfiguration().getBounds();

	public static final int CROSSHAIR_SIZE=40;

	public static final double CAMERA_MOVE_SPEED=0.5d;
	public static final double CAMERA_ROT_SPEED=0.075d;
	public static final double CAMERA_ZOOM_SPEED=2d;
	public static final double CAMERA_ZOOM_FOV_MULT=1.75d;
	public static final double CAMERA_MIN_EASE_DIFF=0.05d;
	public static final double CAMERA_EASE_PROC=0.45d;
	public static final double CAMERA_CAM_NEAR=0.05d;
	public static final double CAMERA_CAM_FAR=1000d;

	public static final double DRAG_POINT_MAX_CAM_DIST=0.5e2;
	public static final double DRAG_POINT_BOX_SIZE=0.1d;
	public static final double DRAG_POINT_ARROW_SIZE=1.5;
	public static final double DRAG_POINT_ARROW_CONE_SIZE=0.4;
	public static final double DRAG_POINT_ARROW_CONE_RADIUS=0.1;
	public static final int DRAG_POINT_ARROW_CONE_DETAIL=20;

	public static final double ROT_POINT_MAX_CAM_DIST=0.5e2;
	public static final double ROT_POINT_RING_OUTER_RADIUS=1.5;
	public static final double ROT_POINT_RING_INNER_RADIUS=1.3;
	public static final double ROT_POINT_DRAG_CIRCLE_DIST=1.4;
	public static final double ROT_POINT_DRAG_CIRCLE_RADIUS=0.3;
	public static final int ROT_POINT_RING_DETAIL=30;
	public static final int ROT_POINT_DRAG_CIRCLE_DETAIL=15;

	public static final double GROUND_MIN_Y_POS=12;
	public static final double GROUND_MAX_Y_POS=17.5;
	public static final double GROUND_TILE_SIZE=1;
	public static final double GROUND_NOISE_DETAIL=12;
	public static final double[] GROUND_MIN_COLOR=new double[]{130/255d,98/255d,77/255d};
	public static final double[] GROUND_MAX_COLOR=new double[]{77/255d,168/255d,69/255d};
	public static final double GROUND_BUFFOR_X=25;
	public static final double GROUND_BUFFOR_Z=25;

	public static final double TRACK_PIECE_BIGGER_RADIUS=0.2;
	public static final double TRACK_PIECE_SMALLER_RADIUS=0.075;
	public static final double TRACK_PIECE_ATTACH_SMALLER_WIDTH=0.3;
	public static final double TRACK_PIECE_ATTACH_BIGGER_WIDTH=0.6;
	public static final double TRACK_PIECE_ATTACH_TOP_ROT=Math.PI/3;
	public static final double TRACK_PIECE_ATTACH_BOTTOM_ROT=Math.PI/2;
	public static final double TRACK_PIECE_TRACK_SEGMENT_LENGTH=1;
	public static final int TRACK_PIECE_LENGTH_CURVE_DETAIL=100;
	public static final int TRACK_PIECE_DETAIL=20;
	public static final int TRACK_PIECE_SMALLER_DETAIL=10;
	public static final int TRACK_PIECE_MOVE_RECT_SIZE=30;
	public static final double TRACK_PIECE_NEW_TRACK_LENGTH=10;

	public static final double BREAK_TRACK_PIECE_TARGET_SPEED=0.25;///////////////
	public static final double BREAK_TRACK_PIECE_EASING_RATIO=0.275;
	public static final double BREAK_TRACK_PIECE_BREAK_HEIGHT=0.075;
	public static final double BREAK_TRACK_PIECE_BREAK_BIGGER_SIZE_OFF=0.035;
	public static final double BREAK_TRACK_PIECE_BREAK_SMALLER_SIZE_OFF=0.11;
	public static final double BREAK_TRACK_PIECE_BREAK_BIGGER_WIDTH=0.11;
	public static final double BREAK_TRACK_PIECE_BREAK_SMALLER_WIDTH=0.02;

	public static final double LIFT_TRACK_PIECE_TARGET_SPEED=0.35;
	public static final double LIFT_TRACK_PIECE_EASING_RATIO=0.875;

	public static final int LOOP_TRACK_PIECE_LENGTH_CURVE_DETAIL=100;
	public static final double LOOP_TRACK_PIECE_NEW_TRACK_LENGTH=10;
	public static final double LOOP_TRACK_PIECE_TRACK_SEP_DIST=1.2;

	public static final double TRACK_SUPPORT_MIN_SUPPORT_LEN=1.75;
	public static final double TRACK_SUPPORT_RADIUS=0.15;
	public static final double TRACK_SUPPORT_DETAIL=20;

	public static final double UNDER_TRACK_SUPPORT_LEG_ANGLE=Math.PI/8;
	public static final double UNDER_TRACK_SUPPORT_MAX_SIDE_SUPPORT_LEN=20;
	public static final double UNDER_TRACK_SUPPORT_MIN_SIDE_SUPPORT_SPLIT_LEN=25;

	public static final double OVER_TRACK_SUPPORT_TOP_BEAM_LEN=2;

	public static final double TRAIN_CAMERA_DIST_ABOVE_TRACK=1;
	public static final double TRAIN_GRAVITY=0.15;
	public static final double TRAIN_FRICTION=0.01;
	public static final double TRAIN_WHEEL_RADIUS=0.1;
	public static final int TRAIN_WHEEL_DETAIL=20;
}