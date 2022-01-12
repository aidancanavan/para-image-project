 

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
    File folder;
    ArrayList<File> allFiles =new ArrayList<File>();
    ArrayList<FileProb> background = new ArrayList<FileProb>();
    ArrayList<FileProb> skin = new ArrayList<FileProb>();
    ArrayList<FileProb> head = new ArrayList<FileProb>();
    ArrayList<FileProb> shirt = new ArrayList<FileProb>();
    ArrayList<FileProb> pants = new ArrayList<FileProb>();
    ArrayList<FileProb> shoes = new ArrayList<FileProb>();
    ArrayList<FileProb> extra = new ArrayList<FileProb>();
    
    int seedCounter =0;
    
    public ParaPng(String path)
    {
        // initialise instance variables
        this.folder = new File(path);
        File[] fileList = folder.listFiles();
        for(File f:fileList){
            allFiles.add(f);
        }
        
        for(File f:allFiles){
            if(f.getName().contains("bg")){
                background.add(new FileProb(f));
            }
            if(f.getName().contains("skin")){
                skin.add(new FileProb(f));
            }
            if(f.getName().contains("head")|| f.getName().contains("empty")){
                head.add(new FileProb(f));
            }
            if(f.getName().contains("shirt")|| f.getName().contains("empty")){
                shirt.add(new FileProb(f));
            }
            if(f.getName().contains("pants")|| f.getName().contains("empty")){
                pants.add(new FileProb(f));
            }
            if(f.getName().contains("shoe")|| f.getName().contains("empty")){
                shoes.add(new FileProb(f));
            }
            if(f.getName().contains("extra") || f.getName().contains("empty")){
                extra.add(new FileProb(f));
            }
        }
        setProbs();
    }
    public void setProbs(){
        for(FileProb f:background){
            f.setProb(.10);
        }
        for(FileProb f:skin){
            f.setProb(.10);
        }
        for(FileProb f:head){
            f.setProb(.10);
        }
        for(FileProb f:shirt){
            f.setProb(.10);
        }
        for(FileProb f:pants){
            f.setProb(.10);
        }
        for(FileProb f:shoes){
            f.setProb(.10);
        }
        for(FileProb f:extra){
            f.setProb(.10);
        }
        extra.get(0).setProb(.9);
        extra.get(1).setProb(.25);
        pants.get(0).setProb(.01);
        shoes.get(0).setProb(.04);
        shoes.get(6).setProb(.04);
        shirt.get(0).setProb(.05);
        shoes.get(6).setProb(.04);
        
        //shirt
        background.get(2).setProb(.04);
        //System.out.println(shoes.size());
    }
    public ArrayList<File> makeNFT(){
        ArrayList<File> output = new ArrayList<File>();
        Random generator = new Random(seedCounter);
        //System.out.println(generator.nextDouble());
        int at = 0;
        while(true){
            
            if(generator.nextDouble()<background.get(at).getProb()){
                output.add(background.get(at).getFile());
                break;
            }
            
            if(at==background.size() -1){at=0;}
            
            else{at++;}
        }
        at = 0;
        while(true){
            
            if(generator.nextDouble()<skin.get(at).getProb()){
                output.add(skin.get(at).getFile());
                break;
            }
            
            if(at==skin.size() -1){at=0;}
            
            else{at++;}
        }
        at = 0;
        while(true){
            
            if(generator.nextDouble()<head.get(at).getProb()){
                output.add(head.get(at).getFile());
                break;
            }
            
            if(at==head.size() -1){at=0;}
            
            else{at++;}
        }
        at = 0;
        while(true){
            
            if(generator.nextDouble()<shirt.get(at).getProb()){
                output.add(shirt.get(at).getFile());
                break;
            }
            
            if(at==shirt.size() -1){at=0;}
            
            else{at++;}
        }
        at = 0;
        while(true){
            
            if(generator.nextDouble()<pants.get(at).getProb()){
                output.add(pants.get(at).getFile());
                break;
            }
            
            if(at==pants.size() -1){at=0;}
            
            else{at++;}
        }
        at = 0;
        
        while(true){
            
            if(generator.nextDouble()<shoes.get(at).getProb()){
                output.add(shoes.get(at).getFile());
                break;
            }
            
            if(at==shoes.size() -1){at=0;}
            
            else{at++;}
        }
        at = 0;
        while(true){
            
            if(generator.nextDouble()<extra.get(at).getProb()){
                output.add(extra.get(at).getFile());
                System.out.println(extra.get(at).getFile());
                break;
            }
            
            if(at==extra.size() -1){at=0;}
            
            else{at++;}
        }
        
        this.seedCounter++;
        //System.out.println(output.size());
        return output;
    }
    
    public ArrayList<ArrayList<File>> makeCollection(int size){
        //we need to check to see if NFT is unique and then add to collection
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
    
    public File[] makeArray(ArrayList<File> nft){
        File[] output = new File[nft.size()];
        int counter =0;
        for(File f:nft){
            output[counter] = f;
            counter++;
        }
        return output;
    }
    
    public BufferedImage overlay(BufferedImage bg, BufferedImage fg){
        Graphics2D g = bg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(bg,0,0,null);
        g.drawImage(fg,0,0,null);
        g.dispose();
        
        return bg;
    }
    
    
    public void createOne(ArrayList<File> f,String fileName) throws IOException{
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        for(File p:f){
        images.add(ImageIO.read(p));
        }
        BufferedImage output = images.get(0);
        for(int i=1;i<images.size();i++){
            output = overlay(output,images.get(i));
        }
        File outputFile = new File("/Users/Aidan Canavan/Desktop/NFT/Parametric Design/outputs/"+fileName);
        output = resize(output,350,350);
        ImageIO.write(output,"png",outputFile);
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
       Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
      BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
      Graphics2D g2d = dimg.createGraphics();
      g2d.drawImage(tmp, 0, 0, null);
      g2d.dispose();
    
      return dimg;
      }
    public void renderCollection(int collectionSize) throws IOException{
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
        ParaPng p = new ParaPng("/Users/Aidan Canavan/Desktop/NFT/Parametric Design/Crypto Runners");
        //ArrayList<File> test = p.makeNFT();
        //p.createOne(test,"test.png");
        //p.makeCollection(10);
        //p.renderCollection(150);
        //p.clearDir(new File("/Users/Aidan Canavan/Desktop/NFT/Parametric Design/outputs"));
    }

    
}
