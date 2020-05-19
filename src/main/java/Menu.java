import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu extends JFrame {

    private static JFrame frame = new JFrame("Find document");
    private static Menu menu = new Menu();

    public  JPanel panel;

    private JTextField textAddress;
    private JTextArea textArea;

    public JList listOfDocuments;

    private JButton buttonSearch;
    private JButton browseButton;
    private JButton showFileContent;
    private JButton searchByTextButton;

    private  JFileChooser fileChooser   = new JFileChooser();
    static DbWorker dbWorker = new DbWorker();
    private boolean flag = false;
    public Menu() {

        buttonSearch.addActionListener(actionEvent ->{
            FileSearcher.setFilesList(menu);
            for(int i = 0; i< listOfDocuments.getModel().getSize();i++) {
               writeToDB(i);

            }

        });

        browseButton.addActionListener(actionEvent -> {

            fileChooser.setDialogTitle("Choose directory");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(Menu.this);
            if (result == JFileChooser.APPROVE_OPTION )
                textAddress.setText(String.valueOf(fileChooser.getSelectedFile()));
        });

        showFileContent.addActionListener(actionEvent -> {
        if(!listOfDocuments.isSelectionEmpty()){
            String fileName = (String) listOfDocuments.getSelectedValue();
            flag = true;
            if(fileName.endsWith(".pdf"))
                textArea.setText(FileReader.readPdfFile(getFolderAddress() + "/" + fileName));
            else if (fileName.endsWith(".doc") || fileName.endsWith(".docx"))
                textArea.setText(FileReader.readWordFile(getFolderAddress() + "/" + fileName));
            else 
                textArea.setText(FileReader.readExcelFile(getFolderAddress() + "/" + fileName));

        }
        });
        searchByTextButton.addActionListener(e -> {

            String word_to_search = textArea.getText();
            dbWorker.insertInSearch(word_to_search);
            ArrayList<String> filesList = new ArrayList<String>();
            filesList = dbWorker.getFileListIncludesSearchingWord(word_to_search);
            String resaultFiles = "";
            for(int i = 0; i< filesList.size();i++) {
                resaultFiles+= filesList.get(i) + "\n";

            }

            textArea.setText(resaultFiles);
        });
    }

    public void writeToDB(int i){
        dbWorker.insertInFileList((String)listOfDocuments.getModel().getElementAt(i), textAddress.getText(), FileReader.readFile(textAddress.getText()+ "/" +listOfDocuments.getModel().getElementAt(i)),
                null);

    }

    public String getFolderAddress(){
        return textAddress.getText();
    }

    static void createFrame(){
        frame.setSize(700, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(menu.panel);
    }


}
