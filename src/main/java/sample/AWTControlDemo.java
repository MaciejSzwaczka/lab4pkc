package sample;

/**
 * Created by maciejszwaczka on 17.11.2018.
 */
import java.awt.*;
import java.awt.event.*;

public class AWTControlDemo {

    private Frame mainFrame;
    private Panel controlPanel;

    public AWTControlDemo(){
        prepareGUI();
    }

    private void prepareGUI(){
        mainFrame = new Frame("Java AWT Examples");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(1, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });


        controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);


    }

    public void showCanvasDemo(){
        controlPanel.add(new MyCanvas());
        mainFrame.setVisible(true);
    }

    class MyCanvas extends Canvas {

        public MyCanvas () {
            setBackground (Color.GRAY);
            setSize(400, 400);
        }

        public void paint (Graphics g) {
            Graphics2D g2;
            g2 = (Graphics2D) g;
            g2.setColor(Color.WHITE);


            for(Localization localization : DBAdapter.localizations){
                int x = (int) Math.round((localization.x - DBAdapter.minx)/(DBAdapter.maxx-DBAdapter.minx)*400);
                int y = (int) Math.round((localization.y - DBAdapter.miny)/(DBAdapter.maxy-DBAdapter.miny)*400);
                g2.drawOval( x, y , 2, 2);
            }
        }
    }
}
