package com.marinabay.cruise.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

public class VelocityUtils {



	public static String merge(VelocityEngine  engine, VelocityContext context, String template) {
        StringWriter outWriter = new StringWriter();
        String result = null;
        if(Velocity.evaluate(context, outWriter, "Parse", template)){
            result = outWriter.toString();
            result = result.replaceAll("[\n\r\t]", "").trim();
        }
		return result;
	}

}
