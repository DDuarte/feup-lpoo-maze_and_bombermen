package pt.up.fe.lpoo.bombermen.gui;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import pt.up.fe.lpoo.bombermen.BombermenServer;
import pt.up.fe.lpoo.bombermen.ClientHandler;
import pt.up.fe.lpoo.bombermen.ClientListener;
import pt.up.fe.lpoo.bombermen.Entity;
import pt.up.fe.lpoo.bombermen.entities.Player;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BombermenServerGui extends JFrame implements ClientListener
{
    @Override
    protected void finalize() throws Throwable
    {
        Stop();
        super.finalize();
    }

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    BombermenServerGui frame = new BombermenServerGui();
                    frame.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getIp() throws Exception
    {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateTextArea(final String text)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                txtConsoleOut.append(text);
            }
        });
    }

    private void redirectSystemStreams()
    {
        OutputStream out = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException
            {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException
            {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private BombermenServer Server;
    private Thread ServerThread;
    private boolean ServerRunning = false;
    private Choice cmbBoxMap;

    private static Color backgroundColor = new Color(240, 240, 240);
    private final JTextArea txtConsoleOut;

    private final List lstClients;

    private void Stop()
    {
        Server.Stop();
        ServerRunning = false;
        ServerThread.interrupt();
        Server = null;
        System.gc();
    }

    /**
     * Create the frame.
     * 
     * @throws Exception
     */
    public BombermenServerGui() throws Exception
    {
        setTitle("Bombermen Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                System.exit(0);
            }
        });
        mnFile.add(mntmExit);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 213 };
        gbl_panel.rowHeights = new int[] { 20, 0, 0 };
        gbl_panel.columnWeights = new double[] { 1.0 };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JTextPane txtpnExtIP = new JTextPane();
        txtpnExtIP.setBackground(backgroundColor);
        txtpnExtIP.setText("External IP: " + getIp());
        txtpnExtIP.setEditable(false);
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        txtpnExtIP.getStyledDocument().setParagraphAttributes(0, txtpnExtIP.getStyledDocument().getLength(), center, false);
        GridBagConstraints gbc_txtpnExtIP = new GridBagConstraints();
        gbc_txtpnExtIP.fill = GridBagConstraints.BOTH;
        gbc_txtpnExtIP.insets = new Insets(0, 0, 0, 5);
        gbc_txtpnExtIP.gridx = 0;
        gbc_txtpnExtIP.gridy = 1;
        panel.add(txtpnExtIP, gbc_txtpnExtIP);

        JTextPane txtPnIntIP = new JTextPane();
        txtPnIntIP.setBackground(backgroundColor);
        txtPnIntIP.setText("Lan IP: " + InetAddress.getLocalHost().getHostAddress());
        txtPnIntIP.setEditable(false);
        txtPnIntIP.getStyledDocument().setParagraphAttributes(0, txtPnIntIP.getStyledDocument().getLength(), center, false);
        GridBagConstraints gbc_txtPnIntIP = new GridBagConstraints();
        gbc_txtPnIntIP.fill = GridBagConstraints.BOTH;
        gbc_txtPnIntIP.gridx = 0;
        gbc_txtPnIntIP.gridy = 0;
        panel.add(txtPnIntIP, gbc_txtPnIntIP);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.SOUTH);

        JTextPane txtPnPort = new JTextPane();
        txtPnPort.setBackground(backgroundColor);
        txtPnPort.setText("Port [1 - 65535]: ");
        txtPnPort.setEditable(false);
        panel_1.add(txtPnPort);

        final JSpinner spnPort = new JSpinner();
        spnPort.setModel(new SpinnerNumberModel(7777, 1, 65535, 1));
        spnPort.setEditor(new JSpinner.NumberEditor(spnPort, "#####"));

        panel_1.add(spnPort);

        final JButton btnStop = new JButton("Stop Server");
        final JButton btnStart = new JButton("Start Server");
        btnStart.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                txtConsoleOut.setText("");
                try
                {
                    Server = new BombermenServer(Integer.parseInt(spnPort.getValue().toString()), cmbBoxMap.getSelectedItem());
                    Server.SetClientListener(BombermenServerGui.this);
                    ServerThread = new Thread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            long millis = System.currentTimeMillis();

                            while (ServerRunning)
                            {
                                int dt = (int) (System.currentTimeMillis() - millis);
                                millis = System.currentTimeMillis();

                                Server.Update(dt);

                                try
                                {
                                    Thread.sleep(20);
                                }
                                catch (InterruptedException e)
                                {
                                }
                            }

                        }
                    });
                    ServerRunning = true;
                    ServerThread.start();

                    btnStart.setEnabled(false);
                    cmbBoxMap.setEnabled(false);
                    spnPort.setEnabled(false);
                    btnStop.setEnabled(true);
                }
                catch (IOException e1)
                {
                    // e1.printStackTrace();
                }
            }
        });

        cmbBoxMap = new Choice();
        cmbBoxMap.setSize(100, cmbBoxMap.getHeight());

        // Directory path here
        String path = "maps";

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                files = listOfFiles[i].getName();
                if (files.endsWith(".bombmap"))
                {
                    cmbBoxMap.add(files.replace(".bombmap", ""));
                }
            }
        }

        panel_1.add(cmbBoxMap);
        panel_1.add(btnStart);

        btnStop.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                Stop();
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                spnPort.setEnabled(true);
                cmbBoxMap.setEnabled(true);
                lstClients.removeAll();
            }
        });
        btnStop.setEnabled(false);
        panel_1.add(btnStop);

        JTextPane txtPnServerIP = new JTextPane();
        txtPnServerIP.setBackground(backgroundColor);
        txtPnServerIP.setEditable(false);
        panel_1.add(txtPnServerIP);

        JPanel panel_2 = new JPanel();
        contentPane.add(panel_2, BorderLayout.CENTER);
        panel_2.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("0px:grow(3)"), FormFactory.GLUE_COLSPEC, }, new RowSpec[] { FormFactory.GLUE_ROWSPEC, }));

        JPanel panel_3 = new JPanel();
        panel_2.add(panel_3, "1, 1, fill, fill");
        panel_3.setLayout(new BorderLayout(0, 0));

        txtConsoleOut = new JTextArea();
        txtConsoleOut.setEditable(false);
        panel_3.add(txtConsoleOut);
        JScrollPane scroll = new JScrollPane(txtConsoleOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        panel_3.add(scroll);

        lstClients = new List();
        panel_2.add(lstClients, "2, 1, fill, fill");

        redirectSystemStreams();
    }

    @Override
    public void AddClient(ClientHandler ch)
    {
        lstClients.add(Integer.toString(ch.Guid) + " - Ping: " + ch.GetIp() + " - " + ch.GetPing() + " ms");
    }

    @Override
    public void RemoveClient(int guid)
    {
        String[] items = lstClients.getItems();
        String guidStr = Integer.toString(guid);

        int i;
        for (i = 0; i < items.length; ++i)
        {
            if (items[i].startsWith(guidStr)) break;
        }

        lstClients.remove(i);
    }

    @Override
    public void UpdateClient(int guid)
    {
        Entity e = Server.GetEntities().get(guid);
        if (e == null) return;
        Player p = e.ToPlayer();
        if (p == null) return;

        ClientHandler ch = Server.GetClient(guid);

        String[] items = lstClients.getItems();
        String guidStr = Integer.toString(guid);

        int i;
        for (i = 0; i < items.length; ++i)
        {
            if (items[i].startsWith(guidStr)) break;
        }

        lstClients.remove(i);

        lstClients.add(guidStr + " - " + p.GetName() + " - Score: " + p.GetScore() + " - Ping: " + ch.GetPing() + " ms", i);

    }

}
