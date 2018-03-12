import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RunSeamCarver {
    final static String USAGE = "Usage: RunSeamCarver <Image Path> <Number of seams> <Direction> <outputPath>";
    public static void main(String args[]) throws Exception {
        RunSeamCarver rsc = new RunSeamCarver();
        BufferedImage img = null;
        File file = null;

        //checks that there are 3 command line arguments
        //else prints usage message
        if (args.length == 4) {
            String filepath = args[0];                                  //the filepath of the image to be used
            String numSeams = args[1];                                  //the number of seams to be removed
            String direction = args[2];                                //direction of seam to be removed, I.E vertical/horizontal
            String outputPath = args[3];                                //the outputpath of the image

            int number = Integer.parseInt(numSeams);                  //converts the string to integer

            try {
                //storing the image file path in the file object
                //f = new File(filepath);
                file = new File(filepath);
                //reading the image file
                img = ImageIO.read(file);
                //catching the IOException if the image cant be read
            } catch (IOException e) {
                System.out.println("Image not found: " + e.getMessage());
            }

            //getting the width and height of the image
            int width = img.getWidth();
            int height = img.getHeight();

            //EnergyCalculator and SeamCarver objects
            EnergyCalculator ec = new EnergyCalculator(img);
            SeamCarver sc = new SeamCarver(img);

            //defines the direction
            if (direction.equals("vertical")) {
                rsc.seamCarvingVertical(filepath, outputPath, ec.getRGB(img), number);
            } else if (direction.equals("horizontal")) {
                rsc.seamCarvingHorizontal(filepath, outputPath, ec.getRGB(img), number);
            }
            //rsc.seamCarvingVertical(filepath, outputPath, ec.getRGB(img), 300);


            rsc.showPic(img, width, height);
            rsc.showPic(rsc.getNewImage(outputPath), width, height);

            System.out.println("Width: " + width + " pixels");
            System.out.println("Height: " + height + " pixels");
        } else {
            System.out.println(USAGE);
        }
    }


    //method to carve a vertical seam
    public void seamCarvingVertical(String file, String output, int [][] rgb, int times) throws Exception {
        File f;
        f = new File(file);
        BufferedImage img = ImageIO.read(f);
        EnergyCalculator ec = new EnergyCalculator(img);
        SeamCarver sc = new SeamCarver(img);

        int[] verticalSeam = sc.findVerticalSeam(ec.energy(ec.image));

        sc.removeVerticalSeam(ec.getRGB(img), verticalSeam);
        sc.outputFile(rgb,output, (rgb.length - 1 - times), (rgb[0].length));
    }

    //method to carve a horizontal seam
    public void seamCarvingHorizontal(String file, String output, int [][] rgb, int times) throws Exception {
        File f;
        f = new File(file);
        BufferedImage img = ImageIO.read(f);
        EnergyCalculator ec = new EnergyCalculator(img);
        SeamCarver sc = new SeamCarver(img);

        int[] horizontalSeam = sc.findHorizontalSeam(ec.energy(img));

        sc.removeHorizontalSeam(ec.getRGB(img), horizontalSeam);
        sc.outputFile(rgb,output, (rgb.length), (rgb[0].length - 1 - times));
    }

    public BufferedImage getNewImage(String output) {
        BufferedImage newImage = null;
        File g = null;
        try {
            //storing the image file path in the file object
            //f = new File(filepath);
            g = new File(output);
            //reading the image file
            newImage = ImageIO.read(g);
            //catching the IOException if the image cant be read
        } catch (IOException e) {
            System.out.println("Image not found: " + e.getMessage());
        }
        return newImage;
    }

    private void showPic(BufferedImage pic, int width, int height) {
        JFrame frame = new JFrame();
        JLabel lbl = new JLabel();
        int iwidth = width / 2;
        int iheight = height / 2;
        Image img = pic.getScaledInstance(iwidth, iheight, Image.SCALE_DEFAULT);
        frame.getContentPane().setLayout(new FlowLayout());
        lbl.setIcon(new ImageIcon(img));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(lbl);
        frame.pack();
        frame.setVisible(true);
    }
}
