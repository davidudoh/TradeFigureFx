package tradefigurefx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javax.swing.Timer;


public class TradeFigureFx extends Application implements Respond,ActionListener{
    public static int DiceNumber = 4;
    
    final Rotate rx = new Rotate(0,Rotate.X_AXIS);
    final Rotate ry = new Rotate(0,Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0,Rotate.Z_AXIS);
    final Translate trans = new Translate();
    
    double angX,angY,angZ;
    
    final Dice[] dice = new Dice[DiceNumber];
        
    Timer timer = new Timer(7000,this);
    
    int bias = Dice.NORMAL_BIAS;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    public final void startMain(Stage s) throws Exception{
        Group gr = new Group();
        for(int i = 0;i < 3;i++){
            final Parent g = FXMLLoader.load(this.getClass().getResource("FXMLTradeFigure.fxml"));
            g.onMouseDraggedProperty().addListener(e->{
                g.translateXProperty().add(10);//e.getX());
                g.translateYProperty().add(10);
            });
            /**
            g.setOnMouseDragged(e->{
                g.translateXProperty().add(10);//e.getX());
                g.translateYProperty().add(10);//e.getY());
            });
            */ 
            gr.getChildren().add(g);
        }
       Scene sc = new Scene(gr,500,700,true);
        sc.fillProperty().setValue(Color.GRAY);
        s.setScene(sc);
        
        s.getScene().setCamera(new PerspectiveCamera());
        s.show();  
    }
   public final void start(Stage s) throws Exception{
       
       Group g = new Group();
        int x = 100,y = 100,w = 50,r = 5,sp = 20;
        for(int i = 0;i < dice.length;i++){
            dice[i] = new CubicDice(x,y,w,r);
            g.getChildren().add(((CubicDice)dice[i]).createDice());
            x = x + w+sp;
        }
        
        g.setOnMouseClicked(e ->{
            timer.start();
            actionPerformed(null);
            
            //for(Dice d:dice)d.roll(Dice.NORMAL_BIAS, this);
            
        });
        
        Scene sc = new Scene(g,500,700,true);
        sc.fillProperty().setValue(Color.GRAY);
        s.setScene(sc);
        
        s.getScene().setCamera(new PerspectiveCamera());
        s.show();
   }
    
    
    int[] faces = new int[DiceNumber];
    int index=0;
    @Override
    public synchronized void exec(Dice d) {
        index++;
        for(int i = 0; i < dice.length;i++){
            if(d == dice[i]){
                faces[i] = d.getFace();
                break;
            }
        }
        if(index == DiceNumber){
            setItems();
            index=0;
        }
    }
    
    int itemsToBuy;
    int[] itemsIndexes;
    private synchronized void setItems() {
        itemsToBuy = sumOfFaces();
        
        Random r = new Random(System.nanoTime());
        
        itemsIndexes = new int[itemsToBuy];
        int itemOnShelf = 100;
        
        for (int i = 0; i < itemsIndexes.length; i++) {
            itemsIndexes[i] = 1 + r.nextInt(itemOnShelf);
        }
        display();
    }
    
    
    int sumOfFaces(){
        int s = 0;
        for(int i: faces)s += i;
        return s;
    }
    
    int num = 1;
    private void display() {
        System.out.println("Roll "+num+": " + itemsToBuy+"\nRack \tItem number");
        for (int i = 0; i < itemsIndexes.length; i++) {
            System.out.println((i + 1) + "\t" + itemsIndexes[i]);
        }
        num++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Dice d:dice)d.roll(bias, this);
    }
}













