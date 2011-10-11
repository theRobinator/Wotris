all : classes
	echo 'Main-class: wotris' > bin/manifest.tmp
	cd bin; jar cvfm wotris.jar manifest.tmp dropper.class prepiece.class song.mid wotris*.class
	-rm bin/manifest.tmp

classes : dropper.java prepiece.java wotris.java
	mkdir -p bin
	-ln -s ../song.mid bin/song.mid
	javac *.java -d bin

clean :
	-rm -r bin/*.class bin/song.mid

spotless : clean
	-rm bin/wotris.jar
