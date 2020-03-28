package com.krzem.simple_3d_coaster - Copy;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class LightList extends Constants{
	public Main.Main_ cls;
	public Game game;
	private List<Light> _lgt;
	private double[] cl=new double[]{1,1,1};



	public LightList(Main.Main_ cls,Game game){
		this.cls=cls;
		this.game=game;
		this._lgt=new ArrayList<Light>();
	}



	public void add(Light l){
		this._lgt.add(l);
	}



	public void color(double... cl){
		this.cl=cl;
	}



	public void draw(GL2 gl,Vector3... vl){
		gl.glEnd();
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(0,1,1);
		for (int vi=1;vi<=vl.length;vi++){
			Vector3 o=vl[vi%vl.length];
			Vector3 d=o.sub(vl[vi-1]).normalize().cross(o.sub(vl[(vi+1)%vl.length]).normalize()).normalize();
			o.vertex(gl);
			o.add(d).vertex(gl);
			o.vertex(gl);
			o.add(d.inv()).vertex(gl);
		}
		gl.glColor3d(1,1,0);
		for (int vi=0;vi<vl.length;vi++){
			vl[vi].vertex(gl);
			this._lgt.get(0).pos.vertex(gl);
		}
		gl.glEnd();
		gl.glBegin(GL2.GL_TRIANGLES);



		gl.glColor3d(this.cl[0],this.cl[1],this.cl[2]);
		for (Vector3 v:vl){
			v.vertex(gl);
		}
	}
}