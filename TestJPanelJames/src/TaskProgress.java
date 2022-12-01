/**
 * @author Iago Paulo 60198
 */

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskProgress {
    private final int DEFAULT_W = 800;
    private final int DEFAULT_H = 500;
    private final int DEFAULT_TASK_W = 300;
    private final int DEFAULT_TASK_H = 30;

    private int panelW;
    private int panelH;

    private int taskPanelW;
    private int taskPanelH;
    Map<String,JProgressBar> task_progress;

    //Painel principal
    private JPanel painel;

    //Detalhezinhos
    private boolean format;
    private int gap;
    private boolean centered;
    private int n_format;

    public TaskProgress(int panelW, int panelH, int taskPanelW, int taskPanelH) {
        init(panelW,panelH,taskPanelW,taskPanelH);
    }

    public TaskProgress(int panelW, int panelH) {
        init(panelW,panelH,DEFAULT_TASK_W,DEFAULT_TASK_H);
    }
    public TaskProgress() {
        init(DEFAULT_W,DEFAULT_H,DEFAULT_TASK_W,DEFAULT_TASK_H);
    }

    private void init(int panelW, int panelH, int taskPanelW, int taskPanelH){
        this.n_format = 0;
        this.centered = true;
        this.gap = 0;
        this.format = true;
        this.panelW = panelW;
        this.panelH = panelH;
        this.taskPanelW = taskPanelW;
        this.taskPanelH = taskPanelH;
        task_progress = new HashMap<>();
        painel = new JPanel();  //Painel principal
        painel.setLayout(null);
        painel.setBounds(0,0,this.panelW,this.panelH);
    }

    public void addTask(String name, int progress){
        if(!task_progress.containsKey(name)) {
            JPanel tmp = new JPanel();                                                          //Cria o Painel que vai ter a Label e a Progress Bar
            tmp.setBackground(Color.CYAN);                                                      //Mete esta cor bonita para diferenciar do Fundo :)
            int x = centered ? centeredX() : 0;                                                 //Se a variavel "centered" for true, a task fica centrada no Painel PRINCIPAL
            tmp.setBounds(x,(taskPanelH+gap)*task_progress.size(),taskPanelW,taskPanelH);    //Altera a posicao e tamanho do painel
            String n = format ? formatName(name) : name;                                        //Formato ou nao o name? (Para as progress bars nao ficarem desalinhadas)
            tmp.add(new JLabel(n));
            JProgressBar p = new JProgressBar();
            p.setMaximum(100);                                                                  //Progress bar vai de 0 a 100
            p.setMinimum(0);
            p.setValue(progress);
            tmp.add(p);
            task_progress.put(name, p);                                                       //Talvez desnecessario o facto de ser um Mapa mas sla,
            painel.add(tmp);                                                                    //queria garantir de alguma forma mais tarde que nao
        }                                                                                       //podem haver tasks com nomes iguais
    }
    public void addTask(String name, JProgressBar progressBar){
        if(!task_progress.containsKey(name)) {
            JPanel tmp = new JPanel();                                                          //Cria o Painel que vai ter a Label e a Progress Bar
            tmp.setBackground(Color.CYAN);                                                      //Mete esta cor bonita para diferenciar do Fundo :)
            int x = centered ? centeredX() : 0;                                                 //Se a variavel "centered" for true, a task fica centrada no Painel PRINCIPAL
            tmp.setBounds(x,(taskPanelH+gap)*task_progress.size(),taskPanelW,taskPanelH);    //Altera a posicao e tamanho do painel
            n_format = Math.max(n_format,name.length());
            String n = format ? formatName(name) : name;                                        //Formato ou nao o name? (Para as progress bars nao ficarem desalinhadas)
            tmp.add(new JLabel(n));
            tmp.add(progressBar);
            task_progress.put(name, progressBar);
            painel.add(tmp);
        }
    }

    public JProgressBar getProgressBarByTask(String name){
        return task_progress.get(name);
    }

    /**
     * Devolve o painel principal (com as tasks) onde mais tarde o utilizador
     * pode adiciona-lo a frame ou a outro painel (I guess)
     * @return Painel Principal
     */
    public JPanel panel(){
        return painel;
    }
//      +-------------------+
//      | Auxiliary Methods |
//      +-------------------+
    private int centeredX(){
        return panelW/2 - taskPanelW/2;
    }

    private String formatName(String name){
        StringBuilder fName = new StringBuilder(name);
        for (int i = name.length(); i < n_format ; i++)
            fName.append("_");
        return fName.toString();
    }
//      +------------------+
//      |       Sets       |
//      +------------------+
    public void isFormatted(boolean b){
        format = b;
    }
    public void setGap(int v){
        gap = v;
    }
    public void isCentered(boolean b){
        centered = b;
    }


}
