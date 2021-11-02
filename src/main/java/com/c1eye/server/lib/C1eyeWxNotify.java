package com.c1eye.server.lib;

import com.c1eye.server.exception.http.ServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author c1eye
 * time 2021/10/31 10:34
 */
public class C1eyeWxNotify {
    public static String fail() {
        return "false";
    }

    public static String success() {
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }

    public static String readNotify(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServerErrorException(9999);
            }
        }
        return builder.toString();
    }
}
