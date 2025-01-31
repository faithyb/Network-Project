/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONObject;

import networking.NetworkClient;

/**
 * @author 201575091_Mtileni_MF
 *
 */
public class AppFrame extends JFrame implements ActionListener{
	
	//selection controls
	JButton btnSelectFile;
	JTextField txtFilename;
	
	//requests Controlls
	JButton btnSpeechToText;
	
	//service controls
	JTextField txtRequestStatus;
	JTextArea txtResponse;
	
	//File
	byte[] audioFile;
	
	public AppFrame(String name) {
		//set frame tittle
		this.setTitle(name);
		
		//initialise conrols
		audioFile = null;
		btnSelectFile = new JButton("Select Audio file");
		txtFilename = new JTextField();
		txtFilename.setEditable(false);
		btnSpeechToText = new JButton("Speech to Text");
		txtRequestStatus = new JTextField();
		txtRequestStatus.setEditable(false);
		txtResponse = new JTextArea();
		
		//set frame display
		this.setLayout(new BorderLayout());
		this.add(new JPanel(),BorderLayout.NORTH);
		this.add(new JPanel(),BorderLayout.SOUTH);
		this.add(new JPanel(),BorderLayout.EAST);
		this.add(new JPanel(),BorderLayout.WEST);
		
		GridLayout tempLayout = new GridLayout(2,1);
		tempLayout.setVgap(50);
		JPanel pnlCenter = new JPanel(tempLayout);
		this.add(pnlCenter,BorderLayout.CENTER);
		
		//selection  controlls
		JPanel pnlClientControls = new JPanel(new BorderLayout());
		GridLayout tempLayout2 = new GridLayout(1,3);
		tempLayout2.setHgap(20);
		tempLayout2.setVgap(10);
		JPanel pnlGetFile = new JPanel(tempLayout2);
		pnlGetFile.add(btnSelectFile);pnlGetFile.add(txtFilename);pnlGetFile.add(new JPanel());
		
		//request  controlls
		JPanel pnlRequestsControls = new JPanel(new GridLayout(3,3));
		pnlRequestsControls.add(new JPanel());pnlRequestsControls.add(new JPanel());pnlRequestsControls.add(new JPanel());
		pnlRequestsControls.add(new JPanel());pnlRequestsControls.add(btnSpeechToText);pnlRequestsControls.add(new JPanel());
		pnlRequestsControls.add(new JPanel());pnlRequestsControls.add(new JPanel());pnlRequestsControls.add(new JPanel());
		pnlClientControls.add(pnlGetFile, BorderLayout.NORTH);
		pnlClientControls.add(pnlRequestsControls, BorderLayout.CENTER);
		
		pnlCenter.add(pnlClientControls);
		
		
		//Sevice controls
		JPanel pnlServiceControls = new JPanel(new BorderLayout());
		pnlServiceControls.add(new JPanel(),BorderLayout.SOUTH);
		pnlServiceControls.add(new JPanel(),BorderLayout.EAST);
		pnlServiceControls.add(new JPanel(),BorderLayout.WEST);
		GridLayout tempLayout3 = new GridLayout(1,3);
		tempLayout3.setHgap(20);
		tempLayout3.setVgap(10);
		JPanel pnlRequesStatus = new JPanel(tempLayout3);
		pnlRequesStatus.add(new JLabel("Request Status: "));pnlRequesStatus.add(txtRequestStatus);pnlRequesStatus.add(new JPanel());
		pnlServiceControls.add(pnlRequesStatus,BorderLayout.NORTH);
		pnlServiceControls.add(new JScrollPane(txtResponse), BorderLayout.CENTER);
		
		pnlCenter.add(pnlServiceControls);
		
		btnSelectFile.addActionListener(this);
		btnSpeechToText.addActionListener(this);
		validate();
	}
	@Override
	public void actionPerformed(ActionEvent event) {
	  if(event.getSource() == btnSelectFile) {  
		//get audio file from file system
		  
		  JFileChooser jfc = new JFileChooser("data/");
  		  int result =jfc.showOpenDialog(AppFrame.this);
  		  switch(result) {
  		    case JFileChooser.APPROVE_OPTION:
  			  if(jfc.getSelectedFile().getAbsolutePath().endsWith("wav") || jfc.getSelectedFile().getAbsolutePath().endsWith("wma")) {
  				File file = new File(jfc.getSelectedFile().getAbsolutePath());
  				try {
  					//read audio file 
  					audioFile = readBytesFromFile(file.getPath());
  					txtFilename.setText(file.getPath());
					} catch (Exception e) {
						txtFilename.setText("failed to read file");
					}
  				
  			  }else {
  				
  				txtFilename.setText("unsupported audio format, please choose '.wma' or 'wav' audio formats ");
  			  }
  			
  		break;
  		}
		  
		    validate();
	  }else if(event.getSource() == btnSpeechToText) { 
		  // convert speech to text using webservice
		  
		  if(audioFile != null) {
			 // writeBytesToFile();
			  
			  try {
				  //send http post method to Microsoft cognitive service(Rest API)
				  JSONObject response = new NetworkClient().SpeechToText(audioFile);
				  //read json response
				  String status = response.getString("RecognitionStatus");
				  txtRequestStatus.setText(status);
				  if(status.contains("Success")) {
					  String TextResults = response.getString("DisplayText");
					  txtResponse.setText(TextResults);
				  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				txtRequestStatus.setText("no audio file selected");
			}
    		validate();
      }
    }
	public void validate() {
		super.validate();
	}
	
	//function to read binary files
	private byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }
	//function to write binary files
	private void writeBytesToFile() {

		try {
	        // save byte[] into a file
	        Path path = Paths.get("data/testAudio.mp3");
	        Files.write(path, audioFile);

	        System.out.println("Done");

	     } catch (IOException e) {
	            e.printStackTrace();
	     }
    }

}

