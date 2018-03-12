import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.PrintWriter;
import java.awt.Point;

public class EnergyCalculator {

    BufferedImage image;

    public double[][] imgEnergy;

    public EnergyCalculator(BufferedImage img) {
        this.image = img;
    }


    /*public int [][] getRGB(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int [][] rgb = new int[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                rgb[x][y] = img.getRGB(x,y);
            }
        }
        return rgb;
    }*/

    /*public double[][] calculatingEnergy(int[][] rgb) throws Exception {
        imgEnergy = new double[width()][height()];

        for (int y = 0; y < height(); y++ ) {
            for (int x = 0;x < width(); x++) {
                int x1 = 0;
                int x2 = 0;
                int y1 = 0;
                int y2 = 0;
                if (x == 0) {
                    x1 = rgb[x][y];
                    x2 = rgb[x+1][y];
                } else if (x == width() - 1) {
                    x1 = rgb[x-1][y];
                    x2 = rgb[x][y];
                } else {
                    x1 = rgb[x-1][y];
                    x2 = rgb[x+1][y];
                }

                if (y == 0) {
                    y1 = rgb[x][y];
                    y2 = rgb[x][y+1];
                } else if (y == height() - 1) {
                    y1 = rgb[x][y-1];
                    y2 = rgb[x][y];
                } else {
                    y1 = rgb[x][y-1];
                    y2 = rgb[x][y+1];
                }

                int redX = Math.abs(((x1 & 0x00ff0000) >> 16) - ((x2 & 0x00ff0000) >> 16));
                int greenX = Math.abs(((x1 & 0x0000ff00) >> 8) - ((x2 & 0x0000ff00) >> 8));
                int blueX = Math.abs((x1 & 0x000000ff) - (x2 & 0x000000ff));
                int redY = Math.abs(((y1 & 0x00ff0000) >> 16) - ((y2 & 0x00ff0000) >> 16));
                int greenY = Math.abs(((y1 & 0x0000ff00) >> 8) - ((y2 & 0x0000ff00) >> 8));
                int blueY = Math.abs((y1 & 0x000000ff) - (y2 & 0x000000ff));;

                double energy = redX + redY + greenX + greenY + blueX + blueY;
                imgEnergy[x][y] = energy;
            }
        }
        return imgEnergy;
    }*/

    //sets the rgb value of an x and y
    public int [][] getRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int [][] rgb = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rgb[x][y] = image.getRGB(x,y);
            }
        }
        return rgb;
    }

    public double[][] energy(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        imgEnergy = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //declaring the RGB values for the right side of the pixel
                int redRightX;
                int greenRightX;
                int blueRightX;

                //checks if the pixel is on the right border, if so sets the right side pixel to the left border
                if (x == width - 1) {

                    redRightX = (image.getRGB(0, y) >> 16) & 0xff;
                    greenRightX = (image.getRGB(0, y) >> 8) & 0xff;
                    blueRightX = image.getRGB(0, y) & 0xff;
                    //otherwise gets the right pixel by gettimg the RBG values of the pixel to the right
                } else {

                    redRightX = (image.getRGB(x + 1, y) >> 16) & 0xff;
                    greenRightX = (image.getRGB(x + 1, y) >> 8) & 0xff;
                    blueRightX = image.getRGB(x + 1, y) & 0xff;
                }


                //declaring the RGB values for the left side of the pixel
                int redLeftX;
                int greenLeftX;
                int blueLeftX;

                //checks if the pixel is on the left border, if so sets the left pixel to the right border
                if (x == 0) {

                    redLeftX = (image.getRGB(width - 1, y) >> 16) & 0xff;
                    greenLeftX = (image.getRGB(width - 1, y) >> 8) & 0xff;
                    blueLeftX = image.getRGB(width - 1, y) & 0xff;
                    //otherwise gets the left pixel by getting the RGB values of the pixel to the left
                } else {

                    redLeftX = (image.getRGB(x - 1, y) >> 16) & 0xff;
                    greenLeftX = (image.getRGB(x - 1, y) >> 8) & 0xff;
                    blueLeftX = image.getRGB(x - 1, y) & 0xff;
                }


                //calculates the difference in RGB values in the x-axis by subtracting the left values from the right
                int redX = redRightX - redLeftX;
                int greenX = greenRightX - greenLeftX;
                int blueX = blueRightX - blueLeftX;


                //declaring the RGB values for the pixel below
                int redDownY;
                int greenDownY;
                int blueDownY;

                //checks if the pixel is on the bottom border, if so sets the pixel below to the top border
                if (y == height - 1) {

                    redDownY = (image.getRGB(x, 0) >> 16) & 0xff;
                    greenDownY = (image.getRGB(x, 0) >> 8) & 0xff;
                    blueDownY = image.getRGB(x, 0) & 0xff;
                    //otherwise gets the pixel below by getting the RGB values of the pixel underneath
                } else {

                    redDownY = (image.getRGB(x, y + 1) >> 16) & 0xff;
                    greenDownY = (image.getRGB(x, y + 1) >> 8) & 0xff;
                    blueDownY = image.getRGB(x, y + 1) & 0xff;
                }

                //declaring the RGB values for the pixel above
                int redUpY;
                int greenUpY;
                int blueUpY;

                //checks if the pixel is on the top border, if so sets the pixel above to the bottom border
                if (y == 0) {

                    redUpY = (image.getRGB(x, height - 1) >> 16) & 0xff;
                    greenUpY = (image.getRGB(x, height - 1) >> 8) & 0xff;
                    blueUpY = image.getRGB(x, height - 1) & 0xff;
                } else {
                    //otherwise gets the pixel above by getting the RGB values of the pixel above
                    redUpY = (image.getRGB(x, y - 1) >> 16) & 0xff;
                    greenUpY = (image.getRGB(x, y - 1) >> 8) & 0xff;
                    blueUpY = image.getRGB(x, y - 1) & 0xff;
                }


                //calculates the difference in the RGB values in the y-axis by subtracting the above values from the below values
                int redY = redDownY - redUpY;
                int greenY = greenDownY - greenUpY;
                int blueY = blueDownY - blueUpY;


                //calculates the total energy of the pixel by adding the square of each of the differences in RGB values for both axis
                double energy = Math.rint(Math.sqrt(Math.pow(redX, 2) + Math.pow(greenX, 2) + Math.pow(blueX, 2)
                        + Math.pow(redY, 2) + Math.pow(greenY, 2) + Math.pow(blueY, 2)));
                imgEnergy[x][y] = energy;

            }
        }
        /*int count = 0;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height;col++) {
                count++;
                System.out.println(energyArray[row][col]);
                System.out.println(count);
            }
        }*/
        //returns the total energy
        return imgEnergy;
    }
}
