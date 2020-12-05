import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.NoSuchElementException;

public class Window extends JFrame {
    private JTabbedPane tabbedPane;
    private MyClock clockTab;
    private JPanel imageSpinTab;
    private MotionImage imageSpin;
    private JSlider spinSpeed;
    private JComboBox<String> spinDirection;
    private JPanel diagramTab;
    private JFreeChart chart;

    public Window() {
        super("laba 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension d = new Dimension(700, 700);
        setPreferredSize(d);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        /*________________Task 1___________________*/
        clockTab = new MyClock();
        tabbedPane.add(clockTab, "Task 1");

        /*________________Task 2___________________*/
        imageSpinTab = new JPanel(new BorderLayout());
        spinSpeed = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
        imageSpinTab.add(spinSpeed, BorderLayout.NORTH);

        String directions[] = {"По часовой", "Против часовой"};

        spinDirection = new JComboBox<String>(directions);

        spinDirection.addActionListener(e -> {
            if (spinDirection.getSelectedIndex() == 0)
                imageSpin.setDirection(1);
            else
                imageSpin.setDirection(-1);
        });

        imageSpinTab.add(spinDirection, BorderLayout.SOUTH);
        imageSpin = new MotionImage();
        imageSpinTab.add(imageSpin);
        tabbedPane.add(imageSpinTab, "Task 2");

        /*________________Task 3___________________*/
        diagramTab = new JPanel(new BorderLayout());
        tabbedPane.add(diagramTab, "Task 3");
        initDiagramTab(diagramTab);

    }

    private class MyClock extends JPanel {
        private int sec;
        private Timer clockTimer;

        public MyClock() {
            super();
            sec = 0;
            clockTimer = new Timer(1000, e -> {
                sec = (sec + 1) % 60;
                clockTab.repaint();
            });
            clockTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D clock = (Graphics2D) g;
            clock.setStroke(new BasicStroke(2));
            int maxX = this.getSize().width;
            int maxY = this.getSize().height;
            clock.setColor(Color.BLUE);
            clock.setStroke(new BasicStroke(5));

            clock.draw(new Ellipse2D.Double(0, 0, maxX, maxY));
            clock.draw(
                    new Line2D.Double(
                            maxX / 2,
                            maxY / 2,
                            maxX / 2 + maxX / 2 * Math.sin((double) sec / 60 * 2 * Math.PI),
                            maxY / 2 - maxY / 2 * Math.cos((double) sec / 60 * 2 * Math.PI)
                    )
            );
        }
    }

    private class MotionImage extends JPanel {
        private Image image;
        private Timer spinTimer;
        private double rotation;
        private int direction;
        private final int IMG_SIZE = 100;

        public MotionImage() {
            super(null);
            rotation = 0;
            direction = 1;
            image = new ImageIcon("it.gif").getImage().getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT);


            spinTimer = new Timer(10, e -> {
                double dRotation = spinSpeed.getValue() / 10;
                rotation = (rotation + direction * dRotation) % 360;
                repaint();
            });
            spinTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2d.setRenderingHints(rh);
            g2d.setStroke(new BasicStroke(3));
            int maxX = this.getSize().width - IMG_SIZE;
            int maxY = this.getSize().height - IMG_SIZE;
            double x = maxX / 2 + maxX / 2 * Math.sin(rotation / 180 * Math.PI);
            double y = maxY / 2 - maxY / 2 * Math.cos(rotation / 180 * Math.PI);
            g2d.setColor(Color.GRAY);


            g2d.draw(new Ellipse2D.Double(IMG_SIZE / 2, IMG_SIZE / 2, maxX, maxY));
            g2d.drawImage(image, (int) x, (int) y, null);
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }
    }

    private void initDiagramTab(JPanel tab) {
        try {

            JsonReader reader = new JsonReader(new FileReader("src\\countries"));
            Gson g = new Gson();
            Country[] countries = g.fromJson(reader, Country[].class);

            for (var country : countries) {
                if (country.getPopulation() <= 0)
                    throw new NumberFormatException("Wrong data!");
            }


            DefaultPieDataset dataset = new DefaultPieDataset();
            for (var elem : countries) {
                dataset.setValue(elem.getName(), elem.getPopulation());
            }

            chart = ChartFactory.createPieChart("Топ 6 стран по населению", dataset, true, true, false);


            //Format Label
            PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                    "{0}: {1}", new DecimalFormat("0"), new DecimalFormat("0%"));
            ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);

            // Create Panel
            ChartPanel panel = new ChartPanel(chart);
            tab.add(panel, BorderLayout.CENTER);

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found!");
        } catch (IllegalArgumentException | NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Wrong format: " + e.getMessage());
        }

    }


}