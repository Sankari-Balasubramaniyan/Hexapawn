
import java.util.*;
import java.lang.StringBuilder;

public class Hexapawn
{
    public class Configuration
    {
        protected String[][] board ;
        private int n;
        private int m;
        private boolean color;
        private String board_f;

        public Configuration(int n,int m,boolean c)  //Creates an empty board array of given size and type String
        {
            this.n=n;
            this.m=m;
            this.color=c;
            this.board_f = "";
            this.board = new String[n][m];
            for (int i=0; i<n; i++) 
            {
                for (int j=0; j<m; j++) 
                {
                    this.board[i][j]=" ";
                }
            }
        }
        public Configuration(int n,int m,boolean c,String[][] board)   //Creates a filled flatten board 
        {
            this.n = n;
            this.m = m;
            this.color=c;
            this.board = board;
            StringBuilder st = new StringBuilder();     
            for (int i=0; i<n; i++) 
            {
                for (int j=0; j<m; j++) 
                {
                    st.append(this.board[i][j]);
                }
            }
            this.board_f = st.toString();
        }
        public void generate_flatboard() 
        { 
            StringBuilder st = new StringBuilder();
            for (int i=0; i<n; i++) 
            {
                for (int j=0; j<m; j++) 
                {
                    st.append(this.board[i][j]);
                }
            }
            this.board_f = st.toString();
        }
        public void addLine(int n,String s) 
        {
            String[] line=s.split("");
            for (int j=0; j<m; j++) 
            {
                board[n][j]=line[j];
            }
        }
        public String[][] copyArr(String tab[][]) 
        {
            String[][] copyArray =new String[this.board.length][];
            for (int i = 0; i < copyArray.length; ++i) 
            {
                copyArray[i] = new String[this.board[i].length];
                for (int j = 0; j < copyArray[i].length; ++j) 
                {
                    copyArray[i][j] = this.board[i][j];
                }
            }
            return copyArray;
        }
        public int hashCode() 
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + (color ? 1231 : 1237);
            result = prime * result + ((board_f == null) ? 0 : board_f.hashCode());
            return result;
        }
        public boolean equals(Object obj) 
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Configuration other = (Configuration) obj;
            if (color != other.color)
                return false;
            if (board_f == null) {
                if (other.board_f != null)
                    return false;
            } else if (!board_f.equals(other.board_f))
                return false;
            return true;
        }

        public ArrayList<Configuration> successors()
        {
            String[][] bis = this.board.clone();  
            ArrayList<Configuration> res = new ArrayList<Configuration>();
            for (int i=0; i<n; i++) 
            {
                for (int j=0; j<m; j++) 
                {
                    if (bis[i][j].compareTo("p") == 0 && !this.color)    // Black movement
                    { 
                        
                        if((j<m-1) && (i<n-1) && bis[i+1][j+1].compareTo("P") == 0 )  //checking for left diagonal and moving left
                        { 
                            bis[i+1][j+1]="p";
                            bis[i][j]=" ";
                            res.add(new Configuration(this.n, this.m, !this.color, this.copyArr(bis)));
                            bis[i+1][j+1]="P";
                            bis[i][j]="p";
                        }
                        if((j>0) && (i<n-1) && bis[i+1][j-1].compareTo("P") == 0 )  //checking for right diagonal and moving right
                        {
                            bis[i+1][j-1]="p";
                            bis[i][j]=" ";
                            res.add(new Configuration(this.n, this.m, !this.color, this.copyArr(bis)));
                            bis[i+1][j-1]="P";
                            bis[i][j]="p";
                        }
                        if((i<n-1) && bis[i+1][j].compareTo(" ") == 0 ) //moving forward
                        {
                            bis[i+1][j]="p";
                            bis[i][j]=" ";
                            res.add(new Configuration(this.n, this.m, !this.color, this.copyArr(bis)));
                            bis[i+1][j]=" ";
                            bis[i][j]="p";
                        }
                    }
                    
                    if (bis[i][j].compareTo("P") == 0 && this.color)     // White movement
                    { 
                       
                        if((i > 0) && bis[i-1][j].compareTo(" ") == 0 ) // white moving forward
                        {
                            bis[i-1][j]="P";
                            bis[i][j]=" ";
                            res.add(new Configuration(this.n, this.m, !this.color, this.copyArr(bis)));
                            bis[i-1][j]=" ";
                            bis[i][j]="P";
                        }
                        if((j > 0) && (i > 0) && bis[i-1][j-1].compareTo("p") == 0)  // left diagonal and moving left
                        {
                            bis[i-1][j-1]="P";
                            bis[i][j]=" ";
                            res.add(new Configuration(this.n, this.m, !this.color, this.copyArr(bis)));
                            bis[i-1][j-1]="p";
                            bis[i][j]="P";
                        }
                        if((j < m-1) && (i > 0) && bis[i-1][j+1].compareTo("p") == 0)   // right diagonal and moving right
                        {
                            bis[i-1][j+1]="P";
                            bis[i][j]=" ";
                            res.add(new Configuration(this.n, this.m, !this.color, this.copyArr(bis)));
                            bis[i-1][j+1]="p";
                            bis[i][j]="P";
                        }
                   
                    }
                }
            }
            return res;
        }
    }
        public int naive_version(Configuration c) 
        {
            if (Arrays.asList(c.board[0]).contains("P"))  // If white reaches black region
            {
                return 0;
            }
            else if (Arrays.asList(c.board[c.n-1]).contains("p"))  //If black reaches white region
            {
                return 0;
            }
            ArrayList<Configuration> successors = c.successors();    //Obtaining the resultant Arraylist configuration from successors function
            if (successors.isEmpty())     //No more moves
            { 
                return 0;
            }
    
    
            boolean Posi = true;
            int maxNega = 0;   
            int maxPosi = 0;   
    
            for (Configuration successor : successors) 
            {
                int value = naive_version(successor);
    
                if (value>0) 
                {
                    maxPosi = value > maxPosi ? value : maxPosi;    
                }
                    
                if (value<0) 
                {
                    Posi = false;
                    if (maxNega!=0) {maxNega = value > maxNega ? value : maxNega; }   
                    else{maxNega = value;}
                }
                if (value == 0) 
                {
                    return 1;
                }
            }
            if (Posi) 
            {
                return -(maxPosi)-1; 
            }
            else 
            {
                return Math.abs(maxNega) + 1;
            }
    }

    public int dynamic_version(Configuration c, Map<Configuration, Integer> memo) 
    {
        if (Arrays.asList(c.board[0]).contains("P")) 
        {
            return 0;
        }
        else if (Arrays.asList(c.board[c.n-1]).contains("p")) 
        {
            return 0;
        }
        
        ArrayList<Configuration> successors = c.successors();
        if (successors.isEmpty()) 
        { 
            return 0;
        }
        boolean Posi = true;
        int maxNega = 0;
        int maxPosi = 0;

        for (Configuration successor : successors) 
        {
            int value = 0;
            
            if (memo.containsKey(successor)) 
            {
                value = memo.get(successor);
            }
            else 
            {
                value = this.dynamic_version(successor, memo);
                memo.put(successor, value); 
            }
            
            if (value>0) 
            {
                maxPosi = value > maxPosi ? value : maxPosi;
            }
            
            if (value < 0) 
            {
                Posi = false;
                if (maxNega!=0) {maxNega=value > maxNega ? value : maxNega; }
                else{maxNega= value;}
            }
            
            if (value == 0) 
            {
                return 1;
            }
        }
        
        if (Posi) 
        {
            return -(maxPosi)-1; 
        }
        else 
        {
            return Math.abs(maxNega) + 1;
        }
    }
    

    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in); 
        Hexapawn h = new Hexapawn();
        int n = Integer.parseInt(sc.nextLine().trim());
        int m = Integer.parseInt(sc.nextLine().trim());
        Configuration c = h.new Configuration(n,m,true);  //Parameterised constructor that returns an empty board.
        for(int i=0;i<n;i++) 
        {
            c.addLine(i,sc.nextLine());
        }
        sc.close();
        c.generate_flatboard();
        Map<Configuration, Integer> memo = new HashMap<Configuration, Integer>();
        System.out.print(h.dynamic_version(c,memo));
    }
}