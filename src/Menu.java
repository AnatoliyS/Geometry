import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

public class Menu extends JComponent {
  private final int window_width = 1200;
  private final int window_height = 700;
  private Hashtable<String, Boolean> toBeDrawn = new Hashtable<>();
  private final Color menuColor = Color.CYAN;

  private MenuConfiguration menuConfiguration = new MenuConfiguration();

  public JPanel chooseAlgorithmPanel;
  public JPanel utilityPanel;

  public ArrayList<String> getAlgorithmsToBeDrawn() {
    ArrayList<String> result = new ArrayList<>();
    for (String algorithmName : toBeDrawn.keySet()) {
      if (toBeDrawn.get(algorithmName)) {
        result.add(algorithmName);
      }
    }
    return result;
  }

  public MenuConfiguration getMenuConfiguration() {
    return menuConfiguration;
  }

  class CheckBoxActionListener implements ActionListener {
    private String name;
    private JCheckBox checkBox;

    public CheckBoxActionListener(String _name, JCheckBox _checkBox) {
      name = _name;
      checkBox = _checkBox;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      toBeDrawn.put(name, checkBox.isSelected());
    }
  }

  class DrawButtonListener implements ActionListener {
    private MenuHandler menuHandler;

    public DrawButtonListener (MenuHandler handler) {
      menuHandler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      menuHandler.onMenuChange();
      menuConfiguration.isCorrectInputDisplayed = true;
    }
  }

  class InputFileTextFieldActionListener implements ActionListener {
    private JTextField textField;

    public InputFileTextFieldActionListener(JTextField _textField) {
      textField = _textField;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      menuConfiguration.inputFile = textField.getText();
      menuConfiguration.isCorrectInputDisplayed = false;
      menuConfiguration.isInputStringAFile = true;
    }
  }

  class GenerateRandomTextFieldActionListener implements ActionListener {
    private JTextField textField;

    public GenerateRandomTextFieldActionListener(JTextField _textField) {
      textField = _textField;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      menuConfiguration.inputString = textField.getText();
      menuConfiguration.isCorrectInputDisplayed = false;
      menuConfiguration.isInputStringAFile = false;
    }
  }

  class SavePointsActionListener implements ActionListener {
    private MenuHandler menuHandler;

    SavePointsActionListener (MenuHandler handler) {
      this.menuHandler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      menuConfiguration.shouldSavePoints = true;
      menuHandler.onMenuChange();
      menuConfiguration.isCorrectInputDisplayed = true;
    }
  }

  public void attachToHandler(MenuHandler menuHandler) {
    JButton button = new JButton("Draw");
    button.addActionListener(new DrawButtonListener(menuHandler));

    JCheckBox delaunayCheckbox = new JCheckBox("Delaunay Triangulation");
    delaunayCheckbox.addActionListener(new CheckBoxActionListener(
        AlgorithmName.DELAUNAY_TRIANGULATION, delaunayCheckbox));

    JCheckBox voronoiCheckbox = new JCheckBox("Voronoi Diaram");
    voronoiCheckbox.addActionListener(new CheckBoxActionListener(
        AlgorithmName.VORONOI_DIAGRAM, voronoiCheckbox));

    JCheckBox MAPCheckbox = new JCheckBox("Minimum Area Polygon");
    MAPCheckbox.addActionListener(new CheckBoxActionListener(
        AlgorithmName.MINIMUM_AREA_POLYGON, MAPCheckbox ));

    JCheckBox convexHullCheckbox = new JCheckBox("Convex Hull");
    convexHullCheckbox.addActionListener(new CheckBoxActionListener(
        AlgorithmName.CONVEX_HULL, convexHullCheckbox));

    JCheckBox minimumSpanningTreeCheckbox = new JCheckBox("Minimum Spanning Tree");
    minimumSpanningTreeCheckbox.addActionListener(new CheckBoxActionListener(
        AlgorithmName.SPANNING_TREE, minimumSpanningTreeCheckbox));

    JCheckBox closestPairCheckbox = new JCheckBox("All Nearest Neighbours");
    closestPairCheckbox.addActionListener(new CheckBoxActionListener(
        AlgorithmName.ALL_NEAREST_NEIGHBOURS, closestPairCheckbox));

    JButton saveButton = new JButton("Save Points");
    saveButton.addActionListener(new SavePointsActionListener(menuHandler));

    chooseAlgorithmPanel = new JPanel(new GridLayout(0, 1, 50, 50));
    chooseAlgorithmPanel.setPreferredSize(new Dimension(200, window_height));
    chooseAlgorithmPanel.add(button);
    chooseAlgorithmPanel.add(convexHullCheckbox);
    chooseAlgorithmPanel.add(delaunayCheckbox);
    chooseAlgorithmPanel.add(voronoiCheckbox);
    chooseAlgorithmPanel.add(MAPCheckbox);
    chooseAlgorithmPanel.add(closestPairCheckbox);
    chooseAlgorithmPanel.add(minimumSpanningTreeCheckbox);
    chooseAlgorithmPanel.add(saveButton);
    //paint all menu components except buttons to menu color
    chooseAlgorithmPanel.setBackground(menuColor);
    for(Component component : chooseAlgorithmPanel.getComponents()) {
      if (!component.getClass().equals(new JButton().getClass())) {
        component.setBackground(menuColor);
      }
    }

    utilityPanel = new JPanel(new GridLayout(2, 0));
    JTextField inputPathTextBox = new JTextField("");
    inputPathTextBox.addActionListener(new InputFileTextFieldActionListener(inputPathTextBox));
    JTextField numberOfRandomPoints = new JTextField("");
    numberOfRandomPoints.addActionListener(
        new GenerateRandomTextFieldActionListener(numberOfRandomPoints));
    utilityPanel.setPreferredSize(new Dimension(window_width, 50));
    utilityPanel.add(new JLabel(""));
    utilityPanel.add(new JLabel(""));
    utilityPanel.add(new JLabel("Path to an input file"));
    utilityPanel.add(inputPathTextBox);
    utilityPanel.add(new JLabel(""));
    utilityPanel.add(new JLabel(""));
    utilityPanel.add(new JLabel("number of randomly generated points"));
    utilityPanel.add(numberOfRandomPoints);
    utilityPanel.setBackground(menuColor);
  }
}
