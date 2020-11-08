JAVADIR=/usr/share/java
JARNAME=woo-app

all:
	(cd src; javac -cp /home/tenjas/Documents/project/po-uuilib/po-uuilib.jar:../../woo-core/woo-core.jar -encoding UTF-8 `find . -name \*.java`)
	(cd src; jar cvf ../$(JARNAME).jar `find . -name \*.class -o -name \*.java` )

clean:
	$(RM) $(JARNAME).jar `find . -name \*.class`
