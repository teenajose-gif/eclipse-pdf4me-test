package test.test;

import com.pdf4me.client.ConvertClient;
import com.pdf4me.client.MergeClient;
import com.pdf4me.client.Pdf4meClient;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class App
{
    public static void main( String[] args )
    {
    	//4 files to convert into pdf
        File file1 = new File("images/arch.jpg");
        File file2 = new File("images/capture.jpg");
        File file3 = new File("images/error.jpg");
        File file4 = new File("images/stack.png");
        //list of files
        List<File> files = new ArrayList<File>();
        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        //token
        String token = "NWM3OTAxNDEtODE2OC00OGM0LWIyMjMtNTQ2OWM5YTlkYjliOlFLZkQxOTQmSzExJW9uZFB3PVZ6WGpTRSZZdHZSVzI3";
        //client
        Pdf4meClient pdf4meClient = new Pdf4meClient("https://api-dev.pdf4me.com", token);
        // setup the convertClient
        ConvertClient convertClient = new ConvertClient(pdf4meClient);

        //for each file in files
        List<File> pdfs = new ArrayList<File>();
		File mergedPdf = new File("mergedPdf.pdf");
        for(int i = 0; i < files.size(); i++) {
        	// conversion and writing the generated PDF to disk
        	File file = files.get(i);
            byte[] generatedPdf = convertClient.convertFileToPdf("file" + i, file);
            try {
    			if(files.size() == 1)
					FileUtils.writeByteArrayToFile(mergedPdf, generatedPdf);
				else {
					File converted = new File("output/generatedPdf"+ i + ".pdf");
    				FileUtils.writeByteArrayToFile(converted, generatedPdf);
        			pdfs.add(converted);
        			System.out.println("single pdf conversion complete");
				}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
    	
        if(pdfs.size() == files.size() && pdfs.size() > 1) {
        	try {
        		MergeClient mergeClient = new MergeClient(pdf4meClient);
        		byte[] mergedByteArray;
        		for(int i = 0; i < pdfs.size(); i++) {
        			if(i == 0) {
        				//for first and second file
        				mergedByteArray = mergeClient.merge2Pdfs(pdfs.get(i), pdfs.get(i + 1));
            			FileUtils.writeByteArrayToFile(mergedPdf, mergedByteArray);
            			pdfs.get(i).delete();
            			pdfs.get(i + 1).delete();
            			i++;
        			} else {
        				//for third onwards
        				mergedByteArray = mergeClient.merge2Pdfs(mergedPdf, pdfs.get(i));
            			FileUtils.writeByteArrayToFile(mergedPdf, mergedByteArray);
            			pdfs.get(i).delete();
        			}
        		}
    			System.out.println("all files merged");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
    }
}