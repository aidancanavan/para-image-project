import java.io.File;
/**
 * Stores a file and probability value
 *
 *
 * @author Aidan Canavan
 * @version 1.0
 */
public class FileProb
{
    // instance variables - replace the example below with your own
    public double prob =0.0;
    public File f;
    /**
     * Constructor for objects of class FileProb
     */
    public FileProb(File f,double prob)
    {
        // initialise instance variables
        this.prob = prob;
        this.f = f;
    }
    public FileProb(File f){
        this.f = f;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setProb(double prob){
        this.prob = prob;
    }
    public double getProb(){
    return prob;
    }
    public File getFile(){
    return f;
    }
}
