package frontend.common.ui;

import common.Utils;
import frontend.GotchaClientApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observer;
import java.util.regex.Pattern;

/**
 * @author Yang He
 * @version 2018-03-14
 */
public class ServerInputPanel extends JPanel implements Observer {
    private ServerInputPanelModel serverInputPanelModel;
    private JTextField serverAddress;
    private JTextField serverPort;
    private JTable serverList;
    private ServerListTableModel serverListTableModel;


    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validateIP(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public ServerInputPanel(ServerInputPanelModel serverInputPanelModel, JFrame parentFrame) {
        this.serverInputPanelModel = serverInputPanelModel;
        this.serverInputPanelModel.addObserver(this);

        JPanel inputPanel = new JPanel();

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        JLabel ipLabel = new JLabel("IP:");
        inputPanel.add(ipLabel, c);

        serverAddress = new JTextField();
        serverAddress.setColumns(10);
        serverAddress.setText(Utils.getServerHostConfig());
        c.gridx = 1;
        c.gridy = 0;
        inputPanel.add(serverAddress, c);

        JLabel portLabel = new JLabel("PORT:");
        c.gridx = 2;
        c.gridy = 0;
        inputPanel.add(portLabel, c);

        serverPort = new JTextField();
        serverPort.setColumns(4);
        serverPort.setText(Integer.toString(Utils.getServerPortConfig()));
        c.gridx = 3;
        c.gridy = 0;
        inputPanel.add(serverPort, c);

        c.gridx = 4;
        c.gridy = 0;
        JButton search = new JButton("Search");
        inputPanel.add(search, c);

        c.gridx = 5;
        c.gridy = 0;
        JButton enter = new JButton("Enter");
        inputPanel.add(enter, c);

        c.gridx = 0;
        c.gridy = 0;
        this.add(inputPanel, c);

        search.addActionListener(e -> {
            serverInputPanelModel.searchServer(Integer.parseInt(serverPort.getText()));
        });

        enter.addActionListener(e -> {
            String server = serverAddress.getText();

            if (!server.toLowerCase().equals("localhost") && !validateIP(server)) {
                JOptionPane.showMessageDialog(null, "The server address is not valid");
                return;
            }

            Utils.setServerHost(serverAddress.getText());
            Utils.setServerPortStr(serverPort.getText());
            parentFrame.setVisible(false);
            // start the main application
            GotchaClientApp.startMainApp();
        });

        serverListTableModel = new ServerListTableModel(new String[0][2], new String[]{"IP", "PORT"});
        serverList = new JTable(serverListTableModel);
        serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serverList.setDefaultEditor(Object.class, null); // disable editing
        serverList.setPreferredSize(new Dimension(100, 150));
        serverList.setPreferredScrollableViewportSize(new Dimension(100,
                (int) serverList.getPreferredSize().getHeight())); // it works!
        serverList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        serverList.getSelectionModel().addListSelectionListener(e -> {
            int row = serverList.getSelectedRow();
            if (row >= 0) {
                String ip = serverList.getValueAt(row, 0).toString();
                serverAddress.setText(ip);
            }
        });
        JScrollPane listPanel = new JScrollPane(serverList);

        c.gridx = 0;
        c.gridy = 1;
        this.add(listPanel, c);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        this.serverListTableModel.updateData(this.serverInputPanelModel.getServerList());
    }

    // TODO
    private class ServerListTableModel extends DefaultTableModel {
        private Object[] tempColumnNames;

        public ServerListTableModel(Object[][] data, Object[] columnNames) {
            tempColumnNames = columnNames;
            setDataVector(data, columnNames);
        }

        private void updateData(ArrayList<String[]> servers) {
            setDataVector(convert(servers), tempColumnNames);
            fireTableDataChanged();
        }

        private String[][] convert(ArrayList<String[]> servers) {
            String[][] res = new String[servers.size()][];
            for (int i = 0; i < servers.size(); i++) {
                res[i] = servers.get(i);
            }
            return res;
        }
    }
}
