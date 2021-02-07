package application;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public LinkedList<memBlock> memChunk;
    public ObservableList<memBlock> processes;

    int processCounter = 0;
    @FXML
    private TextField addProcessTxtField;
    @FXML
    private TextField processInfoTxtField;
    @FXML
    private ListView<memBlock> memoryAllocList = new ListView<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        memChunk = new LinkedList<>();
        processes = FXCollections.observableArrayList(memChunk);
        memBlock mem1 = new memBlock(128, "Free");
        mem1.setMasterMemory(true);
        memChunk.addFirst(mem1);
        processes.setAll(memChunk);
        memoryAllocList.setItems(processes);

    }

    // Show a Information Alert without Header Text
    private void showAlertWithoutHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Memory out of range.");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Not enough free memory to start process.");

        alert.showAndWait();
    }
    private void showAlertWithoutHeaderText2() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid process");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Check the format: ProcessName Size");

        alert.showAndWait();
    }

    public void onClickedAddBtn(MouseEvent mouseEvent) {
        if (addProcessTxtField.getLength() > 1) {
            String temp1 = addProcessTxtField.getText();
            String[] temp2 = temp1.split(" ");
            if (temp2.length != 2) {
                showAlertWithoutHeaderText2();
            }
            else {
                try {
                    String name = temp2[0];
                    int size = Integer.parseInt(temp2[1]);
                    memBlock mem2 = new memBlock(size, name, Integer.toString(++processCounter));

                    int listSize = memChunk.size();
                    boolean isItSet = false;
                    for (int index = 0; index < listSize; index++) {
                        if ((memChunk.get(index).getProcessNumber() == null) && (memChunk.get(index).getBlockSize() >= size)) {
                            addProcess(size, mem2, index);
                            isItSet = true;
                            index = listSize;
                            addProcessTxtField.setText("");
                        }
                    }
                    if (!isItSet) {
                        for (int index = 0; index < listSize; index++) {
                            if ((memChunk.get(index).getProcessNumber() == null) && (!memChunk.get(index).getMasterMemory())) {
                                compaction(index);
                                memChunk.remove(index);
                                processes.setAll(memChunk);
                                listSize--;
                                isItSet = true;
                            }
                        }
                        if (isItSet) {
                            locationCorrector(listSize);
                            for (int index = 0; index < listSize; index++) {
                                if ((memChunk.get(index).getProcessNumber() == null) && (memChunk.get(index).getBlockSize() >= size)) {
                                    addProcess(size, mem2, index);
                                    index = listSize;
                                    addProcessTxtField.setText("");
                                }
                            }
                        } else {
                            //need a warning of not enough space
                            showAlertWithoutHeaderText();
                        }
                    }
                }
                catch (Exception E1) {
                    showAlertWithoutHeaderText2();
                }
            }
        }
    }

    private void compaction(int index) {
        memBlock mem3 = memChunk.get(index);
        memChunk.get(memChunk.size() - 1).setStartIndex(memChunk.get(memChunk.size() - 1).getStartIndex()
                 - mem3.getBlockSize());
    }

    private void locationCorrector(int listSize) {
        for (int index = 0; index < listSize; index++) {
            int temp3 = memChunk.get(index).getBlockSize();
            if (index == 0) {
                memChunk.get(index).setStartIndex(0);
            }
            else {
                memChunk.get(index).setStartIndex(memChunk.get(index - 1).getEndIndex() + 1);
            }
            memChunk.get(index).setEndIndex((memChunk.get(index).getStartIndex() + temp3-1));
        }
    }

    private void addProcess(int size, memBlock mem2, int index) {
        memChunk.get(index).setBlockSize((memChunk.get(index).getBlockSize() - size));
        memChunk.get(index).setStartIndex((memChunk.get(index).getStartIndex() + size));
        memChunk.add(index, mem2);
        memChunk.get(index).setEndIndex((memChunk.get(index + 1).getStartIndex() - 1));
        memChunk.get(index).setStartIndex((memChunk.get(index).getEndIndex() - size + 1));
        processes.setAll(memChunk);
        if ((memChunk.get(index + 1).getProcessNumber() == null) && (!memChunk.get(index + 1).getMasterMemory())) {
            memChunk.remove(index + 1);
            processes.setAll(memChunk);
        }
    }

    public void setMemoryAllocSelect(MouseEvent mouseEvent) {
        memBlock selection;
        selection = memoryAllocList.getSelectionModel().getSelectedItem();
        processInfoTxtField.setText(selection.getProcessNumber() + " " + selection.getBlockId() + " "
                   + selection.getBlockSize() + "  -  location: " + selection.getStartIndex() + " - "
                   + selection.getEndIndex());
    }

    public void onClickedTerminateBtn(MouseEvent mouseEvent) {
        if (memoryAllocList.getSelectionModel().getSelectedIndices() != null) {
            if (!memoryAllocList.getSelectionModel().getSelectedItem().getMasterMemory()) {
                int temp1 = memoryAllocList.getSelectionModel().getSelectedIndex();
                System.out.println(temp1);
                memChunk.get(temp1).setBlockId("Free");
                memChunk.get(temp1).setProcessNumber(null);

                while ((temp1 + 1 < memChunk.size()) && (memChunk.get(temp1 + 1).getProcessNumber() == null)) {
                    memChunk.get(temp1).setEndIndex(memChunk.get(temp1+1).getEndIndex());
                    if (memChunk.get(temp1 + 1).getMasterMemory()) {
                        memChunk.get(temp1).setMasterMemory(true);
                    }
                    memChunk.remove(temp1+1);
                }
                while (((temp1 - 1) >= 0) && (memChunk.get(temp1 - 1).getProcessNumber() == null)) {
                    memChunk.get(temp1 - 1).setEndIndex(memChunk.get(temp1).getEndIndex());
                    if (memChunk.get(temp1).getMasterMemory()) {
                        memChunk.get(temp1-1).setMasterMemory(true);
                    }
                    memChunk.remove(temp1);
                    temp1--;
                }
                processes.setAll(memChunk);
                processInfoTxtField.setText("");
            }
        }
    }
}
