package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;



public class LoopTrackPiece extends TrackElement{
	public LoopTrackPiece(Main.Main_ cls,Game game,TrackList tl,int tli,Vector3 a,Vector3 b,Vector3 anc){
		this.cls=cls;
		this.game=game;
		this.tl=tl;
		this.tli=tli;
		this.a=a;
		this.b=b;
		this.anc=anc;
		this.dpa=new DragPoint(this.cls,this.game,this,this.a);
		this.dpb=new DragPoint(this.cls,this.game,this,this.b);
		this.dpanc=new DragPoint(this.cls,this.game,this,this.anc);
		this.dpb.disable_y();
		this.dpanc.disable_xz();
	}



	@Override
	public void update(GL2 gl){
		if (this.tli<this.tl.tpl.size()-1&&this.tl.tpl.get(this.tli+1).rpa!=null){
			this.tl.tpl.get(this.tli+1).rpa.enable(false);
		}
		this.dpb.enable(this.tli==this.tl.tpl.size()-1);
		this.dpa.update(gl);
		this.dpb.update(gl);
		this.dpanc.update(gl);
		this.anc.x=this.a.x/2+this.b.x/2;
		this.anc.z=this.a.z/2+this.b.z/2;
		this.b.y=this.a.y+0;
	}



	@Override
	public void draw(GL2 gl){
		this.dpa.draw(gl);
		this.dpb.draw(gl);
		this.dpanc.draw(gl);
	}



	@Override
	public Object[] _regenerate(){
		ArrayList<Vector3> lc=this._gen_loop_curve(this.a,this.b,this.anc,LOOP_TRACK_PIECE_LENGTH_CURVE_DETAIL);
		double l=0;
		for (int i=0;i<lc.size()-1;i++){
			l+=Math.sqrt((lc.get(i).x-lc.get(i+1).x)*(lc.get(i).x-lc.get(i+1).x)+(lc.get(i).y-lc.get(i+1).y)*(lc.get(i).y-lc.get(i+1).y)+(lc.get(i).z-lc.get(i+1).z)*(lc.get(i).z-lc.get(i+1).z));
		}
		ArrayList<Double> rt=new ArrayList<Double>();
		for (int i=0;i<=(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH);i++){
			rt.add(0.0d);
		}
		return new Object[]{this._gen_loop_curve(this.a,this.b,this.anc,(int)Math.floor(l/TRACK_PIECE_TRACK_SEGMENT_LENGTH)),rt};
	}



	private ArrayList<Vector3> _gen_loop_curve(Vector3 s,Vector3 _e,Vector3 anc,int n){
		Vector3 off=_e.sub(s).rotate(new Vector3(0,1,0),Math.PI/2).mag(LOOP_TRACK_PIECE_TRACK_SEP_DIST*2);
		Vector3 offi=off.inv();
		Vector3 e=_e.add(off);
		ArrayList<Vector3> l=new ArrayList<Vector3>();
		l.add(s);
		Vector3[] pt=new Vector3[]{s,this._lerp(s,e,1/3d),this._lerp(s,e,8/9d),this._lerp(s,e,10/9d).add(new Vector3(0,this._lerp(0,anc.y-s.y,1/3d),0)),this._lerp(s,e,0.5).add(new Vector3(0,this._lerp(0,anc.y-s.y,1.5d),0)),this._lerp(s,e,-1/9d).add(new Vector3(0,_lerp(0,anc.y-s.y,1/3d),0)),this._lerp(s,e,1/9d),this._lerp(s,e,2/3d),e};
		double[][] spt=this._split_vec(pt);
		for (double t=1d/n;t<1;t+=1d/n){
			Vector3 xzoff=offi.mag(this._lerp(0,LOOP_TRACK_PIECE_TRACK_SEP_DIST*2,t));
			double x=this._simplify(spt[0],t)[0]+xzoff.x;
			double y=this._simplify(spt[1],t)[0];
			double z=this._simplify(spt[2],t)[0]+xzoff.z;
			l.add(new Vector3(x,y,z));
		}
		if (!l.get(l.size()-1).eq(_e)){
			l.add(_e);
		}
		return l;
	}



	private double[][] _split_vec(Vector3[] pt){
		double[] x=new double[pt.length];
		double[] y=new double[pt.length];
		double[] z=new double[pt.length];
		for (int i=0;i<pt.length;i++){
			x[i]=pt[i].x;
			y[i]=pt[i].y;
			z[i]=pt[i].z;
		}
		return new double[][]{x,y,z};
	}



	private double[] _simplify(double[] pt,double t){
		double[] o=new double[pt.length-1];
		for (int i=0;i<pt.length-1;i++){
			o[i]=pt[i]*(1-t)+pt[i+1]*t;
		}
		if (o.length==1){
			return o;
		}
		return this._simplify(o,t);
	}



	private Vector3 _lerp(Vector3 a,Vector3 b,double t){
		return new Vector3(a.x+(b.x-a.x)*t,a.y+(b.y-a.y)*t,a.z+(b.z-a.z)*t);
	}



	private double _lerp(double a,double b,double t){
		return a+(b-a)*t;
	}
}
