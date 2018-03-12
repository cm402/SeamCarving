import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SeamCarver extends JFrame {
    //declares a BufferedImage variable
    private BufferedImage img;

    private JLabel lbl = new JLabel();

    //constructor to set the SeamCarver image to BufferedImage img parameter
    public SeamCarver(BufferedImage img) {
        this.img = img;
    }

    //returns the img
    private BufferedImage img() {
        return img;
    }

    //returns the width of the image
    public int width() {
        return img.getWidth();
    }

    //returns the height of the image
    public int height() {
        return img.getHeight();
    }

    //finds the vertical seam within the energy matrix of a given image
    public int[] findVerticalSeam(double[][] energyTable) {
        int width = energyTable.length;
        int height = energyTable[0].length;
        int vertices = width * height;
        int[] seam = new int[vertices];
        int[] backtrack = new int[vertices];
        double[] distance = new double[vertices];

        //sets all the distances to the max value of double
        for (int v = 0; v < vertices; v++) {
            if (v < width) {
                distance[v] = energyTable[v][0];
            } else {
                distance[v] = Double.MAX_VALUE;
            }
        }

        //iterates through all indexes of row and columns
        //checks that column position is not less than 0 or greater than width - 1

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width - 1; col++) {
                for (int i = -1; i <= 1; i++) {
                    int currentIndex = width * row + col;
                    int nextIndex = width * (row + 1) + (col + i);

                    //checks if it is the top left corner
                    if (col + i == 0) {
                        if(distance[col + 1] > distance[col] + energyTable[col][row]) {
                            distance[col + 1] = distance[col] + energyTable[col][row];
                            backtrack[col + 1] = col;
                        }

                        //checks if it is the bottom right corner
                    } else if (col + i == width - 1) {
                        if(distance[col] > distance[col - 1] + energyTable[col][row]) {
                            distance[col] = distance[col - 1] + energyTable[col][row];
                            backtrack[col] = col - 1;
                        }

                        //everything else in between
                    } else {
                        if(distance[nextIndex] > distance[currentIndex] + energyTable[col][row]) {
                            distance[nextIndex] = distance[currentIndex] + energyTable[col][row];
                            backtrack[nextIndex] = currentIndex;
                        }
                    }
                }
            }
        }

        int lastIndex = -1;                                             //set minimum of the last index
        double shortestDistance = Double.MAX_VALUE;                     //sets the shortest distance to the max double value
        //iterates through the columns
        for (int i = 0; i < width; i++) {
            int index = width * (height - 1) + i;
            //checks if the distance from that coordinate is lesser than the shortest distance
            if (distance[index] < shortestDistance) {
                //if so, sets the shortest distance to the value of the distance from that coordinate
                shortestDistance = distance[index];
                //sets the last index to the new index
                lastIndex = index;
            }
        }

        //iterates through the rows
        for (int j= 0; j < height; j++) {
            int r = height - 1 - j;
            int c = lastIndex - r * width;
            //sets the coordinate of the seams to the columns
            seam[r] = c;
            //sets the last index to the distance of the last index
            lastIndex = backtrack[lastIndex];
        }
        return seam;
    }

    //finds the horizontal seam
    public int[] findHorizontalSeam(double[][] energyTable) {
        double[][] transposedEnergyTable = transpose(energyTable);

        return findVerticalSeam(transposedEnergyTable);
    }

    //trnasposes the rows to columns and columns to rows of an energy matrix
    public double[][] transpose(double[][] energyTable) {
        int width = energyTable.length;
        int height = energyTable[0].length;

        double[][] transposedEnergyTable = new double[height][width];

        for (int row = 0; row < width(); row++) {
            for (int col = 0; col < height; col++) {
                transposedEnergyTable[col][row] = energyTable[row][col];
            }
        }
        return transposedEnergyTable;
    }

    //removes the horizontal seam
    public void removeHorizontalSeam(int[][] rgb, int[] seam) {

        int width = rgb.length;
        int height = rgb[0].length;

        //iterates through the columns
        for (int col = 0; col < width; col++) {
            //iterates from row at the first seam til the row 1 less than the height
            for (int row = seam[col]; row < height-1; row ++) {
                //updates the rgb array
                rgb[col][row] = rgb[col][row+1];

            }
        }
    }

    //removes the vertical seam
    public void removeVerticalSeam(int[][] rgb, int[] seam) {

        int width = rgb.length;
        int height = rgb[0].length;

        //iterates through the rows
        for (int row = 0; row < height; row++) {
            //iterates from the col at the first seam till the column 1 less than the width
            for (int col = seam[row]; col < width - 1; col ++) {
                //updates the rgb array
                rgb[col][row] = rgb[col+1][row];
            }
        }
    }


    //method to print out a copy of the image after seam removal
    public void outputFile(int[][] rgb, String name, int height, int width) throws FileNotFoundException, IOException {
        BufferedImage newImg = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newImg.setRGB(y, x, rgb[y][x]);
            }
        }
        OutputStream out = new FileOutputStream(name);
        ImageIO.write(newImg, "jpg", out);
    }
}





