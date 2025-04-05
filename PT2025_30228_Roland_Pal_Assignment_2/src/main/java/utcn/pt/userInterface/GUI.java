package utcn.pt.userInterface;

import utcn.pt.businessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    public GUI(){
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setTitle("Queue Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));

        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));

        JTextField nrOfClientsField = new JTextField(10);
        JTextField nrOfQueueField = new JTextField(10);
        JTextField simulationIntervalField = new JTextField(10);
        JTextField minArrivalField = new JTextField(10);
        JTextField maxArrivalField = new JTextField(10);
        JTextField minServiceField = new JTextField(10);
        JTextField maxServiceField = new JTextField(10);
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"TimeStrategy", "QueueStrategy"});


        panel.add(new JLabel("      Number of Clients:"));
        panel.add(nrOfClientsField);

        panel.add(new JLabel("      Number of Queues:"));
        panel.add(nrOfQueueField);

        panel.add(new JLabel("      Simulation Time (seconds):"));
        panel.add(simulationIntervalField);

        panel.add(new JLabel("      Minimum Arrival Time:"));
        panel.add(minArrivalField);

        panel.add(new JLabel("      Maximum Arrival Time:"));
        panel.add(maxArrivalField);

        panel.add(new JLabel("      Minimum Service Time:"));
        panel.add(minServiceField);

        panel.add(new JLabel("      Maximum Service Time:"));
        panel.add(maxServiceField);

        panel.add(new JLabel("      Strategy:"));
        panel.add(comboBox);


        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Simulation");

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(550, 200));

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nrOfClients = Integer.parseInt(nrOfClientsField.getText());
                int nrOfQueues = Integer.parseInt(nrOfQueueField.getText());
                int simulationInterval = Integer.parseInt(simulationIntervalField.getText());
                int minArrivalTime = Integer.parseInt(minArrivalField.getText());
                int maxArrivalTime = Integer.parseInt(maxArrivalField.getText());
                int minServiceTime = Integer.parseInt(minServiceField.getText());
                int maxServiceTime = Integer.parseInt(maxServiceField.getText());
                String strategy = comboBox.getSelectedItem().toString();


                SimulationManager simulation = new SimulationManager(nrOfClients, nrOfQueues, simulationInterval,
                        minServiceTime, maxServiceTime, minArrivalTime, maxArrivalTime);

                simulation.selectStrategy(strategy);

                Thread thread = new Thread(simulation);
                thread.start();

                textArea.setText("");

                simulation.setTextArea(textArea);

            }
        });


        buttonPanel.add(startButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(scrollPane, BorderLayout.NORTH);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
