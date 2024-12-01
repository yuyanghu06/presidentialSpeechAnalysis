import java.util.*;
/**
 * This is a representation of a president object
 * @author Yuyang Hu, Angelina Wu, Issac Zhang
 */
public class President {
    private String name;
    private int year;
    private double finalComplexity;
    private ArrayList<Speech> speeches;
    /**
     * Declares and initalizes a new President object
     * @param name
     * @param year
     */
    public President(String name, int year){
        this.name = name;
        this.year = year;
        finalComplexity = 0.0;
        speeches = new ArrayList<Speech>();
    }
    /**
     * Adds a new speech object to the list
     * @param speech
     * @return true if added, false otherwise
     */
    public boolean addSpeech(Speech speech){
        return speeches.add(speech);
    }
    /**
     * Sets the average complexity of a President's speeches
     */
    public void finalComplexity() {
        double sum = 0;
        for(Speech s : speeches){
            sum += s.complexity();
        }
        finalComplexity = sum/((double) speeches.size());
    }
    /**
     * Returns the name of the President
     * @return name
     */
    public String getName(){
        return name;
    }
    /**
     * Returns the year the President took office
     * @return year
     */
    public int getYear(){
        return year;
    }
    /**
     * Returns a String representation of a president
     * @return
     */
    public String toString(){
        return name + " " + year + " " + finalComplexity;
    }
}
