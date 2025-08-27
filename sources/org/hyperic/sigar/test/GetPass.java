package org.hyperic.sigar.test;

import java.io.IOException;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/GetPass.class */
public class GetPass {
    public static void main(String[] args) throws Exception {
        try {
            String password = Sigar.getPassword("Enter password: ");
            System.out.println(new StringBuffer().append("You entered: ->").append(password).append("<-").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
