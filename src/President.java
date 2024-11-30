import java.util.*;
/**
 * This is a representation of a president object
 * @author Yuyang Hu, Angelina Wu, Issac Zhang
 */
public class President {
    private String name;
    private int century;
    private ArrayList<Speech> speeches;
    /**
     * Declares and initalizes a new President object
     * @param name
     * @param century
     */
    public President(String name, int century){
        this.name = name;
        this.century = century;
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
     * Returns the average complexity of a President's speeches
     */
    public double finalComplexity() {
        double sum = 0;
        for(Speech s : speeches){
            sum += s.complexity();
        }
        return sum/((double) speeches.size());
    }

}
