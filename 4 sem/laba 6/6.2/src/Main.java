import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;


public class Main extends JFrame {

    private static final Color3f TEXT_COLOR = new Color3f(Color.BLACK);;
    private static final Color3f BACKGROUND_COLOR = new Color3f(Color.GRAY);;

    private float lightYPos=5.0f;
    private float lightZPos=3.0f;
    private Font font;

    private Material m;
    private JTextField textField;
    private Text3D text;
    private JButton buttonLightColor;
    private Color3f lightColor,lightColor2;

    private Transform3D t;
    private PointLight light1,light2;
    private SimpleUniverse u;


    public Main()
    {
        super("Задание  6.2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);
        add(c, BorderLayout.CENTER);


        u = new SimpleUniverse(c);
        BranchGroup scene = mySceneGraph(c);
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);

        buttonLightColor=new JButton("Color");
        buttonLightColor.addActionListener(ae -> {
            Color color = JColorChooser.showDialog(null, "Choose Color", lightColor.get());
            if (color != null) {
                lightColor.set(color);
            }
            light1.setColor(lightColor);
        });

        c.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                light1.setPosition(25.0f-e.getX()/10.0f, lightYPos, lightZPos);
            }
        });

        textField=new JTextField();
        textField.setText(text.getString());
        textField.addActionListener(e->{
            text.setString(textField.getText());
        });

        JPanel textAndLightPanel = new JPanel(new BorderLayout());
        textAndLightPanel.add(textField, BorderLayout.CENTER);
        textAndLightPanel.add(buttonLightColor, BorderLayout.EAST);


        add(textAndLightPanel, BorderLayout.SOUTH);
    }

    public BranchGroup mySceneGraph(Canvas3D canvas) {
        lightColor = new Color3f(Color.PINK);


        BranchGroup objRoot = new BranchGroup();
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.3);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);


        t = new Transform3D();
        TransformGroup l2Trans = new TransformGroup(t);
        objScale.addChild(l2Trans);

        t = new Transform3D();
        t.set(new Vector3d(0.9, 1.5, 2.0));
        TransformGroup l1Trans = new TransformGroup(t);
        objScale.addChild(l1Trans);


        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Background bg = new Background(BACKGROUND_COLOR);
        bg.setApplicationBounds(bounds);
        objScale.addChild(bg);

        m = new Material(TEXT_COLOR, TEXT_COLOR, TEXT_COLOR, new Color3f(Color.WHITE), 1.0f);
        m.setCapability(Material.ALLOW_COMPONENT_WRITE);
        Appearance a = new Appearance();
        m.setLightingEnable(true);
        a.setMaterial(m);


        font=new Font("TestFont", Font.PLAIN, 1);
        Font3D f3d = new Font3D(font, 0.001, new FontExtrusion(new Line2D.Double(0,0,1,1)));

        text = new Text3D(f3d, "Текст", new Point3f(0.0f, -0.5f, 0.0f));
        text.setAlignment(Text3D.ALIGN_CENTER);
        text.setCapability(Text3D.ALLOW_STRING_WRITE);
        text.setCapability(Text3D.ALLOW_STRING_READ);
        Shape3D s3D2 = new Shape3D(text, a);
        l2Trans.addChild(s3D2);



        light1 = new PointLight();
        light1.setEnable(true);
        light1.setColor(lightColor);
        light1.setAttenuation(1.0f, 0.006f, 0.f);
        light1.setPosition(new Point3f(0.0f, lightYPos, lightZPos));
        light1.setCapability(Light.ALLOW_COLOR_WRITE);
        light1.setCapability(PointLight.ALLOW_STATE_WRITE);
        light1.setCapability(PointLight.ALLOW_POSITION_WRITE);
        light1.setInfluencingBounds(bounds);
        objScale.addChild(light1);


        light2 = new PointLight();
        light2.setEnable(true);
        lightColor2 = new Color3f(Color.BLUE);
        light2.setColor(lightColor2);
        light2.setAttenuation(1.0f, 0.006f, 0.f);
        light2.setPosition(new Point3f(1.0f, 0.0f, 1.0f));
        light2.setCapability(Light.ALLOW_COLOR_WRITE);
        light2.setCapability(PointLight.ALLOW_STATE_WRITE);
        light2.setCapability(PointLight.ALLOW_POSITION_WRITE);
        light2.setInfluencingBounds(bounds);
        objScale.addChild(light2);


        objRoot.compile();

        return objRoot;
    }

    public static void main(String[] args) {
        JFrame app = new Main();
        app.setVisible(true);
    }

}