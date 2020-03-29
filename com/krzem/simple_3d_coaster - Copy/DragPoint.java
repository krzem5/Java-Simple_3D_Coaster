package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.lang.Math;



public class DragPoint extends Constants{
	public Main.Main_ cls;
	public Game game;
	public TrackElement tp;
	private Vector3 target;
	private boolean _at=false;
	private boolean _sel=false;
	private int _di=0;
	private boolean _md=false;
	private boolean _enable=true;
	private boolean _y_enabled=true;
	private boolean _xz_enabled=true;



	public DragPoint(Main.Main_ cls,Game game,TrackElement tp,Vector3 t){
		this.cls=cls;
		this.game=game;
		this.tp=tp;
		this.target=t;
	}



	public void enable(boolean b){
		this._enable=b;
	}



	public void disable_y(){
		this._y_enabled=false;
	}



	public void disable_xz(){
		this._xz_enabled=false;
	}



	public void update(GL2 gl){
		if (this._enable==true&&this.tp.tli==this.tp.tl._t_sel&&Math.sqrt((this.cls.cam.x-this.target.x)*(this.cls.cam.x-this.target.x)+(this.cls.cam.y-this.target.y)*(this.cls.cam.y-this.target.y)+(this.cls.cam.z-this.target.z)*(this.cls.cam.z-this.target.z))<=DRAG_POINT_MAX_CAM_DIST){
			this._at=true;
		}
		else{
			this._at=false;
			if (this._sel==true){
				this.tp.tl._d_sel=false;
			}
			this._sel=false;
			this._di=0;
		}
		if (this._at==true&&this._sel==false&&this.tp.tl._d_sel==false&&this.cls.MOUSE==1&&this.cls.MOUSE_BUTTON==1&&this.cls.MOUSE_COUNT==1&&this._md==false){
			this._md=true;
			if (this._xz_enabled==true&&this._collision_point_poly(new Vector(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2),this._project_click_2d(gl,(this.cls.cam.x>=this.target.x?this._ax_click():this._bx_click())))==true){
				this._sel=true;
				this.tp.tl._d_sel=true;
				this._di=1+(this.cls.cam.x<this.target.x?1:0);
			}
			else if (this._y_enabled==true&&this._collision_point_poly(new Vector(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2),this._project_click_2d(gl,(this.cls.cam.y>=this.target.y?this._ay_click():this._by_click())))==true){
				this._sel=true;
				this.tp.tl._d_sel=true;
				this._di=3+(this.cls.cam.y<this.target.y?1:0);
			}
			else if (this._xz_enabled==true&&this._collision_point_poly(new Vector(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2),this._project_click_2d(gl,(this.cls.cam.z>=this.target.z?this._az_click():this._bz_click())))==true){
				this._sel=true;
				this.tp.tl._d_sel=true;
				this._di=5+(this.cls.cam.z<this.target.z?1:0);
			}
		}
		if (this.cls.MOUSE==0&&this._md==true){
			this.tp.tl._d_sel=false;
			this._sel=false;
			this._di=0;
			this._md=false;
			this.tp.tl.tdt._rg=true;
		}
		if (this._sel==true&&this._md==true){
			Vector3[] ln=null;
			switch (this._di){
				case 1: case 2:
					ln=this._cx_click();
					break;
				case 3: case 4:
					ln=this._cy_click();
					break;
				case 5: case 6:
					ln=this._cz_click();
					break;
			}
			this.target.set(this._3d_line_line_intersection(ln[0],ln[1],new Vector3(this.cls.cam.x,this.cls.cam.y,this.cls.cam.z),Vector3.sphere(1,this.cls.cam.rx/180*Math.PI-Math.PI/2,-this.cls.cam.ry/180*Math.PI+Math.PI/2).add(new Vector3(this.cls.cam.x,this.cls.cam.y,this.cls.cam.z))));
			if (this.cls.KEYBOARD.pressed(17)){
				this.target.set(new Vector3(Math.round(this.target.x*4)/4,Math.round(this.target.y*4)/4,Math.round(this.target.z*4)/4));
			}
		}
	}



