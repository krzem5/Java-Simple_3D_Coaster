package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.lang.Math;



public class Ground extends Constants{
	public Main.Main_ cls;
	public Game game;
	private Vector3[][] _data;
	private Vector3 _min;
	private Vector3 _max;



	public Ground(Main.Main_ cls,Game game){
		this.cls=cls;
		this.game=game;
		this._data=null;
		this._min=new Vector3(0,0,0);
		this._max=new Vector3(0,0,0);
	}



	public void update(GL2 gl){
		Vector3[] _size=this._calc_size();
		if (!_size[0].eq(this._min)||!_size[1].eq(this._max)){
			this._min=_size[0];
			this._max=_size[1];
			// this._data=this._regenerate();
		}
	}



	public void draw(GL2 gl){
		gl.glBegin(GL2.GL_TRIANGLES);
		for (int i=0;i<this._data.length-1;i++){
			for (int j=0;j<this._data[0].length-1;j++){
				float[] c=this._color(this._data[i][j].y,this._data[i+1][j].y,this._data[i][j+1].y);
				gl.glColor3f(c[0],c[1],c[2]);
				this._data[i][j].vertex(gl);
				this._data[i+1][j].vertex(gl);
				this._data[i][j+1].vertex(gl);
				c=this._color(this._data[i+1][j].y,this._data[i][j+1].y,this._data[i+1][j+1].y);
				gl.glColor3f(c[0],c[1],c[2]);
				this._data[i+1][j].vertex(gl);
				this._data[i][j+1].vertex(gl);
				this._data[i+1][j+1].vertex(gl);
			}
		}
		gl.glEnd();
	}



	// private Vector3[][] _regenerate(){
	// 	Vector3[][] dt=new Vector3[(int)Math.floor((this._max.x-this._min.x)/GROUND_TILE_SIZE)][(int)Math.floor((this._max.z-this._min.z)/GROUND_TILE_SIZE)];
	// 	int i=0;
	// 	for (double x=0;x<this._max.x-this._min.x;x+=GROUND_TILE_SIZE){
	// 		Vector3[] zdt=new Vector3[(int)Math.floor((this._max.z-this._min.z)/GROUND_TILE_SIZE)];
	// 		int j=0;
	// 		for (double z=0;z<this._max.z-this._min.z;z+=GROUND_TILE_SIZE){
	// 			double y=this._map(PerlinNoise.get((x-this._min.x)/GROUND_NOISE_DETAIL,(z-this._min.z)/GROUND_NOISE_DETAIL,0),-Math.sqrt(3)/4,Math.sqrt(3)/4,GROUND_MIN_Y_POS,GROUND_MAX_Y_POS);
	// 			zdt[j]=new Vector3(this._min.x+x,y,this._min.z+z);
	// 			j++;
	// 		}
	// 		dt[i]=zdt;
	// 		i++;
	// 	}
	// 	return dt;
	// }



	private Vector3[] _calc_size(){
		Vector3 min=new Vector3(0,0,0);
		Vector3 max=new Vector3(0,0,0);
		for (TrackList tl:this.game.tll){
			for (TrackSupport ts:tl.tsl.tsl){
				this._minmax(min,max,ts.ga);
				this._minmax(min,max,ts.gb);
			}
		}
		double rx=GROUND_TILE_SIZE-(max.x-min.x)%GROUND_TILE_SIZE+GROUND_BUFFOR_X*2;
		double rz=GROUND_TILE_SIZE-(max.z-min.z)%GROUND_TILE_SIZE+GROUND_BUFFOR_Z*2;
		min.x-=rx/2;
		max.x+=rx/2;
		min.z-=rz/2;
		max.z+=rz/2;
		return new Vector3[]{min,max};
	}



	private void _minmax(Vector3 min,Vector3 max,Vector3 t){
		min.x=Math.min(min.x,t.x);
		min.z=Math.min(min.z,t.z);
		max.x=Math.max(max.x,t.x);
		max.z=Math.max(max.z,t.z);
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return (v-aa)/(ab-aa)*(bb-ba)+ba;
	}



	private float[] _color(double y1,double y2,double y3){
		return new float[]{(float)this._map(y1/3+y2/3+y3/3,GROUND_MIN_Y_POS,GROUND_MAX_Y_POS,GROUND_MIN_COLOR[0],GROUND_MAX_COLOR[0]),(float)this._map(y1/3+y2/3+y3/3,GROUND_MIN_Y_POS,GROUND_MAX_Y_POS,GROUND_MIN_COLOR[1],GROUND_MAX_COLOR[1]),(float)this._map(y1/3+y2/3+y3/3,GROUND_MIN_Y_POS,GROUND_MAX_Y_POS,GROUND_MIN_COLOR[2],GROUND_MAX_COLOR[2])};
	}
}