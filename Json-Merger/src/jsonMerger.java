import java.io.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsonMerger {

    public static void main(String[] args) throws IOException {

        Scanner read = new Scanner(System.in);
        System.out.println("Folder Path :");
        String Folder = read.nextLine();
        System.out.println("Input File Base Name :");
        String Input_base_name = read.next();
        System.out.println("Output File Base Name :");
        String Output_base_name = read.next();
        System.out.println("Max File Size");
        double max = read.nextDouble();
        int count2=1;
        File dir  = new File(Folder);
        String[] children = dir.list();
        ArrayList<String> files = new ArrayList<String>();
        int j=0;
        if(children == null){
            System.out.println("Either Directory does not exist or it not a directory \n");
        }
        else {
            for(int i=0;i<children.length;i++){
                File f = new File(children[i]);
                String convertCount = Integer.toString(count2);
                if(f.exists()  &&  !f.isDirectory()  && children[i].equals(Input_base_name + convertCount + ".json")) {
                    files.add(children[i]);
                    count2++;
                }
            }
        }

        JSONArray merger = new JSONArray();
        Iterator itr1 = files.iterator();
        while(itr1.hasNext()){
            String i = (String)itr1.next();
            JSONParser parser = new JSONParser();
            try (FileReader reader = new FileReader(i)) {
                Object obj = parser.parse(reader);
                JSONObject list = (JSONObject) obj;
//                Iterator<JSONObject> itr = list.values().iterator();
//                System.out.println(itr);
//
//                list.keySet().forEach(keyStr ->
//                {
//                    Object keyvalue = keyStr;
////                    System.out.println("key: "+ keyStr + " value: " + keyvalue);
//
//                    //for nested objects iteration if required
//                    //if (keyvalue instanceof JSONObject)
//                    //    printJsonObject((JSONObject)keyvalue);
//                });
                JSONArray arr = (JSONArray) list.get("strikers");
                for(Object o : arr) {
                    merger.add(o);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int k=0;
        double total =0;
        int count=1;
        JSONObject jso = new JSONObject();

        try{
            JSONArray arr1 = new JSONArray();
            JSONArray arr2 = new JSONArray();

            for(Object ij : merger) {
                String s =  ij.toString() ;
                total +=  s.length();
                if(total+15 < max) {

                    arr1.add(ij);
                }
                else {
                    total = s.length();
                    FileWriter writer = new FileWriter(Output_base_name+count+".json");
                    jso.put("strikers",arr1);
                    writer.write(jso.toJSONString());
                    writer.close();
                    arr1.clear();
                    arr1.add(ij);
                    count++;
                }
            }
            FileWriter writer = new FileWriter(Output_base_name+count+".json");
            jso.put("strikers",arr1);
            writer.write(jso.toJSONString());
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}


