package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;



public class TrackData extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackList tl;
	public Vector3[][] vl;
	public double[] dstl;
	public int[] tsl;
	public boolean _rg=true;



	public TrackData(Main.Main_ cls,Game game,TrackList tl){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
	}



	public void update(GL2 gl){
		if (this._rg==true){
			this._rg=false;
			this._regenerate();
		}
	}



	public void draw(GL2 gl){
		// gl.glBegin(GL2.GL_LINES);
		// for (int i=0;i<this.vl.length-1;i++){
		// 	gl.glColor3d(0,1,1);
		// 	this.vl[i][0].vertex(gl);
		// 	this.vl[i][1].vertex(gl);
		// 	gl.glColor3d(1,1,0);
		// 	this.vl[i][0].vertex(gl);
		// 	this.vl[i][0].sub(this.vl[i][1]).add(this.vl[i][0]).vertex(gl);
		// }
		// gl.glEnd();
	}



	public void _regenerate(){
		ArrayList<Vector3[]> vl=new ArrayList<Vector3[]>();
		ArrayList<Double> rzl=new ArrayList<Double>();
		ArrayList<Double> dstl=new ArrayList<Double>();
		ArrayList<Integer> tsl=new ArrayList<Integer>();
		double ld=0.0d;
		for (int ti=0;ti<this.tl.tpl.size();ti++){
			TrackElement tp=this.tl.tpl.get(ti);
			boolean lp=(tp.getClass().getName().indexOf("Loop")>-1?true:false);
			double off=tp.srot+0;
			double sar=tp.cv.get(1).sub(tp.cv.get(0)).normalize().angle().y;
			for (int vi=0;vi<tp.cv.size()-1;vi++){
				Vector3 ea;
				if (ti==this.tl.tpl.size()-1&&vi==tp.cv.size()-2){
					ea=tp.cv.get(vi+1).sub(tp.cv.get(vi)).normalize();
				}
				else if (vi==tp.cv.size()-2){
					TrackElement otp=this.tl.tpl.get(ti+1);
					ea=tp.cv.get(vi+1).sub(tp.cv.get(vi)).center(otp.cv.get(1).sub(otp.cv.get(0))).normalize();
				}
				else{
					ea=tp.cv.get(vi+2).sub(tp.cv.get(vi+1)).center(tp.cv.get(vi+1).sub(tp.cv.get(vi))).normalize();
				}
				if (vl.size()==0){
					Vector3 sa=tp.cv.get(1).sub(tp.cv.get(0)).normalize();
					Vector3 sp=new Vector3(TRAIN_CAMERA_DIST_ABOVE_TRACK,0,0).rotate(new Vector3(0,1,0),-Math.atan2(sa.z,sa.x)+Math.PI/2).rotate(sa,Math.PI/2+off);
					vl.add(new Vector3[]{sp.add(tp.cv.get(vi)),tp.cv.get(vi)});
					dstl.add(ld);
					tsl.add((tp.getClass().getName().indexOf("Break")>-1?1:(tp.getClass().getName().indexOf("Lift")>-1?2:0)));
				}
				double a=ea.angle().y;
				double rt=off+(tp.erot-tp.srot)/tp.cv.size()+(lp==true&&Math.abs(a-sar)>Math.PI/2?Math.PI*(a>sar?-1:1):0);
				Vector3 ep=new Vector3(TRAIN_CAMERA_DIST_ABOVE_TRACK,0,0).rotate(new Vector3(0,1,0),-Math.atan2(ea.z,ea.x)+Math.PI/2).rotate(ea,Math.PI/2+rt);
				vl.add(new Vector3[]{ep.add(tp.cv.get(vi+1)),tp.cv.get(vi+1)});
				Vector3 dv=vl.get(vl.size()-2)[0].sub(vl.get(vl.size()-1)[0]);
				ld+=Math.sqrt(dv.x*dv.x+dv.y*dv.y+dv.z*dv.z);
				dstl.add(ld);
				tsl.add((tp.getClass().getName().indexOf("Break")>-1?1:(tp.getClass().getName().indexOf("Lift")>-1?2:0)));
				off+=(tp.erot-tp.srot)/tp.cv.size();
			}
		}
		this.vl=new Vector3[vl.size()][2];
		for (int i=0;i<vl.size();i++){
			this.vl[i]=vl.get(i);
		}
		this.dstl=new double[dstl.size()];
		for (int i=0;i<dstl.size();i++){
			this.dstl[i]=(double)dstl.get(i);
		}
		this.tsl=new int[vl.size()];
		for (int i=0;i<rzl.size();i++){
			this.tsl[i]=(int)tsl.get(i);
		}
	}
}