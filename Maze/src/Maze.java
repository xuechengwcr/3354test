import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

//Class
public class Maze
{
    //Constructor
    public Maze(int mRows,int mColumns)
    {
        //Initialize
        this.mRows=mRows;
        this.mColumns=mColumns;
        this.MAT=mRows*mColumns;
    }

    //Method mazeGetter()
    public void mazeGetter()
    {
        //Function calls
        initialization();
        mazeGenerator();
        mazeDisplay();

        //Initialize
        solution=null;
    }

    //Method mazeGetter()
    public void mazeGetter(int mRows,int mColumns)
    {
        //Initialize
        this.mRows=mRows;
        this.mColumns=mColumns;
        this.MAT=mRows*mColumns;

        //Function calls
        initialization();
        mazeGenerator();
        updateMaze();

        //Initialize
        solution=null;
    }

    //Declare needed variables and initialize
    private int mRows;
    private int mColumns;
    private int MAT;
    private DisjSets djsets;
    private MazeNode[][] info;
    private List<MazeEdge> edgeOption;
    private String solution;
    MazeEdge eVertical=new MazeEdge(0,0,EdgeType.eVertical);
    MazeEdge eHorizontal=new MazeEdge(0,0,EdgeType.eHorizontal);
    MazeEdge VISITED=new MazeEdge(0,0,EdgeType.eHorizontal);

    //Method initialization
    private void initialization()
    {
        //Initialize
        djsets=new DisjSets(mRows*mColumns);
        info=new MazeNode[mRows][mColumns];
        edgeOption=new LinkedList<MazeEdge>();

        //Loop to iterate rows
        for(int i1=0;i1<mRows;i1++)
        {
            //Loop to iterate columns
            for(int j1=0;j1<mColumns;j1++)
            {
                //Update
                info[i1][j1]=new MazeNode(String.valueOf(i1*mColumns+j1),i1,j1);
            }
        }

        //Loop to iterate columns
        for(int j1=0;j1<mColumns;j1++)
        {
            //Update
            info[0][j1].upward=eHorizontal;
            info[mRows-1][j1].downward=eHorizontal;
        }

        //Loop to iterate rows
        for(int i1=0;i1<mRows;i1++)
        {
            //Update
            info[i1][0].leftside=eVertical;
            info[i1][mColumns-1].rightside=eVertical;
        }

        //Loop to iterate rows
        for(int i1=0;i1<mRows-1;i1++)
        {
            //Loop to iterate columns
            for(int j1=0;j1<mColumns;j1++)
            {
                //Update
                info[i1][j1].downward=new MazeEdge(i1*mColumns+j1,(i1+1)*mColumns+j1,EdgeType.eHorizontal);
                info[i1+1][j1].upward=info[i1][j1].downward;
                edgeOption.add(info[i1][j1].downward);
            }
        }

        //Loop to iterate columns
        for(int j1=0;j1<mColumns-1;j1++)
        {
            //Loop to iterate rows
            for(int i1=0;i1<mRows;i1++)
            {
                //Update
                info[i1][j1].rightside=new MazeEdge(i1*mColumns+j1,i1*mColumns+j1+1,EdgeType.eVertical);
                info[i1][j1+1].leftside=info[i1][j1].rightside;
                edgeOption.add(info[i1][j1].rightside);
            }
        }
    }

    //Method mazeGenerator()
    private void mazeGenerator()
    {
        //Create
        Random myrand=new Random();

        //Loop
        while(!edgeOption.isEmpty())
        {
            //Initialize
            int idx=myrand.nextInt(edgeOption.size());
            MazeEdge opted=edgeOption.remove(idx);
            int edge1=opted.edge1;
            int edge2=opted.edge2;
            int root1=djsets.find(edge1);
            int root2=djsets.find(edge2);

            //Check condition
            if(root1!=root2)
            {
                //Function call
                djsets.union(root1, root2);

                //Check condition
                if(opted.etype==EdgeType.eVertical)
                {
                    //Update
                    info[edge1/mColumns][edge1%mColumns].rightside=null;
                    info[edge2/mColumns][edge2%mColumns].leftside=null;
                }

                //Otherwise
                else
                {
                    //Update
                    info[edge1/mColumns][edge1%mColumns].downward=null;
                    info[edge2/mColumns][edge2%mColumns].upward=null;
                }
            }
        }

        //Update
        info[0][0].upward=null;
        info[mRows-1][mColumns-1].downward=null;
    }

    //Create
    JFrame jframe=new JFrame("Maze");
    JPanel jpanel=new JPanel();

    //Method updateMaze()
    private void updateMaze()
    {
        //Initialize
        jframe.remove(jpanel);
        jpanel=new JPanel();
        jframe.add(BorderLayout.CENTER,jpanel);
        jpanel.setLayout(new GridLayout(mRows, mColumns));

        //Loop
        for(int i1=0;i1<mRows;i1++)
        {
            //Loop
            for(int j1=0;j1<mColumns;j1++)
            {
                //Initialize
                String jlabelValue=info[i1][j1].displayInfo;
                JLabel jlabel=new JLabel(jlabelValue);
                jlabel.setHorizontalAlignment(SwingConstants.CENTER);
                int upward=1,downward=1,rightside=1,leftside=1;

                //Check condition
                if(info[i1][j1].upward==null)
                {
                    //Update
                    upward=0;
                }

                //Check condition
                else if(info[i1][j1].upward==eHorizontal)
                {
                    //Update
                    upward=2;
                }

                //Check condition
                if(info[i1][j1].leftside==null)
                {
                    //Update
                    leftside=0;
                }

                //Check condition
                else if(info[i1][j1].leftside==eVertical)
                {
                    //Update
                    leftside=2;
                }

                //Check condition
                if(info[i1][j1].downward==null)
                {
                    //Update
                    downward=0;
                }

                //Check condition
                else if(info[i1][j1].downward==eHorizontal)
                {
                    //Update
                    downward=2;
                }

                //Check condition
                if(info[i1][j1].rightside==null)
                {
                    //Update
                    rightside=0;
                }

                //Check condition
                else if(info[i1][j1].rightside==eVertical)
                {
                    //Update
                    rightside=2;
                }
                jlabel.setBorder(BorderFactory.createMatteBorder(upward, leftside, downward, rightside, Color.BLACK));
                jpanel.add(jlabel);
            }
        }
        jframe.setVisible(true);
    }

