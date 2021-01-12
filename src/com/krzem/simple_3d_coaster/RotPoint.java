package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.lang.Math;



public class RotPoint extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackElement tp;
	private int target_id;
	private Vector3 o;
	private Vector3 ax;
	private double _lst=0.0d;
	private boolean _at=false;
	private boolean _sel=false;
	private boolean _md=false;
	private boolean _enable=true;



	public RotPoint(Main.Main_ cls,Game game,TrackElement tp,Vector3 o,Vector3 ax,int t){
		this.cls=cls;
		this.game=game;
		this.tp=tp;
		this.o=o;
		this.ax=ax;
		this.target_id=t;
	}



	public void enable(boolean b){
		this._enable=b;
	}



	public void setup(Vector3 o,Vector3 ax){
		this.o=o;
		this.ax=ax;
	}



	public void update(GL2 gl){
		if (this._enable==true&&this.tp.tli==this.tp.tl._t_sel&&Math.sqrt((this.cls.cam.x-this.o.x)*(this.cls.cam.x-this.o.x)+(this.cls.cam.y-this.o.y)*(this.cls.cam.y-this.o.y)+(this.cls.cam.z-this.o.z)*(this.cls.cam.z-this.o.z))<=ROT_POINT_MAX_CAM_DIST){
			this._at=true;
		}
		else{
			this._at=false;
			if (this._sel==true){
				this.tp.tl._d_sel=false;
			}
			this._lst=0.0d;
			this._sel=false;
		}
		if (this._at==true&&this._sel==false&&this.tp.tl._d_sel==false&&this.cls.MOUSE==1&&this.cls.MOUSE_BUTTON==1&&this.cls.MOUSE_COUNT==1&&this._md==false){
			this._md=true;
			if (this._collision_point_poly(new Vector(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2),this._project_click_2d(gl,this._circle_click()))==true){
				this._sel=true;
				this.tp.tl._d_sel=true;
				this._lst=-(this.target_id==0?-this.tp.srot:this.tp.erot)+0;
			}
		}
		if (this.cls.MOUSE==0&&this._md==true){
			this.tp.tl._d_sel=false;
			this._sel=false;
			this._md=false;
			this._lst=0.0d;
			this.tp.tl.tdt._rg=true;
		}
		if (this._sel==true&&this._md==true){
			Vector3[] ln=this._get_ln();
			Vector3 nv=this._3d_line_line_intersection(ln[0],ln[1],new Vector3(this.cls.cam.x,this.cls.cam.y,this.cls.cam.z),Vector3.sphere(1,this.cls.cam.rx/180*Math.PI-Math.PI/2,-this.cls.cam.ry/180*Math.PI+Math.PI/2).add(new Vector3(this.cls.cam.x,this.cls.cam.y,this.cls.cam.z))).sub(this.o);
			nv=nv.rotate(new Vector3(0,1,0),Math.atan2(this.ax.z,this.ax.x)-Math.PI/2).rotate(new Vector3(1,0,0),-this.ax.angle().x);
			nv=nv.rotate(new Vector3(1,0,0),-nv.angle().x);
			double a=(nv.angle().x-Math.PI)*(Math.abs(nv.angle().y)>=3?1:-1);
			if (this.target_id==0){
				this.tp.srot-=this._lst-a+(Math.abs(this._lst-a)>Math.PI?(this._lst<a?Math.PI*2:-Math.PI*2):0);
				this._lst=a;
			}
			else{
				this.tp.erot+=this._lst-a+(Math.abs(this._lst-a)>Math.PI?(this._lst<a?Math.PI*2:-Math.PI*2):0);
				this._lst=a;
			}
		}
	}



	public void draw(GL2 gl){
		gl.glEnable(GL2.GL_BLEND);
		if (this._at==true&&(this.tp.tl._d_sel==false||(this.tp.tl._d_sel==true&&this._sel==true))){
			gl.glBegin(GL2.GL_TRIANGLES);
			if (this._sel==false){
				gl.glColor4f(1,1,0,1f);
			}
			else{
				gl.glColor4f(1,1,0,0.6f);
			}
			this._draw_circle(gl);
			gl.glColor4f(1,1,1,0.2f);
			this._draw_ring(gl);
			gl.glEnd();
		}
		gl.glDisable(GL2.GL_BLEND);
	}



	private void _draw_circle(GL2 gl){
		Vector3 v=new Vector3(ROT_POINT_DRAG_CIRCLE_DIST,0,0).rotate(new Vector3(0,1,0),-Math.atan2(this.ax.z,this.ax.x)+Math.PI/2).rotate(this.ax,Math.PI/2+(this.target_id==0?-this.tp.srot:this.tp.erot));
		Vector3 o=this.o.add(v);
		Vector3 p=v.mag(ROT_POINT_DRAG_CIRCLE_RADIUS);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/ROT_POINT_DRAG_CIRCLE_DETAIL){
			p.rotate(this.ax,i).add(o).vertex(gl);
			p.rotate(this.ax,i+Math.PI*2/ROT_POINT_DRAG_CIRCLE_DETAIL).add(o).vertex(gl);
			o.vertex(gl);
			this.game._tn++;
		}
	}



	private void _draw_ring(GL2 gl){
		Vector3 po=this.ax.perpendicular().mag(ROT_POINT_RING_OUTER_RADIUS);
		Vector3 pi=this.ax.perpendicular().mag(ROT_POINT_RING_INNER_RADIUS);
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/ROT_POINT_RING_DETAIL){
			po.rotate(this.ax,i).add(this.o).vertex(gl);
			po.rotate(this.ax,i+Math.PI*2/ROT_POINT_RING_DETAIL).add(this.o).vertex(gl);
			pi.rotate(this.ax,i+Math.PI/ROT_POINT_RING_DETAIL).add(this.o).vertex(gl);
			this.game._tn++;
			po.rotate(this.ax,i).add(this.o).vertex(gl);
			pi.rotate(this.ax,i-Math.PI/ROT_POINT_RING_DETAIL).add(this.o).vertex(gl);
			pi.rotate(this.ax,i+Math.PI/ROT_POINT_RING_DETAIL).add(this.o).vertex(gl);
			this.game._tn++;
		}
	}



	private Vector[] _project_click_2d(GL2 gl,Vector3[] c){
		Vector[] pl=new Vector[c.length];
		int i=0;
		for (Vector3 v:c){
			pl[i]=v.project_2d(gl,this.cls.cam);
			i++;
		}
		return pl;
	}



	private Vector3[] _circle_click(){
		Vector3[] pl=new Vector3[ROT_POINT_DRAG_CIRCLE_DETAIL+1];
		Vector3 v=new Vector3(ROT_POINT_DRAG_CIRCLE_DIST,0,0).rotate(new Vector3(0,1,0),-Math.atan2(this.ax.z,this.ax.x)+Math.PI/2).rotate(this.ax,Math.PI/2+(this.target_id==0?-this.tp.srot:this.tp.erot));
		Vector3 o=this.o.add(v);
		Vector3 p=v.mag(ROT_POINT_DRAG_CIRCLE_RADIUS);
		int j=0;
		for (double i=0;i<Math.PI*2;i+=Math.PI*2/ROT_POINT_DRAG_CIRCLE_DETAIL){
			pl[j]=p.rotate(this.ax,i).add(o);
			j++;
		}
		return pl;
	}



	private Vector3[] _get_ln(){
		Vector3 v=new Vector3(ROT_POINT_DRAG_CIRCLE_DIST,0,0).rotate(new Vector3(0,1,0),-Math.atan2(this.ax.z,this.ax.x)+Math.PI/2).rotate(this.ax,Math.PI/2+(this.target_id==0?-this.tp.srot:this.tp.erot));
		Vector3 o=this.o.add(v);
		Vector3 p=v.mag(ROT_POINT_DRAG_CIRCLE_RADIUS);
		return new Vector3[]{p.rotate(this.ax,Math.PI/2).add(o),p.rotate(this.ax,-Math.PI/2).add(o)};
	}



	private boolean _collision_point_poly(Vector pt,Vector[] pl){
		boolean c=false;
		for (int i=0;i<pl.length;i++){
			if (pl[i]==null||pl[(i+1)%pl.length]==null){
				return false;
			}
			if (((pl[i].y>pt.y&&pl[(i+1)%pl.length].y<pt.y)||(pl[i].y<pt.y&&pl[(i+1)%pl.length].y>pt.y))&&(pt.x<(pl[(i+1)%pl.length].x-pl[i].x)*(pt.y-pl[i].y)/(pl[(i+1)%pl.length].y-pl[i].y)+pl[i].x)){
				c=!c;
			}
		}
		return c;
	}



	private Vector3 _3d_line_line_intersection(Vector3 a,Vector3 b,Vector3 c,Vector3 d){
		double n=((a.x-c.x)*(d.x-c.x)+(a.y-c.y)*(d.y-c.y)+(a.z-c.z)*(d.z-c.z))*((d.x-c.x)*(b.x-a.x)+(d.y-c.y)*(b.y-a.y)+(d.z-c.z)*(b.z-a.z))-((a.x-c.x)*(b.x-a.x)+(a.y-c.y)*(b.y-a.y)+(a.z-c.z)*(b.z-a.z))*((d.x-c.x)*(d.x-c.x)+(d.y-c.y)*(d.y-c.y)+(d.z-c.z)*(d.z-c.z));
		double dn=((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)+(b.z-a.z)*(b.z-a.z))*((d.x-c.x)*(d.x-c.x)+(d.y-c.y)*(d.y-c.y)+(d.z-c.z)*(d.z-c.z))-((d.x-c.x)*(b.x-a.x)+(d.y-c.y)*(b.y-a.y)+(d.z-c.z)*(b.z-a.z))*((d.x-c.x)*(b.x-a.x)+(d.y-c.y)*(b.y-a.y)+(d.z-c.z)*(b.z-a.z));
		double u=n/dn;
		double x=a.x+u*(b.x-a.x);
		double y=a.y+u*(b.y-a.y);
		double z=a.z+u*(b.z-a.z);
		return new Vector3(x,y,z);
	}
}