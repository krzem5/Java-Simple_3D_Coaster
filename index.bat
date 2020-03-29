echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac -cp com/krzem/simple_3d_coaster/modules/jogl-all.jar;com/krzem/simple_3d_coaster/modules/jogl-all-natives-windows-i586.jar;com/krzem/simple_3d_coaster/modules/gluegen-rt.jar;com/krzem/simple_3d_coaster/modules/gluegen-rt-natives-windows-i586.jar; com/krzem/simple_3d_coaster/Main.java&&java -cp com/krzem/simple_3d_coaster/modules/jogl-all.jar;com/krzem/simple_3d_coaster/modules/jogl-all-natives-windows-i586.jar;com/krzem/simple_3d_coaster/modules/gluegen-rt.jar;com/krzem/simple_3d_coaster/modules/gluegen-rt-natives-windows-i586.jar; com/krzem/simple_3d_coaster/Main test.xml
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"