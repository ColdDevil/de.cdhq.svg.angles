package de.cdhq.svg.angles;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DrawStuff {

    int                width     = 500;
    int                height    = 500;

    String             lineStyle = "fill:none;stroke-width:1.7;stroke-linejoin:miter;stroke-miterlimit:100";            // stroke-linecap:round";
    String             arcStyle  = "fill:none;stroke:black;stroke-width:1.4;stroke-miterlimit:4;stroke-dasharray:none";

    ArrayList<Integer> angleList;

    public DrawStuff() {
        angleList = new ArrayList<Integer>();
        angleList.add(60);
        angleList.add(43);
        angleList.add(59);
        angleList.add(85);
        angleList.add(91);

        angleList.add(123);
        angleList.add(18);
        angleList.add(27);
        angleList.add(48);
        angleList.add(55);

        angleList.add(77);
        angleList.add(90);
        angleList.add(10);
        angleList.add(64);
        angleList.add(100);

        angleList.add(140);
        angleList.add(210);
        angleList.add(96);
        angleList.add(39);
        angleList.add(88);

        angleList.add(180);
        angleList.add(57);
        angleList.add(35);
        angleList.add(99);
        angleList.add(245);

        angleList.add(300);
        angleList.add(66);
        angleList.add(30);
        angleList.add(40);
        angleList.add(102);

        angleList.add(85);
        angleList.add(62);
        angleList.add(50);
        angleList.add(144);
        angleList.add(200);

        angleList.add(80);
        angleList.add(20);
        angleList.add(35);
        angleList.add(60);

        angleList.add(120);
        angleList.add(77);
        angleList.add(98);
        angleList.add(160);

        angleList.add(70);
        angleList.add(48);
        angleList.add(160);
        angleList.add(50);
    }

    public void start() {

        // "<text x=\"130\" y=\"118\" style=\"font-size:16px;font-style:italic;font-family:Computer Modern, Times New Roman\">r</text>";

        int radius = 50;
        int lineRadius = 200;
        Point center = new Point(width / 2, height / 2);

        int i = 1;
        for (Integer angle : angleList) {
            String filename = "winkel" + (i < 10 ? "0" : "") + i + "_" + angle;

            String content = "";
            content += angle(center.x, center.y, radius, angle);
            content += angleLines(center, lineRadius, angle, "black");
            writeFile(wrap(content), "./output/" + filename + ".svg");

            i++;
        }
    }

    public String angle(int centerX, int centerY, int radius, int angle) {
        Point arc2start = new Point(centerX + radius, centerY);
        Point arc2end = polarToCartesian(centerX, centerY, radius, 180 - angle);
        return arc(arc2start.x, arc2start.y, arc2end.x, arc2end.y, radius, angle > 180);
    }

    public String angleLines(Point center, int lineRadius, int angle, String col) {
        Point a = new Point(center.x + lineRadius, center.y); //polarToCartesian(center.x, center.y, lineRadius, 180 - 0);
        Point b = polarToCartesian(center.x, center.y, lineRadius, 180 - angle);

        return "<path d=\"M " + a.x + " " + a.y + " L " + center.x + " " + center.y + " L " + b.x + " " + b.y + " \" style=\"stroke:" + col + ";" + lineStyle + "\" />";
    }

    public String arc(int startX, int startY, int endX, int endY, int radius) {
        return arc(startX, startY, endX, endY, radius, false);
    }

    public String arc(int startX, int startY, int endX, int endY, int radius, boolean isLarge) {
        String large = isLarge ? "1" : "0";
        return "<path d=\"M " + startX + " " + startY + " A " + radius + " " + radius + " 0 " + large + " 0 " + endX + " " + endY + " \" style=\"" + arcStyle + "\" />";
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
        // System.out.println(x + "," + y);
        return new Point((new Double(x)).intValue(), (new Double(y)).intValue());
    }

    public String wrap(String input) {
        StringBuffer output = new StringBuffer();

        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height + "\" xml:space=\"preserve\">");
        output.append(input);
        output.append("</svg>");

        return output.toString();
    }

    synchronized public void writeFile(String content, String filename) {
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
