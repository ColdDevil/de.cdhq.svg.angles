package de.cdhq.svg.angles;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates SVG files drawing a specified angle between two lines and an arc connecting them.
 * 
 * @author Florian Weßling <flo@cdhq.de>
 *
 */
public class CreateAngles {
    // width and height of output images
    private int                width     = 500;
    private int                height    = 500;

    // style of the lines
    private String             lineStyle = "fill:none;stroke-width:1.7;stroke-linejoin:miter;stroke-miterlimit:100";           // stroke-linecap:round";
    // style of the arc connecting the lines
    private String             arcStyle  = "fill:none;stroke:black;stroke-width:1.4;stroke-miterlimit:4;stroke-dasharray:none";

    // list of angles to create
    private ArrayList<Integer> angleList;
    
    public static void main(String[] args) {
        (new CreateAngles()).start();
    }

    public CreateAngles() {
        // create several angles
        angleList = new ArrayList<Integer>();
        angleList.add(10);
        angleList.add(90);
        angleList.add(130);
        angleList.add(123);
        angleList.add(17);
        angleList.add(2);
        angleList.add(215);
        angleList.add(306);
    }
    
    public void start() {
        int radius = 50;
        int lineRadius = 200;
        Point center = new Point(width / 2, height / 2);

        // create image files
        for (Integer angle : angleList) {
            String filename = "angle_" + angle + "deg";

            StringBuffer content = new StringBuffer();
            content.append(angle(center.x, center.y, radius, angle));
            content.append(angleLines(center, lineRadius, angle, "black"));

            writeFile(wrap(content.toString()), "./output/" + filename + ".svg");
        }
    }

    public String angle(int centerX, int centerY, int radius, int angle) {
        Point arc2start = new Point(centerX + radius, centerY);
        Point arc2end = polarToCartesian(centerX, centerY, radius, 180 - angle);

        return arc(arc2start.x, arc2start.y, arc2end.x, arc2end.y, radius, angle > 180);
    }

    public String angleLines(Point center, int lineRadius, int angle, String col) {
        Point a = new Point(center.x + lineRadius, center.y);
        Point b = polarToCartesian(center.x, center.y, lineRadius, 180 - angle);

        return "<path d=\"M " + a.x + " " + a.y + " L " + center.x + " " + center.y + " L " + b.x + " " + b.y + " \" style=\"stroke:" + col + ";" + lineStyle + "\" />";
    }

    public String arc(int startX, int startY, int endX, int endY, int radius) {
        return arc(startX, startY, endX, endY, radius, false);
    }

    public String arc(int startX, int startY, int endX, int endY, int radius, boolean isLarge) {
        return "<path d=\"M " + startX + " " + startY + " A " + radius + " " + radius + " 0 " + (isLarge ? "1" : "0") + " 0 " + endX + " " + endY + " \" style=\"" + arcStyle + "\" />";
    }

    public String dot(int x, int y) {
        return "<ellipse cx=\"" + x + "\" cy=\"" + y + "\" rx=\"5\" ry=\"5\" />";
    }

    public String dot(Point p) {
        return dot(p.x, p.y);
    }

    public String line(int ax, int ay, int bx, int by) {
        return line(ax, ay, bx, by, "black");
    }

    public String line(int ax, int ay, int bx, int by, String col) {
        return "<path d=\"M " + ax + " " + ay + " L " + bx + " " + by + " \" style=\"stroke:" + col + ";" + lineStyle + "\" />";
    }

    public String line(Point a, Point b) {
        return line(a.x, a.y, b.x, b.y);
    }

    public String line(Point a, Point b, String col) {
        return line(a.x, a.y, b.x, b.y, col);
    }

    public Point polarToCartesian(int centerX, int centerY, int radius, int angleInDegrees) {
        double angleInRadians = angleInDegrees * Math.PI / 180.0d;
        double x = centerX - radius * Math.cos(angleInRadians);
        double y = centerY - radius * Math.sin(angleInRadians);

        return new Point((new Double(x)).intValue(), (new Double(y)).intValue());
    }

    public String wrap(String input) {
        StringBuffer output = new StringBuffer();

        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height + "\" xml:space=\"preserve\">");
        output.append(input);
        output.append("</svg>");

        return output.toString();
    }

    public void writeFile(String content, String filename) {
        File outputfile;
        FileWriter fw;
        BufferedWriter bw;

        try {
            outputfile = new File(filename);
            fw = new FileWriter(outputfile);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
        }
    }
}
