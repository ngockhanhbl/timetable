package sample;

import com.sun.javafx.runtime.VersionInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Calendar");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(1000);
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();


        List<List<Integer>> x = new ArrayList<>();
        List<Integer> x1 = new ArrayList<>();
        List<Integer> x2 = new ArrayList<>();
        List<Integer> x3 = new ArrayList<>();

        List<Integer> x4 = new ArrayList<>();

        x1.add(0);
        x1.add(1);

        x2.add(2);
        x2.add(3);

        x3.add(4);
        x3.add(5);

        x4.add(6);
        x4.add(7);

        x.add(x1);
        x.add(x2);
        x.add(x3);
        x.add(x4);
        List<List<Integer>> myData = new ArrayList<>();
        int loopTotal = 16;

        int lengthOfList = 4;



        List<Integer> depthIndex = new ArrayList<>();
        for (var i = 0; i< lengthOfList; i++){
            depthIndex.add(0);
        }


        for (var i = 0; i< loopTotal; i++){
            List<Integer> temporaryList = new ArrayList<Integer>();
            System.out.println("\n\n\n" +"i: " + i );
            System.out.println("depthIndex.get(123): 0: " + depthIndex.get(0) + "  1: " + depthIndex.get(1) +  "  2: " + depthIndex.get(2) +  "  3: " + depthIndex.get(3));
            for(int j = 0; j < lengthOfList ; j++){
                System.out.println("j: " + j + "\n");
                var data = x.get(j).get(depthIndex.get(j));
                System.out.println("data:x.get(j).get(depthIndex.get(j)): " + x.get(j).get(depthIndex.get(j)));

                temporaryList.add(data);

                //loop toi thang cuoi cung thi check
                if (j == lengthOfList - 1){
                    System.out.println("<3 depthIndex.get(lengthOfList - 1): " + depthIndex.get(lengthOfList - 1));
                    System.out.println("### x.get(lengthOfList - 1).size(): " + x.get(lengthOfList - 1).size());

                    //lay size thang cuoi cung ra check index(value) cua no hien tai coi da full chua

                    int k = lengthOfList - 1;
                    while (true){
                        if (k < 0){
                            System.out.println("out of array");
                            break;
                        }
                        System.out.println("depthIndex.get(k): " + depthIndex.get(k));
                        System.out.println("x.get(k).size(): " + x.get(k).size());

                        if (depthIndex.get(k) == x.get(k).size()  ){
                            //nothing do, just minus k below
                            k--;
                        }else if (  (k == lengthOfList - 1) && depthIndex.get(k) + 1 == x.get(k).size() ) {
                            k--;
                            while (k != -1 && depthIndex.get(k) + 1 == x.get(k).size()){
                                System.out.println("k in while: " + k);
                                k--;
                            }
                            System.out.println("k out :  " + k);
                        }
                        else {
                            int newDepthIndex = depthIndex.get(k) + 1;
                            depthIndex.set(k, newDepthIndex);

                            System.out.println("newDepthIndex: " + newDepthIndex + " k: " + k);
//                            System.out.println("tai vi tri depthindex: " + k + " voi new value la: : " + newDepthIndex);
                            boolean isSet = false;
                            while (true){
//                                System.out.println("depthIndex.get(k) " + depthIndex.get(k));
                                if (k == -1){
                                    break;
                                }
                                if (depthIndex.get(k) == x.get(k).size() ){
                                    System.out.println(depthIndex.get(k) + "  hihihi  " + x.get(k).size());
                                    System.out.println(x.get(k-1).size()  + "---" + depthIndex.get(k - 1));
                                    k--;
                                    System.out.println("K NENE " + k);

                                    isSet = true;
                                }else {
                                    break;
                                }
                            }
                            System.out.println("Is Set = " + isSet);
                            if (isSet && k != -1){
//                                System.out.println(depthIndex.get(k) + 1);
                                depthIndex.set(k, depthIndex.get(k) + 1);
                            }

                            System.out.println(k + " ok ne");
//                            while (depthIndex.get(k) == x.get(k).size()){
//                                k--;
//                            }

                            //reset for children
                            System.out.println("k: "+ k +" m: "+ (lengthOfList - 1));
                            for(int m = lengthOfList - 1; m > k && k != -1; m--){
                                depthIndex.set(m, 0);
//                                System.out.println(m + " depthIndex.get(m, 0): " + depthIndex.get(m));
                            }

                            System.out.println("break");
                            break;
                        }
                    }

                    System.out.println("-----------------------------------------------");
                }
            }
            System.out.println("temporaryList: " + temporaryList);
//            myData.addAll(temporaryList);

        }

//        for (var i = 0; i< loopTotal; i++){
//            System.out.println("i");
//            System.out.println(i);
//
//            List<Integer> temporaryList = new ArrayList<Integer>();
//
//            for(int j = lengthOfList; j > 0 ; j--){
//                System.out.println("j");
//                System.out.println(j);
//
//                System.out.println("currentIndexOfArray "+ currentIndexOfArray);
//                System.out.println("currentIndexArrayOfArray " + currentIndexArrayOfArray);
//
//                int data;
//
//                if (currentIndexArrayOfArray == j-1){
//                    System.out.println(" from 1 ");
//                    data = x.get(j-1).get(currentIndexOfArray);
//                    currentIndexOfArray++;
//                }else {
//                    data = x.get(j-1).get(0);
//                    System.out.println(" from 2 ");
//                }
//                System.out.println(" ok "+ data);
//
//                temporaryList.add(data);
//                System.out.println("temporaryList");
//                System.out.println(temporaryList);
//                System.out.println(currentIndexOfArray);
//                System.out.println(x.get(currentIndexArrayOfArray).size());
//
//            }
//            if (currentIndexOfArray == x.get(currentIndexArrayOfArray).size()){
//                currentIndexArrayOfArray--;
//                if (currentIndexArrayOfArray == 0){
//                    currentIndexOfArray = 0;
//                }else {
//                    currentIndexOfArray = 0;
//                }
//            }
////     myData.addAll(temporaryList);
//
//            System.out.println("temporaryList done ");
//            System.out.println(temporaryList);
//
//
//        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
