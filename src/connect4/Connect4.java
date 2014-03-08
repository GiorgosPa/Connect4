package connect4;

import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.*;

public class Connect4 extends JFrame implements MouseListener {

    private int width, height;
    Thread paint, calculate;
    private JPanel menuPanel, board;
    private JButton reset, newGame;
    private JRadioButton easy, medium, hard;
    private JTextField[] playerScore;
    private JLabel[][] box;
    private State state;
    private GamePlayer computer;
    private Move lastmove;
    private final int depth = 5;
    private JLabel turnPlayerIcon;
    private int yellow = 0, red = 0;

    public Connect4() {

        super("Mario Score 4");
        state = new State();
        computer = new GamePlayer(depth);

        //tixaia epilogi tis 1is kinisis me pithanotita 50% to kentro
        Random r = new Random();
        if (r.nextInt(4) == 0) {
            lastmove = new Move(5, 2);
        } else if (r.nextInt(4) == 3) {
            lastmove = new Move(5, 4);
        } else {
            lastmove = new Move(5, 3);
        }
        state.createChildren();
        state = state.makeMove(lastmove);

        File file = new File("mario_background2.gif");
        ImageIcon imageIcon = new ImageIcon(file.getPath());
        final Image image = imageIcon.getImage();
        width = image.getWidth(this);
        height = image.getHeight(this);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, this);
            }
        };

        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        getContentPane().setLayout(g);
        setPreferredSize(new Dimension(width + 5, height + 30));

        mainPanel.add(boardPanel());
        mainPanel.add(menuPanel());

        display(state.getLastMove().getRow(), state.getLastMove().getCol());

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        getContentPane().add(mainPanel, c);
        setResizable(false);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //Create board panel
    private JPanel boardPanel() {
        board = new JPanel();
        board.setLayout(new GridLayout(6, 7));
        board.setOpaque(false);
        board.setPreferredSize(new Dimension(400, 400));

        box = new JLabel[6][7];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                box[row][col] = new JLabel();
                box[row][col].setAlignmentY(SwingConstants.CENTER);
                box[row][col].setAlignmentY(SwingConstants.CENTER);
                box[row][col].setBorder(new BevelBorder(BevelBorder.RAISED));//,Color.magneta,Color.cyan,Color.magenta,Color.cyan));//new LineBorder(Color.cyan));
                box[row][col].addMouseListener(this);
                board.add(box[row][col]);
            }
        }
        return board;
    }

    // createMenuPanel
    private JPanel menuPanel() {

        // Create panel that cointains score and buttons
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 2));
        menuPanel.setOpaque(false);

        JLabel scoreLabel = new JLabel("SCORE:", JLabel.CENTER);
        scoreLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));

        // Create JLabels for players
        JLabel[] playerLabel = new JLabel[2];
        playerLabel[0] = new JLabel("Player");
        playerLabel[1] = new JLabel("Computer");
        for (int i = 0; i < 2; i++) {
            playerLabel[i].setFont(new Font("Sans-Serif", Font.ROMAN_BASELINE, 14));
        }

        // Create the array of textfield for players score
        playerScore = new JTextField[2];
        for (int i = 0; i < 2; i++) {
            playerScore[i] = new JTextField();
            playerScore[i].setText("0");
            playerScore[i].setEditable(false);
            playerScore[i].setOpaque(false);
            playerScore[i].setHorizontalAlignment(JTextField.CENTER);
            playerScore[i].setPreferredSize(new Dimension(50, 20));
        }

        // Create score panel
        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);

        scorePanel.add(scoreLabel);
        scorePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        for (int i = 0; i < 2; i++) {
            scorePanel.add(playerLabel[i]);
            scorePanel.add(playerScore[i]);
        }

        //Player's turn
        JPanel turnPanel = new JPanel();
        turnPanel.setOpaque(false);
        JLabel turnLabel = new JLabel("PLAYER TURN:", JLabel.CENTER);
        turnLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        turnPlayerIcon = new JLabel();
        turnPlayerIcon.setOpaque(false);

        turnPanel.add(turnLabel);
        turnPanel.add(turnPlayerIcon);
        menuPanel.add(scorePanel);

        displayTurn();

        //Button's Panel
        JPanel buttonPanel = new JPanel();
        reset = new JButton("Clear Score");
        reset.addMouseListener(this);
        buttonPanel.add(reset);
        newGame = new JButton("New Game");
        newGame.addMouseListener(this);
        buttonPanel.add(newGame);
        buttonPanel.setOpaque(false);
        menuPanel.add(buttonPanel);
        menuPanel.add(turnPanel);

        //Level panel
        String[] level = {"Easy", "Medium", "Hard"};
        easy = new JRadioButton(level[0]);
        medium = new JRadioButton(level[1]);
        hard = new JRadioButton(level[2]);
        ButtonGroup levelGroup = new ButtonGroup();
        levelGroup.add(easy);
        levelGroup.add(medium);
        levelGroup.add(hard);
        medium.setSelected(true);
        easy.setOpaque(false);
        medium.setOpaque(false);
        hard.setOpaque(false);
        easy.addMouseListener(this);
        medium.addMouseListener(this);
        hard.addMouseListener(this);
        JPanel radioPanel = new JPanel(new GridLayout(1, 0));
        radioPanel.setOpaque(false);
        radioPanel.add(easy);
        radioPanel.add(medium);
        radioPanel.add(hard);
        menuPanel.add(radioPanel);

        return menuPanel;
    }

    //Clears board
    public void clearBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                box[row][col].setIcon(null);
            }
        }
        state = new State();
        //tixaia epilogi tis 1is kinisis me pithanotita 50% to kentro

        Random r = new Random();
        if (r.nextInt(4) == 0) {
            lastmove = new Move(5, 2);
        } else if (r.nextInt(4) == 3) {
            lastmove = new Move(5, 4);
        } else {
            lastmove = new Move(5, 3);
        }
        state.createChildren();
        state = state.makeMove(lastmove);
        display(state.getLastMove().getRow(), state.getLastMove().getCol());
        displayTurn();
    }

    public void clearScore() {
        for (int i = 0; i < 2; i++) {
            playerScore[i].setText("0");
        }
        red = 0;
        yellow = 0;
    }

    private void displayTurn() {
        try {
            if (!state.getTurn()) {
                turnPlayerIcon.setIcon(new ImageIcon("smallRedMushroom.png"));
            } else {
                turnPlayerIcon.setIcon(new ImageIcon("smallYellowMushroom.png"));
            }
            turnPlayerIcon.paint(turnPlayerIcon.getGraphics());
            Thread.sleep(20);
        } catch (InterruptedException ie) {
        }
    }

    public void drawIcon(int x, int y) {
        if (!state.getTurn()) {
            this.getGraphics().drawImage((new ImageIcon("yellowMushroom.png")).getImage(), x, y, board);
        } else {
            this.getGraphics().drawImage((new ImageIcon("redMushroom.png")).getImage(), x, y, board);
        }
        this.repaint();
    }
    //Drop mushroom

    private void display(int row, int col) {
        if (state.getTurn()) {
            box[row][col].setIcon(new ImageIcon("redMushroom.png"));
        } else {
            box[row][col].setIcon(new ImageIcon("yellowMushroom.png"));
        }
        box[row][col].paint(box[row][col].getGraphics());
    }

    /*private synchronized void display(int row, int col)
     {
     try
     {
     int newrow = 0;
     if (newrow==0)
     {
     if(state.getTurn())
     box[newrow][col].setIcon(new ImageIcon("redMushroom.png"));
     else
     box[newrow][col].setIcon(new ImageIcon("yellowMushroom.png"));
     box[newrow][col].paint(box[newrow][col].getGraphics());
     }
     while (newrow < row)
     {
     box[newrow][col].setIcon(new ImageIcon());
     box[newrow][col].paint(box[newrow][col].getGraphics());
     ++newrow;
     if(state.getTurn())
     box[newrow][col].setIcon(new ImageIcon("redMushroom.png"));
     else
     box[newrow][col].setIcon(new ImageIcon("yellowMushroom.png"));
     box[newrow][col].paint(box[newrow][col].getGraphics());
     Thread.sleep(20);
     }
     }
     catch (InterruptedException ie)
     {
     }
     }*/
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() == reset) {
            int r = JOptionPane.showConfirmDialog(this, "Clear Score?",
                    "Reset Scoreboard", JOptionPane.YES_NO_OPTION);
            if (r == 0) {
                clearScore();
            }
        } else if (e.getComponent() == newGame) {
            int newgame = JOptionPane.showConfirmDialog(this, "Do you want to start a new game?",
                    "New Game", JOptionPane.YES_NO_OPTION);
            if (newgame == 0) {
                clearBoard();
            }
        } else if (e.getComponent() == easy) {
            int lev = JOptionPane.showConfirmDialog(this, "Changing level clears board and score.\nDo you want to continue?",
                    "Change Level", JOptionPane.YES_NO_OPTION);
            if (lev == 0) {
                computer = new GamePlayer(3);
                clearBoard();
                clearScore();
            }
        } else if (e.getComponent() == medium) {
            int lev = JOptionPane.showConfirmDialog(this, "Changing level clears board and score.\nDo you want to continue?",
                    "Change Level", JOptionPane.YES_NO_OPTION);
            if (lev == 0) {
                computer = new GamePlayer(5);
                clearBoard();
                clearScore();
            }
        } else if (e.getComponent() == hard) {
            int lev = JOptionPane.showConfirmDialog(this, "Changing level clears board and score.\nDo you want to continue?",
                    "Change Level", JOptionPane.YES_NO_OPTION);
            if (lev == 0) {
                computer = new GamePlayer(7);
                clearBoard();
                clearScore();
            }
        } else if (e.getComponent().toString().contains("javax.swing.JLabel")) {
            JLabel label = (JLabel) e.getComponent();
            state.createChildren();

            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 7; col++) {
                    if (box[row][col] == label) {
                        lastmove = new Move(row, col);
                        if (state.makeMove(lastmove) == null)//mi epitrepti kinisi (gemati stili)
                        {
                            return;
                        }
                        state = state.makeMove(lastmove);
                    }
                }
            }

            //ipologismos ton x,y tou frame pou prepei na emfanistei to eikonidio
            //kathe koutaki exei diastaseis 57*64
            int x = (state.getLastMove().getCol() + 1) * 57 + 67;
            int y = (state.getLastMove().getRow() + 1) * 64 - 14;
            int dy = 22;
