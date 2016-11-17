/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fruitproject;

import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javaws.ui.SplashScreen;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Personal
 */
public class FruitProject extends Application {
    
    
    public static final String Column1MapKey = "A";
    public static final String Column2MapKey = "B";
    public static final String Column3MapKey = "C";
    
    List<String> addPairs=new ArrayList<String>();
    int rows=0;
    
    @Override
    public void start(Stage primaryStage) {
        first(primaryStage);     
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    public void first(final Stage primaryStage)
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        rows=0;
        addPairs.clear();
        
        Text lb=new Text();
        lb.setText("J-Fruit");
        //lb.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(lb, 1, 0);
        
        final ToggleGroup grp=new ToggleGroup();
        RadioButton rb1=new RadioButton();
        rb1.setText("Add Fruit file");
        rb1.setUserData("add");
        rb1.setToggleGroup(grp);
        rb1.setSelected(true);
        grid.add(rb1, 1, 1);
        
        RadioButton rb2=new RadioButton();
        rb2.setText("Load Fruit file");
        rb2.setUserData("load");
        rb2.setToggleGroup(grp);
        grid.add(rb2, 1, 2);
        
        Label label1 = new Label("Enter File Name:");
        final TextField tfFilename = new TextField ();
        final HBox hb = new HBox();
        hb.getChildren().addAll(label1, tfFilename);
        hb.setSpacing(10);
        hb.setVisible(false);
        tfFilename.setText("");
        grid.add(hb, 1, 3);
        
          grp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
          public void changed(ObservableValue<? extends Toggle> ov,
          Toggle old_toggle, Toggle new_toggle) {
           if (grp.getSelectedToggle() != null) {
          // System.out.println(grp.getSelectedToggle().getUserData().toString());
           if(grp.getSelectedToggle().getUserData().toString()=="load")
               hb.setVisible(true);
           else{
               hb.setVisible(false);
               tfFilename.setText("");
           }
           }
      }
    });

        if(rb2.isSelected()==true)
        {
            hb.setVisible(true);
        }
        
        Button btn = new Button();
        btn.setText("GO");
        grid.add(btn, 1, 4);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
               if(tfFilename.getText()=="")
                second("");
               else
                 second(tfFilename.getText());
                primaryStage.close();
            }
        });
        
        //StackPane root = new StackPane();
        //root.getChildren().add(lb);
        //root.getChildren().add(rb1);
        //root.getChildren().add(rb2);
        //root.getChildren().add(btn);
        
        
        Scene scene = new Scene(grid, 400,450);        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);        
        primaryStage.show();

    }
    public void second(final String pfname)
    {
       
        final Stage st=new Stage();
        Scene scene=null;
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        TableView tv=new TableView();
        
        final TableColumn<Map, String> firstDataColumn = new TableColumn<>("Name");
        final TableColumn<Map, String> secondDataColumn = new TableColumn<>("Amount");
        final TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Remove");
        firstDataColumn.setMinWidth(130);
        secondDataColumn.setMinWidth(130);
        thirdDataColumn.setMinWidth(130);
        
        if(!pfname.equals(""))
        {
 
        firstDataColumn.setCellValueFactory(new MapValueFactory(Column1MapKey));        
        secondDataColumn.setCellValueFactory(new MapValueFactory(Column2MapKey));        
        thirdDataColumn.setCellValueFactory(new MapValueFactory(Column3MapKey));
       
        
        rows=0;
        tv = new TableView<>(generateDataInMap(pfname,addPairs));
        
        }
        
        tv.getColumns().setAll(firstDataColumn,secondDataColumn,thirdDataColumn);
        
        
       // secondDataColumn.setCellFactory(TextFieldTableCell.forTableColumn‌​());
        
       
        ScrollPane sp=new ScrollPane();
        sp.setMinWidth(400);
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setContent(tv);        
        grid.add(sp, 0, 3);        
        
        final ComboBox comboBox = new ComboBox();
        HBox hb1 = new HBox();
        comboBox.setValue("FILE");
        comboBox.getItems().addAll("Save this file","Load a new file");
        Button btnOk = new Button();
        btnOk.setText("OK");
        hb1.getChildren().addAll(comboBox,btnOk);
        hb1.setSpacing(10);
        grid.add(hb1, 0, 1);
        
        
        Label label1 = new Label("Title:");
        final TextField tfFilename = new TextField ();
        tfFilename.setText(getTitle(pfname));                
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, tfFilename);
        hb.setSpacing(10);
        grid.add(hb,0,2);
        
        final Stage ps=new Stage();
        final TableView tv1=tv;
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
                
                if(comboBox.getValue().equals("Load a new file"))
                { first(ps);
                  st.close();  
                }
                else
                {
                   PrintWriter pw=null;
                   try{
                       pw=new PrintWriter("abc.json");
                       BufferedWriter bw=new BufferedWriter(new FileWriter(pfname,false));
                       bw.write("{title:\""+tfFilename.getText()+"\"");
                       bw.write(",fruits:[");
                       for(int i=0;i<rows;i++)
                       { bw.write("{name:\""+String.valueOf(firstDataColumn.getCellData(i))+"\",amount:"+String.valueOf(secondDataColumn.getCellData(i))+"}");
                         if(i!=rows-1)bw.write(",");
                       }
                       bw.write("]}");
                       
                       bw.close();
                  
                   }
                   catch(Exception e)
                   {
                       System.out.println(e.toString());
                   }
                   
                }
            }
        });    
        
        
        Button btn = new Button();
        btn.setText("New Fruit");
        grid.add(btn, 1, 2);
                
       
      
        
               // TableView tv=new TableView();
       // TableColumn Col1 = new TableColumn("Name");
       // TableColumn Col2 = new TableColumn("Amount");
       // TableColumn Col3 = new TableColumn("Remove");
       // tv.getColumns().addAll(Col1, Col2, Col3);

        //sp.setFitToWidth(true);
                        
        
         Image img=new Image("file:music.jpg");
         ImageView iv2 = new ImageView();
         iv2.setImage(img);
         iv2.setFitWidth(200);
         iv2.setPreserveRatio(true);
         iv2.setSmooth(true);
         iv2.setCache(true);
         grid.add(iv2,1,3);
       
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
                
                System.out.println(comboBox.valueProperty());
                st.close();
                third(pfname);
                
            }
        });
        
        scene = new Scene(grid,700,450);        
        st.setScene(scene);
        st.show();
        
          
    }
    
    public void third(final String pfname)
    {
        final Stage st=new Stage();
        GridPane grid = new GridPane();        
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label label1 = new Label("New Fruit");
        grid.add(label1, 1, 0);
        final TextField txtName = new TextField ();
        grid.add(txtName, 1, 1);
        final TextField txtAmount = new TextField ();
        grid.add(txtAmount, 1, 2);
        Button btn = new Button();
        btn.setText("OK");
        grid.add(btn, 1, 3);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
                addPairs.add(txtName.getText());
                addPairs.add(txtAmount.getText());
                st.close();
                second(pfname);
            }
        });


        
        Scene scene = new Scene(grid, 500,500);        
        st.setScene(scene);
        st.show();
        
        
    }
    
     private ObservableList<Map> generateDataInMap(String filename,List<String> addPairs1) {
        int max = 10;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        //JSON Scan
        File f=new File(filename);
        FileInputStream fis;
        try {
        fis=new FileInputStream(f);
        
        int content;
        String result="";
	while ((content = fis.read()) != -1) {
		// convert to char and display it
		//System.out.print((char) content);
                result=result+((char)content);
	}
        JSONObject js=new JSONObject(result);
        String js1=js.getString("title");
        System.out.println(js1);
        JSONArray arr=js.getJSONArray("fruits");
        //System.out.println(arr.get(0));
        int i=0;
        
        while(i<arr.length())
        {
            
            Map<String, String> dataRow = new HashMap<>();
            JSONObject farr=arr.getJSONObject(i);
            //System.out.println(farr.get("amount"));
            //System.out.println(farr.get("name"));
            
            
            String value1 = String.valueOf(farr.get("name"));
            String value2 = String.valueOf(farr.get("amount"));
            String value3 = String.valueOf(farr.get("amount"));
 
            dataRow.put(Column1MapKey, value1);
            dataRow.put(Column2MapKey, value2);
            dataRow.put(Column3MapKey, "REMOVE");
            
            i++;
            allData.add(dataRow);
            rows++;

        }
        
        i=0;
        while(i<(addPairs1.size()))
        {
            Map<String, String> dataRow = new HashMap<>();
            
            dataRow.put(Column1MapKey, addPairs1.get(i));
            dataRow.put(Column2MapKey, addPairs1.get(i+1));
            dataRow.put(Column3MapKey, "REMOVE");
            
            i=i+2;
            allData.add(dataRow);
            rows++;
        }
        
        fis.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        
        
        return allData;
    }
     
    public String getTitle(String fname)
    {
        String js1="";
        File f=new File(fname);
        FileInputStream fis;
        try {
        fis=new FileInputStream(f);
        
        int content;
        String result="";
	while ((content = fis.read()) != -1) {
		// convert to char and display it
		//System.out.print((char) content);
                result=result+((char)content);
	}
        JSONObject js=new JSONObject(result);
        js1=js.getString("title");
        
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        
        return js1;
    }
}
