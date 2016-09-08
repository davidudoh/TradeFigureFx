package tradefigurefx;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class CubicDice implements Dice{
    int x,y,width,radius;
    
    final Rotate rx = new Rotate(0,Rotate.X_AXIS);
    final Rotate ry = new Rotate(0,Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0,Rotate.Z_AXIS);
    final Translate trans = new Translate();
   
    //for rotation
    private Timeline animation;
    //for translation
    private Timeline animation2;
    
    double angX,angY,angZ;
    
    //speed
    double sp = 0.5d;
    
    byte state = INITIAL_STATE;
  	
    public CubicDice(int x,int y,int w,int r){
        this.x = x;
        this.y = y;
        this.width = w;
        this.radius = r;
    }

    void setup(){
        Random ran = new Random(System.nanoTime());
        
        int N=1+ran.nextInt(3);
        
        animation = new Timeline();
        rx.setOnTransformChanged(e -> rx.setPivotY(trans.getY()));
        ry.setOnTransformChanged(e -> ry.setPivotY(trans.getY()));
        rz.setOnTransformChanged(e -> rz.setPivotY(trans.getY()));
        
       if (N == 1) {
            animation.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                    new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(rx.angleProperty(), 0d),
                    new KeyValue(rz.angleProperty(), 0d),
                    new KeyValue(trans.yProperty(), 0d)
            ),
                    new KeyFrame(Duration.seconds(sp),
                            new KeyValue(rx.angleProperty(), 360d),
                            new KeyValue(ry.angleProperty(), 0d),
                            new KeyValue(rz.angleProperty(), 0d),
                      new KeyValue(trans.yProperty(), 0d)
                    )
            );
        }else if(N == 2){
            animation.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                    new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(rx.angleProperty(), 0d),
                    new KeyValue(rz.angleProperty(), 0d),
                    new KeyValue(trans.yProperty(), 0d)
            ),
                    new KeyFrame(Duration.seconds(sp),
                            new KeyValue(ry.angleProperty(), 360d),
                             new KeyValue(rx.angleProperty(), 0d),
                           new KeyValue(rz.angleProperty(), 0d),
                     new KeyValue(trans.yProperty(), 0d)
                    )
            );
        }else if(N==3){
            animation.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                    new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(rx.angleProperty(), 0d),
                    new KeyValue(rz.angleProperty(), 0d),
                    new KeyValue(trans.yProperty(), 0d)
            ),
                    new KeyFrame(Duration.seconds(sp),
                            new KeyValue(rz.angleProperty(), 360d),
                              new KeyValue(rx.angleProperty(), 0d),
                            new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(trans.yProperty(), 0d)
                    )
            );
        }else{
            //ry.setAngle(0);
            //rz.setAngle(30);
            animation.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                    //new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(rx.angleProperty(), 0d),
                   // new KeyValue(rz.angleProperty(), -30d),
                    new KeyValue(trans.yProperty(), 0d)
            ),
                    new KeyFrame(Duration.seconds(sp),
                           // new KeyValue(rz.angleProperty(), 30d),
                              new KeyValue(rx.angleProperty(), 360d),
                            //new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(trans.yProperty(), 0d)
                    )
            );
            rx.setOnTransformChanged(e->rz.setAngle(45));
        }
          
        
        animation.setCycleCount(10);
        
        animation2 = new Timeline();
        animation2.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                        new KeyValue(trans.yProperty(), 0d),
                        new KeyValue(ry.angleProperty(), 0d),
                        new KeyValue(rx.angleProperty(), 0d),
                        new KeyValue(rz.angleProperty(), 0d)
                          
                ),
                new KeyFrame(Duration.seconds(1*sp),
                        new KeyValue(trans.yProperty(), 400d),
                        new KeyValue(ry.angleProperty(), angX),
                        new KeyValue(rx.angleProperty(), angY),
                        new KeyValue(rz.angleProperty(), angZ)
                )
        );
          
        
        //animation.setCycleCount(Animation.INDEFINITE);
        animation.setOnFinished(e ->{
            setFace();
            
            animation2 = new Timeline();
            animation2.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                    new KeyValue(trans.yProperty(), 0d),
                    new KeyValue(ry.angleProperty(), 0d),
                    new KeyValue(rx.angleProperty(), 0d),
                    new KeyValue(rz.angleProperty(), 0d)
            ),
                    new KeyFrame(Duration.seconds(1 * sp),
                            new KeyValue(trans.yProperty(), 400d),
                            new KeyValue(ry.angleProperty(), angX),
                            new KeyValue(rx.angleProperty(), angY),
                            new KeyValue(rz.angleProperty(), angZ)
                    )
            );
           
            animation2.setOnFinished(f ->{
           synchronized (this) {
                state = Dice.FINAL_STATE;
                notify();
            }
        });
            animation2.play();
        });
        
    }
    
    @Override
    public void setState(int n) {
       state = (byte) n;
    }

    @Override
    public int getState() {
        return state;
    }

    byte bias;
    @Override
    public void roll(int bias, final Respond caller) {
        setup();
        this.bias = (byte) bias;
        state = Dice.INITIAL_STATE;
        animation.play();
        
        Thread t = new Thread(new Runnable(){
            public void run(){
                runCaller(caller,2222221);
            }
        });
        t.setDaemon(true);
        t.start();
    }
    
    private <T extends Respond,B extends Number> void runCaller(T caller,B b) {
        synchronized (this) {
            while (state != Dice.FINAL_STATE) {
                try {
                    wait();
                } catch (Exception e) {
                }
            }
            System.out.println(b.intValue()*b.intValue());
            caller.exec(this);
        }
    }

    @Override
    public int getFace() {
        return face;
    }
    
    int face;
    public int setFace(){
        Random ran = new Random();
        int i = 0;
        if(bias == Dice.NORMAL_BIAS)i=1 + ran.nextInt(6);
        else if(bias == Dice.HIGH_BIAS)i = 4+ran.nextInt(3);
        else if(bias == Dice.LOW_BIAS)i = 1 + ran.nextInt(4);
        face = i;
            
        switch(i){
            case 1:{
                angX = 0;
                angY = 180;
                angZ = 0;
                break;
            }
            case 2:{
                angX = 0;
                angY = 90;
                angZ = 0;
                break;
            }
            
            case 3:{
                angX = 0;
                angY = 0;
                angZ = 0;
                break;
            }
            case 4:{
                angX = 0;
                angY = 270;
                angZ = 0;
                break;
            }
            case 5:{
                angX = 270;
                angY = 0;
                angZ = 0;
                break;
            }
            
            case 6:{
                angX = 90;
                angY = 0;
                angZ = 0;
                break;
            }
        }
        return i;
    }
    
 public Group createDice(){
        int w = width;
        int r = radius;
        
        Color c = Color.BLACK;
        LinearGradient rec = new LinearGradient(0,0,1,1,true,CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0.2,Color.WHITE),new Stop(0.9,Color.GRAY)});
        double s = (w - 6*r)/4;
        double dis = 0.5;
        Group g = new Group();
       
       
        g.getTransforms().addAll(rx,ry,rz,trans);
        
        Group g2 = new Group();
        Rotate rot = new Rotate(90,Rotate.X_AXIS);
        g2.getTransforms().add(rot);
        
        g2.getChildren().addAll(
         RectangleBuilder.create()// 2 top 
                .width(w).height(w)
                .translateY(-w/2)
                .translateX(-w/2)
                .translateZ(w/2)
                .fill(rec)
                .build()
                ,
                
        CircleBuilder.create()
                .radius(r)
                .translateY(-w/2+(s+r))
                .translateX(-w/2+(s+r))
                .translateZ(w/2+dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .centerX(w/2-(s+r))
                .centerY(w/2-(s+r))
                .translateZ(w/2+dis)
                .fill(c)
                .build()
                ,
        RectangleBuilder.create()// 4 bottom 
                .width(w).height(w)
                .translateY(-w/2)
                .translateX(-w/2)
                .translateZ(-w/2)
                .fill(rec)
                .build()
                ,
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(-w/2+(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(w/2-(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(w/2-(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(-w/2+(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build()
                
        );
        
        
        Group g1 = new Group();
        
        g1.getChildren().addAll(
                
        RectangleBuilder.create()// 1 front 
                .width(w).height(w)
                .translateX(-w/2)
                .translateY(-w/2)
                .translateZ(w/2)
                .fill(rec)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateZ(dis+w/2)
                .fill(c)
                .build()
        ,
        RectangleBuilder.create()// 3 back 
                .width(w).height(w)
                .translateX(-w/2)
                .translateY(-w/2)
                .translateZ(-w/2)
                .fill(rec)
                .build() ,
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+s+r)
                .translateY(-w/2+s+r)
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(w/2-(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build()
        );
        
        Group g3 = new Group();
        rot = new Rotate(90,Rotate.Y_AXIS);
        g3.getTransforms().add(rot);
        
       
        g3.getChildren().addAll(
        RectangleBuilder.create()// 5 left 
                .width(w).height(w)
                .translateX(-w/2)
                .translateY(-w/2)
                .translateZ(-w/2)
                .fill(rec)
                .build(),
                
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(-w/2+(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(-w/2+(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(w/2-(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(w/2-(s+r))
                .translateZ(-w/2-dis)
                .fill(c)
                .build(),
       RectangleBuilder.create()// 6 right 
                .width(w).height(w)
                .translateX(-w/2)
                .translateY(-w/2)
                .translateZ(w/2)
                
                .fill(rec)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(-w/2+(s+r))
                .translateZ(w/2+dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(-w/2+2*s+3*r)
                .translateZ(w/2+dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(-w/2+(s+r))
                .translateY(w/2-(s+r))
                .translateZ(w/2+dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(-w/2+(s+r))
                .translateZ(w/2+dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(-w/2+2*s+3*r)
                .translateZ(w/2+dis)
                .fill(c)
                .build(),
        CircleBuilder.create()
                .radius(r)
                .translateX(w/2-(s+r))
                .translateY(w/2-(s+r))
                .translateZ(w/2+dis)
                .fill(c)
                .build()
                
        );
        
       g.getChildren().addAll(g1,g2,g3);
       
       g.setTranslateX(x);
       g.setTranslateY(y);
        return g;
    }   
}