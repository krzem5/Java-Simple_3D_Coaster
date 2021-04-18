package com.krzem.simple_3d_coaster;



import com.jogamp.opengl.GL2;
import java.lang.Math;



public class Vector3 extends Constants{
	public double x;
	public double y;
	public double z;



	public Vector3(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}



	@Override
	public Vector3 clone(){
		return new Vector3(this.x+0,this.y+0,this.z+0);
	}



	public Vector3 add(Vector3 v){
		return new Vector3(this.x+v.x,this.y+v.y,this.z+v.z);
	}



	public Vector3 set(Vector3 v){
		this.x=v.x;
		this.y=v.y;
		this.z=v.z;
		return this;
	}



	public Vector3 sub(Vector3 v){
		return new Vector3(this.x-v.x,this.y-v.y,this.z-v.z);
	}



	public boolean eq(Vector3 v){
		return (Math.abs(this.x-v.x)<=1e-6&&Math.abs(this.y-v.y)<=1e-6&&Math.abs(this.z-v.z)<=1e-6);
	}



	public void vertex(GL2 gl){
		gl.glVertex3d(this.x,this.y,this.z);
	}



	public Vector angle(){
		double aa=Math.acos(this.y/Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z));
		double ab=Math.atan2(this.z,this.x);
		return new Vector(aa,ab);
	}



	public Vector3 center(Vector3 v){
		return this.normalize().add(v.normalize()).normalize();
	}



	public Vector3 mag(double d){
		Vector3 v=this.clone().normalize();
		v.x*=d;
		v.y*=d;
		v.z*=d;
		return v;
	}



	public Vector project_2d(GL2 gl,Camera cam){
		float[] pp=new float[3];
		float[] mm=new float[16];
		float[] pm=new float[16];
		int[] vp={0,0,cam.cls.WINDOW_SIZE.width,cam.cls.WINDOW_SIZE.height};
		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX,mm,0);
		gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX,pm,0);
		cam.cls.glu.gluProject((float)this.x,(float)this.y,(float)this.z,mm,0,pm,0,vp,0,pp,0);
		if (pp[2]>=1){
			return null;
		}
		return new Vector(pp[0],pp[1]);
	}



	public Vector3 normalize(){
		if (Math.abs(1-this.x-this.y-this.z)<=1e-6){
			return this.clone();
		}
		double rt=Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
		return new Vector3(this.x/rt,this.y/rt,this.z/rt);
	}



	public Vector3 perpendicular(){
		Vector3 v=new Vector3(0,0,0);
		if (this.x!=0){
			v.x=-this.z;
			v.z=this.x;
		}
		else if (this.y!=0){
			v.x=this.y;
			v.y=-this.x;
		}
		else{
			v.y=this.z;
			v.z=-this.y;
		}
		return v;
	}



	public Vector3 rotate(Vector3 a,double v){
		double x=this.x*(Math.cos(v)+a.x*a.x*(1-Math.cos(v)))+this.y*(a.x*a.y*(1-Math.cos(v))-a.z*Math.sin(v))+this.z*(a.x*a.z*(1-Math.cos(v))+a.y*Math.sin(v));
		double y=this.x*(a.y*a.x*(1-Math.cos(v))+a.z*Math.sin(v))+this.y*(Math.cos(v)+a.y*a.y*(1-Math.cos(v)))+this.z*(a.y*a.z*(1-Math.cos(v))-a.x*Math.sin(v));
		double z=this.x*(a.z*a.x*(1-Math.cos(v))-a.y*Math.sin(v))+this.y*(a.z*a.y*(1-Math.cos(v))+a.x*Math.sin(v))+this.z*(Math.cos(v)+a.z*a.z*(1-Math.cos(v)));
		return new Vector3(x,y,z);
	}



	public Vector3 cross(Vector3 v){
		return new Vector3(this.y*v.z+this.z*v.y,this.z*v.x+this.x*v.z,this.x*v.y+this.y*v.x);
	}


	public Vector3 inv(){
		return new Vector3(-this.x,-this.y,-this.z);
	}



	public Vector3 plane_perpendicular(){
		Vector a=this.angle();
		if (a.x-Math.PI/2<0){
			a.x=Math.abs(a.x-Math.PI/2);
			a.y+=Math.PI;
		}
		else{
			a.x-=Math.PI/2;
		}
		return new Vector3(Math.sin(a.x)*Math.cos(a.y),Math.cos(a.x),Math.sin(a.x)*Math.sin(a.y)).normalize();
	}



	public static Vector3 fromAngle(Vector a){
		return Vector3.sphere(1,a.x,a.y);
	}



	public static Vector3 sphere(double r,double a,double b){
		return new Vector3(r*Math.sin(a)*Math.cos(b),r*Math.cos(a),r*Math.sin(a)*Math.sin(b));
	}










	@Override public String toString(){
		return String.format("Vector3(x=%f, y=%f, z=%f)",this.x,this.y,this.z);
	}
}
