import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.time.LocalDateTime;
/**
 * This is an implementaiton of a data access panel
 * to access information regarding a President's
 * average speech complexity
 * @author Yuyang Hu, Angelina Wu, Issac Zhang
 */
public class Main {
    public static void main(String[] args) {
        String fileName = ""; //Used to catch errors in the speech directory
        try{
            ArrayList<President> presidents = new ArrayList<President>();
            //MAKE SURE US_Presidents_Years.csv FILE IS PRESENT IN DIRECTORY
            //SHOULD BE INCLUDED IN THE GITHUB DIRECTORY, PROJECT WILL NOT RUN WITHOUT IT
            File presidentFile = new File("US_Presidents_Years.csv");
            Scanner s = null;
            try{
                s = new Scanner(presidentFile);
            }catch(FileNotFoundException e){
                System.err.println("Error: File Not Found");
                System.exit(1);
            }
            s.nextLine();
            //Converts an index of all the presidents and the years they took office
            // from a .csv file to an Arraylist of Presidents
            while(s.hasNextLine()){
                String line = s.nextLine();
                Scanner l =  new Scanner(line);
                l.useDelimiter(",");
                String name = l.next();
                int year =  l.nextInt();
                President p = new President(name, year);
                presidents.add(p);
            }
            //Scans through the directory given while running,
            // adding the speeches in the directory to their matching presidents
            // before summing their complexities
            String directory = args[0];
            File dir = new File(directory);
            File[] files = dir.listFiles();
            if(dir.isDirectory()) {
                for(File file : files){
                    if(file.getName().equalsIgnoreCase(".DS_Store")){ //Skip DS Store file, only applicable to Macs
                        continue;
                    }
                    fileName = file.getName(); //debugging, to be removed
                    if(file.isFile()) {
                        Speech speech = new Speech(file.getAbsolutePath());
                        int presidentIndex = -1;
                        for(int i = 0; i < presidents.size(); i++){
                            if(presidents.get(i).getName().equalsIgnoreCase(speech.getPresidentName())){
                                presidentIndex = i;
                                break;
                            }
                        }
                        if(presidentIndex != -1) {
                            presidents.get(presidentIndex).addSpeech(speech);
                        }
                    }
                }
                for(President p : presidents){
                    p.finalComplexity();
                }
            }
            //Initalizes and displays the screen for information access
            Scanner in = new Scanner(System.in);
            String input = "";
            initalizeSelection();
            do{
                input = in.nextLine();
                Scanner l =  new Scanner(input);
                String choice = l.next();
                if(choice.equalsIgnoreCase("name")){
                    String name = "";
                    while(l.hasNext()){
                        name += l.next() + " ";
                    }
                    name = name.substring(0, name.length()-1);
                    for(President p : presidents){
                        if(p.getName().equalsIgnoreCase(name)){
                            System.out.println(p);
                        }
                    }
                }
                //Exports the data to a .csv file, formatted based off the current date + time
                else if(choice.equalsIgnoreCase("export")){
                    String[] headers = {"President", "# of Speeches", "Inauguration Year", "Speech Complexity"};
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    //CHANGE FILE PATH TO DESIRED EXPORT LOCATION, OTHERWISE THE SYSTEM WILL EXPORT TO THE PROJECT DIRECTORY
                    String filepath = "/Users/yuyang/Documents/presidentialSpeechesOutput/" + currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";
                    try{
                        FileWriter writer = new FileWriter(filepath);
                        writer.append(String.join(",", headers));
                        writer.append("\n");
                        for(int i = 0; i < presidents.size(); i++){
                            String name = presidents.get(i).getName();
                            String year = Integer.toString(presidents.get(i).getYear());
                            String complexity = presidents.get(i).getComplexity();
                            String numOfSpeeches = Integer.toString(presidents.get(i).getSpeechCount());
                            writer.write(name + "," + numOfSpeeches + ", "
                                    + year + "," + complexity + '\n');
                        }
                        writer.close();
                    }catch(IOException e){
                        System.err.println("Error writing to .csv" + e.getMessage());
                    }
                }
            }while(!input.equalsIgnoreCase("quit"));
            in.close();
        }catch(NoSuchElementException e) {
            System.err.println("Error" + fileName);
        }
    }
    /**
     * Initalizes the selection UI
     */
    public static void initalizeSelection(){
        System.out.println("Avaliable Commands: ");
        System.out.println("NAME + FIRST_NAME + MIDDLE_INITAL + LAST_NAME  - retrieves information for the President provided");
        System.out.println("EXPORT - exports the data to a .csv file");
        System.out.println("QUIT   - exits the program" + '\n');
    }
}