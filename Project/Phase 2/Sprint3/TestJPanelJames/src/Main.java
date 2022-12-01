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
        TaskProgress tp = new TaskProgress(width/2 - 5,heigth);
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

        TaskProgress tp2 = new TaskProgress(width/2 - 5,heigth);
        tp2.isCentered(false);
        tp2.isFormatted(true);
        tp2.setGap(5);
        tp2.setBounds(width/2 + 5,0);
        tp2.addTask("Task Boazona",95);
        tp2.addTask("Taskzona",5);
        JPanel panel2 = tp2.panel();

        frame.add(panel);  //Adiciona ele na frame
        frame.add(panel2);
        frame.setVisible(true);

        //Tests with Progress bar...
        int a = tp.getProgress("Task2");
        while(a < 100){
            Thread.sleep(1000);
            tp.addProgress("Task2",5);
            tp.addProgress("Task1Banana",7);
            tp2.setProgress("Taskzona",55);
            a = tp.getProgress("Task2");

        }

    }


}
