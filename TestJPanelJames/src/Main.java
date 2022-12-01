import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int width = 800;
        int heigth = 400;
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setTitle("Java stuff");
        frame.setSize(width,heigth);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        //Iniciando a minha framework :)
        TaskProgress tp = new TaskProgress(width,heigth);
        tp.isCentered(true);
        tp.isFormatted(true);

        tp.setGap(10);

        //adicionando as tasks
        int currV1 = 30;
        progressBar.setValue(currV1);
        tp.addTask("Task1Banana",progressBar); //Pode adicionar uma progress bar
        tp.addTask("Task2",60); //Ou um numero e ele la cria depois sozinho
        tp.addTask("Task3",80);
        tp.addTask("Task4",100);
        tp.addTask("Task443",5);
        tp.addTask("Task23",15);


        JPanel panel = tp.panel();  //Recebe o painel magicamente criado com as tasks

        frame.add(panel);  //Adiciona ele na frame
        frame.setVisible(true);

        //Tests with Progress bar...
        JProgressBar p2 = tp.getProgressBarByTask("Task2");
        int currV2 = p2.getValue();
        while(currV1 < 100){
            Thread.sleep(1000);
            //System.out.print("Task1 value: ");
            currV1 += 10;
            progressBar.setValue(currV1);
            //System.out.println(currV1);

            //System.out.print("Task2 value: ");
            currV2 += 5;
            p2.setValue(currV2);
            //System.out.println(currV2);



        }

    }


}
