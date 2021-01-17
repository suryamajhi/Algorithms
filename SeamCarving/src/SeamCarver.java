import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private  Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture){
        if(picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        this.energy = new double    [width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if(!isOnMargin(col, row))
                    energy[col][row] = calculateEnergy(col, row);
                else energy[col][row] = 1000;
            }
        }
    }
    private double calculateEnergy(int col, int row){
        if(isValid(col, row)) throw new IllegalArgumentException();
        return Math.sqrt(deltaXSquare(col, row) + deltaYSquare(col, row));
    }

    // current picture
    public Picture picture(){
        return this.picture;
    }

    // width of current picture
    public int width(){
        return picture.width();
    }

    // height of current picture
    public int height(){
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y){
        if(isValid(x, y)) throw new IllegalArgumentException();
        return energy[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam(){
        int graphNodes = width() * height() + 2;
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(graphNodes);
        for(int i = 1; i <= height(); i++){
            DirectedEdge e = new DirectedEdge(0, 1 + width() * (i - 1),1000);
            DirectedEdge e1 = new DirectedEdge(width() * i,graphNodes - 1, 0);
            G.addEdge(e);
            G.addEdge(e1);
        }

        for(int col= 0; col < width(); col++){
            for(int row = 0; row < height(); row++){
                if(!isValid(col + 1,row - 1)){
                    DirectedEdge e = new DirectedEdge(getVertex(col,row),getVertex(col+1,row-1),energy(col+1,row-1));
                    G.addEdge(e);
                }
                if(!isValid(col + 1,row)){
                    DirectedEdge e = new DirectedEdge(getVertex(col,row),getVertex(col+1,row),energy(col+1,row));
                    G.addEdge(e);
                }
                if(!isValid(col+1,row + 1)){
                    DirectedEdge e = new DirectedEdge(getVertex(col,row),getVertex(col+1,row+1),energy(col+1,row+1));
                    G.addEdge(e);
                }
            }
        }
        DijkstraSP dijkstraSP = new DijkstraSP(G, 0);
        int[] horizontalSeam = new int[width()];
        int i = 0;
        for (DirectedEdge edge:
             dijkstraSP.pathTo(graphNodes - 1)) {
            int vertex = edge.to();
            horizontalSeam[i++] = (vertex -1) /width();
            if(i == width()) break;
        }
        return horizontalSeam;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        int graphNodes = width() * height() + 2;
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(graphNodes);
        for(int i = 1; i <= width(); i++){
            DirectedEdge e = new DirectedEdge(0,i,energy(i-1, 0));
            DirectedEdge e1 = new DirectedEdge(height() * width() - width() + i,graphNodes - 1, 0);
            G.addEdge(e);
            G.addEdge(e1);
        }
        for(int col= 0; col < width(); col++){
            for(int row = 0; row < height(); row++){
                if(!isValid(col-1,row + 1)){
                    DirectedEdge e = new DirectedEdge(getVertex(col,row),getVertex(col-1,row+1),energy(col-1,row+1));
                    G.addEdge(e);
                }
                if(!isValid(col,row + 1)){
                    DirectedEdge e = new DirectedEdge(getVertex(col,row),getVertex(col,row+1),energy(col,row+1));
                    G.addEdge(e);
                }
                if(!isValid(col+1,row + 1)){
                    DirectedEdge e = new DirectedEdge(getVertex(col,row),getVertex(col+1,row+1),energy(col+1,row+1));
                    G.addEdge(e);
                }
            }
        }
        DijkstraSP dijkstraSP = new DijkstraSP(G, 0);
        int[] verticalSeam = new int[height()];
        int i = 0;
        for (DirectedEdge e :
                dijkstraSP.pathTo(graphNodes - 1)) {
            int vertex = e.to();
            verticalSeam[i++] = (vertex - 1) % width();
            if(i >= height()) break;
        }
        return verticalSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        if(seam == null) throw new IllegalArgumentException();
        if(seam.length != width()) throw new IllegalArgumentException();

        for(int i = 0; i < seam.length - 1; i++){
            if(Math.abs(seam[i] - seam[i+1]) > 1) throw new IllegalArgumentException();
            if(seam[i] < 0 || seam[i] > height()) throw new IllegalArgumentException();
            if(i == seam.length - 2)
                if(seam[i+1] < 0 || seam[i+1] > height()) throw new IllegalArgumentException();
        }


        Picture newOne = new Picture(width(), height() - 1);
        for(int col = 0; col < width(); col++){
            for(int row = 0; row < height() - 1; row++){
                if(seam[col] > row){
                    newOne.setRGB(col, row, picture.getRGB(col, row));
                }else {
                    newOne.setRGB(col, row, picture.getRGB(col, row + 1));
                }
            }
        }
        picture = newOne;
        energy = new double[width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if(!isOnMargin(col, row))
                    energy[col][row] = calculateEnergy(col, row);
                else energy[col][row] = 1000;
            }
        }

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        if(seam == null) throw new IllegalArgumentException();
        if(seam.length != height()) throw new IllegalArgumentException();

        for(int i = 0; i < seam.length - 1; i++){
            if(Math.abs(seam[i] - seam[i+1]) > 1) throw new IllegalArgumentException();
            if(seam[i] < 0 || seam[i] > width()) throw new IllegalArgumentException();
            if(i == seam.length - 2)
                if(seam[i+1] < 0 || seam[i+1] > width()) throw new IllegalArgumentException();
        }

        for (int value : seam) {
            if (value < 0 || value >= width())
                throw new IllegalArgumentException();
        }

        Picture newOne = new Picture(width()-1, height());
        for(int row = 0; row < height(); row++) {
            for(int col = 0; col < width() - 1; col++){
                if(seam[row] > col){
                    newOne.setRGB(col, row, picture.getRGB(col, row));
                }else{
                    newOne.setRGB(col, row, picture.getRGB(col + 1, row));
                }
            }
        }
        picture = newOne;
        energy = new double[width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if(!isOnMargin(col, row))
                    energy[col][row] = calculateEnergy(col, row);
                else energy[col][row] = 1000;
            }
        }
    }




    //helper functions
    private double deltaXSquare(int col, int row){
        Color right = picture.get(col + 1 , row);
        Color left = picture.get(col - 1, row);
        int Rx = right.getRed() - left.getRed();
        int Gx = right.getGreen() - left.getGreen();
        int Bx = right.getBlue() - left.getBlue();
        return Rx*Rx + Gx*Gx + Bx*Bx;
    }
    private double deltaYSquare(int col, int row){
        Color right = picture.get(col, row + 1);
        Color left = picture.get(col, row - 1);
        int Rx = right.getRed() - left.getRed();
        int Gx = right.getGreen() - left.getGreen();
        int Bx = right.getBlue() - left.getBlue();
        return Rx*Rx + Gx*Gx + Bx*Bx;
    }

    private boolean isValid(int col, int row) {
        return col < 0 || row < 0 || col >= width() || row >= height();
    }

    private boolean isOnMargin(int col, int row) {
        return col == 0 || row == 0 || row == height() - 1 || col == width() - 1;
    }

    //mapping vertex to coordinate
    private int getVertex(int col, int row){
        return row * width() + col + 1;
    }

    //  unit testing (optional)
    public static void main(String[] args){}

}
