package com.fiorano.esb.junit.rmi.serialize;

import java.util.HashMap;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: arun
 * Date: Oct 28, 2009
 * Time: 2:39:28 PM
 * To change this template use File | Settings | File Templates.
 */
class FieldSpy<T> {

    private HashMap<Long, String> uniqueIDs;
    private List<String> classNames = new ArrayList<String>();

    public static boolean Test(String[] args) throws IOException, ClassNotFoundException {

        FieldSpy<?> FS = new FieldSpy();
        FS.uniqueIDs = new HashMap();
        JarFile jarFile = new JarFile(args[0]);
        File file = new File(args[0]);
        URLClassLoader classloader = URLClassLoader.newInstance(new URL[]{file.toURL()});
        Enumeration enums = jarFile.entries();
        String stringName;
        boolean allIsGood = true;

        while (enums.hasMoreElements()) {
            stringName = enums.nextElement().toString();
        
            if (stringName.endsWith(".class")) {
                String newname = stringName.substring(0, stringName.length() - 6).replace("/", ".");
                System.out.println(" Class name is :::*************** "+newname);
                Class<?> c = Class.forName(newname, false, classloader);
                if (!c.isInterface() && c instanceof Serializable) {
                    if (c.getName().indexOf('$') == -1)  //ignores classes with $ in em. (indexOf returns -1 if character is not present. Hence the if condition will be true if $ not present
                        allIsGood &= FS.checkForSerial(c);
                }
            }
        }
        return (allIsGood);

    }

    public boolean checkForSerial(Class c) {
        try {
            int x = 0x0;
            String[] arg = {"static", "final"};
            int m = Modifier.STATIC | Modifier.FINAL;
            Field varField = c.getDeclaredField("serialVersionUID");
            varField.setAccessible(true);
            if (((varField.getModifiers() & x) == x) && varField.getType().toString().equals("long")) {
                if (this.uniqueIDs.containsKey(varField.getLong(c))) {
                    System.out.println("\n\n Key Clash between " + c.getName() + "  and " + this.uniqueIDs.get(varField.getLong(c)));
                    return false;
                } else
                    this.uniqueIDs.put(varField.getLong(c), c.getName());
            }

        } catch (NoSuchFieldException e) {
            System.out.println(" Class \" " + c.getName() + "\" does not have SerialVersionUID");
            classNames.add(c.getName());
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

 public List<String> getClassNamesWOSVUID()
 {
     return classNames;
 }

}