    //Method mazeDisplay()
    private void mazeDisplay()
    {
        //Initialize
        jframe.setSize(600, 600);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new BorderLayout());
        JPanel jnorth=new JPanel();
        jframe.add(BorderLayout.NORTH,jnorth);
        final JTextArea solution=new JTextArea();
        solution.setColumns(10);
        solution.setRows(3);
        solution.setAutoscrolls(true);
        final JTextField rowsField=new JTextField(String.valueOf(mRows));
        rowsField.setSize(100, 20);
        final JTextField columnsField=new JTextField(String.valueOf(mColumns));
        columnsField.setSize(100, 20);
        JButton generate=new JButton("generate");
        jnorth.add(generate);
        jnorth.add(rowsField);
        jnorth.add(columnsField);

        //ActionListener()
        generate.addActionListener(new ActionListener()
        {
            //Method actionPerformed()
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Initialize
                solution.setText("");
                mazeGetter(Integer.valueOf(rowsField.getText()),
                        Integer.valueOf(columnsField.getText()));
            }
        });

        //Initialize
        jframe.add(BorderLayout.EAST,new JLabel("    "));
        jframe.add(BorderLayout.WEST,new JLabel("    "));
        jframe.add(BorderLayout.CENTER,jpanel);
        JPanel jsouth=new JPanel();
        jframe.add(BorderLayout.SOUTH,jsouth);
        JButton showAnswer=new JButton("show solution");
        jsouth.add(showAnswer);
        jsouth.add(solution);

        //ActionListener()
        showAnswer.addActionListener(new ActionListener()
        {
            //Method actionPerformed()
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Initialize
                String solutionStr= getSolution();
                solution.setText(solutionStr);
            }
        });

        //Function call
        updateMaze();
    }

    //Method getSolution()
    public String getSolution()
    {
        //Check condition
        if(null==solution)
        {
            //Initialize
            Stack<MazeNode> mstack=new Stack<MazeNode>();
            mstack.add(info[mRows-1][mColumns-1]);
            info[mRows-1][mColumns-1].downward=VISITED;
            while(!mstack.isEmpty()&&mstack.peek()!=info[0][0])
            {
                //Initialize
                MazeNode top=mstack.peek();

                //Check condition
                if(null==top.upward)
                {
                    //Initialize
                    mstack.push(info[top.i1-1][top.j1]);
                    top.upward=VISITED;
                    info[top.i1-1][top.j1].downward=VISITED;
                }

                //Check condition
                else if(null==top.leftside)
                {
                    //Initialize
                    mstack.push(info[top.i1][top.j1-1]);
                    top.leftside=VISITED;
                    info[top.i1][top.j1-1].rightside=VISITED;
                }

                //Check condition
                else if(null==top.downward)
                {
                    //Initialize
                    mstack.push(info[top.i1+1][top.j1]);
                    top.downward=VISITED;
                    info[top.i1+1][top.j1].upward=VISITED;
                }

                //Check condition
                else if(null==top.rightside)
                {
                    //Initialize
                    mstack.push(info[top.i1][top.j1+1]);
                    top.rightside=VISITED;
                    info[top.i1][top.j1+1].leftside=VISITED;
                }

                //Otherwise
                else
                {
                    //Initialize
                    mstack.pop();
                }
            }

            //Check condition
            if(mstack.isEmpty())
            {
                //Return
                return "system error!";
            }

            //Initialize
            StringBuilder result=new StringBuilder();
            int i1=0;

            //Loop
            while(mstack.peek()!=info[mRows-1][mColumns-1])
            {
                //Update
                result.append(mstack.pop().displayInfo).append("->");

                //Check condition
                if(++i1%10==0)
                {
                    //Update
                    result.append("\n");
                }
            }

            //Update
            result.append(mstack.pop().displayInfo);
            solution=result.toString();

            //Return
            return result.toString();
        }

        //Return
        return solution;
    }

    //Enumurator
    private static enum EdgeType
    {
        //Variables
        eVertical,eHorizontal;
    }

    //Class
    private static class MazeEdge
    {
        //Variables
        int edge1;
        int edge2;
        EdgeType etype;

        //Constructor
        public MazeEdge(int edge1,int edge2,EdgeType etype)
        {
            //Initialize
            this.edge1=edge1;
            this.edge2=edge2;
            this.etype=etype;
        }
    }

    //Class
    private static class MazeNode
    {
        //Variables
        String displayInfo;
        MazeEdge upward;
        MazeEdge leftside;
        MazeEdge downward;
        MazeEdge rightside;
        int i1;
        int j1;

        //Constructor
        public MazeNode(String info,int i1,int j1)
        {
            //Initialize
            this.displayInfo=info;
            this.i1=i1;
            this.j1=j1;
        }
    }

    //Driver
    public static void main(String[] args)
    {
        //Initialize
        Maze maze=new Maze(20, 20);

        //Function call
        maze.mazeGetter();
    }
}