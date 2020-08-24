package editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Direction {
    NEXT,
    PREVIOUS,
    NONE
}

public class TextEditor extends JFrame {
    private JMenuBar jMenuBar;
    private JTextField jTextField;
    private JButton saveButton;
    private JButton loadButton;
    private JButton searchButton;
    private JButton previousButton;
    private JButton nextButton;
    private JTextArea jTextArea;
    private JCheckBox regexButton;

    private ActionListener saveActionListener;
    private ActionListener loadActionListener;
    private ActionListener exitActionListener;
    private ActionListener searchActionListener;
    private ActionListener nextMatchActionListener;
    private ActionListener previousMatchActionListener;
    private ActionListener regexActionListener;

    private File selectedFile = null;
    private JFileChooser jFileChooser = null;

    private int index;
    private int lastSize = 0;

    public TextEditor() {
        super("Text editor");
        setIconImage(new ImageIcon(new File("editor.png").getAbsolutePath()).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // initialize components
        initActionListeners();
        initMenuBar();
        initComponents();

        setJMenuBar(jMenuBar);
        setVisible(true);
    }

    private void initMenuBar() {
        // JMenuBar
        jMenuBar = new JMenuBar();

        // JMenu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setMnemonic(KeyEvent.VK_F);        // ALT + F to open File menu

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");
        searchMenu.setMnemonic(KeyEvent.VK_S);      // ALT + S to open Search menu

        jMenuBar.add(fileMenu);
        jMenuBar.add(searchMenu);

        // menu items
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.setName("MenuOpen");
        loadItem.addActionListener(loadActionListener);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setName("MenuSave");
        saveItem.addActionListener(saveActionListener);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setName("MenuExit");
        exitItem.addActionListener(exitActionListener);

        JMenuItem startSearch = new JMenuItem("Start search");
        startSearch.setName("MenuStartSearch");
        startSearch.addActionListener(searchActionListener);
        JMenuItem previousSearch = new JMenuItem("Previous search");
        previousSearch.setName("MenuPreviousMatch");
        previousSearch.addActionListener(previousMatchActionListener);
        JMenuItem nextMatch = new JMenuItem("Next match");
        nextMatch.setName("MenuNextMatch");
        nextMatch.addActionListener(nextMatchActionListener);
        JMenuItem regex = new JMenuItem("Use regular expressions");
        regex.setName("MenuUseRegExp");
        regex.addActionListener(regexActionListener);

        // add menu items to fileMenu
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        searchMenu.add(startSearch);
        searchMenu.add(previousSearch);
        searchMenu.add(nextMatch);
        searchMenu.add(regex);
    }

    private void initComponents() {
        // load button
        loadButton = new JButton();
        loadButton.setName("OpenButton");
        loadButton.addActionListener(loadActionListener);

        // save button
        saveButton = new JButton();
        saveButton.setName("SaveButton");
        saveButton.addActionListener(saveActionListener);

        // input text field
        jTextField = new JTextField();
        jTextField.setName("SearchField");

        // search button
        searchButton = new JButton();
        searchButton.setName("StartSearchButton");
        searchButton.addActionListener(searchActionListener);

        // previous button
        previousButton = new JButton();
        previousButton.setName("PreviousMatchButton");
        previousButton.addActionListener(previousMatchActionListener);

        // next button
        nextButton = new JButton();
        nextButton.setName("NextMatchButton");
        nextButton.addActionListener(nextMatchActionListener);

        // regex button
        regexButton = new JCheckBox();
        regexButton.setName("UseRegExCheckbox");
        regexButton.setText("Use regex");

        // set icon to each button
        initButtonIcons();

        // panel
        JPanel jPanel = new JPanel();
        jPanel.add(loadButton);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.add(saveButton);
        jPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        jPanel.add(jTextField);
        jPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        jPanel.add(searchButton);
        jPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        jPanel.add(previousButton);
        jPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        jPanel.add(nextButton);
        jPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        jPanel.add(regexButton);

        // output text area
        jTextArea = new JTextArea(30, 60);
        jTextArea.setName("TextArea");
        jTextArea.setLineWrap(true);

        // scroll pane
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setName("ScrollPane");
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // grouping all elements
        JPanel groupPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(groupPanel);
        groupPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jPanel)
                .addComponent(jScrollPane))
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(jPanel, 25, 25, 25)
                .addComponent(jScrollPane)
        );

        // for testing purposes adding JFileChooser to frame
        jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jFileChooser.setName("FileChooser");
        this.add(jFileChooser);

        add(groupPanel);
        pack();
    }

    private void initButtonIcons() {
        try {
            saveButton.setIcon(new ImageIcon(ImageIO.read(new File("saveIcon.png"))
                    .getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
            loadButton.setIcon(new ImageIcon(ImageIO.read(new File("loadIcon.png"))
                    .getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
            searchButton.setIcon(new ImageIcon(ImageIO.read(new File("searchIcon.png"))
                    .getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
            previousButton.setIcon(new ImageIcon(ImageIO.read(new File("previousIcon.png"))
                    .getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
            nextButton.setIcon(new ImageIcon(ImageIO.read(new File("nextIcon.png"))
                    .getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initActionListeners() {
        // action listener for saving data from jTextArea to specified file
        saveActionListener = actionEvent -> {
            int returnValue = jFileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = jFileChooser.getSelectedFile();
                FileManager fileManager = new FileManager(selectedFile);

                try {
                    fileManager.writeToFile(jTextArea.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // action listener for loading data from file to jTextArea
        loadActionListener = actionEvent -> {
            int returnValue = jFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = jFileChooser.getSelectedFile();
                FileManager fileManager = new FileManager(selectedFile);

                jTextArea.setText("");

                try {
                    jTextArea.append(fileManager.readFileAsString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // action listener for searching data specified in jTextArea
        searchActionListener = actionEvent -> findWord(Direction.NONE);

        // action listener for searching previous match from current position
        previousMatchActionListener = actionEvent -> findWord(Direction.PREVIOUS);

        // action listener for searching next match from current position
        nextMatchActionListener = actionEvent -> findWord(Direction.NEXT);

        // action listener for marking regex check box
        regexActionListener = actionEvent -> regexButton.setSelected(!regexButton.isSelected());

        // action listener for exiting the application
        exitActionListener = actionEvent -> System.exit(0);
    }

    private void findWord(Direction direction) {
        SwingUtilities.invokeLater(() -> {
            String textArea = jTextArea.getText().replaceAll("\\n", " ");
            int wordLength = jTextField.getText().length();

            Pattern pattern;
            Matcher matcher;

            // find index
            int indexOf = -1;
            if (direction == Direction.NONE || direction == Direction.NEXT) {
                if (direction == Direction.NONE) {
                    index = 0;
                }

                String substring = textArea.substring(index);

                if (regexButton.isSelected()) {
                    pattern = Pattern.compile(jTextField.getText());
                    matcher = pattern.matcher(substring);

                    if (matcher.find()) {
                        indexOf = matcher.start();
                        wordLength = matcher.end() - matcher.start();
                    }
                } else {
                    indexOf = substring.indexOf(jTextField.getText());
                }

                index += indexOf;
            } else if (direction == Direction.PREVIOUS) {
                if (regexButton.isSelected()) {
                    String substring = textArea.substring(0, index - lastSize);
                    pattern = Pattern.compile(jTextField.getText());
                    matcher = pattern.matcher(substring);

                    while (matcher.find()) {
                        indexOf = matcher.start();
                        wordLength = matcher.end() - matcher.start();
                    }
                } else {
                    if (index - wordLength >= 0) {
                        String substring = textArea.substring(0, index - lastSize);
                        indexOf = substring.lastIndexOf(jTextField.getText());
                    }
                }
                index -= index - indexOf;
            }
            lastSize = wordLength;

            // select text
            if (indexOf != -1) {
                if (direction == Direction.NONE || direction == Direction.NEXT) {
                    jTextArea.setCaretPosition(index + wordLength);
                    jTextArea.select(index, index + wordLength);
                } else {
                    jTextArea.setCaretPosition(indexOf + wordLength);
                    jTextArea.select(indexOf, indexOf + wordLength);
                }

                jTextArea.grabFocus();
                index += wordLength;
            } else {
                if (direction == Direction.PREVIOUS) {
                    index = textArea.length() + lastSize;
                } else {
                    index = 0;
                }

                if (regexButton.isSelected()) {
                    if (textArea.matches(".*" + jTextField.getText() + ".*")) {
                        findWord(direction);
                    }
                } else {
                    if (textArea.contains(jTextField.getText())) {
                        findWord(direction);
                    }
                }
            }
        });
    }
}