	public void draw(GL2 gl){
		gl.glEnable(GL2.GL_BLEND);
		if (this._at==true&&(this.tp.tl._d_sel==false||(this.tp.tl._d_sel==true&&this._sel==true))){
			if (this._sel==false){
				if (this._xz_enabled==true){
					if (this.cls.cam.x>=this.target.x){
						gl.glColor4f(1,0,0,1);
						this._draw_arrow(gl,this._ax_click());
					}
					else{
						gl.glColor4f(0.5f,0,0,1);
						this._draw_arrow(gl,this._bx_click());
					}
				}
				if (this._y_enabled==true){
					if (this.cls.cam.y>=this.target.y){
						gl.glColor4f(0,1,0,1);
						this._draw_arrow(gl,this._ay_click());
					}
					else{
						gl.glColor4f(0,0.5f,0,1);
						this._draw_arrow(gl,this._by_click());
					}
				}
				if (this._xz_enabled==true){
					if (this.cls.cam.z>=this.target.z){
						gl.glColor4f(0,0,1,1);
						this._draw_arrow(gl,this._az_click());
					}
					else{
						gl.glColor4f(0,0,0.5f,1);
						this._draw_arrow(gl,this._bz_click());
					}
				}
			}
			else{
				switch (this._di){
					case 1:
						gl.glColor4f(1,0,0,0.6f);
						this._draw_arrow(gl,this._ax_click());
						break;
					case 2:
						gl.glColor4f(0.5f,0,0,0.6f);
						this._draw_arrow(gl,this._bx_click());
						break;
					case 3:
						gl.glColor4f(0,1,0,0.6f);
						this._draw_arrow(gl,this._ay_click());
						break;
					case 4:
						gl.glColor4f(0,0.5f,0,0.6f);
						this._draw_arrow(gl,this._by_click());
						break;
					case 5:
						gl.glColor4f(0,0,1,0.6f);
						this._draw_arrow(gl,this._az_click());
						break;
					case 6:
						gl.glColor4f(0,0,0.5f,0.6f);
						this._draw_arrow(gl,this._bz_click());
						break;
				}
			}
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor4f(1,1,1,0.5f);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.target.add(new Vector3(DRAG_POINT_BOX_SIZE,-DRAG_POINT_BOX_SIZE,DRAG_POINT_BOX_SIZE)).vertex(gl);
			this.game._tn++;
			gl.glEnd();
		}
		gl.glDisable(GL2.GL_BLEND);
	}



	private void _draw_arrow(GL2 gl,Vector3[] l){
		gl.glBegin(GL2.GL_LINES);
		for (Vector3 v:l){
			v.vertex(gl);
		}
		gl.glEnd();
		if (l[0].x<l[1].x||l[0].y<l[1].y||l[0].z<l[1].z){
			gl.glBegin(GL2.GL_TRIANGLES);
			Vector3 ax=l[0].sub(l[1]).normalize();
			Vector3 o=(l[0].x<l[1].x?l[1].sub(new Vector3(DRAG_POINT_ARROW_CONE_SIZE,0,0)):(l[0].y<l[1].y?l[1].sub(new Vector3(0,DRAG_POINT_ARROW_CONE_SIZE,0)):l[1].sub(new Vector3(0,0,DRAG_POINT_ARROW_CONE_SIZE))));
			Vector3 p=l[0].sub(l[1]).perpendicular().mag(DRAG_POINT_ARROW_CONE_RADIUS);
			for (double i=0;i<Math.PI*2;i+=Math.PI*2/DRAG_POINT_ARROW_CONE_DETAIL){
				p.rotate(ax,i).add(o).vertex(gl);
				p.rotate(ax,i+Math.PI*2/DRAG_POINT_ARROW_CONE_DETAIL).add(o).vertex(gl);
				l[1].vertex(gl);
				this.game._tn++;
				p.rotate(ax,i).add(o).vertex(gl);
				p.rotate(ax,i+Math.PI*2/DRAG_POINT_ARROW_CONE_DETAIL).add(o).vertex(gl);
				o.vertex(gl);
				this.game._tn++;
			}
			gl.glEnd();
		}
		else{
			gl.glBegin(GL2.GL_TRIANGLES);
			l=new Vector3[]{l[1],l[0]};
			Vector3 ax=l[0].sub(l[1]).normalize();
			Vector3 o=(l[0].x<l[1].x?l[0].sub(new Vector3(-DRAG_POINT_ARROW_CONE_SIZE,0,0)):(l[0].y<l[1].y?l[0].sub(new Vector3(0,-DRAG_POINT_ARROW_CONE_SIZE,0)):l[0].sub(new Vector3(0,0,-DRAG_POINT_ARROW_CONE_SIZE))));
			Vector3 p=l[1].sub(l[0]).perpendicular().mag(DRAG_POINT_ARROW_CONE_RADIUS);
			for (double i=0;i<Math.PI*2;i+=Math.PI*2/DRAG_POINT_ARROW_CONE_DETAIL){
				p.rotate(ax,i).add(l[0]).vertex(gl);
				p.rotate(ax,i+Math.PI*2/DRAG_POINT_ARROW_CONE_DETAIL).add(l[0]).vertex(gl);
				l[0].vertex(gl);
				this.game._tn++;
				p.rotate(ax,i).add(l[0]).vertex(gl);
				p.rotate(ax,i+Math.PI*2/DRAG_POINT_ARROW_CONE_DETAIL).add(l[0]).vertex(gl);
				o.vertex(gl);
				this.game._tn++;
			}
			gl.glEnd();
		}
	}



