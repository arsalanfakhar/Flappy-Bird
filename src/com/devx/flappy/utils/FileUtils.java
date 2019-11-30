package com.devx.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileUtils {

		//it will read a file and return a string for shader
		private FileUtils() {
			
		}
		
		//load all data from file line by line and return as String
		public static String loadAsString(String file) {
			StringBuilder result=new StringBuilder();
			
			try {
				BufferedReader reader=new BufferedReader(new FileReader(file));
				String buffer="";
				while((buffer=reader.readLine())!=null) {
					result.append(buffer + '\n');
				}
				reader.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return result.toString();
					
		}
}
