 

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collection;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Creates n images given layers.
 * Version 1.0 is specific for my current project
 * Future versions will work with different projects
 * 
 *
 * @author Aidan Canavan
 * @version 1.0
 */
public class ParaPng 
{
    // instance variables
    
    String outputDirectory = "/Users/Aidan Canavan/Desktop/NFT/Parametric Design/outputs/";
    Random generator = new Random(1);
    ArrayList<File> allFiles =new ArrayList<File>();
    ArrayList<ArrayList<FileProb>> sortedFiles = new ArrayList();
    
    File folder;
    
    
    
    public ParaPng(String path, ArrayList<String> fileNameRoots)
    {
        // fileNameRoots must be in layer order
        this.folder = new File(path);
        File[] fileList = folder.listFiles();
        for(File f:fileList){
            allFiles.add(f);
        }
        
        for(String s: fileNameRoots){
            sortedFiles.add(new ArrayList<FileProb>());
            
        }
        //must include blank png with for each fileroot name with no option
        
        for(File f:allFiles){//creates sorted files
            int attributeNumber = 0;
            for(String s: fileNameRoots){
                if(f.getName().contains(s)){
                    sortedFiles.get(attributeNumber).add(new FileProb(f));
                }
                attributeNumber++;
            }
            
        }
        setProbs();
        
        //testing for prob change
        //changeProb(.05,0,0,3);
        //changeProb(.05,0,1,3);
        //changeProb(.05,0,2,3);
        //System.out.println(pickWithOdds(0));
        /*
        for(int x =0;x<sortedFiles.size();x++){
            for(int y =0;y<sortedFiles.get(x).size();y++){
                //System.out.print(" "+sortedFiles.get(x).get(y).getProb());
            }
            //System.out.println();
        }
        */
        
    }
    
    public void changeProb(double newProb,int listNumber, int positionChange,int positionEffect){
        //pick two numbers one to increase prob and one to decrease prob
        //first number gets decreased second increased
        double startingProb = sortedFiles.get(listNumber).get(positionChange).getProb();
        double change = startingProb-newProb;
        sortedFiles.get(listNumber).get(positionChange).setProb(newProb);
        double effectProbNew = sortedFiles.get(listNumber).get(positionEffect).getProb() +change;
        sortedFiles.get(listNumber).get(positionEffect).setProb(effectProbNew);
        
        
    }
    
    public void setProbs(){
        //makes base probs equal between attributes
        for(int j =0;j<sortedFiles.size();j++){
            double probInit = 1.0/(double)sortedFiles.get(j).size();
            //System.out.print(probInit);
            for(int i=0;i<sortedFiles.get(j).size();i++){
                
                sortedFiles.get(j).get(i).setProb(probInit);
                
            }
        }
        
        
    }
    
    public File pickWithOdds(int listNumber){
        //this function uses Random to pick a file based on the probs in FileProb
        
        //this picks a File type because FileProb is no longer useful after prob has been used
        double d =0.0;
        
        double test = this.generator.nextDouble();
        
        System.out.println(test);
        
        for(int i=0;i<sortedFiles.get(listNumber).size();i++){
            d +=sortedFiles.get(listNumber).get(i).getProb();
            if(test<=d){
                return sortedFiles.get(listNumber).get(i).getFile();
            }
        }
        
        
        return sortedFiles.get(listNumber).get(0).getFile();
    }
    
    public ArrayList<File> makeNFT(){
        //makes a list of Files containing attributes
        
        ArrayList<File> output = new ArrayList<File>();
        
        
        
        for(int p=0;p<sortedFiles.size();p++){
            output.add(pickWithOdds(p));
        }
        
        
        
        
        return output;
    }
    
    public ArrayList<ArrayList<File>> makeCollection(int size){
        //makes list of all attribute lists in the collection
        
        //first we need to check to see if NFT is unique and then add to collection
        ArrayList<ArrayList<File>> output = new ArrayList();
        
        ArrayList<File> f = makeNFT();
        for(int i=0;i<size;i++){
        if(!output.contains(f)){
            output.add(f);
        }
        else{i--;}
        f=makeNFT();
        //System.out.println(output.size());
        }
        
        return output;
    }
    
    
    public BufferedImage overlay(BufferedImage bg, BufferedImage fg){
        //overlays two png images
        Graphics2D g = bg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(bg,0,0,null);
        g.drawImage(fg,0,0,null);
        g.dispose();
        
        return bg;
    }
    
    
    public void createOne(ArrayList<File> f,String fileName) throws IOException{
        //makes one image using the list of files(that store each attribute)
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        for(File p:f){
        images.add(ImageIO.read(p));
        }
        BufferedImage output = images.get(0);
        for(int i=1;i<images.size();i++){
            output = overlay(output,images.get(i));
        }
        File outputFile = new File(this.outputDirectory+fileName);
        output = resize(output,350,350); //xxx
        ImageIO.write(output,"png",outputFile);
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
      //standard opensea format that looks the best is 350x350
      //this block can be deleted or the 350x350 inputs can be changed where the xxx comment is above
      Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
      BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
      Graphics2D g2d = dimg.createGraphics();
      g2d.drawImage(tmp, 0, 0, null);
      g2d.dispose();
    
      return dimg;
      }
    public void renderCollection(int collectionSize) throws IOException{
        //renders entire collection of images into output directory
        ArrayList<ArrayList<File>> collection = makeCollection(collectionSize);
        int counter =0;
        
        for(ArrayList<File> f:collection){
            createOne(f,Integer.toString(counter)+".png");
            //System.out.println("made");
            counter++;
        }
    
    }
    public void clearDir(File f){//broken
        String path = f.getPath();
        f.delete();
        File replace = new File(path);
    }
    
    
    public static void main(String[] args) throws IOException{
        ArrayList<String> roots = new ArrayList<String>();
        roots.add("bg");
        roots.add("skin");
        roots.add("head");
        roots.add("shirt");
        roots.add("pants");
        roots.add("shoe");
        roots.add("extra");
        ParaPng p = new ParaPng("/Users/Aidan Canavan/Desktop/NFT/Parametric Design/Crypto Runners",roots);
        p.renderCollection(200);
        //ArrayList<File> test = p.makeNFT();
        //p.createOne(test,"test.png");
        //p.makeCollection(10);
        //p.renderCollection(150);
        //p.clearDir(new File("/Users/Aidan Canavan/Desktop/NFT/Parametric Design/outputs"));
    }

    
}