	private Vector[] _project_click_2d(GL2 gl,Vector3[] c){
		Vector a=c[0].project_2d(gl,this.cls.cam);
		Vector b=c[1].project_2d(gl,this.cls.cam);
		if (a==null||b==null){
			return new Vector[4];
		}
		double ang=new Vector(a.x-b.x,a.y-b.y).angle();
		return new Vector[]{a.add(20,0).rotate(a,ang-Math.PI/2),a.add(20,0).rotate(a,ang+Math.PI/2),b.add(20,0).rotate(b,ang+Math.PI/2),b.add(20,0).rotate(b,ang-Math.PI/2)};
	}



	private Vector3[] _ax_click(){
		return new Vector3[]{this.target.clone(),this.target.add(new Vector3(DRAG_POINT_ARROW_SIZE,0,0))};
	}



	private Vector3[] _ay_click(){
		return new Vector3[]{this.target.clone(),this.target.add(new Vector3(0,DRAG_POINT_ARROW_SIZE,0))};
	}



	private Vector3[] _az_click(){
		return new Vector3[]{this.target.clone(),this.target.add(new Vector3(0,0,DRAG_POINT_ARROW_SIZE))};
	}



	private Vector3[] _bx_click(){
		return new Vector3[]{this.target.clone(),this.target.add(new Vector3(-DRAG_POINT_ARROW_SIZE,0,0))};
	}



	private Vector3[] _by_click(){
		return new Vector3[]{this.target.clone(),this.target.add(new Vector3(0,-DRAG_POINT_ARROW_SIZE,0))};
	}



	private Vector3[] _bz_click(){
		return new Vector3[]{this.target.clone(),this.target.add(new Vector3(0,0,-DRAG_POINT_ARROW_SIZE))};
	}



	private Vector3[] _cx_click(){
		return new Vector3[]{this.target.add(new Vector3(-DRAG_POINT_ARROW_SIZE,0,0)),this.target.add(new Vector3(DRAG_POINT_ARROW_SIZE,0,0))};
	}



	private Vector3[] _cy_click(){
		return new Vector3[]{this.target.add(new Vector3(0,-DRAG_POINT_ARROW_SIZE,0)),this.target.add(new Vector3(0,DRAG_POINT_ARROW_SIZE,0))};
	}



	private Vector3[] _cz_click(){
		return new Vector3[]{this.target.add(new Vector3(0,0,-DRAG_POINT_ARROW_SIZE)),this.target.add(new Vector3(0,0,DRAG_POINT_ARROW_SIZE))};
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