//            while (dy < y) {
//                try {
//                    Thread.sleep(15);
//                } catch (Exception ee) {
//                }
//                drawIcon(x, dy);
//                dy += 35;
//            }
            drawIcon(x, y);
            paint(getGraphics());
            display(state.getLastMove().getRow(), state.getLastMove().getCol());
            displayTurn();
            if (state.isFinal()) {
                if (state.getTie()) {
                    JOptionPane.showMessageDialog(this, "Tie", "Score", JOptionPane.PLAIN_MESSAGE);
                    clearBoard();
                    return;
                }
                JOptionPane.showMessageDialog(this, "You won!", "Score", JOptionPane.PLAIN_MESSAGE, new ImageIcon("mario.gif"));
                red++;
                playerScore[0].setText("" + red);
                clearBoard();
                return;
            }
            lastmove = computer.MiniMax(state);
            state.createChildren();
            state = state.makeMove(lastmove);


            x = (state.getLastMove().getCol() + 1) * 57 + 67;
            y = (state.getLastMove().getRow() + 1) * 64 - 14;
            dy = 22;
//            while (dy < y) {
//                try {
//                    Thread.sleep(15);
//                } catch (Exception ee) {
//                }
//                drawIcon(x, dy);
//                dy += 35;
//            }
            drawIcon(x, y);

            display(state.getLastMove().getRow(), state.getLastMove().getCol());
            displayTurn();

            if (state.isFinal()) {
                if (state.getTie()) {
                    JOptionPane.showMessageDialog(this, "Tie", "Score", JOptionPane.INFORMATION_MESSAGE);
                    clearBoard();
                    return;
                }
                JOptionPane.showMessageDialog(this, "You loose!", "Score", JOptionPane.INFORMATION_MESSAGE);
                yellow++;
                playerScore[1].setText("" + yellow);
                clearBoard();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent().toString().contains("javax.swing.JLabel")) {
            if (!state.getTurn()) {
                e.getComponent().setCursor(getToolkit().createCustomCursor(new ImageIcon("redMushroom.png").getImage(), new Point(15, 15), "validMove"));
            } else {
                e.getComponent().setCursor(getToolkit().createCustomCursor(new ImageIcon("yellowMushroom.png").getImage(), new Point(15, 15), "validMove"));
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {

        Connect4 frame = new Connect4();

    }
}
