import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * This is an implementaiton of a data access panel
 * to access information regarding a President's
 * average speech complexity
 * @author Yuyang Hu, Angelina Wu, Issac Zhang
 */
public class Main {
    public static void main(String[] args) {
        String fileName = ""; //debugging, to be removed
        try{
            ArrayList<President> presidents = new ArrayList<President>();
            File presidentFile = new File("/Users/yuyang/Documents/Intellij IDEA/Presidential Speech Analysis/US_Presidents_Years.csv");
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
            initalize();
            do{
                input = in.nextLine() + '\n';
                Scanner l =  new Scanner(input);
                String choice = l.next();
                if(choice.equalsIgnoreCase("name")){
                    String name = "";
                    while(l.hasNext()){
                        name += l.next() + " ";
                    }
                    for(President p : presidents){
                        if(p.getName().equalsIgnoreCase(name)){
                            System.out.println(p);
                        }
                    }
                }
            }while(!input.equalsIgnoreCase("quit"));
            in.close();
        }catch(NoSuchElementException e) {
            System.err.println("Error" + fileName);
        }
    }
    /**
     * Initalizes the inital UI
     */
    public static void initalize(){
        System.out.println("Welcome to President Stats!");
        System.out.println("Avaliable Commands: ");
        System.out.println("NAME FIRST_NAME LAST_NAME  - retrieves information for the President provided");
        System.out.println("QUIT   - exits the program" + '\n');
    }
}